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

package com.bueno.domain.entities.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Deck;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.round.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class Hand {

    private Card vira;

    private final List<Card> openCards;
    private final List<Round> roundsPlayed;

    private Player firstToPlay;
    private Player lastToPlay;
    private Player lastScoreIncrementRequester;

    private Card cardToPlayAgainst;
    private HandScore score;
    private HandResult result;

    private final static Logger LOGGER = Logger.getLogger(Hand.class.getName());

    public Hand(Player firstToPlay, Player lastToPlay) {
        this(firstToPlay, lastToPlay, new Deck());
    }

    public Hand(Player firstToPlay, Player lastToPlay, Deck deck){
        this.firstToPlay = Objects.requireNonNull(firstToPlay);
        this.lastToPlay = Objects.requireNonNull(lastToPlay);
        dealCards(deck);

        score = HandScore.ONE;
        roundsPlayed = new ArrayList<>();
        openCards = new ArrayList<>();
        addOpenCard(vira);
    }

    private void dealCards(Deck deck) {
        deck.shuffle();

        firstToPlay.setCards(deck.take(3));
        lastToPlay.setCards(deck.take(3));
        vira = deck.takeOne();

        LOGGER.info("Vira: " + vira);
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

    private void defineRoundPlayingOrder() {
        getLastRoundWinner().filter(lastToPlay::equals).ifPresent(unused -> changePlayingOrder());
    }

    private void changePlayingOrder() {
        Player referenceHolder = firstToPlay;
        firstToPlay = lastToPlay;
        lastToPlay = referenceHolder;
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

    public void setCardToPlayAgainst(Card cardToPlayAgainst) {
        this.cardToPlayAgainst = cardToPlayAgainst;
        updatePlayersIntel();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgainst);
    }

    public void addOpenCard(Card card){
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

    public Player getLastScoreIncrementRequester() {
        return lastScoreIncrementRequester;
    }

    public void setLastScoreIncrementRequester(Player lastScoreIncrementRequester) {
        this.lastScoreIncrementRequester = lastScoreIncrementRequester;
    }

    public Card getVira() {
        return vira;
    }

    public List<Card> getOpenCards() {
        return openCards;
    }

    public Intel getIntel(){
        return new Intel(this);
    }
}
