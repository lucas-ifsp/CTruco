package com.luna.jundi.jokerBot.utils;

import com.bueno.spi.model.GameIntel;

public class RoundUtils {
    //Transform these methods in to functions



    /**
     * Return true if is start of Round
     * Return false if it does not
     */
    public static boolean isStartOfRound(GameIntel intel){
        return intel.getRoundResults().isEmpty();
    }

    /**
     * Returns the number of the current round
     * considering: round 0, round 1, round 2
     */
    public static int getRoundNumber(GameIntel intel){
        return intel.getRoundResults().size();
    }

}
