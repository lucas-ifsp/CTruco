package com.fernandoAloneBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class MeuBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
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
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }





    // Verifica se o jogador tem duas manilhas
    private boolean hasDuasManilhas(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        int contador = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(cardVira)) {
                contador++;
            }
        }
        return contador >= 2;
    }


    private boolean hasCasalMaior(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        boolean hasZap = false;
        boolean hasCopas = false;

        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(cardVira)) {
                hasZap = true;
            }
            if (card.isCopas(cardVira)) {
                hasCopas = true;
            }
        }

        return hasZap && hasCopas;
    }



    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }


}




