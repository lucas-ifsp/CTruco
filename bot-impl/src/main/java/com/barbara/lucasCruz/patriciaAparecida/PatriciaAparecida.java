package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatriciaAparecida implements BotServiceProvider {

    /* TIP quando a gente tem que começar:
    //é a primeira rodada?
    //sim:

    //a probabilidade de o oponente ter cartas maiores que cada uma das minhas é:
    //baixa para pelo menos 2 cartas: - definir quanto
    //pedir truco

    //qual(is) a(s) carta(s) com MENOR probabilidade do oponente ter carta maior?
    //se tiver 2 com a mesma probabilidade: jogar a menor
        //se nao: jogar a carta com menor probabilidade do oponente ter carta maior

    //nao:

    //é a segunda rodada? sempre que a gente que começa jogando a segunda é pq ganhamos a primeira
    //sim:
    //a probabilidade de o oponente ter cartas maiores que cada uma das minhas é:
    //baixa para pelo menos uma das restantes: -defifinir quanto
    //pedir truco, aumentar
    //jogar a menor, virada para baixo
    //nao: começar a 3a rodada significa que perdemos a primeira e ganhamos a segunda:
    //a probabilidade de o ter a carta maior que a minha é:
    //baixa: -defifinir quanto
    //pedir truco, aumentar
    //jogar a carta

    //quando o oponente joga e a gente tem que tornar:
    //é a primeira rodada:
    //sim:
    //tenho uma carta que empata?
    //sim:
    //qual a probabilidade do oponente ter uma carta maior que a minha melhor carta?
    //probabilidade baixa: - a ser definida
    //pede truco, aumenta
    //jogo a carta que empata
    //probabilidade alta: - a ser definida
    //tenho uma carta que ganha?
    //sim:
    //joga a minha menor carta que ganha da que o oponente jogou
    //nao:
    //joga a menor carta, virada
    //nao:
    //tenho uma carta que ganha?
    //sim:
    //qual a probabilidade do oponente ter uma carta maior que as minhas cartas - retirando a menor carta que ganha
    //baixa: pede truco, aumenta
    //jogo a menor carta que ganha
    // nao:
    //é a segunda rodada?
    //sim: o oponente começar a segunda rodada significa que perdemos a primeira
    //tenho uma carta que ganha?
    // sim: joga a carta
    // nao: jogo a menor carta da minha mao - PERDEMOS!
    //nao: é a terceira rodada, ganhamos a primeira e o oponente a segunda
    //tenho uma carta que empata ou ganha?
    //sim:
    //pede truco, aumenta
    //joga a carta

    //para pensar: se o oponente inicia o jogo, ele joga geralmente sua maior carta,
    // talvez seja bom guardar as cartas que ele jogou numa lista
    //para pensar: é bom jogar carta virada para baixo pro oponente nao ter referencial
    //quando jogamos uma carta virada para baixo, perdemos seu valor,
    // talvez seja bom guardar as cartas que jogamos numa lista

    //quando aceitar truco, aceitar que aumente?
    //ideia: aumentar sempre que a gente ja ia pedir
    //
    //quando jogar mao de 11?
    //ideias:
    //se o oponente tem menos de 8 pontos da pra escolher uma mao "menos melhor"
    ////se o oponentem tem 8 ou mais pontos tem que ser uma mao "mais melhor"
    */



    //decide se o bot joga uma "mão de onze".
    // Retornar false significa sair da mão.
    //Retornar true significa aceitar e jogar uma mão de três pontos;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
            if(intel.getScore() != 11) throw new IllegalArgumentException("Hand of Eleven can't be called without 11 Points");

        return false;

    }

    //decide se o bot inicia uma solicitação de aumento de ponto.
    //Retornar false significa não fazer nada.
    //Retornar true significa solicitar um aumento de ponto;
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(intel.getHandPoints() > 1) throw new IllegalArgumentException("Points above limit of hand (12 points) ");

        return false;
    }

    //fornece o cartão a ser jogado ou descartado na rodada atual.
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(intel.getCards().isEmpty()) throw new IllegalStateException("Cannot choose a card without cards");


        if(intel.getOpponentCard().isPresent()){
              Optional<TrucoCard> weakestCardThatWins = getWeakestCardThatWins(intel.getCards(),intel);
              if(weakestCardThatWins.isPresent()) return CardToPlay.of(weakestCardThatWins.get());
              else return CardToPlay.discard(sortCards(intel.getCards(),intel).stream().findFirst().get());
        }
        return CardToPlay.of(intel.getCards().get(0));
        }

    private Optional<TrucoCard> getWeakestCardThatWins(List<TrucoCard> cards, GameIntel intel) {
         List<TrucoCard> tempcards = sortCards(cards, intel);
         tempcards = tempcards.stream().filter(trucoCard -> trucoCard.compareValueTo(intel.getOpponentCard().get(),intel.getVira()) > 0).toList();

        return tempcards.stream().findFirst();
    }

    private static List<TrucoCard> sortCards(List<TrucoCard> cards, GameIntel intel) {
        List<TrucoCard> tempcards = new ArrayList<>(cards);
        tempcards.sort((myCard,otherCard) -> myCard.compareValueTo(otherCard, intel.getVira()));
        return tempcards;
    }

    //responde a uma solicitação de aumento de ponto em uma mão de truco.
    //O valor de retorno deve ser um dos seguintes:
    //-1 (sair), 0 (aceitar), 1 (re-aumentar/chamar);
    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    public int getNumberOfCardsInHand(GameIntel intel) {
        return intel.getCards().size();
    }

    public int getNumberOfPlayedCards(GameIntel intel){
        return  intel.getOpenCards().size();
    }

    public int getNumberOfRemainderCards(GameIntel intel) {
        return (40 - getNumberOfCardsInHand(intel) - getNumberOfPlayedCards(intel));
    }

    public int getNumberOfBestCardsInMyHand(TrucoCard card, GameIntel intel){
        int count = 0;

        for(int i = 0; i < getNumberOfCardsInHand(intel); i++){
            if(card.compareValueTo(intel.getCards().get(i),intel.getVira())<0){
                count ++;
            }
        }

        return count;
    }

    public int getNumberOfBestCardsPlayed(TrucoCard card, GameIntel intel){
        int count =0;

        for(int i = 0; i < getNumberOfPlayedCards(intel) ; i++){
            if(card.compareValueTo(intel.getOpenCards().get(i),intel.getVira())<0){
                count++;
            }
        }

        return count;
    }

    public int getNumberOfBestCardsKnown(TrucoCard card, GameIntel intel){
        return getNumberOfBestCardsInMyHand(card, intel) + getNumberOfBestCardsPlayed(card, intel);
    }

    public int getNumberOfBestCardsUnknown(TrucoCard card, GameIntel intel) {

        if (card.isManilha(intel.getVira())) {
            return ((13 - card.relativeValue(intel.getVira())) - getNumberOfBestCardsKnown(card, intel));
        }
        return ((4*(10-card.relativeValue(intel.getVira())))) - getNumberOfBestCardsKnown(card, intel);
    }

    public boolean winTheLastRound (GameIntel intel){
        if (intel.getRoundResults().isEmpty()) {
            return false;
        }
        return intel.getRoundResults().get(intel.getRoundResults().size()-1)
                == GameIntel.RoundResult.WON ;
    }

    public int getNumberOfOpponentsCards(GameIntel intel){
        return  3- intel.getRoundResults().size() - (winTheLastRound(intel) ? 0 : 1 );
    }

    public double probabilityONEOpponentCardIsBetter (TrucoCard card, GameIntel intel){
        return (double) (getNumberOfRemainderCards(intel) - getNumberOfBestCardsUnknown(card, intel)) /getNumberOfRemainderCards(intel);
        //probabilidadeOponenteNaoTerUMACartaMaior = ((qntCartasNaoVistas-qntCartasMaioresDesconhecidas)/qntCartasNaoVistas);
    }

    public double probabilityOpponentCardIsBetter (TrucoCard card, GameIntel intel){
        return (1 - (Math.pow(probabilityONEOpponentCardIsBetter(card,intel), getNumberOfOpponentsCards(intel))));
        //robabilidadeOponenteTerCartaMaior =
        //                ( 1 - (Math.pow(probabilidadeOponenteNaoTerUMACartaMaior, cartasNaMaoDoAdversario))) * 100;
    }

    public List<Double> listProbAllCards(GameIntel intel){
        List<Double> listProbAllCards = new ArrayList<>();
        return listProbAllCards;
    }
}
