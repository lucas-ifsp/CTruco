package com.bueno.truco.application.desktop.model;

import com.bueno.truco.application.desktop.controller.GameTableController;
import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserPlayer extends Player {

    private final GameTableController controller;
    private Card cardToPlay;
    //TODO remove code smell caused by Optional as field
    private Optional<Boolean> trucoRequestDecision = Optional.empty();
    private Optional<Integer> trucoResponseDecision = Optional.empty();
    private Optional<Boolean> maoDeOnzeResponseDecision = Optional.empty();

    private List<Card> receivedCards;

    public UserPlayer(GameTableController controller, String name) {
        super(name);
        this.controller = controller;
    }

    @Override
    public void handleRoundConclusion(){
        controller.clearPlayedCards();
    }

    @Override
    public void handleOpponentPlay(){
        controller.showOpponentTurn();
    }

    @Override
    public boolean requestTruco() {
        controller.startPlayerTurn();
        trucoRequestDecision = Optional.empty();

        Task<Card> task  = createWaitingTask(this::getTrucoRequestDecision, 300);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        waitUntilComplete(task, 100);
        Boolean result = trucoRequestDecision.get();

        trucoRequestDecision = Optional.empty();
        executor.shutdown();

        return result;
    }

    //TODO remove code smell caused by raw Optional
    private Task<Card> createWaitingTask(Callable<Optional> conclusionCheck, final int checkingPeriodInMillis) {
        return new Task<>() {
            @Override
            protected Card call() {
                try {
                    while (conclusionCheck.call().isEmpty()){
                        Thread.sleep(checkingPeriodInMillis);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void waitUntilComplete(Task<Card> task, final int checkingPeriodInMillis) {
        while (!task.isDone()) {
            try {
                Thread.sleep(checkingPeriodInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getTrucoResponse(HandScore newHandScore) {
        controller.requestTrucoResponse();

        Task<Card> task  = createWaitingTask(this::getTrucoResponseDecision, 300);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        waitUntilComplete(task, 100);
        Integer result = trucoResponseDecision.orElse(null);

        trucoResponseDecision = Optional.empty();
        executor.shutdown();

        return result;
    }

    @Override
    public Card playCard(){
        controller.startPlayerTurn();

        Task<Card> task = createWaitingTask(this::getCardToPlay, 1000);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        waitUntilComplete(task, 100);
        Card result = cardToPlay;

        cardToPlay = null;
        executor.shutdown();

        return result;
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        controller.requestMaoDeOnzeResponse();

        Task<Card> task  = createWaitingTask(this::getMaoDeOnzeResponseDecision, 300);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        waitUntilComplete(task, 100);
        Boolean result = maoDeOnzeResponseDecision.orElse(null);

        maoDeOnzeResponseDecision = Optional.empty();
        executor.shutdown();

        return result;
    }

    @Override
    public void setCards(List<Card> cards) {
        super.setCards(cards);
        receivedCards = cards;
    }

    public void setCardToPlay(Card cardToPlay) {
        this.cardToPlay = cardToPlay;
    }

    public void setTrucoRequestDecision(boolean isCalling){
        trucoRequestDecision = Optional.of(isCalling);
    }

    public void setTrucoResponseDecision(int response){
        trucoResponseDecision = Optional.of(response);
    }

    public void setMaoDeOnzeResponseDecision(boolean response){
        maoDeOnzeResponseDecision = Optional.of(response);
    }

    public List<Card> getReceivedCards() {
        return receivedCards;
    }

    private Optional<Card> getCardToPlay() {
        return Optional.ofNullable(cardToPlay);
    }

    private Optional<Boolean> getTrucoRequestDecision() {
        return trucoRequestDecision;
    }

    private Optional<Integer> getTrucoResponseDecision() {
        return trucoResponseDecision;
    }

    private Optional<Boolean> getMaoDeOnzeResponseDecision() {
        return maoDeOnzeResponseDecision;
    }
}
