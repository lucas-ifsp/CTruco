package com.bueno.truco.domain.entities.player;

import com.bueno.truco.domain.entities.deck.Card;

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
        return true;
    }

    @Override
    public int getTrucoResponse(int newHandPoints) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        return false;
    }
}
