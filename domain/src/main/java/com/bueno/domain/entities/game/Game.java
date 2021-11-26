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

import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.player.util.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class Game {

    private final Player player1;
    private final Player player2;

    private final List<Hand> hands;

    private Player firstToPlay;
    private Player lastToPlay;

    private final static Logger LOGGER = Logger.getLogger(Game.class.getName());

    public Game(Player player1, Player player2) {
        this.player1 = Objects.requireNonNull(player1);
        this.player2 = Objects.requireNonNull(player2);
        this.hands = new ArrayList<>();
    }

    public Hand prepareNewHand(){
        LOGGER.info("Preparing to play new hand...");
        defineHandPlayingOrder();
        Hand hand = new Hand(firstToPlay, lastToPlay);
        hands.add(hand);
        return hand;
    }

    private void defineHandPlayingOrder() {
        firstToPlay = player1.equals(firstToPlay) ? player2 : player1;
        lastToPlay = firstToPlay.equals(player1) ? player2 : player1;
        LOGGER.info("First to play: " + firstToPlay + " | Last to play: " + lastToPlay );
    }

    public void updateScores() {
        final HandResult result = getCurrentHand().getResult().orElseThrow();
        Optional<Player> winner = result.getWinner();

        if (winner.isEmpty()) return;
        if (winner.get().equals(player1)) player1.addScore(result.getScore());
        else player2.addScore(result.getScore());
    }

    public Optional<Player> getWinner() {
        if (player1.getScore() == Player.MAX_SCORE) return Optional.of(player1);
        if (player2.getScore() == Player.MAX_SCORE) return Optional.of(player2);
        return Optional.empty();
    }

    public Player getFirstToPlay() {
        return firstToPlay;
    }

    public Player getLastToPlay() {
        return lastToPlay;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public List<Hand> getHands() {
        return new ArrayList<>(hands);
    }

    public Hand getCurrentHand(){
        int lastHandIndex = hands.size() - 1;
        return hands.get(lastHandIndex);
    }

    public boolean isMaoDeOnze() {
        return player1.getScore() == 11 ^ player2.getScore() == 11;
    }
}
