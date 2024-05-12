package com.luna.jundi.jokerBot.states;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.luna.jundi.jokerBot.JokerBotUtils.jokerBotWonFirstRound;
import static com.luna.jundi.jokerBot.utils.CardUtils.getBestCard;

public class RoundThreeState implements RoundState {

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(jokerBotWonFirstRound(intel)) return getBestCard(intel);
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
