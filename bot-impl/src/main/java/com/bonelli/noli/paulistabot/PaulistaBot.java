package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class PaulistaBot implements BotServiceProvider {

    private Object instanceRound;

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 1;
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
        return switch (intel.getRoundResults().size()) {
            case 0 -> new FirstRound(intel).chooseCard();
            case 1 -> new SecondRound(intel).chooseCard();
            case 2 -> new ThirdRound(intel).chooseCard();
            default -> throw new IllegalStateException("Unexpected value: " + intel.getCards().size());
        };
    }
}
