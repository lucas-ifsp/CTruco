/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.application.model;

import com.bueno.application.controller.GameTableController;
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.HandScore;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;
import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserPlayer extends Player {

    private final GameTableController controller;

    private Card cardToPlayDecision;
    private Boolean trucoRequestDecision;
    private Integer trucoResponseDecision;
    private Boolean maoDeOnzeResponseDecision ;
    private final Object monitor = new Object();

    private List<Card> receivedCards;

    public UserPlayer(GameTableController controller, String name) {
        super(name);
        this.controller = controller;
    }

    @Override
    public void setCards(List<Card> cards) {
        super.setCards(cards);
        receivedCards = cards;
    }

    @Override
    public boolean requestTruco() {
        trucoRequestDecision = null;
        controller.startPlayerTurn();
        return getUserResponseAsync(this::getTrucoRequestDecision);
    }

    @Override
    public int getTrucoResponse(HandScore newHandScore) {
        trucoResponseDecision = null;
        controller.requestTrucoResponse();
        return getUserResponseAsync(this::getTrucoResponseDecision);
    }

    @Override
    public CardToPlay chooseCardToPlay(){
        controller.startPlayerTurn();
        final Card card = getUserResponseAsync(this::getCardToPlayDecision);
        final CardToPlay cardToPlay = card.equals(Card.closed()) ? CardToPlay.ofDiscard(card) : CardToPlay.of(card);
        cardToPlayDecision = null;
        return cardToPlay;
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        controller.requestMaoDeOnzeResponse();
        final Boolean decision = getUserResponseAsync(this::getMaoDeOnzeResponseDecision);
        maoDeOnzeResponseDecision = null;
        return decision;
    }

    private <T> T getUserResponseAsync(Callable<T> responseGetter){
        T result = null;

        Task<T> task  = new Task<>() {
            @Override
            protected T call() throws Exception {
                while (responseGetter.call() == null) synchronized (monitor) {
                    monitor.wait();
                }
                return responseGetter.call();
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        try {
            result = task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return result;
    }

    @Override
    public void handleRoundConclusion(){
        controller.clearPlayedCards();
    }

    @Override
    public void handleOpponentPlay(){
        controller.showOpponentTurn();
    }

    public void setCardToPlayDecision(Card cardToPlayDecision) {
        this.cardToPlayDecision = cardToPlayDecision;
        notifyReceivedResponse();
    }

    public void setTrucoRequestDecision(boolean isCalling){
        trucoRequestDecision = isCalling;
        notifyReceivedResponse();
    }

    public void setTrucoResponseDecision(int response){
        trucoResponseDecision = response;
        notifyReceivedResponse();
    }

    public void setMaoDeOnzeResponseDecision(boolean response){
        maoDeOnzeResponseDecision = response;
        notifyReceivedResponse();
    }

    private void notifyReceivedResponse() {
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    public List<Card> getReceivedCards() {
        return receivedCards;
    }

    private Boolean getTrucoRequestDecision() {
        return trucoRequestDecision;
    }

    private Integer getTrucoResponseDecision() {
        return trucoResponseDecision;
    }

    private Boolean getMaoDeOnzeResponseDecision() {
        return maoDeOnzeResponseDecision;
    }

    private Card getCardToPlayDecision() {
        return cardToPlayDecision;
    }
}
