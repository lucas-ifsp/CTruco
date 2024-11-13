package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.GameIntel;

public final class Playing {
    private Playing() {}

    public static RoundStrategy buildStrategy(GameIntel intel) {
        final int roundsPlayed = intel.getRoundResults().size();

        return switch (roundsPlayed) {
            case 0 -> new FirstRoundStrategy();
            case 1 -> new SecondRoundStrategy();
            case 3 -> new ThirdRoundStrategy();
            default -> throw new IllegalStateException("Unexpected value: " + roundsPlayed);
        };
    }
}
