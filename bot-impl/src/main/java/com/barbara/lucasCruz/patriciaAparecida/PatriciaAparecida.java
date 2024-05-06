package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.max;
import static java.util.Collections.min;


public class PatriciaAparecida implements BotServiceProvider {


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
        if(intel.getHandPoints() > 12) throw new IllegalArgumentException("Cant Increase Points indefinitely");
        return false;
    }

    //fornece o cartão a ser jogado ou descartado na rodada atual.
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(intel.getCards().isEmpty()) throw new IllegalStateException("Cannot choose a card without cards");

        List<TrucoCard> tempcards = new ArrayList<>(intel.getCards());
        TrucoCard vira = intel.getVira();
        tempcards.sort((myCard,otherCard) -> myCard.compareValueTo(otherCard, vira));

        if(intel.getOpponentCard().isPresent()){

            Optional<TrucoCard> weakestCardThatWins = getWeakestCardThatWins(tempcards,intel);

            if(getWeakestCardThatWins(tempcards,intel).isPresent()) return CardToPlay.of(weakestCardThatWins.get());

            Optional<TrucoCard> cardThatDraws = getCardThatDraws(tempcards,intel);
            //tem que considerar a prob das cartas que voce nao vai jogar pra empatar
            //empatar vem antes de ter carta que ganha na primiera rodada
            //se fizemos a primeira vem antes na segunda tb

            if (cardThatDraws.isPresent()) return CardToPlay.of(cardThatDraws.get());


            if(!intel.getRoundResults().isEmpty()) {
                 return CardToPlay.discard(tempcards.stream().findFirst().get());
                 //ta dizendo que no primeiro round a gente só torna pra cima?
            }
            else return CardToPlay.of(tempcards.stream().findFirst().get());

        }
        List<Double> probCards = listProbAllCards(intel);
        List<Double> StrongestCards = probCards.stream().filter(probability -> probability < 0.05).toList();

        if(!StrongestCards.isEmpty()){
            return CardToPlay.of(tempcards.get(tempcards.size() - StrongestCards.size()));
        }

        if(intel.getRoundResults().contains(GameIntel.RoundResult.LOST)) return CardToPlay.of(intel.getCards().get(0));

        if( chanceToDrawIsBetter(intel,tempcards)) return cardWithHighestChanceToDraw(listProbDrawAllCards(intel,tempcards),tempcards);

        return CardToPlay.of(intel.getCards().get(0));
        }


    //responde a uma solicitação de aumento de ponto em uma mão de truco.
    //O valor de retorno deve ser um dos seguintes:
    //-1 (sair), 0 (aceitar), 1 (re-aumentar/chamar);
    @Override
    public int getRaiseResponse(GameIntel intel) {

        return 0;
    }

    public int getNumberOfRounds(GameIntel intel){
        if(intel.getRoundResults().isEmpty()){
            return 1;
        }
        return intel.getRoundResults().size() + 1;
    }

    private Double probabilityOpponentCardDraws(TrucoCard trucoCard, GameIntel intel) {

        int numberOfSameRankCards = 4;
        for (TrucoCard card : intel.getOpenCards()) {
            if(card.getRank() == trucoCard.getRank()) --numberOfSameRankCards;
        }
        final int remainderSameRankCards = getNumberOfRemainderCards(intel) - numberOfSameRankCards;
        return (1 - (Math.pow((double)
                numberOfSameRankCards /remainderSameRankCards,getNumberOfOpponentsCards(intel))));
    }

    private List<Double> listProbDrawAllCards(GameIntel intel ,List<TrucoCard> tempcards) {
        List<Double> listProbDrawAllCards = new ArrayList<>();
        for(int i=0; i<intel.getCards().size(); i++){
            listProbDrawAllCards.add(probabilityOpponentCardDraws(tempcards.get(i),intel));
        }
        return listProbDrawAllCards;
    }

    private CardToPlay cardWithHighestChanceToDraw(List<Double> probabilities, List<TrucoCard> tempcards) {
        final Double maxProbability = min(probabilities);
        probabilities.indexOf(maxProbability);
        return CardToPlay.of(tempcards.get(probabilities.indexOf(maxProbability)));
    }

    private boolean chanceToDrawIsBetter(GameIntel intel, List<TrucoCard> tempcards) {
        List<Double> chanceToDraw = listProbDrawAllCards(intel,tempcards);
        List<Double> chanceToHaveStrongerCard = listProbAllCards(intel);
        final double maxDraw = max(chanceToDraw);
        final double maxStronger = max(chanceToHaveStrongerCard);
        return (maxDraw > maxStronger);
    }

    private Optional<TrucoCard> getCardThatDraws(List<TrucoCard> cards, GameIntel intel) {
        for (TrucoCard card : cards)
            if(card.compareValueTo(intel.getOpponentCard().get(),intel.getVira()) == 0) return Optional.of(card);
        return Optional.empty();
    }

    private Optional<TrucoCard> getWeakestCardThatWins(List<TrucoCard> cards, GameIntel intel) {
        cards = cards.stream().filter(trucoCard -> trucoCard.compareValueTo(intel.getOpponentCard().get(),intel.getVira()) > 0).toList();
        return cards.stream().findFirst();
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
        for(int i=0; i<intel.getCards().size(); i++){
            listProbAllCards.add(probabilityOpponentCardIsBetter(intel.getCards().get(i),intel));
        }
        return listProbAllCards;
    }
}
