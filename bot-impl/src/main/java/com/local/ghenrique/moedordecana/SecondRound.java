package com.local.ghenrique.moedordecana;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;



public class SecondRound implements SuperGameStrategy {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        boolean hasZap = TrucoTools.cards(intel).anyMatch(card -> card.isZap(vira));
        boolean hasCopas = TrucoTools.cards(intel).anyMatch(card -> card.isCopas(vira));
        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);

        if (firstRoundResult.equals(GameIntel.RoundResult.DREW) && (hasZap || hasCopas)) {
            return 1;
        }
        if (firstRoundResult.equals(GameIntel.RoundResult.WON) && TrucoTools.countStrongCards(intel, 8) >= 1) {
            return 0;
        }
        if (firstRoundResult.equals(GameIntel.RoundResult.LOST) && (hasZap && hasCopas)) {
            return 1;
        }
        if (intel.getOpponentScore() < 9 && TrucoTools.countManilha(intel) >= 1) {
            return 0;
        }
        return -1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        boolean hasStrongManilha = TrucoTools.countManilha(intel) >= 2;
        boolean hasMultipleStrongCards = TrucoTools.countStrongCards(intel, 8) >= 2;
        GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);
        if (firstRoundResult.equals(GameIntel.RoundResult.WON) && (hasStrongManilha || hasMultipleStrongCards)) {
            return true;
        }
        if (firstRoundResult.equals(GameIntel.RoundResult.DREW) && TrucoTools.isPlayingSecond(intel)) {
            if (intel.getOpponentCard().isPresent()) {
                TrucoCard opponentCard = intel.getOpponentCard().get();
                if (intel.getCards().stream().anyMatch(card -> card.compareValueTo(opponentCard, intel.getVira()) > 0)) {
                    return true;
                }
            }
        }
        return hasStrongManilha;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (TrucoTools.isPlayingSecond(intel)) {
            return CardToPlay.of(
                    TrucoTools.chooseOptimalCard(intel, true)
                            .orElse(TrucoTools.selectStrongestCard(intel))
            );
        }
        boolean firstRoundWon = intel.getRoundResults().stream()
                .findFirst()
                .filter(round -> round == GameIntel.RoundResult.WON)
                .isPresent();
        if (firstRoundWon) {
            return CardToPlay.of(TrucoTools.selectWeakestCard(intel));
        }
        return CardToPlay.of(TrucoTools.selectStrongestCard(intel));
    }
}