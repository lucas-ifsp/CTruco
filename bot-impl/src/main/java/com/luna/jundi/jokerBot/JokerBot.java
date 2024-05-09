package com.luna.jundi.jokerBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

import static com.luna.jundi.jokerBot.RoundOne.*;

public class JokerBot implements BotServiceProvider {

    private HandState currentRound;

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
        switch (getRoundNumber(intel)){
            case 1: return null;//roundOne(intel);
            case 2: return null; //roundTwo(intel);;
            case 3: return null;//roundThree(intel);
            default: return CardToPlay.of(intel.getCards().get(0));
        }

    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    int getRoundNumber (GameIntel intel) {

        return intel.getRoundResults().size() + 1;
    }
}
