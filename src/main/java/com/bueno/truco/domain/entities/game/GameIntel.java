package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameIntel {

    private Game game;

    public GameIntel(Game game) {
        this.game = game;
    }

    public Card getCurrentVira() {
        return game.getCurrentVira();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return game.getCardToPlayAgainst();
    }

    public int getOpponentScore(Player requester) {
        return getOpponent(requester).getScore();
    }

    public String getOpponentId(Player requester) {
        return getOpponent(requester).getId();
    }

    private Player getOpponent(Player requester){
        return requester.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
    }

    public Set<Card> getOpenCards() {
        return game.getOpenCards();
    }

    public List<Round> getRoundsPlayed() {
        return game.getCurrentHand().getRoundsPlayed();
    }
}
