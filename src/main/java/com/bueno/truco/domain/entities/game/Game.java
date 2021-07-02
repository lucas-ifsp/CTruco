package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Deck;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.entities.utils.Observable;
import com.bueno.truco.domain.entities.utils.Observer;

import java.util.*;

public class Game implements Observable {

    private Player player1;
    private Player player2;
    private Player firstToPlay;
    private Card currentVira;

    private Card cardToPlayAgainst;
    private Set<Card> openCards;
    private List<Hand> hands;
    private Hand currentHand;
    private int currentHandPoints;

    private final List<Observer> observers;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.hands = new ArrayList<>();
        this.openCards = new LinkedHashSet<>();
        this.observers = new ArrayList<>();
        registerObserver(player1);
        registerObserver(player2);
    }

    public void dealCards() {
        organizeForNewHand();

        Deck deck = new Deck();
        deck.shuffle();

        player1.setCards(deck.take(3));
        player2.setCards(deck.take(3));
        currentVira = deck.takeOne();
        openCards.add(currentVira);
    }

    private void organizeForNewHand() {
        firstToPlay = player1.equals(firstToPlay) ? player2 : player1;
        currentVira = null;
        cardToPlayAgainst = null;
        currentHand = null;
        currentHandPoints = 1;
        openCards.clear();
    }

    public void updateCurrentHandPoints(){
        currentHandPoints = currentHand.getHandPoints();
        notifyObservers();
    }

    public int getCurrentHandPoints() {
        return currentHandPoints;
    }

    public void updateGameWithLastHand(Hand hand) {
        updateGameStatus(hand.getResult().get());
        hands.add(hand);
    }

    private void updateGameStatus(HandResult result) {
        Optional<Player> winner = result.getWinner();
        if (winner.isEmpty()) return;
        if (winner.get().equals(player1)) player1.incrementScoreBy(result.getPoints());
        else player2.incrementScoreBy(result.getPoints());
    }

    public Optional<Player> getWinner() {
        if (player1.getScore() == Player.MAX_SCORE) return Optional.of(player1);
        if (player2.getScore() == Player.MAX_SCORE) return Optional.of(player2);
        return Optional.empty();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(new GameIntel(this)));
    }

    public Card getCurrentVira() {
        return currentVira;
    }

    public Player getFirstToPlay() {
        return firstToPlay;
    }

    public Player getLastToPlay() {
        return firstToPlay.equals(player1) ? player2 : player1;
    }

    public void setCardToPlayAgainst(Card card) {
        this.cardToPlayAgainst = card;
        notifyObservers();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgainst);
    }

    public void addOpenCard(Card card){
        openCards.add(card);
        notifyObservers();
    }

    public Set<Card> getOpenCards() {
        return openCards;
    }

    public void setCurrentHand(Hand currentHand) {
        this.currentHand = currentHand;
        notifyObservers();
    }

    public Hand getCurrentHand() {
        return currentHand;
    }

    public List<Hand> getHands() {
        return new ArrayList<>(hands);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
