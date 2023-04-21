package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class PaulistaBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).getRaiseResponse(intel);
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).chooseCard(intel);
    }

    private Strategy getInstanceByRound(int roundNumber) {
        return switch (roundNumber) {
            case 0 -> new FirstRound();
            case 1 -> new SecondRound();
            case 2 -> new ThirdRound();
            default -> throw new IllegalStateException("Unexpected value: " + roundNumber);
        };
    }
}
