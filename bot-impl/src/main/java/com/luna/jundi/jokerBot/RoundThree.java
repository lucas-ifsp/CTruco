package com.luna.jundi.jokerBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class RoundThree implements HandState{

    public RoundThree(GameIntel intel) {
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
