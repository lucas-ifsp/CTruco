package com.local.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class FirstRoundState implements GameRound {
    @Override
    public boolean decideIfRaises(GameIntel intel) {
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

        /*if (manilhaCounter(intel) >= 2) {
            return 1;
        }

        if ((hasZap(intel) || hasCopas(intel)) && strongCardsCounter(intel) >= 2) {
            return 0;
        }*/

        return -1;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (TrucoUtils.strongCardsCounter(intel, 10) >= 2)
            return CardToPlay.of(TrucoUtils.pickWeakestCard(intel));

        if (TrucoUtils.isPlayingSecond(intel)) {
            if (TrucoUtils.manilhaCounter(intel) > 0)
                return CardToPlay.of(TrucoUtils.optimalCardPick(intel, true).orElse(TrucoUtils.pickWeakestCard(intel)));
            return CardToPlay.of(TrucoUtils.optimalCardPick(intel, false).orElse(TrucoUtils.pickWeakestCard(intel)));
        }

        if (TrucoUtils.cards(intel).anyMatch(card1 -> card1.isOuros(intel.getVira())))
            return CardToPlay.of(intel.getCards().stream()
                .findAny()
                    .filter(card -> card.isOuros(intel.getVira()))
                    .orElse(TrucoUtils.pickStrongestCard(intel)));

        if (TrucoUtils.manilhaCounter(intel) >= 1)
            return CardToPlay.of(TrucoUtils.strongestNonManilha(intel)
                    .orElse(TrucoUtils.pickStrongestCard(intel)));

        return CardToPlay.of(TrucoUtils.pickStrongestCard(intel));
    }
}
