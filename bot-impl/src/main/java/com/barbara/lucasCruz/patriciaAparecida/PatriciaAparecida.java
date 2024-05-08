/*
 *  Copyright (C) 2024 Bárbara Branc oGasques - IFSP/SCL and Lucas da Silva Cruz - IFSP/SCL
 *  Contact: barbara <dot> branco <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: a <dot> strapasson <at> aluno <dot> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */


package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.Collections.min;


public class PatriciaAparecida implements BotServiceProvider {

    static final double LOWER_PROB_RAISE_RESPONSE = 0.11;
    static final double UPPER_PROB_RAISE_RESPONSE = 0.22;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
            if(intel.getScore() != 11) throw new IllegalArgumentException("Hand of Eleven can't be called without 11 Points");

            return true;

    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(intel.getHandPoints() > 12) throw new IllegalArgumentException("Cant Increase Points indefinitely");
        return (getRaiseResponse(intel) == 1 || getRaiseResponse(intel) == 0);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(intel.getCards().isEmpty()) throw new IllegalStateException("Cannot choose a card without cards");

        List<TrucoCard> tempcards = new ArrayList<>(intel.getCards());
        TrucoCard vira = intel.getVira();
        tempcards.sort((myCard,otherCard) -> myCard.compareValueTo(otherCard, vira));

