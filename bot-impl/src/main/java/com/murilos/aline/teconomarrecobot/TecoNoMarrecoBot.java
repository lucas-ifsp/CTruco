package com.murilos.aline.teconomarrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class TecoNoMarrecoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        if(possuiCasalMaior(intel)){
            return true;
        }

        return false;

    }
    public boolean possuiCasalMaior(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard cardVira = intel.getVira();

        for(int i = 0; i < 3; i++){
            if (cards.get(i).isZap(cardVira)) {
                for (int k = 0; k < 3; k++) {
                    if (cards.get(k).isCopas(cardVira)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {return 0;}


}
