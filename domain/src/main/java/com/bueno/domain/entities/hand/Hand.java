/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
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

package com.bueno.domain.entities.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.hand.states.*;
import com.bueno.domain.entities.intel.Event;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;

import java.util.*;

public class Hand {

    private final Card vira;
    private final List<Card> dealtCards;
    private final List<Card> openCards;
    private final List<Round> roundsPlayed;
    private final List<Intel> history;
    private EnumSet<PossibleAction> possibleActions;

    private Player firstToPlay;
    private Player lastToPlay;
    private Player currentPlayer;
    private Player lastBetRaiser;
    private Player eventPlayer;

    private Card cardToPlayAgainst;
    private HandPoints points;
    private HandPoints pointsProposal;

    private HandResult result;
    private HandState state;

    //This method must only be used to recovery the object state from database. Do not use for creating a new hand.
    //To create a hand, use the Game class, since it is its bounded context border.
    public Hand(Card vira, List<Card> dealtCards, List<Card> openCards, List<Round> roundsPlayed, List<Intel> history,
                EnumSet<PossibleAction> possibleActions, Player firstToPlay, Player lastToPlay, Player currentPlayer,
                Player lastBetRaiser, Player eventPlayer, Card cardToPlayAgainst, HandPoints points,
                HandPoints pointsProposal, HandResult result, String stateName){
        this.vira = vira;
        this.dealtCards = new ArrayList<>(dealtCards);
        this.openCards = new ArrayList<>(openCards);
        this.roundsPlayed = new ArrayList<>(roundsPlayed);
        this.history = new ArrayList<>(history);
        this.possibleActions = EnumSet.copyOf(possibleActions);
        this.firstToPlay = firstToPlay;
        this.lastToPlay = lastToPlay;
        this.currentPlayer = currentPlayer;
        this.lastBetRaiser = lastBetRaiser;
        this.eventPlayer = eventPlayer;
        this.cardToPlayAgainst = cardToPlayAgainst;
        this.points = points;
        this.pointsProposal = pointsProposal;
        this.result = result;
        this.state = stateFromString(stateName);
    }

    private HandState stateFromString(String stateName) {
        return switch (stateName){
            case "DONE" -> new Done(this);
            case "NOCARD" -> new NoCard(this);
            case "ONECARD" -> new OneCard(this);
            case "WAITINGMAODEONZE" -> new WaitingMaoDeOnze(this);
            case "WAITINGRAISERESPONSE" -> new WaitingRaiseResponse(this);
            default -> throw new IllegalArgumentException("No state for name: " + stateName);
        };
    }

    public Hand(Player firstToPlay, Player lastToPlay, Card vira){
        this.firstToPlay = Objects.requireNonNull(firstToPlay);
        this.lastToPlay = Objects.requireNonNull(lastToPlay);
        this.vira = Objects.requireNonNull(vira);

        dealtCards = new ArrayList<>();
        dealtCards.add(vira);
        dealtCards.addAll(firstToPlay.getCards());
        dealtCards.addAll(lastToPlay.getCards());

        points = HandPoints.ONE;
        roundsPlayed = new ArrayList<>();
        openCards = new ArrayList<>();
        history = new ArrayList<>();

        addOpenCard(vira);

        if(isMaoDeOnze()) setMaoDeOnzeMode();
        else setOrdinaryMode();

        updateHistory(Event.HAND_START);
    }

    private void setMaoDeOnzeMode() {
        currentPlayer = this.firstToPlay.getScore() == 11 ? this.firstToPlay : this.lastToPlay;
        state = new WaitingMaoDeOnze(this);
    }

    private void setOrdinaryMode() {
        currentPlayer = this.firstToPlay;
        state = new NoCard(this);
    }

    public void playFirstCard(Player player, Card card){
        final var requester = Objects.requireNonNull(player, "Player must not be null!");
        final var requesterCard = Objects.requireNonNull(card, "Card must not be null!");
        validateRequest(requester, PossibleAction.PLAY);
        eventPlayer = currentPlayer;
        state.playFirstCard(requester, requesterCard);
    }

    public void playSecondCard(Player player, Card cards){
        final var requester = Objects.requireNonNull(player, "Player must not be null!");
        final var requesterCard = Objects.requireNonNull(cards, "Card must not be null!");
        validateRequest(requester, PossibleAction.PLAY);
        eventPlayer = currentPlayer;
        state.playSecondCard(requester,requesterCard);
    }

    public void raise(Player requester){
        final var player = Objects.requireNonNull(requester, "Player must not be null!");
        validateRequest(requester, PossibleAction.RAISE);
        eventPlayer = currentPlayer;
        state.raise(player);
    }

    public void accept(Player responder){
        final var player = Objects.requireNonNull(responder, "Player must not be null!");
        validateRequest(player, PossibleAction.ACCEPT);
        eventPlayer = currentPlayer;
        state.accept(player);
    }

    public void quit(Player responder){
        final var player = Objects.requireNonNull(responder, "Player must not be null!");
        validateRequest(player, PossibleAction.QUIT);
        eventPlayer = currentPlayer;
        state.quit(player);
    }

    private void validateRequest(Player requester, PossibleAction action){
        if(!requester.equals(currentPlayer))
            throw new IllegalArgumentException(requester + " can not " + action + " in " + currentPlayer + " turn.");
        if(!possibleActions.contains(action))
            throw new IllegalStateException("Can not " + action + ", but " + possibleActions + ".");
    }

    public void updateHistory(Event event) {
        history.add(Intel.ofHand(this, event));
    }

    public void playRound(Card lastCard){
        final var round = new Round(firstToPlay, cardToPlayAgainst, lastToPlay, lastCard, vira);
        round.play();
        roundsPlayed.add(round);
    }

