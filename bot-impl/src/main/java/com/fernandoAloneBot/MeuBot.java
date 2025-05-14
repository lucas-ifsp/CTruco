package com.fernandoAloneBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

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
            if (card.getRank().value() > 1 && card.getRank().value() <=3) { // Considera dama ou mais
                contador++;
            }
        }
        return contador == 2 && cardVira.isManilha(cardVira);
    }

    private boolean hasMaoFraca(GameIntel intel) {
        int count = 0;
        for (TrucoCard card : intel.getCards()) {
            if ( card.getRank().value()<= 10) {
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
        return isMaoDeOnze(intel) && hasMaoDeTresManilhas(intel);
    }



















    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }


}




