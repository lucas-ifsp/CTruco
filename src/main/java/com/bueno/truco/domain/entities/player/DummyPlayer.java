package com.bueno.truco.domain.entities.player;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.GameIntel;

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
}
