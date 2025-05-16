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

        TrucoCard vira = intel.getVira();
        List<TrucoCard> cartas = intel.getCards();
        Optional<TrucoCard> cartaOponente = intel.getOpponentCard();

        int manilhas = manilhaCount(cartas, vira);



        if (cartaOponente.isPresent()) {
            // se tiver casal maior ou duas manilhas na primeira rodada, joga carta fraca
            if ((hasCasalMaior(intel) || hasDuasManilhas(intel)) && intel.getRoundResults().isEmpty()) {
                return CardToPlay.of(maoFraca(intel));
            }

            // tenta amarrar se tiver zap seco e for a primeira rodada ---
            if (manilhas == 1 && hasZap(intel) && intel.getRoundResults().isEmpty()) {
                for (TrucoCard carta : intel.getCards()) {
                    if (!carta.isManilha(vira) && carta.getRank() == cartaOponente.get().getRank()) {
                        return CardToPlay.of(carta); // Amarra com a mesma carta (sem ser manilha)
                    }
                }
            }

            // tenta matar a manilha com uma manilha mais forte
            List<TrucoCard> minhasManilhas = manilhasNaMaoQueGanhamDoAdversario(intel);

            if (!minhasManilhas.isEmpty()) {
                return CardToPlay.of(cartasMaisFortesQueAdversari(intel));
            } else {
                return CardToPlay.of(maoFraca(intel));
            }



        }

        // joga se tiver manilha
        if (manilhas>0){

            // se tiver amarrado joga maior manilha
            if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
                return CardToPlay.of(cartasMaisFortesQueAdversari(intel));
            }
            //se tiver casal maior joga nada na primeira
            if (hasCasalMaior(intel) && intel.getRoundResults().isEmpty()){
                return CardToPlay.of(maoFraca(intel));
            }

            if(hasDuasManilhas(intel)&& intel.getRoundResults().isEmpty()){
                return CardToPlay.of(maoFraca(intel));
            }
        }





        return CardToPlay.of(maiorCartaDaMao(intel));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {


        return 0;


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


    private TrucoCard maoFraca(GameIntel intel) {
        TrucoCard weakest = null;
        int menorValor = Integer.MAX_VALUE;
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int valor = card.isManilha(vira) ? card.relativeValue(vira) : card.getRank().value();

            if (valor < menorValor) {
                menorValor = valor;
                weakest = card;
            }
        }

        return weakest;
    }
    // Auxiliar para pegar as cartas que vencem a do adversário
    private TrucoCard cartasMaisFortesQueAdversari(GameIntel intel) {
        TrucoCard maisForte = null;
        TrucoCard vira = intel.getVira();

        for (TrucoCard carta : intel.getCards()) {
            if (maisForte == null || carta.relativeValue(vira) > maisForte.relativeValue(vira)) {
                maisForte = carta;
            }
        }

        return maisForte;
    }

    private List<TrucoCard> cartasMaisFortesQueAdversarioo(GameIntel intel) {
        List<TrucoCard> cartasFortes = new ArrayList<>();

        // Primeiro, verifica se o oponente já jogou uma carta
        if (intel.getOpponentCard().isEmpty()) {
            return cartasFortes; // retorna lista vazia se não tem carta do oponente
        }

        TrucoCard cartaOponente = intel.getOpponentCard().get();
        int valorCartaOponente = cartaOponente.getRank().value();

        for (TrucoCard carta : intel.getCards()) {
            if (carta.getRank().value() > valorCartaOponente) {
                cartasFortes.add(carta);
            }
        }
        return cartasFortes;
    }


    private TrucoCard matarCarta(GameIntel intel) {
        List<TrucoCard> cartasMaisFortes = cartasMaisFortesQueAdversarioo(intel);
        TrucoCard cartaParaJogar;
        TrucoCard vira = intel.getVira();

        if (!cartasMaisFortes.isEmpty()) {
            cartaParaJogar = cartasMaisFortes.get(0);
            for (TrucoCard carta : cartasMaisFortes) {
                if (carta.relativeValue(vira) < cartaParaJogar.relativeValue(vira)) {
                    cartaParaJogar = carta;
                }
            }
        } else {
            cartaParaJogar = maoFraca(intel);
        }

        return cartaParaJogar;
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
        int fracas = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.getRank().value() <= 10) {
                fracas++;
            }
        }

        return fracas == 2 && perdeuPrimeiraRodada(intel);
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




