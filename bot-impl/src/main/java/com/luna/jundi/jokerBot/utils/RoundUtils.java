package com.luna.jundi.jokerBot.utils;

import com.bueno.spi.model.GameIntel;

import java.util.function.Predicate;

public class RoundUtils {

    public static Predicate<GameIntel> jokerBotStartsTheRound() {
        return intel -> intel.getOpponentCard().isEmpty();
    }

    public static int getRoundNumber(GameIntel intel) {
        return 1 + intel.getRoundResults().size();
    }
}
