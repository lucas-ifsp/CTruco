package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

import static com.eduardo.vinicius.camaleaotruqueiro.HandsCardSituation.evaluateHandSituation;
import static com.eduardo.vinicius.camaleaotruqueiro.TrucoUtils.*;

public class FirstRoundStrategy implements RoundStrategy {
    public FirstRoundStrategy(GameIntel intel) {
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        HandsCardSituation situation = evaluateHandSituation(intel);
        if (situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        if (situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        return intel.getOpponentScore() < 7 && getNumberOfHighRankCards(intel.getCards(), intel.getVira()) > 0;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        HandsCardSituation situation = evaluateHandSituation(intel);
        if (situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        if (situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        if (situation == HandsCardSituation.BLUFF_TO_GET_POINTS) return true;
        if (isWinning(intel.getScore(), intel.getOpponentScore()))
            return situation == HandsCardSituation.BLUFF_TO_INTIMIDATE;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard selectedCard;

        if (theBotPlaysFirst(intel)) {
            HandsCardSituation situation = evaluateHandSituation(intel);
            if (situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY ||
                    situation == HandsCardSituation.BLUFF_TO_GET_POINTS) {
                selectedCard = getGreatestCard(cards, vira);
            } else {
                selectedCard = getLowestCard(cards, vira);

            }
        } else {
            if (!(haveStrongestCard(intel, cards).isEmpty())) {
                selectedCard = getLowestCard(haveStrongestCard(intel, cards), vira);
            } else if (!haveEqualCard(intel, cards).isEmpty()) {
                selectedCard = getLowestCard(haveEqualCard(intel, cards), vira);
            } else {
                selectedCard = getLowestCard(cards, vira);
            }
        }
        return CardToPlay.of(selectedCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return switch (evaluateHandSituation(intel)) {
            case ALMOST_ABSOLUTE_VICTORY -> 1;
            case ALMOST_CERTAIN_DEFEAT -> -1;
            default -> 0;
        };
    }
}