package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

import static com.eduardo.vinicius.camaleaotruqueiro.TrucoUtils.*;

public class FirstRoundStrategy implements RoundStrategy{
    public FirstRoundStrategy(GameIntel intel) {
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(getNumberOfHighRankCards(cards,vira) >= 2 && numberOfManilhas(cards,vira) >= 1) return true;
        else return intel.getOpponentScore() < 9 && getNumberOfHighRankCards(cards, vira) >= 1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (theBotPlaysFirst(intel)) {
            HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
            return situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY ||
                    isHighChangesOpponentRunFromTruco(intel);
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard selectedCard;

        if (theBotPlaysFirst(intel)) {
            HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
            if (situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY ||
                    situation == HandsCardSituation.BLUFF_TO_GET_POINTS) {
                selectedCard = getLowestCard(cards, vira);
            } else {
                selectedCard = getGreatestCard(cards, vira);

            }
        }
        else {
            if(opponentPlayedInvincibleCard(intel)) selectedCard = getLowestCard(cards, vira);
            else selectedCard = getLowestCard(haveStrongestCard(intel, cards), vira);
        }

        return CardToPlay.of(selectedCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
