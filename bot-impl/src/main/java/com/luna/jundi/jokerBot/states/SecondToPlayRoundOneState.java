package com.luna.jundi.jokerBot.states;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.luna.jundi.jokerBot.exceptions.OpponentWithoutCardsException;

import java.util.Objects;

public final class SecondToPlayRoundOneState implements RoundState {

    private final GameIntel intel;

    public SecondToPlayRoundOneState(GameIntel intel) {
        if (!isValidRoundState().test(intel, 1)) throw new IllegalStateException("is not" + getClass().getSimpleName());
        this.intel = intel;
    }

    @Override
    public CardToPlay cardChoice() {
        Objects.requireNonNull(intel.getOpponentCard().orElseThrow(
                () -> new OpponentWithoutCardsException("no opponent Card available at" + getClass().getSimpleName())));
        return secondRoundsChoicesCard().apply(intel);
    }

    @Override
    public boolean raiseDecision() {
        if (raiseHandByOpponentCard(intel)) return true;
        return raiseHandByMyCards(intel);
    }

    @Override
    public int raiseResponse() {
        return defaultRaiseResponse(intel);
    }
}
