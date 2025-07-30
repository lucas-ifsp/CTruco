package com.local.pedro.herick.skilldiffbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

abstract class BotUtils {
    protected TrucoCard getWeakestCardToWin(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if (intel.getOpponentCard().isEmpty()) return null;

        TrucoCard opponentCard = intel.getOpponentCard().get();

        return cards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((c1, c2) -> c1.compareValueTo(c2, vira))
                .orElse(getWeakestCard(cards, vira));
    }

    protected TrucoCard getStrongestCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .max((c1, c2) -> c1.compareValueTo(c2, vira))
                .orElse(cards.get(0));
    }

    protected TrucoCard getWeakestCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .min((c1, c2) -> c1.compareValueTo(c2, vira))
                .orElse(cards.get(0));
    }

    protected int getStrongCardsCount(List<TrucoCard> cards, TrucoCard vira) {
        return (int) cards.stream()
                .filter(card -> !card.isManilha(vira))
                .filter(card -> card.getRank() == CardRank.THREE ||
                        card.getRank() == CardRank.TWO ||
                        card.getRank() == CardRank.ACE)
                .count();
    }

    protected int getManilhaCount(List<TrucoCard> cards, TrucoCard vira) {
        return (int) cards.stream().filter(card -> card.isManilha(vira)).count();
    }
    
    protected boolean wonFirstRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }
}
