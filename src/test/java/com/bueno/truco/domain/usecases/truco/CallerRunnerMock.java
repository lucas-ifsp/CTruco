package com.bueno.truco.domain.usecases.truco;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.Player;

public class CallerRunnerMock extends Player {

    public CallerRunnerMock(String id){
        super("Mock - " + id);
    }

    @Override
    public Card playCard() {
        return null;
    }

    @Override
    public boolean requestTruco() {
        return true;
    }

    @Override
    public int getTrucoResponse(int newHandPoints) {
        return -1;
    }

}
