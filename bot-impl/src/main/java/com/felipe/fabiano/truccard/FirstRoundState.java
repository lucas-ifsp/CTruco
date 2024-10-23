package com.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.felipe.fabiano.truccard.TrucoUtils.*;
import static com.felipe.fabiano.truccard.TrucoUtils.pickStrongestCard;

public class FirstRoundState implements GameRound {
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
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
        if (strongCardsCounter(intel, 10) >= 2) return CardToPlay.of(pickWeakestCard(intel));

        if (isPlayingSecond(intel)) {
            if (manilhaCounter(intel)>0) return CardToPlay.of(optimalCardPick(intel, true).orElse(pickWeakestCard(intel)));
            return CardToPlay.of(optimalCardPick(intel, false).orElse(pickWeakestCard(intel)));
        }

        if (hasOuros(intel)) return CardToPlay.of(intel.getCards().stream()
                .findAny().filter(card -> card.isOuros(intel.getVira())).orElse(pickStrongestCard(intel)));

        if (manilhaCounter(intel)>=1) return CardToPlay.of(strongestNonManilha(intel).orElse(pickStrongestCard(intel)));

        return CardToPlay.of(pickStrongestCard(intel));
    }
}
