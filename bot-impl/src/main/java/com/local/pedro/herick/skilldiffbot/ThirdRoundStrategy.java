package com.local.pedro.herick.skilldiffbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class ThirdRoundStrategy extends BotUtils implements Strategy {
    private final HandEvaluator evaluator = new HandEvaluator();
    private static final double CONSERVATIVE_THRESHOLD = 0.5;


    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return evaluator.evaluateHand(intel.getCards(), intel.getVira()) > CONSERVATIVE_THRESHOLD;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return CardToPlay.of(getStrongestCard(intel.getCards(), intel.getVira()));
    }
}
