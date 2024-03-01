package com.gustavo.contiero.lazybot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;


public class LazyBot implements BotServiceProvider {

    /**
     * <p>Decides if the bot wants to play the "mão de onze". If it decides to play, the hand points will be
     * increased to 3. Otherwise, the bot loses the current hand and the opponent receives 1 point.</p>
     *
     * @ return {@code true} if it accepts to play the hand or {@code false} if it quits.
     */
    private List<TrucoCard> myCards;
    private TrucoCard vira;
    private TrucoCard bestCard;
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

    public void setBestCard() {
        bestCard = myCards.get(0);
        for (TrucoCard card : myCards){
            if (card.relativeValue(vira) > bestCard.relativeValue(vira)){
                bestCard = card;
            }
        }
    }

    public void setWorstCard() {
        worstCard = myCards.get(0);
        for (TrucoCard card : myCards){
            if (card.relativeValue(vira) < worstCard.relativeValue(vira)){
                worstCard = card;
            }
        }
    }

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
        List<TrucoCard> opponentCardsThrown = setCardsThrownByOpponent(intel);

        if (firstRound(intel) && firstToPlay(intel)){
            return powerLevel >= powerToDecideIfRaises;
        }
        else if (firstRound(intel)){
            if (bestCard.relativeValue(vira) >= opponentCardsThrown.get(0).relativeValue(vira)){
                return powerLevelAllCards() >= 7.5;
            }
            else return false;
        }
        else{
            boolean iWonFirstRound = (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON);

            if (iWonFirstRound){
                return bestCard.relativeValue(vira) >= opponentCardsThrown.get(0).relativeValue(vira);
            }
            else{
                return bestCard.relativeValue(vira) > opponentCardsThrown.get(0).relativeValue(vira);
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
    public int getRaiseResponse(GameIntel intel) {
        setState(intel);
        int oponentScoreNow = intel.getScore();
        double myPower = powerLevelAllCards();
        if (oponentScoreNow >= 9){
            if (myPower >= 10) return 1;
            if (myPower >= 7.5) return 0;
            return -1;
        }
        else if (oponentScoreNow >= 6){
            if (myPower >= 8.5) return 1;
            if (myPower >= 6) return 0;
            return -1;
        }
        else if (oponentScoreNow >= 3){
            if (myPower >= 7) return 1;
            if (myPower >= 5.5) return 0;
            return -1;
        }
        else{
            return 1;
        }
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

    private void cardsPowerPrinter(List<TrucoCard> myCards,TrucoCard vira){
        for (TrucoCard card : myCards) {
            System.out.printf("%d|", card.relativeValue(vira));
        }
        System.out.println();
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

    private List<TrucoCard> setCardsThrownByOpponent(GameIntel intel){
        List<TrucoCard> opponentCardsThrown = new ArrayList<>();
        List<TrucoCard> cardsThrown = intel.getOpenCards();
        if (firstToPlay(intel)){
            for (int i = 0; i<cardsThrown.size();i++){
                if (i % 2 == 0 && i != 0){
                    opponentCardsThrown.add(cardsThrown.get(i));
                }
            }
        }
        else{
            for (int i = 0; i<cardsThrown.size();i++){
                if (i % 2 != 0){
                    opponentCardsThrown.add(cardsThrown.get(i));
                }
            }
        }
        return opponentCardsThrown;
    }

    private void setState(GameIntel intel){
        setMyCards(intel.getCards());
        setVira(intel.getVira());
        if (! myCards.isEmpty()){
            setBestCard();
            setWorstCard();
        }
        setCardsThrownByOpponent(intel);
    }

    private CardToPlay firstRoundChooseCard(GameIntel intel){
        TrucoCard cardThrownByOpponent;
        int sizeOpenCards = intel.getOpenCards().size();
        if (! firstToPlay(intel)) {
            cardThrownByOpponent = intel.getOpenCards().get(sizeOpenCards - 1);
            if (bestCard.relativeValue(vira) > cardThrownByOpponent.relativeValue(vira)) return CardToPlay.of(bestCard);
            else return CardToPlay.of(worstCard);
        }
        return CardToPlay.of(bestCard);
    }
    private CardToPlay secondRoundChooseCard(GameIntel intel){
        TrucoCard cardThrownByOpponent;
        int sizeOpenCards = intel.getOpenCards().size();
        if (! firstToPlay(intel)) {
            cardThrownByOpponent = intel.getOpenCards().get(sizeOpenCards - 1);
            if (bestCard.relativeValue(vira) > cardThrownByOpponent.relativeValue(vira)) return CardToPlay.of(bestCard);
            else return CardToPlay.discard(worstCard);
        }
        else if (intel.getRoundResults().contains(GameIntel.RoundResult.WON)){
            if (worstCard.relativeValue(vira) >= 7) return CardToPlay.of(worstCard);
            else return CardToPlay.of(bestCard);
        }
        return CardToPlay.of(bestCard);
    }
}
