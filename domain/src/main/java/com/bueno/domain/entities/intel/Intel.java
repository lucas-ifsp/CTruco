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

package com.bueno.domain.entities.intel;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.hand.Round;
import com.bueno.domain.entities.player.Player;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Intel{

    private final Instant timestamp;

    private boolean gameIsDone;
    private UUID gameWinner;
    private boolean maoDeOnze;

    private Integer handPoints;
    private Integer handPointsProposal;
    private List<Optional<String>> roundWinners;
    private int roundsPlayed;

    private Player currentPlayer;
    private int currentPlayerScore;
    private String currentPlayerUsername;

    private String eventPlayerUsername;
    private UUID eventPlayerUUID;

    private int currentOpponentScore;
    private String currentOpponentUsername;

    private List<PlayerIntel> players;

    private Card vira;
    private Card cardToPlayAgainst;
    private List<Card> openCards;
    private String event;
    private Set<String> possibleActions;
    private String handWinner;

    static public Intel ofHand(Hand currentHand, Event event){
        final Hand hand = Objects.requireNonNull(currentHand);
        final Intel result = new Intel();
        result.event = event.toString();
        result.setHandIntel(hand);
        result.setPlayersIntel(hand);
        return result;
    }

    static public Intel ofGame(Game currentGame){
        final Game game = Objects.requireNonNull(currentGame);
        final Intel result = ofHand(game.currentHand(), Event.GAME_OVER);
        result.setGameIntel(game);
        return result;
    }

    private void setHandIntel(Hand hand){
        maoDeOnze = hand.isMaoDeOnze();
        handPoints = hand.getPoints().get();
        if(hand.getPointsProposal() != null) handPointsProposal = hand.getPointsProposal().get();
        roundWinners = getRoundWinners(hand);
        roundsPlayed = roundWinners.size();
        vira = hand.getVira();
        handWinner = hand.getResult().flatMap(HandResult::getWinner).map(Player::getUsername).orElse(null);
        openCards = List.copyOf(hand.getOpenCards());
        cardToPlayAgainst = hand.getCardToPlayAgainst().orElse(null);
        possibleActions = hand.getPossibleActions().stream().map(Objects::toString).collect(Collectors.toSet());
    }

    private void setPlayersIntel(Hand hand){
        players = List.of(new PlayerIntel(hand.getFirstToPlay()), new PlayerIntel(hand.getLastToPlay()));

        final Player eventPlayer = hand.getEventPlayer();
        eventPlayerUsername = eventPlayer != null ? eventPlayer.getUsername() : null;
        eventPlayerUUID = eventPlayer != null ? eventPlayer.getUuid() : null;

        currentPlayer = hand.getCurrentPlayer();
        currentPlayerScore = currentPlayer != null ?  currentPlayer.getScore() : 0;
        currentPlayerUsername = currentPlayer != null ?  currentPlayer.getUsername() : null;

        currentOpponentScore = currentPlayer != null ?  hand.getOpponentOf(currentPlayer).getScore() : 0;
        currentOpponentUsername = currentPlayer != null ?  hand.getOpponentOf(currentPlayer).getUsername() : null;
    }

    private void setGameIntel(Game game){
        gameIsDone = game.isDone();
        gameWinner = game.getWinner().map(Player::getUuid).orElse(null);
    }

    private Intel() {
        timestamp = Instant.now();
    }

    public static class PlayerIntel{
        private final UUID uuid;
        private final String username;
        private final int score;
        private final List<Card> cards;

        public PlayerIntel(Player player) {
            this.uuid = player.getUuid();
            this.username = player.getUsername();
            this.score = player.getScore();
            this.cards = List.copyOf(player.getCards());
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getUsername() {
            return username;
        }

        public int getScore() {
            return score;
        }

        public List<Card> getCards() {
            return cards;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerIntel that = (PlayerIntel) o;
            return score == that.score && uuid.equals(that.uuid) && username.equals(that.username) && cards.equals(that.cards);
        }
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

    public Integer handPoints() {
        return handPoints;
    }

    public Optional<Integer> pointsProposal() {
        return Optional.ofNullable(handPointsProposal);
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

    public Optional<String> handWinner() {
        return Optional.ofNullable(handWinner);
    }

    public List<Card> openCards() {
        return openCards;
    }

    public Optional<Card> cardToPlayAgainst() {
        return Optional.ofNullable(cardToPlayAgainst);
    }

    public Set<String> possibleActions() {
        return possibleActions;
    }

    public List<PlayerIntel> players(){
        return players;
    }

    public Optional<UUID> currentPlayerUuid() {
        return currentPlayer != null ? Optional.of(currentPlayer.getUuid()) : Optional.empty();
    }
    
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

    public Optional<String> event() {
        return Optional.ofNullable(event);
    }

    public Optional<UUID> eventPlayer() {
        return Optional.ofNullable(eventPlayerUUID);
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
        final String userInMaoDeOnze = currentPlayerScore < currentOpponentScore ? currentOpponentUsername : currentPlayerUsername;

        return "[" + timestamp +
                "] Event = " + (event == null ? "--" : event) +
                " | Event player = " + eventPlayerUsername +
                " | Next player = " + currentPlayerUsername +
                " | Possible actions = " + possibleActions +
                " | Vira = " + vira +
                " | Card to play against = " + cardToPlayAgainst +
                " | Open cards = " + openCards +
                " | Rounds = " + roundWinners +
                " | Hand Score = " + handPoints +
                (pointsProposal().isPresent() ? " | Score Proposal = " + pointsProposal().get() : "") +
                (isMaoDeOnze() ? " | Mão de Onze = " + userInMaoDeOnze  :  "") +
                " | Winner = " + handWinner;
    }
}
