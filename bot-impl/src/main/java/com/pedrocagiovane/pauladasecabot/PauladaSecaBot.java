package com.pedrocagiovane.pauladasecabot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


public class PauladaSecaBot implements BotServiceProvider {
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

    private boolean temCasalMenor(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isEspadilha(vira) || card.isOuros(vira)){
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

    private int contManilha(List<TrucoCard> cartas, TrucoCard vira){
        int cont = 0;
        for(TrucoCard card : cartas){
            if(card.isManilha(vira)){
                cont++;
            }
        }
        return cont;
    }

    private Boolean temZap(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isZap(build.getVira()))
                return true;
        }
        return false;
    }

    private Boolean temCopas(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isCopas(build.getVira()))
                return true;
        }
        return false;
    }

    private Boolean temOuros(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isOuros(build.getVira())) {
                return true;
            }
        }
        return false;
    }

    private Boolean temEspada(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isEspadilha(build.getVira())) {
                return true;
            }
        }
        return false;
    }

    public int aumentarAposta(GameIntel build) {

        //verifica se a mão não esta na primeira , se tem zap e se eu ganhei a primeira mão
        if (!build.getRoundResults().isEmpty() && temZap(build) && build.getRoundResults().get(0) == GameIntel.RoundResult.WON)return 1;
        if (!build.getRoundResults().isEmpty() && temCopas(build) && build.getRoundResults().get(0) == GameIntel.RoundResult.WON)return 1;

        return -1;
    }

    public int trucoRato(GameIntel build) {

        //verifica se a mão esta na primeira e se tem casal menor
        if (build.getRoundResults().isEmpty() && temCasalMenor(build)) return 1;

        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel build) {
        Integer qtdManilha = contManilha(build.getCards(),build.getVira());

        // PRIMEIRA: joga pior carta se tiver casal maior
        if (temCasalMaior(build) && build.getRoundResults().isEmpty()){
            return CardToPlay.of(piorCarta(build));
        }

        // PRIMEIRA: tenta amarrar se tiver zap ou copas
        if (qtdManilha == 1 && temZap(build) || qtdManilha == 1 && temCopas(build)) {
            if (build.getRoundResults().isEmpty()) {
                for (TrucoCard card : build.getCards()) {
                    if (card.getRank() == build.getOpponentCard().get().getRank() && !card.isManilha(build.getVira())) {
                        return CardToPlay.of(card);
                    }
                }
            }
        }

        // PRIMEIRA: joga melhor carta se não tiver manilha
        if(qtdManilha == 0){
            return CardToPlay.of(melhorCarta(build));
        }

        // PRIMEIRA: joga ouros ou espadas se tiver
        if(temOuros(build) || temEspada(build)){
            for (TrucoCard card : build.getCards()) {
                if (card.isOuros(build.getVira()) || card.isEspadilha(build.getVira())) {
                    return CardToPlay.of(card);
                }
            }
        }

        return CardToPlay.of(melhorCarta(build));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
