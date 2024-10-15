package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

import static com.eduardo.vinicius.camaleaotruqueiro.TrucoUtils.*;


public class ThirdRoundStrategy implements RoundStrategy{
    public ThirdRoundStrategy(GameIntel intel) {
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(winFistRound(intel)) return true;
        else if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        else if((winFistRound(intel) || drewFistRound(intel)) &&
                situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        else return false;
        //todo
        // pedir truco quando tem carta forte, mesmo não ganhando ou empatando a primeira
        // pedir truco quando oponent jogar carta vencível,
        // não pedir quando carta é invencível (mesma ganhando primeiro round)
    }
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard card = getGreatestCard(cards,vira);
        return CardToPlay.of(card);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return 1;
        else if(situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return 0;
        else return -1;
        //todo nunca ocorrerá situacao de Bluff_to_get_points ou Bluff_to_intimidate
    }
}
