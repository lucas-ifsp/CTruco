package com.local.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.bueno.spi.model.GameIntel.RoundResult.WON;

public class ThirdRoundState implements GameRound {
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        //if (wonFirstRound(intel) && manilhaCounter(intel)==1) return true;
        //return hasStrongManilha(intel);
        if (TrucoUtils.manilhaCounter(intel) >= 1) return true;
        return TrucoUtils.firstRoundMatches(intel, round -> round == WON) && TrucoUtils.strongCardsCounter(intel) >= 1;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (TrucoUtils.strongCardsCounter(intel) >= 2 || (TrucoUtils.manilhaCounter(intel) >= 1 && TrucoUtils.strongCardsCounter(intel, 7) >= 2)) {
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
        return TrucoUtils.playRemainingCard(intel);
    }
}
