package com.bueno.truco.application.desktop.model;

import com.bueno.truco.application.desktop.controller.GameTableController;
import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.GameIntel;
import com.bueno.truco.domain.entities.player.Player;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserPlayer extends Player {

    private final GameTableController controller;
    private Card cardToPlay;
    private Optional<Boolean> hasTrucoRequestDecision = Optional.empty();
    private Optional<Integer> hasTrucoResponseDecision = Optional.empty();


    private List<Card> receivedCards;

    public UserPlayer(GameTableController controller, String name) {
        super(name);
        this.controller = controller;
    }

    public void setTrucoRequestDecision(boolean isCalling){
        hasTrucoRequestDecision = Optional.of(isCalling);
    }

    public void setTrucoResponseDecision(int response){
        hasTrucoResponseDecision = Optional.of(response);
    }


    @Override
    public boolean requestTruco() {
        hasTrucoRequestDecision = Optional.empty();
        controller.startPlayerTurn();

        Task<Card> task  = new Task<>() {
            @Override
            protected Card call() throws Exception {
                while (hasTrucoRequestDecision.isEmpty()){
                    Thread.sleep(300);
                }
                return null;
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        while (!task.isDone()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Boolean result = hasTrucoRequestDecision.get();

        executor.shutdown();
        return result;
    }

    @Override
    public int getTrucoResponse(int newHandPoints) {
        hasTrucoResponseDecision = Optional.empty();
        controller.requestTrucoResponse();

        Task<Card> task  = new Task<>() {
            @Override
            protected Card call() throws Exception {
                while (hasTrucoResponseDecision.isEmpty()){
                    Thread.sleep(300);
                }
                return null;
            }

        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        while (!task.isDone()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Integer result = hasTrucoResponseDecision.get();

        executor.shutdown();
        return result;
    }

    public void setCardToPlay(Card cardToPlay) {
        this.cardToPlay = cardToPlay;
    }

    @Override
    public Card playCard(){
        controller.startPlayerTurn();

        Task<Card> task  = new Task<>() {
            @Override
            protected Card call() throws Exception {
                while (cardToPlay == null){
                    Thread.sleep(1000);
                }
                return null;
            }

        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);

        while (!task.isDone()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Card result = cardToPlay;
        cardToPlay = null;

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

    @Override
    public boolean getMaoDeOnzeResponse() {
        return false;
    }

    public List<Card> getReceivedCards() {
        return receivedCards;
    }

    @Override
    public void setCards(List<Card> cards) {
        super.setCards(cards);
        receivedCards = cards;
    }

    public GameIntel getIntel(){
        return getGameIntel();
    }
}
