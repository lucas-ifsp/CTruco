package com.daniel.therover;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class TheRover implements BotServiceProvider {

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

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "The Rover";
    }

    public boolean isPlayingFirst(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    public int getCurrentRound (GameIntel intel) {
        return 1;
    }
}
