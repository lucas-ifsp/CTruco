package com.brito.macena.boteco.utils;

import com.bueno.spi.model.GameIntel;

public class Game {
    public static boolean wonFirstRound(GameIntel intel) {
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
        return false;
    }
}
