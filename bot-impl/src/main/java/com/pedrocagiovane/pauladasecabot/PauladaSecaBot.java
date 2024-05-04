package com.pedrocagiovane.pauladasecabot;
import com.bueno.spi.model.CardRank;
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

    private boolean temCasalPreto(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isZap(vira) || card.isEspadilha(vira)){
                contador ++;
            }
        }
        if(contador == 2){
            return true;
        }
        return false;
    }

    private boolean temCasalVermelho(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isCopas(vira) || card.isOuros(vira)){
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

    private TrucoCard cartaMedia(GameIntel build) {
        TrucoCard cartaMedia = null;
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() != melhorCarta(build).getRank().value() && carta.getRank().value() != piorCarta(build).getRank().value()) {
                cartaMedia = carta;
            }
        }
        return cartaMedia;
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

    private int contTres(GameIntel build) {
        int contadorTres = 0;
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 10) {
                contadorTres++;
            }
        }
        return contadorTres;
    }
    private int contDois(GameIntel build) {
        int contadorDois = 0;
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 9) {
                contadorDois++;
            }
        }
        return contadorDois;
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
        if (intel.getRoundResults().isEmpty() && temCasalMenor(intel)){
            System.out.println("truco se tiver casal menor primeira");
            return true;
        }
        // SEGUNDA: se tiver casal maior pede truco
        if (!intel.getRoundResults().isEmpty() && temCasalMaior(intel)) {
            System.out.println("truco se tiver casal maior segunda");
            return true;
        }
        // SEGUNDA: se tiver casal menor pede truco
        if (!intel.getRoundResults().isEmpty() && temCasalMenor(intel)) {
            System.out.println("truco se tiver casal menor segunda");
            return true;
        }

        // JOGA PRIMEIRO
        if (!intel.getOpponentCard().isPresent()) {
            //TERCEIRA: pede truco se tiver três ou manilha
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2){
                System.out.println("terceira tem manilha ou 3");
                if( temTres(intel) || contManilha(intel.getCards(), intel.getVira()) > 0) {
                    return true;
                }
            }

            //SEGUNDA: se tiver ganhado a primeira e tem manilha pra segunda, pede truco
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
                System.out.println("segunda, truco se ganhando primeira e tem manilha pra segunda");
                if( contManilha(intel.getCards(), intel.getVira()) > 0) {
                    return true;
                }
            }

            // SEGUNDA: se tiver feito a primeira e tem 3 pra segunda
            if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
                if(temTres(intel)) {
                    System.out.println("truco se fez a primeira e tem 3 pra segunda");
                    return true;
                }
            }
        }

        // JOGA DEPOIS
        else if (intel.getOpponentCard().isPresent()) {

            //TERCEIRA: pede truco se consegue amarrar a terceira
            if (intel.getRoundResults().size() == 2 && intel.getRoundResults().get(1) == GameIntel.RoundResult.LOST) {
                System.out.println("truco se amarra terceira e ganhou primeira");
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if(!opponentCard.isManilha(intel.getVira())) {
                        for (TrucoCard carta : intel.getCards()) {
                            if (carta.getRank().equals(opponentCard.getRank())) {
                                return true;
                            }
                        }
                    }
                }
            }
            //TERCEIRA: BLEFE se a carta do oponente for um valete ou menor(sem ser manilha) e se tiver ganho a primeira, TRUCA no safado!!
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
                System.out.println("blefe");
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if(!opponentCard.isManilha(intel.getVira())){
                        if(opponentCard.getRank().value() <= 6){
                            return true;
                        }
                    }
                }
            }
            //TERCEIRA: trucar se a carta for maior do que a do oponente e se não for manilha e tiver ganho a primeira
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
                System.out.println("terceira truco se carta for maior e nao for manilha");
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if(!opponentCard.isManilha(intel.getVira())){
                        for (TrucoCard carta : intel.getCards()) {
                            if(opponentCard.getRank().value() < carta.getRank().value()){
                                return true;
                            }
                        }
                    }
                }
            }
            //TERCEIRA: trucar se a carta for maior do que a do oponente e for manilha
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2) {
                System.out.println("terceira truco se carta for maior e for manilha");
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if(opponentCard.isManilha(intel.getVira())){
                        for (TrucoCard carta : intel.getCards()) {
                            if(matarManilha(intel).getSuit().ordinal() > opponentCard.getSuit().ordinal()){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("não trucamo");
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel build) {
        Integer qtdManilha = contManilha(build.getCards(),build.getVira());

        // PRIMEIRA: joga pior carta se tiver casal maior
        if (temCasalMaior(build) && build.getRoundResults().isEmpty()){
            System.out.printf("temos casal maior e jogamos %s", piorCarta(build));
            return CardToPlay.of(piorCarta(build));
        }

        // PRIMEIRA: joga pior carta se tiver casal menor
        if (temCasalMenor(build) && build.getRoundResults().isEmpty()){
            System.out.printf("temos casal menor e jogamos %s", piorCarta(build));
            return CardToPlay.of(piorCarta(build));
        }

        // PRIMEIRA: joga pior carta se tiver casal preto
        if (temCasalPreto(build) && build.getRoundResults().isEmpty()){
            System.out.printf("temos casal preto e jogamos %s", piorCarta(build));
            return CardToPlay.of(piorCarta(build));
        }

        // PRIMEIRA: joga pior carta se tiver casal vermelho
        if (temCasalVermelho(build) && build.getRoundResults().isEmpty()){
            System.out.printf("temos casal vermelho e jogamos: %s", piorCarta(build));
            return CardToPlay.of(piorCarta(build));
        }

        // JOGA PRIMEIRO
        if (!build.getOpponentCard().isPresent()) {

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
        }

        // JOGA DEPOIS DO PATO
        if (build.getOpponentCard().isPresent()) {

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

            // mata a manilha oponente com uma manilha maior
            if (build.getOpponentCard().get().isManilha(build.getVira())) {
                return CardToPlay.of(matarManilha(build));
            }

            // mata carta do oponente com a menor que tiver se possivel se nao tiver manilha
            if(!build.getOpponentCard().get().isManilha(build.getVira())){
                return CardToPlay.of(matarCartaComMenor(build));
            }
        }
        return CardToPlay.of(melhorCarta(build));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        //vendo qtdd manilha
        int manilha =  contManilha(intel.getCards(),intel.getVira());
        //vendo qtdd de tres
        int quantTres = contTres(intel);

        int quantDois = contDois(intel);

        //verifica se a mão não esta na primeira , se tem zap e se eu ganhei a primeira mão
        if (!intel.getRoundResults().isEmpty() && temZap(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
            System.out.println("aceitou truco se ganhou a primeira e tem zap");
            return 1;
        }
        //verifica se a mão não esta na primeira , se tem copas e se eu ganhei a primeira mão
        if (!intel.getRoundResults().isEmpty() && temCopas(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
            System.out.println("aceitou truco se ganhou a primeira e tem copas");
            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal vermelho e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalVermelho(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {
            System.out.println("aceitou truco se perdeu mas tem casal vermelho");
            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal preto e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalPreto(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {
            System.out.println("aceitou truco se perdeu mas tem casal preto");
            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal maior e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalMaior(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {
            System.out.println("aceitou truco se perdeu mas tem casal maior");
            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal menor e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalMenor(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {
            System.out.println("aceitou truco se perdeu mas tem casal menor");
            return 1;
        }

        //verifica se a mão não esta na primeira , se tem mais de um 2 e se eu ganhei a primeira mão
        if ( quantDois > 1 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
            System.out.println("aceitou truco se ganhou a primeira e ainda tem dois 2");
            return 1;
        }

        //se tivermos mais de uma, independente do nipe, desce
        if (manilha > 1) {
            System.out.println("aceitou truco com mais de uma manilha");
            return 0;
        }
        //se tivermos manilha e tres
        if (manilha >= 1 && temTres(intel)) {
            System.out.println("aceitou trucco com manilha e tres de uma manilha");
            return 0;
        }

        if (temDois(intel) && temTres(intel)) {
            System.out.println("aceitou trucco com dois e tres");
            return 0;
        }

        //se tivermos mais de um tres
        if (quantTres > 1) {
            System.out.println("aceitou truco com mais de um tres");
            return 0;
        }
        // se tiver feito a primeira e tem tres pra segunda
        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON && temTres(intel)) {
            System.out.println("fez a primeira e aceitou truco com tres na segunda");
            return 0;
        }

        //se temos um tres na terceira e ganhamos a primeira
        if (!intel.getRoundResults().isEmpty() && temTres(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
            System.out.println("aceitou truco com tres na terceira");
            return 0;
        }

        System.out.println("corremo");
        return -1;
    }
}
