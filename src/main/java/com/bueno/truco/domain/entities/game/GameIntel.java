package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameIntel {

    private Hand currentHand;

    public GameIntel(Hand hand) {
        this.currentHand = hand;
    }

    public Card getCurrentVira() {
        return currentHand.getVira();
    }

    public int getCurrentHandPoints() {
        return currentHand.getPoints();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return currentHand.getCardToPlayAgainst();
    }

    public List<Card> getOpenCards() {
        return currentHand.getOpenCards();
    }

    public List<Round> getRoundsPlayed() {
        return currentHand.getRoundsPlayed();
    }

    public int getOpponentScore(Player requester) {
        return getOpponent(requester).getScore();
    }

    public String getOpponentId(Player requester) {
        return getOpponent(requester).getId();
    }

    private Player getOpponent(Player requester){
        return requester.equals(currentHand.getPlayer1()) ? currentHand.getPlayer2() : currentHand.getPlayer1();
    }

    public Optional<HandResult> getResult(){
        return currentHand.getResult();
    }
}
