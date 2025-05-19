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
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();


        if(manilhaCount(cards, vira) >= 2){
            return true;
        }


        if(hasMaoDeTresManilhas(intel)){
            return true;
        }

        if(hasDuasManilhas(intel)){
            return true;
        }

        if(hasCasalMaior(intel)){
            return true;
        }

        if((!intel.getRoundResults().isEmpty()) && intel.getRoundResults().get(0) != GameIntel.RoundResult.DREW && contaRankCartas(intel) >= 15){
            return true;
        }

        if((intel.getRoundResults().size() == 2) &&  contaRankCartas(intel) >= 10){
            return true;
        }

        if((intel.getRoundResults().size() == 1 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON && contaRankCartas(intel) >= 10)){
            return true;

        }
        if((manilhaCount(cards, intel.getVira()) > 0) && (!intel.getRoundResults().isEmpty()) && intel.getRoundResults().get(0).equals(GameIntel.RoundResult.DREW)){
            if(maiorCartaDaMao(intel).relativeValue(intel.getVira()) > 11){
                return true;
            }
        }


            return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        TrucoCard vira = intel.getVira();
        List<TrucoCard> minhasCartas = intel.getCards();
        Optional<TrucoCard> cartaOponenteOpt = intel.getOpponentCard();


        // 1. Adversário já jogou?
        if (cartaOponenteOpt.isPresent()) {
            TrucoCard cartaOponente = cartaOponenteOpt.get();
            int manilhas = manilhaCount(minhasCartas, vira);

            // Tenta matar com a menor carta que ganhe
            TrucoCard cartaParaMatar = null;
            for (TrucoCard carta : minhasCartas) {
                if (carta.relativeValue(vira) > cartaOponente.relativeValue(vira)) {
                    if (cartaParaMatar == null || carta.relativeValue(vira) < cartaParaMatar.relativeValue(vira)) {
                        cartaParaMatar = carta;
                    }
                }
            }

            // Zap seco tentando amarrar na primeira rodada
            if (manilhas == 1 && hasZap(intel) && intel.getRoundResults().isEmpty()) {
                for (TrucoCard carta : minhasCartas) {
                    if (!carta.isManilha(vira) && carta.getRank() == cartaOponenteOpt.get().getRank()) {
                        return CardToPlay.of(carta);
                    }
                }
            }

            if (cartaParaMatar != null) {
                return CardToPlay.of(cartaParaMatar); // Mata de forma econômica
            } else {
                return CardToPlay.of(maoFraca(intel)); // Não dá pra matar, joga fraca
            }



        }

        // 2. Primeira carta da rodada (ninguém jogou ainda)
        boolean primeiraRodada = intel.getRoundResults().isEmpty();
        int manilhas = manilhaCount(minhasCartas, vira);

        // Se tiver 2 manilhas ou casal maior, joga a mais fraca primeiro
        if ((hasDuasManilhas(intel) || hasCasalMaior(intel)) && primeiraRodada) {
            return CardToPlay.of(maoFraca(intel));
        }

        // Se empatou primeira e tem manilha, tenta definir
        if (!primeiraRodada && empatouPrimeiraRodada(intel) && manilhas > 0) {
            return CardToPlay.of(maiorCartaDaMao(intel)); // Força o jogo
        }

        // Se ganhou a primeira, tenta garantir a vitória jogando a melhor carta possível
        if (!intel.getRoundResults().isEmpty() && ganhouPrimeiraRodada(intel)) {
            return CardToPlay.of(cartasMaisFortesQueAdversari(intel));
        }

        // Se é a segunda rodada e perdeu a primeira, tenta ganhar
        if (intel.getRoundResults().size() == 1 && perdeuPrimeiraRodada(intel)) {
            return CardToPlay.of(cartasMaisFortesQueAdversari(intel));
        }

        if (manilhas > 0) {
            // Se empatou a primeira, joga a mais forte
            if (!intel.getRoundResults().isEmpty() &&
                    intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW) {
                return CardToPlay.of(cartasMaisFortesQueAdversari(intel));
            }

            if ((hasCasalMaior(intel) || hasDuasManilhas(intel)) && intel.getRoundResults().isEmpty()) {
                return CardToPlay.of(maoFraca(intel));
            }
        }




        // Em caso normal, joga a melhor carta
        return CardToPlay.of(maiorCartaDaMao(intel));
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        int manilhas = manilhaCount(intel.getCards(), intel.getVira());

        if (hasCasalMaior(intel)) return 1;
        if (!intel.getRoundResults().isEmpty() && hasZap(intel)) {
            GameIntel.RoundResult primeira = intel.getRoundResults().get(0);
            if (primeira == GameIntel.RoundResult.WON || primeira == GameIntel.RoundResult.DREW) return 1;
        }



        if (manilhas > 0 && contaRankCartas(intel) >= 1) return 0;
        if (manilhas >= 1) return 0;
        if (hasDuasManilhas(intel)) return 0;
        if (hasMaoEquilibrada(intel)) return 0;
        if (blefar(intel)) return 0;


        // Mão fraca, corre (-1)

        if (contaRankCartas(intel) == 0 && !hasDuasManilhas(intel)) return -1;








        return 0;


    }




    int manilhaCount(List<TrucoCard> cards, TrucoCard vira){
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

    private List<TrucoCard> cartasMaisFortesQueAdversario(GameIntel intel) {
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
        List<TrucoCard> cartasMaisFortes = cartasMaisFortesQueAdversario(intel);
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




