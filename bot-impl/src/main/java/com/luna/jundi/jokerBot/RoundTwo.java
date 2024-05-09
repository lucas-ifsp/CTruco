package com.luna.jundi.jokerBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.luna.jundi.jokerBot.JokerBotUtils.bestCard;
import static com.luna.jundi.jokerBot.JokerBotUtils.jokerBotWonFirstRound;

public class RoundTwo implements HandState {


    public RoundTwo(GameIntel intel) {
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(jokerBotWonFirstRound(intel)) return bestCard(intel);
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
