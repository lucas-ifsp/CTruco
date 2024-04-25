package com.pedrocagiovane;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class PauladaSecaBot {
    private boolean temCasalMaior(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isZap(vira) || card.isCopas(vira)){
                contador ++;
            }
        }
        if(contador == 2){
            return true;
        }
        return false;
    }

    private TrucoCard piorCarta(GameIntel build) {
        Integer menor = 1000;
        TrucoCard cartaMenor = null;

        for (TrucoCard carta : build.getCards()) {
            if (carta.isManilha(build.getVira())) {
                if (carta.relativeValue(build.getVira()) < menor) {
                    menor = carta.relativeValue(build.getVira());
                    cartaMenor = carta;
                }
            } else {
                if (carta.getRank().value() < menor) {
                    menor = carta.getRank().value();
                    cartaMenor = carta;
                }
            }
        }
        return cartaMenor;
    }

    private TrucoCard melhorCarta(GameIntel build) {
        Integer maior = 0;
        TrucoCard cartaMaior = null;
        for (TrucoCard carta : build.getCards()) {
                if (carta.getRank().value() > maior) {
                    maior = carta.getRank().value();
                    cartaMaior = carta;
                }
            }
        return cartaMaior;
    }

    public CardToPlay escolherCarta(GameIntel build) {

        if (temCasalMaior(build) && build.getRoundResults().isEmpty()){
            return CardToPlay.of(piorCarta(build));
        }
        return CardToPlay.of(melhorCarta(build));
    }

}
