package com.gustavo.contiero.lazybot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;


public class LazyBot implements BotServiceProvider {


    private List<TrucoCard> myCards;
    private TrucoCard vira;
    private TrucoCard bestCard;
    private TrucoCard secondBestCard;
    private TrucoCard worstCard;

    private void setMyCards(List<TrucoCard> myCards) {
        if (myCards == null) this.myCards = new ArrayList<>();
        else{
            this.myCards = myCards;
        }
    }

    private void setVira(TrucoCard vira) {
        this.vira = vira;
    }

    private void setCardsQuality(){
        if (! myCards.isEmpty()){
            setBestCard();
            setWorstCard();
            setSecondBestCard();
        }
        else{
            this.bestCard = TrucoCard.closed();
            this.secondBestCard = TrucoCard.closed();
            this.worstCard = TrucoCard.closed();
        }
    }

    private void setBestCard() {
        bestCard = myCards.get(0);
        for (TrucoCard card : myCards){
            if (card.relativeValue(vira) > bestCard.relativeValue(vira)){
                bestCard = card;
            }
        }
    }

    private void setWorstCard() {
        worstCard = myCards.get(0);
        for (TrucoCard card : myCards){
            if (card.relativeValue(vira) < worstCard.relativeValue(vira)){
                worstCard = card;
            }
        }
    }

    private void setSecondBestCard(){
        this.secondBestCard = bestCard;
        for (TrucoCard card : myCards){
            if (card != bestCard && card != worstCard){
                this.secondBestCard = card;
            }
        }
    }

    private void setState(GameIntel intel){
        setMyCards(intel.getCards());
        setVira(intel.getVira());
        setCardsQuality();
    }

