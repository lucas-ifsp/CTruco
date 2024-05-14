package com.luna.jundi.jokerBot.states;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.luna.jundi.jokerBot.utils.RoundUtils.jokerBotStartsTheRound;

public final class RoundThreeState implements RoundState {

    private final GameIntel intel;

    public RoundThreeState(GameIntel intel) {
        if (!(isValidRoundState().test(intel, 3)))
            throw new IllegalStateException("is not" + getClass().getSimpleName());
        this.intel = intel;
    }

    @Override
    public CardToPlay cardChoice() {
        return intel.getCards().stream()
                .map(CardToPlay::of)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("JokerBot have no cards to play at" + getClass().getSimpleName()));
    }

    @Override
    public boolean raiseDecision() {
        boolean anyGoodCardWasPlayed = intel.getOpenCards().stream()
                .map(card -> card.relativeValue(intel.getVira()))
                .filter(value -> value > 3)
                .toList()
                .isEmpty();
        if (jokerBotStartsTheRound().test(intel) && raiseHandByOpponentCard(intel)) return true;
        int myCardsEvaluation = getHandEvaluation(intel).value();
        if (myCardsEvaluation >= 4 && isNotLoosingHand(intel)) return true;
        if (anyGoodCardWasPlayed && myCardsEvaluation > 2) return true;
        return false;
    }

    @Override
    public int raiseResponse() {
        return defaultRaiseResponse(intel);
    }
}
