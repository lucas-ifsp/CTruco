/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.entities.game;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.player.util.Player;

import java.util.*;

public class Hand {

    private Card vira;

    private final List<Card> openCards;
    private final List<Round> roundsPlayed;
    private EnumSet<PossibleActions> possibleActions;

    private Player firstToPlay;
    private Player lastToPlay;
    private Player currentPlayer;
    private Player lastBetRaiser;

    private Card cardToPlayAgainst;
    private HandScore score;
    private HandScore scoreProposal;

    private HandResult result;
    private HandState state;

    public Hand(Player firstToPlay, Player lastToPlay, Card vira){
        this.firstToPlay = Objects.requireNonNull(firstToPlay);
        this.lastToPlay = Objects.requireNonNull(lastToPlay);
        this.vira = Objects.requireNonNull(vira);

        score = HandScore.ONE;
        roundsPlayed = new ArrayList<>();
        openCards = new ArrayList<>();
        addOpenCard(vira);

        if(isMaoDeOnze()){
            currentPlayer = firstToPlay.getScore() == 11 ? firstToPlay : lastToPlay;
            state = new WaitingMaoDeOnzeState(this);
        } else{
            currentPlayer = firstToPlay;
            state = new NoCardState(this);
        }
    }

    public void playFirstCard(Player player, Card card){
        final Player requester = Objects.requireNonNull(player, "Player must not be null!");
        final Card requesterCard = Objects.requireNonNull(card, "Card must not be null!");
        if(!requester.equals(currentPlayer))
            throw new IllegalArgumentException("First to play must be " + currentPlayer + ", not " + requester + ".");
        state.playFirstCard(requester, requesterCard);

    }

    public void playSecondCard(Player player, Card cards){
        final Player requester = Objects.requireNonNull(player, "Player must not be null!");
        final Card requesterCard = Objects.requireNonNull(cards, "Card must not be null!");
        if(!requester.equals(currentPlayer))
            throw new IllegalArgumentException("Second to play must be " + currentPlayer + ", not " + requester + ".");
        state.playSecondCard(requester,requesterCard);
    }

    public void accept(Player responder){
        final Player player = Objects.requireNonNull(responder, "Player must not be null!");
        if(!player.equals(currentPlayer))
            throw new IllegalArgumentException(player + " can not accept a bet requested to " + currentPlayer + ".");
        state.accept(player);
    }

    public void quit(Player responder){
        final Player player = Objects.requireNonNull(responder, "Player must not be null!");
        if(!player.equals(currentPlayer))
            throw new IllegalArgumentException(player + " can not quit a bet requested to " + currentPlayer + ".");
        state.quit(player);
    }

    public void raiseBet(Player requester){
        final Player player = Objects.requireNonNull(requester, "Player must not be null!");
        if(!player.equals(currentPlayer))
            throw new IllegalArgumentException(player + " can not raise the bet in " + currentPlayer + " turn.");
        if(player.equals(lastBetRaiser))
            throw new IllegalStateException(player + " can not raise the bet consecutively.");
        state.raiseBet(player);
    }

    void playRound(Card lastCard){
        final Round round = new Round(firstToPlay, cardToPlayAgainst, lastToPlay, lastCard, vira);
        round.play2();
        roundsPlayed.add(round);
    }

    boolean isMaoDeOnze() {
        return firstToPlay.getScore() == 11 ^ lastToPlay.getScore() == 11;
    }

    void addScoreProposal() {
        final int maxHandScore = getMaxHandScore();
        final HandScore requestingScore = score.increase();

        if(requestingScore.get() > maxHandScore) {
            final String message = String.format("Can not raise bet to %d. Maximum valid score is %d",
                    requestingScore.get(), score.get());
            throw new GameRuleViolationException(message);
        }
        scoreProposal = requestingScore;
    }

    void removeScoreProposal(){
        scoreProposal = null;
    }

    public HandScore getScoreProposal() {
        return scoreProposal;
    }

    int getMaxHandScore(){
        final int firstToPlayScore = firstToPlay.getScore();
        final int lastToPlayScore = lastToPlay.getScore();

        final int scoreToLosingPlayerWin = Player.MAX_SCORE - Math.min(firstToPlayScore, lastToPlayScore);

        return scoreToLosingPlayerWin % 3 == 0 ? scoreToLosingPlayerWin
                : scoreToLosingPlayerWin + (3 - scoreToLosingPlayerWin % 3);
    }









