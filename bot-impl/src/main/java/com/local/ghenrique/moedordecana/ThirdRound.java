package com.local.ghenrique.moedordecana;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;


import static com.local.ghenrique.moedordecana.TrucoTools.playLastCard;

public class ThirdRound implements SuperGameStrategy {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (TrucoTools.countStrongCards(intel, 7) >= 2 ||
                (TrucoTools.countManilha(intel) >= 1 && TrucoTools.countStrongCards(intel, 7) >= 1)) {
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
    public boolean decideIfRaises(GameIntel intel) {
        boolean hasOpponentCard = intel.getOpponentCard().isPresent();
        boolean wonFirstRound = intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON);

        if (hasOpponentCard && intel.getCards().stream()
                .anyMatch(card -> card.compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0) && wonFirstRound) {
            return true;
        }
        if (intel.getOpponentScore() < 9) {
            return true;
        }
        long strongCardCount = intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= 7)
                .count();
        if (strongCardCount > 1) {
            return true;
        }
        return intel.getCards().stream()
                .anyMatch(card -> card.relativeValue(intel.getVira()) >= 6) && intel.getOpponentScore() <= 12;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return playLastCard(intel);
    }
}
