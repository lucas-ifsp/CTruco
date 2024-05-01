package com.shojisilva.fernasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class FernasBot implements BotServiceProvider {
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

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).getRaiseResponse(intel);
    }

    private FernasStrategy getInstanceByRound(int roundNumber) {
        return switch (roundNumber) {
            case 0 -> new FernasFirstHand();
            case 1 -> new FernasSecondHand();
            case 2 -> new FernasThirdHand();
            default -> throw new IllegalStateException("Unexpected value: " + roundNumber);
        };
    }
}
