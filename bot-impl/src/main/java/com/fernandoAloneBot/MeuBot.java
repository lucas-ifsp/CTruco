package com.fernandoAloneBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MeuBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        if(intel.getOpponentScore() == 11 && intel.getScore() == 11){
            return true;
        }

        if (hasCasalMaior(intel)) {
            return true;
        }

        if(hasDuasManilhas(intel)){
            return true;
        }

        if(hasMaoDeTresManilhas(intel)){
            return true;
        }

        if(hasMaoEquilibrada(intel)){
            return true;
        }

        if(manilhaCount(cards, cardVira) >= 2){
            return true;
        }

        if(hasMaoFraca(intel)){
            return false;
        }



        if(contaRankCartas(intel) == 3 || hasMaoEquilibrada(intel) ){
            return true;
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
    public int getRaiseResponse(GameIntel intel) {

        if (hasCasalMaior(intel)) {
            return 1; // Reaumentar
        } else if (hasZap(intel)) {
            return 0; // Aceitar
        } else {
            return -1; // Fugir
        }




    }




    private int manilhaCount(List<TrucoCard> cards, TrucoCard vira){
        int manilhaCount = 0;
        for (TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                manilhaCount++;
            }
        }
        return manilhaCount;
    }

    // métodos GameIntel

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

    private boolean hasMaoEquilibrada(GameIntel intel) {
        int contador = 0;
        TrucoCard cardVira = intel.getVira();
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() >1 && card.getRank().value() <=3) {//refatorar
                contador++;
            }
        }
        return contador == 2 && cardVira.isManilha(cardVira);
    }

    private boolean hasMaoFraca(GameIntel intel) {
        int count = 0;
        for (TrucoCard card : intel.getCards()) {
            if ( card.getRank().value()<= 7) {
                count++;
            }
        }
        return count == 3;
    }


    public boolean ganhouPrimeiraRodada(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    public boolean ganhouSegundaRodada(GameIntel intel) {
        return intel.getRoundResults().get(1) == GameIntel.RoundResult.WON;
    }
    public boolean perdeuPrimeiraRodada(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST;
    }
    public boolean empatouPrimeiraRodada(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW;
    }

    public boolean maoValePontos(GameIntel intel) {
        return intel.getHandPoints() >= 6;
    }
    public boolean pedirTruco(GameIntel intel) {
        return hasMaoDeTresManilhas(intel) || hasCasalMaior(intel);
    }

    public boolean isMaoDeOnze(GameIntel intel) {
        return intel.getScore() == 11;
    }

    public boolean deveCorrer(GameIntel intel) {
        return hasMaoFraca(intel) && perdeuPrimeiraRodada(intel);
    }

    public boolean blefar(GameIntel intel) {
        return hasMaoEquilibrada(intel) && !isMaoDeOnze(intel);
    }

    public boolean aceitarTruco(GameIntel intel) {
        return hasDuasManilhas(intel) || hasCasalMaior(intel);
    }

    public boolean pedirNove(GameIntel intel) {
        return intel.getScore() >= 9 && hasMaoDeTresManilhas(intel);
    }

    public boolean aceitarMaoDeOnze(GameIntel intel) {
        boolean euTenhoOnze = intel.getScore() == 11;
        boolean adversarioTemOnze = intel.getOpponentScore() == 11;
        boolean minhaMaoEhForte = hasMaoDeTresManilhas(intel) || hasCasalMaior(intel) || hasZap(intel);

        if (euTenhoOnze && adversarioTemOnze) {
            // Ambos têm 11 pontos: joga, mesmo se mão for fraca
            return true;
        }

        if (euTenhoOnze && !adversarioTemOnze) {
            // Só o bot tem 11 pontos: joga apenas se a mão for forte
            return minhaMaoEhForte;
        }

        return false;
    }

    private int contaRankCartas(GameIntel intel){
        Integer contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.getRank().value() >= 10){
                contador += 1;
            }
        }
        return contador;
    }

    private TrucoCard maiorCartaDaMao(GameIntel intel) {
        TrucoCard maior = null;
        for (TrucoCard card : intel.getCards()) {
            if (maior == null || card.relativeValue(intel.getVira()) > maior.relativeValue(intel.getVira())) {
                maior = card;
            }
        }
        return maior;
    }

    private List<TrucoCard> manilhasNaMaoQueGanhamDoAdversario(GameIntel intel) {
        List<TrucoCard> vencedoras = new ArrayList<>();
        Optional<TrucoCard> cartaOponenteOpt = intel.getOpponentCard();

        if (cartaOponenteOpt.isEmpty()) return vencedoras;

        TrucoCard cartaOponente = cartaOponenteOpt.get();
        TrucoCard vira = intel.getVira();

        for (TrucoCard minhaCarta : intel.getCards()) {
            if (minhaCarta.isManilha(vira) &&
                    minhaCarta.relativeValue(vira) > cartaOponente.relativeValue(vira)) {
                vencedoras.add(minhaCarta);
            }
        }

        return vencedoras;
    }





















    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }


}




