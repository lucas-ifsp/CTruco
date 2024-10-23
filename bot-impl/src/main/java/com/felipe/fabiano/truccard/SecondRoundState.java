package com.felipe.fabiano.truccard;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

import static com.felipe.fabiano.truccard.TrucoUtils.*;

public class SecondRoundState implements GameRound {
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (manilhaCounter(intel)>=2) return true;
        if (wonFirstRound(intel)) {
            if (strongCardsCounter(intel, 6)==0) return true;
            return manilhaCounter(intel) >= 1 && strongCardsCounter(intel, 6) >= 2;
        }
        if (drewFirstRound(intel)) {
            if (isPlayingSecond(intel) && pickStrongestCard(intel).relativeValue(intel.getVira())>intel.getOpenCards().get(0).relativeValue(intel.getVira())) return true;
            return hasStrongManilha(intel);
        }
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
        return -1;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (isPlayingSecond(intel)) return CardToPlay.of(optimalCardPick(intel, false).orElse(pickStrongestCard(intel)));

        if (wonFirstRound(intel)) return CardToPlay.of(pickWeakestCard(intel));

        return CardToPlay.of(pickStrongestCard(intel));
    }
}
