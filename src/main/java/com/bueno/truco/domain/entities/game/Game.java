package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Deck;
import com.bueno.truco.domain.entities.player.Player;

import java.util.*;

public class Game {

    private final Player player1;
    private final Player player2;

    private final List<Hand> hands;

    private Player firstToPlay;
    private Card currentVira;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.hands = new ArrayList<>();
    }

    public void dealCards() {
        Deck deck = new Deck();
        deck.shuffle();

        player1.setCards(deck.take(3));
        player2.setCards(deck.take(3));
        currentVira = deck.takeOne();
    }

    public Hand prepareNewHand(){
        Hand hand = new Hand(this, currentVira);
        firstToPlay = player1.equals(firstToPlay) ? player2 : player1;
        hands.add(hand);
        return hand;
    }

    public void updateScores() {
        final HandResult result = getCurrentHand().getResult().get();
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

    public Player getFirstToPlay() {
        return firstToPlay;
    }

    public Player getLastToPlay() {
        return firstToPlay.equals(player1) ? player2 : player1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public List<Hand> getHands() {
        return new ArrayList<>(hands);
    }

    public Hand getCurrentHand(){
        int lastHandIndex = hands.size() - 1;
        return hands.get(lastHandIndex);
    }

    public boolean isMaoDeOnze() {
        return player1.getScore() == 11 ^ player2.getScore() == 11;
    }
}
