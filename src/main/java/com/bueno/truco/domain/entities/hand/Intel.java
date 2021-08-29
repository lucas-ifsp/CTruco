package com.bueno.truco.domain.entities.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.entities.round.Round;

import java.util.List;
import java.util.Optional;

public class Intel {

    private Hand hand;

    public Intel(Hand hand) {
        this.hand = hand;
    }

    public Card getVira() {
        return hand.getVira();
    }

    public HandScore getHandScore() {
        return hand.getScore();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return hand.getCardToPlayAgainst();
    }

    public List<Card> getOpenCards() {
        return hand.getOpenCards();
    }

    public List<Round> getRoundsPlayed() {
        return hand.getRoundsPlayed();
    }

    public int getOpponentScore(Player requester) {
        return getOpponent(requester).getScore();
    }

    public String getOpponentId(Player requester) {
        return getOpponent(requester).getUsername();
    }

    private Player getOpponent(Player requester){
        return requester.equals(hand.getFirstToPlay()) ? hand.getLastToPlay() : hand.getFirstToPlay();
    }

    public Optional<HandResult> getResult(){
        return hand.getResult();
    }
}
