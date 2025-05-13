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

        if (hasCasalMaior(intel)) {
            return 1; // Reaumentar
        } else if (hasZap(intel)) {
            return 0; // Aceitar
        } else {
            return -1; // Fugir
        }




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

    // Verifica se tem o Zap
    private Boolean hasZap(GameIntel intel){
        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira()))
                return true;
        }
        return false;
    }

    private boolean hasMaoDeTresManilhas(GameIntel intel) {
        int boas = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value()  >= 10) {
                boas++;
            }
        }
        return boas == 3;
    }

    private boolean hasMaoEquilibrada(GameIntel intel) {
        int contador = 0;
        TrucoCard cardVira = intel.getVira();
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() > 1 && card.getRank().value() <=3) { // Considera dama ou mais
                contador++;
            }
        }
        return contador == 2 && cardVira.isManilha(cardVira);
    }






    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }


}




