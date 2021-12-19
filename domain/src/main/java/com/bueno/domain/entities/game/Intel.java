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

public class Intel {

    private Game game;
    private final Hand hand;

    Intel(Hand hand) {
        this.hand = Objects.requireNonNull(hand);
    }

    Intel(Game game) {
        this.game = Objects.requireNonNull(game);
        this.hand = game.currentHand();
    }

    public boolean isMaoDeOnze() {
        return game.isMaoDeOnze();
    }

    public boolean isForbidenToRaiseBet() {
        return hand.isForbidenToRaiseBet();
    }

    public int maximumHandScore() {
        return hand.getMaxHandScore();
    }

    public boolean isGameDone() {
        return hand.getFirstToPlay().getScore() == 12 || hand.getLastToPlay().getScore() == 12;
    }

    public Optional<UUID> gameWinner() {
        if (hand.getFirstToPlay().getScore() == 12) return Optional.of(hand.getFirstToPlay().getUuid());
        if (hand.getLastToPlay().getScore() == 12) return Optional.of(hand.getLastToPlay().getUuid());
        return Optional.empty();
    }

    public Card vira() {
        return hand.getVira();
    }

    public List<Card> ownedCards(UUID requesterUUID){
        return new ArrayList<>(getPlayer(requesterUUID).getCards());
    }

    public List<Card> openCards() {
        return hand.getOpenCards();
    }

    public List<Optional<String>> roundWinners() {
        return hand.getRoundsPlayed().stream()
                .map(Round::getWinner)
                .map(maybeWinner -> maybeWinner.orElse(null))
                .map(player -> player != null ? player.getUsername() : null)
                .map(Optional::ofNullable)
                .toList();
    }

    public int roundsPlayed() {
        return hand.getRoundsPlayed().size();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return hand.getCardToPlayAgainst();
    }

    public EnumSet<PossibleActions> possibleActions(UUID requesterUUID) {
        if(requesterUUID.equals(currentPlayer())) return hand.getPossibleActions();
        return EnumSet.noneOf(PossibleActions.class);
    }

    public UUID currentPlayer() {
        return hand.getCurrentPlayer().getUuid();
    }

    public HandScore getHandScore() {
        return hand.getScore();
    }

    public HandScore getScoreProposal() {
        return hand.getScoreProposal();
    }

    public Optional<HandResult> getResult() {
        return hand.getResult();
    }

    public int getOpponentScore(UUID requesterUUID) {
        return getOpponent(requesterUUID).getScore();
    }

    public String getOpponentUsername(UUID requesterUUID) {
        return getOpponent(requesterUUID).getUsername();
    }

    private Player getOpponent(UUID requesterUUID) {
        return requesterUUID.equals(hand.getFirstToPlay().getUuid()) ? hand.getLastToPlay() : hand.getFirstToPlay();
    }

    private Player getPlayer(UUID requesterUUID) {
        return requesterUUID.equals(hand.getFirstToPlay().getUuid()) ? hand.getFirstToPlay() : hand.getLastToPlay();
    }

    @Override
    public String toString() {
        return "Vira = " + vira() +
                " | Card to play against = " + getCardToPlayAgainst() +
                " | Rounds = " + roundWinners() +
                " | Open cards = " + openCards() +
                " | Result = " + getResult();
    }
}
