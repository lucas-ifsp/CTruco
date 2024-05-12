
package com.luna.jundi.jokerBot.states;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public interface RoundState {

    //static pois nao muda para rounds
    static boolean getMaoDeOnzeResponse(GameIntel intel) {
        for (TrucoCard card : intel.getCards()) {
            if(card.isZap(intel.getVira()))
                return true;
        }
        if(intel.getOpponentScore() == 11) return true;
        if(intel.getOpponentScore() >=9)return false; //TODO true se tiver carta forte na mao
        if(intel.getOpponentScore() <= 6) return true;

        return false;
    }

    boolean decideIfRaises(GameIntel intel);
    CardToPlay chooseCard(GameIntel intel);
    int getRaiseResponse(GameIntel intel);
}
