package com.local.ghenrique.moedordecana;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class FirstRound implements SuperGameStrategy {


    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> currentCards = intel.getCards();
        boolean opponentHasCard = TrucoTools.isPlayingSecond(intel);
        if (opponentHasCard && intel.getOpponentCard().isPresent() &&
                currentCards.stream()
                        .filter(e -> e.compareValueTo
                                (intel.getOpponentCard().get(), intel.getVira()) >= 0)
                        .count() >= 2) {
            return 0;
        }
        if (intel.getOpponentScore() < 6 && TrucoTools.countStrongCards(intel) > 0) {
            return 0;
        }
        boolean hasZap = TrucoTools.cards(intel).anyMatch(card -> card.isZap(intel.getVira()));
        boolean hasManilha = TrucoTools.cards(intel).anyMatch
                (card -> card.isManilha(intel.getVira()) && !card.isZap(intel.getVira()));
        if (hasZap && hasManilha) {
            return 1;
        }
        if (TrucoTools.countManilha(intel) > 0) {
            return 0;
        }
        if (TrucoTools.countStrongCards(intel, 8) >= 2) {
            return 0;
        }
        return -1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (TrucoTools.countManilha(intel) >= 2 || TrucoTools.countStrongCards(intel) > 2) {
            return true;
        }
        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            boolean canBeatOpponent = intel.getCards()
                    .stream()
                    .anyMatch(e -> e.compareValueTo(opponentCard, intel.getVira()) > 0);

            if (canBeatOpponent || TrucoTools.countStrongCards(intel, 7) >= 2) {
                return true;
            }
            return intel.getCards()
                    .stream()
                    .filter(e -> e.compareValueTo(opponentCard, intel.getVira()) >= 0)
                    .count() >= 2 && TrucoTools
                    .countStrongCards(intel, 8) > 0;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (intel.getScore() < 6 && intel.getOpponentScore() > 9) {
            if (TrucoTools.countStrongCards(intel, 8) >= 2) {
                return CardToPlay.of(TrucoTools.selectWeakestCard(intel));
            }
        }
        if (TrucoTools.countStrongCards(intel, 10) >= 2)
            return CardToPlay.of(TrucoTools.selectWeakestCard(intel));
        if (TrucoTools.isPlayingSecond(intel)) {
            if (TrucoTools.countManilha(intel) > 0)
                return CardToPlay.of(TrucoTools.chooseOptimalCard(intel, true).orElse(TrucoTools.selectWeakestCard(intel)));
            return CardToPlay.of(TrucoTools.chooseOptimalCard(intel, false).orElse(TrucoTools.selectWeakestCard(intel)));
        }
        if (TrucoTools.cards(intel).anyMatch(card -> card.isZap(intel.getVira())) &&
                TrucoTools.cards(intel).anyMatch(card -> card.isCopas(intel.getVira()))) {
            return CardToPlay.of(TrucoTools.selectWeakestCard(intel));
        }
        if (TrucoTools.cards(intel).anyMatch(card -> card.isOuros(intel.getVira())))
            return CardToPlay.of(
                    TrucoTools.cards(intel)
                            .filter(card -> card.isOuros(intel.getVira()))
                            .findAny()
                            .orElse(TrucoTools.selectStrongestCard(intel))
            );
        if (TrucoTools.countManilha(intel) >= 1)
            return CardToPlay.of(TrucoTools.strongestNonManilha(intel)
                    .orElse(TrucoTools.selectStrongestCard(intel)));
        return CardToPlay.of(TrucoTools.selectStrongestCard(intel));
    }
}