    void defineRoundPlayingOrder() {
        getLastRoundWinner().filter(lastToPlay::equals).ifPresent(unused -> changePlayingOrder());
        currentPlayer = firstToPlay;
    }

    private void changePlayingOrder() {
        Player referenceHolder = firstToPlay;
        firstToPlay = lastToPlay;
        lastToPlay = referenceHolder;
    }

    public Player getOpponentOf(Player player){
        return player.equals(firstToPlay)? lastToPlay : firstToPlay;
    }

    public void checkForWinnerAfterSecondRound() {
        Optional<Player> firstRoundWinner = roundsPlayed.get(0).getWinner();
        Optional<Player> secondRoundWinner = roundsPlayed.get(1).getWinner();

        if (firstRoundWinner.isEmpty() && secondRoundWinner.isPresent())
            result = new HandResult(secondRoundWinner.get(), score);
        else if (firstRoundWinner.isPresent() && secondRoundWinner.isEmpty())
            result =  new HandResult(firstRoundWinner.get(), score);
        else if (secondRoundWinner.isPresent() && secondRoundWinner.get().equals(firstRoundWinner.get()))
            result = new HandResult(secondRoundWinner.get(), score);
    }

    public void checkForWinnerAfterThirdRound() {
        Optional<Player> firstRoundWinner = roundsPlayed.get(0).getWinner();
        Optional<Player> lastRoundWinner = roundsPlayed.get(2).getWinner();

        if (lastRoundWinner.isEmpty() && firstRoundWinner.isPresent())
            result = new HandResult(firstRoundWinner.get(), score);
        else
            result = lastRoundWinner.map(player -> new HandResult(player, score)).orElseGet(HandResult::new);
    }

    private void updatePlayersIntel() {
        firstToPlay.setIntel(new Intel(this));
        lastToPlay.setIntel(new Intel(this));
    }

    void setCardToPlayAgainst(Card cardToPlayAgainst) {
        this.cardToPlayAgainst = cardToPlayAgainst;
        updatePlayersIntel();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgainst);
    }

    void addOpenCard(Card card){
        openCards.add(card);
        updatePlayersIntel();
    }

    public Optional<Player> getLastRoundWinner(){
        if(roundsPlayed.isEmpty()) return Optional.empty();
        return roundsPlayed.get(roundsPlayed.size() - 1).getWinner();
    }

    public List<Round> getRoundsPlayed() {
        return new ArrayList<>(roundsPlayed);
    }

    public boolean isDone(){
        return state instanceof DoneState;
    }

    public int numberOfRoundsPlayed(){
        return roundsPlayed.size();
    }

    public Optional<HandResult> getResult() {
        return Optional.ofNullable(result);
    }

    public void setResult(HandResult result) {
        this.result = result;
    }

    public boolean hasWinner(){
        return result != null;
    }

    public void setScore(HandScore score) {
        this.score = score;
        updatePlayersIntel();
    }

    public Player getFirstToPlay() {
        return firstToPlay;
    }

    public Player getLastToPlay() {
        return lastToPlay;
    }

    public HandScore getScore() {
        return score;
    }

    void setLastBetRaiser(Player lastBetRaiser) {
        this.lastBetRaiser = lastBetRaiser;
    }

    public List<Card> getOpenCards() {
        return openCards;
    }

    public Intel getIntel(){
        return new Intel(this);
    }

    void setState(HandState state) {
        this.state = state;
    }

    void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Card getVira() {
        return vira;
    }

    public Player getLastBetRaiser() {
        return lastBetRaiser;
    }

    public void playNewRound(){
        if(roundsPlayed.size() == 3)
            throw new GameRuleViolationException("The number of rounds exceeded the maximum of three.");

        defineRoundPlayingOrder();
        Round round = new Round(firstToPlay, lastToPlay, this);
        round.play();

        firstToPlay.handleRoundConclusion();
        lastToPlay.handleRoundConclusion();
        roundsPlayed.add(round);
    }

    public EnumSet<PossibleActions> getPossibleActions() {
        return possibleActions;
    }

    void setPossibleActions(EnumSet<PossibleActions> actions){
        this.possibleActions = EnumSet.copyOf(actions);
    }

    public boolean isForbidenToRaiseBet() {
        return firstToPlay.getScore() == 11 || lastToPlay.getScore() == 11;
    }
}
