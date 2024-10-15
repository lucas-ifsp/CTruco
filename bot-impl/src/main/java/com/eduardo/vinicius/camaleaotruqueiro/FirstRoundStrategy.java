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
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        else if(situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        else if(intel.getOpponentScore()<7 && getNumberOfHighRankCards(intel.getCards(),intel.getVira())>0) return true;
        else return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        else if(situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        else if(situation == HandsCardSituation.BLUFF_TO_GET_POINTS) return true;
        else if(isWinning(intel.getScore(),intel.getOpponentScore())){
            return situation == HandsCardSituation.BLUFF_TO_INTIMIDATE;
        }
        else return false;
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
                selectedCard = getGreatestCard(cards, vira);
            } else {
                selectedCard = getLowestCard(cards, vira);

            }
        }
        else {
            if(!(haveStrongestCard(intel, cards).isEmpty())){
                selectedCard = getLowestCard(haveStrongestCard(intel, cards), vira);
            }
            else if(!haveEqualCard(intel, cards).isEmpty()){
                selectedCard = getLowestCard(haveEqualCard(intel, cards), vira);
            }
            else {
                selectedCard = getLowestCard(cards, vira);
            }
        }
        return CardToPlay.of(selectedCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return 1;
        else if(situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return 0;
        else if(situation == HandsCardSituation.BLUFF_TO_GET_POINTS) return 0;
        else if(situation == HandsCardSituation.BLUFF_TO_INTIMIDATE) return 0;
        else return -1;
    }
}
