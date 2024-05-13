package com.luna.jundi.jokerBot.states;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

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
        return defaultRaiseHandDecision(intel);
    }

    @Override
    public int raiseResponse() {
        return defaultRaiseResponse(intel);
    }
}
