package com.luna.jundi.jokerBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class JokerBotUtils {
    static boolean isJokerBotWhoPlaysFirst(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }
    static boolean isOpponentsCardBetterThenMine(GameIntel intel){return false;}
    static boolean isSameCardToDrew(GameIntel intel){return false;}
    static boolean jokerBotWonFirstRound (GameIntel intel){return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;}


    public static CardToPlay bestCard(GameIntel intel){
        return null;
    }

    public static CardToPlay worstCard(GameIntel intel){
        return null;
    }

    public static CardToPlay cardToDrew(GameIntel intel){
        return null;
    }


}
