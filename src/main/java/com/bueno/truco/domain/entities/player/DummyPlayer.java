package com.bueno.truco.domain.entities.player;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.hand.HandScore;

public class DummyPlayer extends Player{

    public DummyPlayer() {
        super("Dummy bot");
    }

    @Override
    public Card playCard() {
        return cards.remove(0);
    }

    @Override
    public boolean requestTruco() {
        return false;
    }

    @Override
    public int getTrucoResponse(HandScore newHandScore) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        return false;
    }
}
