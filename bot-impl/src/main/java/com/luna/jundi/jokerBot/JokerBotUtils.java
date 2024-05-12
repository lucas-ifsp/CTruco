package com.luna.jundi.jokerBot;

import com.bueno.spi.model.GameIntel;

public class JokerBotUtils {
    public static boolean isJokerBotWhoPlaysFirst(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }
    public static boolean isOpponentsCardBetterThenMine(GameIntel intel){return false;}
    public static boolean isSameCardToDrew(GameIntel intel){return false;}
    public static boolean jokerBotWonFirstRound (GameIntel intel){return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;}

}
