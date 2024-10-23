package com.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.felipe.fabiano.truccard.TrucoUtils.*;

public class ThirdRoundState implements GameRound {
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        //if (wonFirstRound(intel) && manilhaCounter(intel)==1) return true;
        //return hasStrongManilha(intel);
        if (manilhaCounter(intel)>=1) return true;
        return wonFirstRound(intel) && strongCardsCounter(intel) >= 1;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (strongCardsCounter(intel) >= 2 || (manilhaCounter(intel) >= 1 && strongCardsCounter(intel, 7) >= 2)) {
            return 1;
        }

        if (intel.getCards().stream()
                .mapToInt(card -> card.relativeValue(intel.getVira()))
                .average()
                .orElse(0) >= 7) {
            return 0;
        }
        return -1;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return playRemainingCard(intel);
    }
}
