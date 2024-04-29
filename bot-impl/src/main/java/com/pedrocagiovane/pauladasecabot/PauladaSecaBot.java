package com.pedrocagiovane.pauladasecabot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
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

    private List<TrucoCard> melhoresCartas(GameIntel build){
        List<TrucoCard> maioresCartas = new ArrayList<>();
        for (TrucoCard trucoCard : build.getCards()) {
            if (trucoCard.getRank().value() > build.getOpponentCard().get().getRank().value()) {
                maioresCartas.add(trucoCard);
            }
        }
        return maioresCartas;
    }

    private TrucoCard matarCartaComMenor(GameIntel build) {
        TrucoCard trucoCard = null;
        List<TrucoCard> maioresCartas = melhoresCartas(build);
        if (!maioresCartas.isEmpty()) {
            trucoCard = maioresCartas.get(0);
            for (TrucoCard carta : maioresCartas) {
                if (carta.getRank().value() < trucoCard.getRank().value()) {
                    trucoCard = carta;
                }
            }
        } else {
            trucoCard = piorCarta(build);
        }
        return trucoCard;
    }

    private List<TrucoCard> manilhasOponente(GameIntel build){
        List<TrucoCard> manilhas = new ArrayList<>();
        for (TrucoCard carta : build.getCards()) {
            if (carta.isManilha(build.getVira())) {
                if (carta.relativeValue(build.getVira()) > build.getOpponentCard().get().relativeValue(build.getVira())){
                    manilhas.add(carta);
                }
            }
        }
        return manilhas;
    }

    private TrucoCard matarManilha(GameIntel build) {
        List<TrucoCard> manilhas = manilhasOponente(build);
        TrucoCard cardPlay = piorCarta(build);
        if (!manilhas.isEmpty()) {
            cardPlay = manilhas.get(0);
            for (TrucoCard manilha : manilhas) {
                if (manilha.relativeValue(build.getVira()) < cardPlay.relativeValue(build.getVira())){
                    cardPlay = manilha;
                }
            }
        }
        return cardPlay;
    }

    private Boolean temTres(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 10) {
                return true;
            }
        }
        return false;
    }

    private Boolean temDois(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 9) {
                return true;
            }
        }
        return false;
    }




    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        // PRIMEIRA: verifica se a mão esta na primeira e se tem casal menor
        if (intel.getRoundResults().isEmpty() && temCasalMenor(intel)) return true;

        // SEGUNDA: se tiver casal maior pede truco
        if (!intel.getRoundResults().isEmpty() && temCasalMaior(intel)) return true;

        //SEGUNDA: se tiver ganhado a primeira e tem manilha pra segunda, pede truco
        if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
            if( contManilha(intel.getCards(), intel.getVira()) > 0) return true;
        }

        //TERCEIRA: pede truco se tiver dois ou três
        if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2){
            if( temTres(intel) || temDois(intel) || contManilha(intel.getCards(), intel.getVira()) > 0) return true;
        }

        //TERCEIRA: pede truco se consegue amarrar a terceira
        if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2){
            for (TrucoCard carta : intel.getCards()){
                if(carta.getRank().equals(intel.getOpponentCard().get().getRank())) return true;
            }

        }


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

        //jogar depois do pato
        if (build.getOpponentCard().isPresent()) {
            if (build.getOpponentCard().get().isManilha(build.getVira())) {
                return CardToPlay.of(matarManilha(build));
            }
            return CardToPlay.of(matarCartaComMenor(build));
        }
        return CardToPlay.of(melhorCarta(build));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        //verifica se a mão não esta na primeira , se tem zap e se eu ganhei a primeira mão
        if (!intel.getRoundResults().isEmpty() && temZap(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)return 1;

        if (!intel.getRoundResults().isEmpty() && temCopas(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)return 1;

        return -1;
    }
}
