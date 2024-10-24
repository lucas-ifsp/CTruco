package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface RoundStrategy {
    static RoundStrategy of(GameIntel intel) {
        int roundNumber = intel.getRoundResults().size() + 1;

        RoundStrategy roundStrategy;
        switch (roundNumber) {
            case 1:
                roundStrategy = new FirstRoundStrategy(intel);
                break;
            case 2:
                roundStrategy = new SecondRoundStrategy(intel);
                break;
            case 3:
                roundStrategy = new ThirdRoundStrategy(intel);
                break;
            default:
                throw new IllegalStateException("Illegal number of rounds to play: " + roundNumber);
        }

        return roundStrategy;
    }

    boolean getMaoDeOnzeResponse(GameIntel intel);

    boolean decideIfRaises(GameIntel intel);

    CardToPlay chooseCard(GameIntel intel);

    int getRaiseResponse(GameIntel intel);
}