    /**
     * <p>Decides if the bot wants to play the "mão de onze". If it decides to play, the hand points will be
     * increased to 3. Otherwise, the bot loses the current hand and the opponent receives 1 point.</p>
     *
     * @ return {@code true} if it accepts to play the hand or {@code false} if it quits.
     */
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        setState(intel);
        int oponentScore = intel.getOpponentScore();
        double powerLevel = powerLevelOfTheTwoBestCards();
        double powerNeededToAccept = 7.0;
        if (oponentScore >= 9) powerNeededToAccept = 10;
        else if (oponentScore >= 6) powerNeededToAccept = 8.5;
        return powerLevel >= powerNeededToAccept;
    }

    /**
     * <p>Decides if the bot wants to request a hand points raise.</p>
     *
     * @return {@code true} if it wants to request a hand points raise or {@code false} otherwise.
     */

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        setState(intel);
        double powerLevel = powerLevelAllCards();
        double powerToDecideIfRaises = 8.5;
        int opponentCardOnTableValue;
        if (intel.getOpponentCard().isPresent()){
            opponentCardOnTableValue = intel.getOpponentCard().get().relativeValue(vira);
        }
        else{
            opponentCardOnTableValue = 0;
        }
        if (firstRound(intel) && firstToPlay(intel)){
            return powerLevel >= powerToDecideIfRaises;
        }
        else if (firstRound(intel)){
            if (bestCard.relativeValue(vira) >= opponentCardOnTableValue){
                return powerLevelAllCards() >= 7.5;
            }
            else return false;
        }
        else{
            boolean iWonFirstRound = (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON);

            if (iWonFirstRound){
                return bestCard.relativeValue(vira) >= opponentCardOnTableValue;
            }
            else{
                return bestCard.relativeValue(vira) > opponentCardOnTableValue;
            }
        }
    }

    /**
     * <p>Choose a card to be played or discarded. The card is represented by a {@link CardToPlay} object,
     * which wraps a {@link TrucoCard} and adds information about whether it should be played or discarded.</p>
     *
     * @return a TrucoCard representing the card to be played or discarded.
     */
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        setState(intel);
        if (firstRound(intel)){
            return (firstRoundChooseCard(intel));
        }
        else if (secondRound(intel)){
            return(secondRoundChooseCard(intel));
        }
        else{
            return CardToPlay.of(bestCard);
        }
    }

    /**
     * <p>Decides what the bot does when the opponent requests to increase the hand points. If the bot decides to
     * quit, it loses the hand. If it decides to accept, the hand points will be increased and the game continues.
     * If it decides to re-raise, the hand points will be increased and a higher bet will be placed to the bot
     * opponent. If the current hand points request is already enough to the losing player to win and the bot decides
     * to re-raise, the decision will be considered as acceptance and no request will be made to the opponent.</p>
     *
     * @return {@code -1} if the bot quits, {@code 0} if it accepts, and {@code 1} if bot wants to place a re-raise
     * request.
     */
    @Override
    public int getRaiseResponse(GameIntel intel) {  // in BETA
        setState(intel);
        double twoBestCardsPowerLevel = powerLevelOfTheTwoBestCards();
        int handPoints = intel.getHandPoints();
        int opponentScore = intel.getOpponentScore();
        int scoreDifference = opponentScore - intel.getScore();
        if (scoreDifference > 4 && handPoints <= 3 || twoBestCardsPowerLevel >= 11) return playingAgressive(intel);
        return playingSafe(intel);
    }

    /**
     * <p>Returns the bot name. By default, the bot name is the name of the class implementing this interface.</p>
     *
     * @return The bot name that will be used during the game.
     */
    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    private double powerLevelAllCards() {
        if (! myCards.isEmpty()) return absoluteCardsPowerCounter(myCards) / myCards.size();
        else return 0;
    }

    private double powerLevelOfTheTwoBestCards() {
        if (myCards.size() != 3) return 0;
        List<TrucoCard> theTwoBestCards = twoBestCardsFinder(myCards, vira);
        //Best value = 13,5
        return absoluteCardsPowerCounter(theTwoBestCards) / 2;
    }

    private List<TrucoCard> twoBestCardsFinder(List<TrucoCard> myCards, TrucoCard vira) {
        TrucoCard worstCard = worstCardFinder(myCards, vira);
        List<TrucoCard> theTwoBestCards = new ArrayList<>();
        for (TrucoCard card : myCards) {
            if (card != worstCard) theTwoBestCards.add(card);
        }
        return theTwoBestCards;
    }

    private TrucoCard worstCardFinder(List<TrucoCard> myCards, TrucoCard vira) {
        TrucoCard weakestCard = myCards.get(0);
        for (TrucoCard card : myCards) {
            if (card.relativeValue(vira) < weakestCard.getRank().value()) {
                weakestCard = card;
            }
        }
        return weakestCard;
    }

    private double absoluteCardsPowerCounter(List<TrucoCard> cards) {
        double absolutePower = 0;
        if (!cards.isEmpty()) {
            for (TrucoCard card : cards) {
                absolutePower += card.relativeValue(vira);
            }
        }
        return absolutePower;
    }

    private boolean firstRound(GameIntel intel){
        return intel.getRoundResults().isEmpty();
    }

    private boolean secondRound(GameIntel intel){
        return intel.getRoundResults().size() == 1;
    }

    private boolean firstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    private CardToPlay firstRoundChooseCard(GameIntel intel){
        int bestCardValue = bestCard.relativeValue(vira);
        int secondBestCardValue = secondBestCard.relativeValue(vira);
        int worstCardValue = worstCard.relativeValue(vira);
        CardToPlay playedCard = CardToPlay.of(bestCard);
        if (intel.getOpponentCard().isPresent()){
            int opponentCardOnTableValue = intel.getOpponentCard().get().relativeValue(vira);
            if (worstCardValue >= opponentCardOnTableValue) playedCard = CardToPlay.of(worstCard);
            else if (secondBestCardValue >= opponentCardOnTableValue) playedCard = CardToPlay.of(secondBestCard);
            else if (bestCardValue > opponentCardOnTableValue) playedCard = CardToPlay.of(bestCard);
        }
        else{
            if (secondBestCardValue >= 9) playedCard = CardToPlay.of(secondBestCard);
            else playedCard = CardToPlay.of(bestCard);
        }
        return playedCard;
    }

    private CardToPlay secondRoundChooseCard(GameIntel intel){
        return CardToPlay.of(bestCard);
    }
    private boolean wonFirstRound(GameIntel intel){
        boolean iWonFirstRound = false;
        if (!intel.getRoundResults().isEmpty()){
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) iWonFirstRound = true;
        }
        return iWonFirstRound;
    }

    private int playingSafe(GameIntel intel){
        boolean iWonFirstRound = wonFirstRound(intel);
        double twoBestCardsPowerLevel = powerLevelOfTheTwoBestCards();
        if (firstRound(intel)){
            for (TrucoCard card : myCards) {
                if (card.isManilha(vira)){
                    if (twoBestCardsPowerLevel >= 9.5) return 1;
                    if (twoBestCardsPowerLevel >= 8) return 0;
                }
            }
            if(twoBestCardsPowerLevel >= 10) return 1;
            if(twoBestCardsPowerLevel >= 9) return 0;
            return -1;
        }
        if (iWonFirstRound){
            return getResponseRaiseWinningFirstRound();
        }
        else{
            for (TrucoCard card : myCards) {
                if (card.isManilha(vira) && twoBestCardsPowerLevel >= 8.5) return 0;
            }
            return -1;
        }
    }
    private int playingAgressive(GameIntel intel){
        boolean iWonFirstRound = wonFirstRound(intel);
        double twoBestCardsPowerLevel = powerLevelOfTheTwoBestCards();
        if (firstRound(intel)){
            if (twoBestCardsPowerLevel >= 10) return 1;
            else return 0;
        }
        if (iWonFirstRound){
            return getResponseRaiseWinningFirstRound();
        }
        else{
            if (twoBestCardsPowerLevel >= 9) return 1;
            else return 0;
        }
    }
    private int getResponseRaiseWinningFirstRound(){
        for (TrucoCard card : myCards) {
            if (card.isZap(vira) || card.isCopas(vira) || card.isEspadilha(vira)) return 1;
            if (card.isOuros(vira) || card.relativeValue(vira) >= 9) return 0;
        }
        return 0;
    }
}
