package com.pedro.herick.skilldiffbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class SkillDiffBot implements BotServiceProvider {
    private static final int REFUSE = -1;
    private static final int ACCEPT = 0;
    private static final int RAISE = 1;

    private static final double RAISE_THRESHOLD = 0.7;
    private static final double ACCEPT_THRESHOLD = 0.5;

    private final HandEvaluator evaluator = new HandEvaluator();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()) ||
                card.isCopas(intel.getVira()));
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int currentRound = intel.getRoundResults().size();
        return strategyForTheRound(currentRound).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int currentRound = intel.getRoundResults().size();
        return strategyForTheRound(currentRound).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        double handStrength = evaluator.evaluateHand(intel.getCards(), intel.getVira());

        if (handStrength > RAISE_THRESHOLD) return RAISE;
        if (handStrength > ACCEPT_THRESHOLD) return ACCEPT;

        return REFUSE;
    }

    private Strategy strategyForTheRound(int round){
        return switch (round){
            case 0 -> new FirstRoundStrategy();
            case 1 -> new SecondRoundStrategy();
            case 2 -> new ThirdRoundStrategy();
            default -> throw new IllegalStateException("Unexpected value: " + round);
        };
    }
    @Override
    public String getName() {
        return "SkillDiffBot";
    }
}
