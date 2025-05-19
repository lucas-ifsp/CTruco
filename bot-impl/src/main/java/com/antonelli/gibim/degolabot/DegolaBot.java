package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class DegolaBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int round = getRoundNumber(intel);
        return getStrategyForRound(round).getRaiseResponse(intel);
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int soma = BotUtils.handStrength(intel);
        return soma > 21;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int round = getRoundNumber(intel);
        return getStrategyForRound(round).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int round = getRoundNumber(intel);
        return getStrategyForRound(round).chooseCard(intel);
    }

    private Strategy getStrategyForRound(int round) {
        return switch (round) {
            case 1 -> new FirstRound();
            case 2 -> new SecondRound();
            case 3 -> new ThirdRound();
            default -> throw new IllegalStateException("Unexpected value: " + round);
        };
    }

    private int getRoundNumber(GameIntel intel) {
        int playedRounds = intel.getRoundResults().size();
        return playedRounds + 1;
    }
}
