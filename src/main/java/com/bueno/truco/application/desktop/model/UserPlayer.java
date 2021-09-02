package com.bueno.truco.application.desktop.model;

import com.bueno.truco.application.desktop.controller.GameTableController;
import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;
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
    private Object monitor = new Object();

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
    public Card playCard(){
        controller.startPlayerTurn();
        final Card card = getUserResponseAsync(this::getCardToPlayDecision);
        cardToPlayDecision = null;
        return card;
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
