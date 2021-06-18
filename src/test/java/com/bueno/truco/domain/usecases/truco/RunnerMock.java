package com.bueno.truco.domain.usecases.truco;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;

public class RunnerMock extends Player {

    public RunnerMock(String id){
        super("Mock - " + id);
    }

    @Override
    public Card playCard() {
        return null;
    }

    @Override
    public boolean requestTruco() {
        return false;
    }

    @Override
    public int getTrucoResponse(int newHandPoints) {
        return -1;
    }

}
