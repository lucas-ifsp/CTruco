package com.pedro.herick.skilldiffbot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

abstract class BotUtils {
    protected TrucoCard getBestCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .max((c1, c2) -> c1.compareValueTo(c2, vira))
                .orElse(cards.get(0));
    }

    protected TrucoCard getBestCardComparedToOpponent(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if (intel.getOpponentCard().isEmpty()) return null;

        TrucoCard opponentCard = intel.getOpponentCard().get();

        return cards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((c1, c2) -> c1.compareValueTo(c2, vira))
                .orElse(getWorstCard(cards, vira));
    }

    protected TrucoCard getWorstCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .min((c1, c2) -> c1.compareValueTo(c2, vira))
                .orElse(cards.get(0));
    }

    protected boolean hasManilha(List<TrucoCard> cards, TrucoCard vira) {
        return getBestCard(cards, vira).isManilha(vira);
    }
    
    protected boolean wonFirstRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }
}