        boolean opponentHasPlayedFirst = intel.getOpponentCard().isPresent();
        if(opponentHasPlayedFirst) return getCardToReturn(intel, tempcards);
        return getCardToPlayFirst(intel, tempcards);
    }

    private CardToPlay getCardToPlayFirst(GameIntel intel, List<TrucoCard> tempcards) {
        List<Double> probCards = listProbAllCards(intel);
        final CardToPlay strongestCard = getCardWithBestProbability(tempcards, probCards);
        if( chanceToDrawIsBetter(intel, tempcards)) return cardWithHighestChanceToDraw(listProbDrawAllCards(intel, tempcards), tempcards);
        return strongestCard;
    }

    private CardToPlay getCardWithBestProbability(List<TrucoCard> tempcards, List<Double> probCards) {
        List<Double> StrongestCards = probCards.stream().filter(probability -> probability < 0.05).toList();

        if(!StrongestCards.isEmpty()) return CardToPlay.of(tempcards.get(tempcards.size() - StrongestCards.size()));
        return CardToPlay.of(tempcards.get(tempcards.size()-1));
    }

    private CardToPlay getCardToReturn(GameIntel intel, List<TrucoCard> tempcards) {
        final CardToPlay weakestCardThatWins = returnWeakestThatWins(intel, tempcards);
        if (weakestCardThatWins != null) return weakestCardThatWins;

        final CardToPlay cardThatDraws = returnCardThatDraws(intel, tempcards);
        if (cardThatDraws != null) return cardThatDraws;

        return returnWeakestCardThatLoses(intel, tempcards);
    }

    private CardToPlay returnWeakestCardThatLoses(GameIntel intel, List<TrucoCard> tempcards) {
        if(!intel.getRoundResults().isEmpty())  return CardToPlay.discard(tempcards.stream().findFirst().get());
        else return CardToPlay.of(tempcards.stream().findFirst().get());
    }

    private CardToPlay returnCardThatDraws(GameIntel intel, List<TrucoCard> tempcards) {
        //nao consegue fazer, tenta achar carta que empata
        Optional<TrucoCard> cardThatDraws = getCardThatDraws(tempcards, intel);

        //se tiver carta que empata, empatar
        return cardThatDraws.isPresent() ? CardToPlay.of(cardThatDraws.get()) : null;
    }

    private CardToPlay returnWeakestThatWins(GameIntel intel, List<TrucoCard> tempcards) {
        Optional<TrucoCard> weakestCardThatWins = getWeakestCardThatWins(tempcards, intel);
        return getWeakestCardThatWins(tempcards, intel).isPresent() ? CardToPlay.of(weakestCardThatWins.get()) : null;
    }

    public int countProbs (double prob, GameIntel intel){
        List<Double> listProb = listProbAllCards(intel);
        int count = 0;

        for(int i=0; i<listProb.size(); i++){
            if(listProb.get(i)< prob){
                count++;
            }
        }
        return count;
    }

    public int getRaiseResponseWithRemoveProbCardThatWin(double prob, GameIntel intel){
        int verifyHight = 0;
        int verifyLower = 0;

        if(prob < UPPER_PROB_RAISE_RESPONSE){
            verifyHight = 1;
            if(prob < LOWER_PROB_RAISE_RESPONSE){
                verifyLower = 1;
            }
        }

        if(countProbs(LOWER_PROB_RAISE_RESPONSE, intel) - verifyLower >= 1){ return 1; }
        if((countProbs(UPPER_PROB_RAISE_RESPONSE, intel) - verifyHight) >= 1){ return 0; }
        return -1;
    }

    int getRaiseResponseIfOpponentStarts (GameIntel intel){
        List<TrucoCard> tempcards = new ArrayList<>(intel.getCards());
        TrucoCard vira = intel.getVira();
        tempcards.sort((myCard,otherCard) -> myCard.compareValueTo(otherCard, vira));

        Optional<TrucoCard> tempCardThatWins = getWeakestCardThatWins(tempcards,intel);
        if (tempCardThatWins.isPresent()) {
            TrucoCard cardThatWins = tempCardThatWins.get();
            double probCardThatWins = probabilityOpponentCardIsBetter(cardThatWins,intel);
            return getRaiseResponseWithRemoveProbCardThatWin(probCardThatWins,intel);
        }

        return -1;
    }

    int getRaiseResponse1Round (GameIntel intel){

        if(intel.getOpponentCard().isEmpty()){
            if(countProbs(LOWER_PROB_RAISE_RESPONSE, intel) >= 2){ return 1; }
            if(countProbs(UPPER_PROB_RAISE_RESPONSE, intel) >= 2){ return 0; }
        }
        return getRaiseResponseIfOpponentStarts(intel);
    }

    int getRaiseResponse2Round (GameIntel intel){

        if(intel.getOpponentCard().isEmpty()){
            if(countProbs(LOWER_PROB_RAISE_RESPONSE, intel) >= 1){ return 1; }
            if(countProbs(UPPER_PROB_RAISE_RESPONSE, intel) >= 1){ return 0; }
        }

        return getRaiseResponseIfOpponentStarts(intel);
    }

    int getRaiseResponse3Round(GameIntel intel){
        List<TrucoCard> tempcards = new ArrayList<>(intel.getCards());
        TrucoCard vira = intel.getVira();
        tempcards.sort((myCard,otherCard) -> myCard.compareValueTo(otherCard, vira));
        List<Double> listProb = listProbAllCards(intel);
        if(intel.getOpponentCard().isEmpty()){
            if(listProb.get(0) < 0.1){ return 1; }
            if(listProb.get(0) < 0.2){ return 0; }
        }
        boolean WeakestCardThatWinsExists = getWeakestCardThatWins(tempcards, intel).isPresent();
        boolean CardDrawsExists = getCardThatDraws(tempcards, intel).isPresent();
        if (WeakestCardThatWinsExists || CardDrawsExists){
            return 1;
        }
        return -1;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        int round = getNumberOfRounds(intel);

        switch (round){
            case 1: return getRaiseResponse1Round(intel);
            case 2: return getRaiseResponse2Round(intel);
            case 3: return getRaiseResponse3Round(intel);
        }

        return -1;
    }

    public int getNumberOfRounds(GameIntel intel){
        if(intel.getRoundResults().isEmpty()){
            return 1;
        }
        return intel.getRoundResults().size() + 1;
    }

    private Double probabilityOpponentCardDraws(TrucoCard trucoCard, GameIntel intel) {

        int numberOfSameRankCards = 4;
        for (TrucoCard card : intel.getOpenCards()) if (card.getRank() == trucoCard.getRank()) --numberOfSameRankCards;
        for (TrucoCard card : intel.getCards()) if (card.getRank() == trucoCard.getRank()) --numberOfSameRankCards;
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
        final double maxDraw = min(chanceToDraw);
        final double maxStronger = min(chanceToHaveStrongerCard);
        return (maxDraw < maxStronger);
    }

    public Optional<TrucoCard> getCardThatDraws(List<TrucoCard> cards, GameIntel intel) {
        if(!cards.isEmpty() && intel.getOpponentCard().isPresent()){
        for (TrucoCard card : cards)
            if(card.compareValueTo(intel.getOpponentCard().get(),intel.getVira()) == 0) return Optional.of(card);
        }
        return Optional.empty();
    }

    public Optional<TrucoCard> getWeakestCardThatWins(List<TrucoCard> cards, GameIntel intel) {
        if(!cards.isEmpty() && intel.getOpponentCard().isPresent()){
        cards = cards.stream().filter(trucoCard -> trucoCard.compareValueTo(intel.getOpponentCard().get(),intel.getVira()) > 0).toList();
        return cards.stream().findFirst();
        }
        return Optional.empty();
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
    }

    public double probabilityOpponentCardIsBetter (TrucoCard card, GameIntel intel){
        return( 1 - (Math.pow(probabilityONEOpponentCardIsBetter(card,intel), getNumberOfOpponentsCards(intel))));
    }

    public List<Double> listProbAllCards(GameIntel intel){
        List<Double> listProbAllCards = new ArrayList<>();
        for(int i=0; i<intel.getCards().size(); i++){
            listProbAllCards.add(probabilityOpponentCardIsBetter(intel.getCards().get(i),intel));
        }
        return listProbAllCards;
    }
}