    public void defineRoundPlayingOrder() {
        final var lastRoundWinner =
                roundsPlayed.isEmpty() ? Optional.empty() : roundsPlayed.get(roundsPlayed.size() - 1).getWinner();
        lastRoundWinner.filter(lastToPlay::equals).ifPresent(unused -> changePlayingOrder());
        currentPlayer = firstToPlay;
    }

    private void changePlayingOrder() {
        var referenceHolder = firstToPlay;
        firstToPlay = lastToPlay;
        lastToPlay = referenceHolder;
    }

    public void addOpenCard(Card card){
        if(!dealtCards.contains(card) && !card.equals(Card.closed()))
            throw new GameRuleViolationException("Card has not been dealt in this hand.");
        if(openCards.contains(card) && !card.equals(Card.closed()) )
            throw new GameRuleViolationException("Card " + card + " has already been played during hand.");
        openCards.add(card);
    }

    public void checkForWinnerAfterSecondRound() {
        var firstRoundWinner = roundsPlayed.get(0).getWinner();
        var secondRoundWinner = roundsPlayed.get(1).getWinner();

        if (firstRoundWinner.isEmpty() && secondRoundWinner.isPresent())
            result = HandResult.of(secondRoundWinner.get(), points);
        else if (firstRoundWinner.isPresent() && secondRoundWinner.isEmpty())
            result =  HandResult.of(firstRoundWinner.get(), points);
        else if (secondRoundWinner.isPresent() && secondRoundWinner.get().equals(firstRoundWinner.get()))
            result = HandResult.of(secondRoundWinner.get(), points);
    }

    public void checkForWinnerAfterThirdRound() {
        var firstRoundWinner = roundsPlayed.get(0).getWinner();
        var lastRoundWinner = roundsPlayed.get(2).getWinner();

        if (lastRoundWinner.isEmpty() && firstRoundWinner.isPresent())
            result = HandResult.of(firstRoundWinner.get(), points);
        else
            result = lastRoundWinner.map(player -> HandResult.of(player, points)).orElseGet(HandResult::ofDraw);
    }

    public Optional<Card> getCardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgainst);
    }

    public void setCardToPlayAgainst(Card cardToPlayAgainst) {
        this.cardToPlayAgainst = cardToPlayAgainst;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getEventPlayer() {
        return eventPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean hasWinner(){
        return result != null;
    }

    public Optional<HandResult> getResult() {
        return Optional.ofNullable(result);
    }

    public EnumSet<PossibleAction> getPossibleActions() {
        return possibleActions;
    }

    public void setPossibleActions(EnumSet<PossibleAction> actions){
        this.possibleActions = EnumSet.copyOf(actions);
    }

    public Intel getLastIntel(){
        return history.get(history.size() - 1);
    }

    public Player getOpponentOf(Player player){
        return player.equals(firstToPlay)? lastToPlay : firstToPlay;
    }

    public List<Round> getRoundsPlayed() {
        return new ArrayList<>(roundsPlayed);
    }

    public int numberOfRoundsPlayed(){
        return roundsPlayed.size();
    }

    public boolean isDone(){
        return state instanceof Done;
    }

    public void setResult(HandResult result) {
        this.result = result;
    }

    public void setPoints(HandPoints points) {
        this.points = points;
    }

    public Player getFirstToPlay() {
        return firstToPlay;
    }

    public Player getLastToPlay() {
        return lastToPlay;
    }

    public HandPoints getPoints() {
        return points;
    }

    public void setLastBetRaiser(Player lastBetRaiser) {
        this.lastBetRaiser = lastBetRaiser;
    }

    public List<Card> getOpenCards() {
        return openCards;
    }

    public List<Card> getDealtCards() {
        return new ArrayList<>(dealtCards);
    }

    public List<Intel> getIntelHistory(){
        return List.copyOf(history);
    }

    public Player getLastBetRaiser() {
        return lastBetRaiser;
    }

    public HandState getState() {
        return state;
    }

    public void setState(HandState state) {
        this.state = state;
    }

    public Card getVira() {
        return vira;
    }

    public boolean isMaoDeOnze() {
        return firstToPlay.getScore() == 11 ^ lastToPlay.getScore() == 11;
    }

    public void addPointsProposal() {
        pointsProposal = points.increase();
    }

    public void removePointsProposal(){
        pointsProposal = null;
    }

    public HandPoints getPointsProposal() {
        return pointsProposal;
    }

    public boolean canRaiseBet(){
        return isPlayerAllowedToRaise() && isAllowedToRaise() && isAllowedToReRaise();
    }

    private boolean isPlayerAllowedToRaise() {
        return currentPlayer != lastBetRaiser;
    }

    private boolean isAllowedToRaise() {
        return points.get() < 12 && points.increase().get() <= getMaxHandPoints()
                && firstToPlay.getScore() < 11 && lastToPlay.getScore() < 11;
    }

    private boolean isAllowedToReRaise() {
        return pointsProposal == null || pointsProposal.get() < 12 && pointsProposal.increase().get() <= getMaxHandPoints();
    }

    private int getMaxHandPoints(){
        final int firstToPlayScore = firstToPlay.getScore();
        final int lastToPlayScore = lastToPlay.getScore();
        final int pointsToLosingPlayerWin = Player.MAX_SCORE - Math.min(firstToPlayScore, lastToPlayScore);
        return pointsToLosingPlayerWin % 3 == 0 ? pointsToLosingPlayerWin
                : pointsToLosingPlayerWin + (3 - pointsToLosingPlayerWin % 3);
    }
}