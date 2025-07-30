package com.local.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.bueno.spi.model.GameIntel.RoundResult.DREW;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;

public class SecondRoundState implements GameRound {
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (TrucoUtils.manilhaCounter(intel) >= 2) return true;
        if (TrucoUtils.firstRoundMatches(intel, round -> round == WON)) {
            if (TrucoUtils.strongCardsCounter(intel, 6) == 0) return true;
            return TrucoUtils.manilhaCounter(intel) >= 1 && TrucoUtils.strongCardsCounter(intel, 6) >= 2;
        }
        if (TrucoUtils.firstRoundMatches(intel, round -> round == DREW)) {
            if (TrucoUtils.isPlayingSecond(intel) && TrucoUtils.pickStrongestCard(intel).relativeValue(intel.getVira()) > intel.getOpenCards().get(0).relativeValue(intel.getVira())) return true;
            return TrucoUtils.hasStrongManilha(intel);
        }
        return false;
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
        if (TrucoUtils.isPlayingSecond(intel)) return CardToPlay.of(TrucoUtils.optimalCardPick(intel, false).orElse(TrucoUtils.pickStrongestCard(intel)));

        if (TrucoUtils.firstRoundMatches(intel, round -> round == WON)) return CardToPlay.of(TrucoUtils.pickWeakestCard(intel));

        return CardToPlay.of(TrucoUtils.pickStrongestCard(intel));
    }
}
