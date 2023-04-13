package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class FirstRound implements Strategy{

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }
}
