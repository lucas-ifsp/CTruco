package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

import static com.eduardo.vinicius.camaleaotruqueiro.TrucoUtils.*;


public class SecondRoundStrategy implements RoundStrategy{
    public SecondRoundStrategy(GameIntel intel) {
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        else if((winFistRound(intel) || drewFistRound(intel)) &&
                situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        else return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard selectedCard;

        if (theBotPlaysFirst(intel)) {
            selectedCard = getGreatestCard(cards, vira);
        }
        else {
            if(opponentPlayedInvincibleCard(intel)) selectedCard = getLowestCard(cards, vira);
            else if(!(haveStrongestCard(intel, cards).isEmpty())){
                selectedCard = getLowestCard(haveStrongestCard(intel, cards), vira);
            }
            else selectedCard = getLowestCard(cards, vira);
        }

        return CardToPlay.of(selectedCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return 1;
        else if(situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return 0;
        else if(winFistRound(intel) || drewFistRound(intel)){
            if(isWinning(intel.getScore(),intel.getOpponentScore())
                    && situation == HandsCardSituation.BLUFF_TO_GET_POINTS) return 0;
            else if(isWinning(intel.getScore(),intel.getOpponentScore())
                    && situation == HandsCardSituation.BLUFF_TO_INTIMIDATE) return 0;
            else return -1;
        }
        else return -1;
    }
}
