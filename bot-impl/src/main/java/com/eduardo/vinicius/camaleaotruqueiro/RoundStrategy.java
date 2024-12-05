package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface RoundStrategy {
    static RoundStrategy of(GameIntel intel) {
        int roundNumber = intel.getRoundResults().size() + 1;

        return switch (roundNumber) {
            case 1 -> new FirstRoundStrategy(intel);
            case 2 -> new SecondRoundStrategy(intel);
            case 3 -> new ThirdRoundStrategy(intel);
            default -> throw new IllegalStateException("Illegal number of rounds to play: " + roundNumber);
        };
    }

    boolean getMaoDeOnzeResponse(GameIntel intel);

    boolean decideIfRaises(GameIntel intel);

    CardToPlay chooseCard(GameIntel intel);

    int getRaiseResponse(GameIntel intel);
}
