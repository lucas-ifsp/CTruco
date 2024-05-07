package com.lucas.felipe.newbot;

import com.bonelli.noli.paulistabot.FirstRound;
import com.bonelli.noli.paulistabot.SecondRound;
import com.bonelli.noli.paulistabot.ThirdRound;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class NewBot implements BotServiceProvider {
    public boolean getMaoDeOnzeResponse(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).getMaoDeOnzeResponse(intel);
    }

    public boolean decideIfRaises(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).decideIfRaises(intel);
    }

    public CardToPlay chooseCard(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).chooseCard(intel);
    }

    public int getRaiseResponse(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).getRaiseResponse(intel);
    }

    private StrategyByRound decideStrategyToPlay(int round) {
        return switch (round) {
            case 0 -> new FirstRoundStrategy();
            case 1 -> new SecondRoundStrategy();
            case 2 -> new ThirdRoundStrategy();
            default -> throw new IllegalArgumentException("Invalid round number: " + round);
        };
    }

    private int getRound(GameIntel intel) {
        return intel.getRoundResults().size();
    }
}
