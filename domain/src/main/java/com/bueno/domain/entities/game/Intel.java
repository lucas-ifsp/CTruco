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

import java.time.Instant;
import java.util.*;

public class Intel{

    private final Instant timestamp;

    private final boolean gameIsDone;
    private final UUID gameWinner;
    private final boolean maoDeOnze;

    private final HandScore handScore;
    private final HandScore handScoreProposal;
    private final int maximumHandScore;
    private final List<Optional<String>> roundWinners;
    private final int roundsPlayed;

    private final Player currentPlayer;
    private final int currentPlayerScore;
    private final String currentPlayerUsername;
    private final int currentOpponentScore;
    private final String currentOpponentUsername;
    private final Card vira;
    private final Card cardToPlayAgaist;
    private final List<Card> openCards;
    private final List<Card> currentPlayerCards;
    private final EnumSet<PossibleActions> possibleActions;
    private final HandResult handResult;


    Intel(Hand hand) {
        Objects.requireNonNull(hand);

        timestamp = Instant.now();

        gameIsDone = getGameResult(hand);
        gameWinner = getGameWinner(hand);
        maoDeOnze = hand.isMaoDeOnze();

        handScore = hand.getScore();
        handScoreProposal = hand.getScoreProposal();
        maximumHandScore = hand.getMaxHandScore();
        roundWinners = getRoundWinners(hand);
        roundsPlayed = roundWinners.size();
        vira = hand.getVira();
        handResult = hand.getResult().orElse(null);
        openCards = List.copyOf(hand.getOpenCards());
        cardToPlayAgaist = hand.getCardToPlayAgainst().orElse(null);
        possibleActions = EnumSet.copyOf(hand.getPossibleActions());


        currentPlayer = hand.getCurrentPlayer();
        currentPlayerCards = List.copyOf(currentPlayer.getCards());
        currentPlayerScore = currentPlayer.getScore();
        currentPlayerUsername = currentPlayer.getUsername();
        currentOpponentScore = hand.getOpponentOf(currentPlayer).getScore();
        currentOpponentUsername = hand.getOpponentOf(currentPlayer).getUsername();
    }

    private boolean getGameResult(Hand hand) {
        return hand.getFirstToPlay().getScore() == 12 || hand.getLastToPlay().getScore() == 12;
    }

    private UUID getGameWinner(Hand hand) {
        if (hand.getFirstToPlay().getScore() == 12) return hand.getFirstToPlay().getUuid();
        if (hand.getLastToPlay().getScore() == 12) return hand.getLastToPlay().getUuid();
        return null;
    }

    private List<Optional<String>> getRoundWinners(Hand hand) {
        return hand.getRoundsPlayed().stream()
                .map(Round::getWinner)
                .map(maybeWinner -> maybeWinner.orElse(null))
                .map(player -> player != null ? player.getUsername() : null)
                .map(Optional::ofNullable)
                .toList();
    }

    public Instant timestamp() {
        return timestamp;
    }

    public boolean isGameDone() {
        return gameIsDone;
    }

    public Optional<UUID> gameWinner() {
        return Optional.ofNullable(gameWinner);
    }

    public boolean isMaoDeOnze() {
        return maoDeOnze;
    }

    public HandScore handScore() {
        return handScore;
    }

    public HandScore scoreProposal() {
        return handScoreProposal;
    }

    public int maximumHandScore() {
        return maximumHandScore;
    }

    public List<Optional<String>> roundWinners() {
        return roundWinners;
    }

    public int roundsPlayed() {
        return roundsPlayed;
    }

    public Card vira() {
        return vira;
    }

    public Optional<HandResult> handResult() {
        return Optional.ofNullable(handResult);
    }

    public List<Card> openCards() {
        return openCards;
    }

    public Optional<Card> cardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgaist);
    }

    public EnumSet<PossibleActions> possibleActions() {
        return possibleActions;
    }

    public UUID currentPlayerUuid() {
        return currentPlayer.getUuid();
    }

    public List<Card> currentPlayerCards(){return new ArrayList<>(currentPlayerCards);}

    public int currentPlayerScore() {
        return currentPlayerScore;
    }

    public String currentPlayerUsername() {
        return currentPlayerUsername;
    }

    public int currentOpponentScore() {
        return currentOpponentScore;
    }

    public String currentOpponentUsername() {
        return currentOpponentUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intel intel = (Intel) o;
        return timestamp.equals(intel.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }

    @Override
    public String toString() {
        return "[" + timestamp +
                " ] Current player = " + currentPlayerUsername +
                " ] Vira = " + vira +
                " | Card to play against = " + cardToPlayAgaist +
                " | Open cards = " + openCards +
                " | Possible actions = " + openCards +
                " | Rounds = " + roundWinners +
                " | Result = " + handResult;
    }
}
