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

package com.bueno.domain.usecases.hand;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.player.util.Player;

public class PlayHandUseCase {

    private Game game;
    private Hand hand;

    public PlayHandUseCase(Game game) {
        this.game = game;
    }

    public Hand play() {
        hand = game.prepareNewHand();

        if(game.isMaoDeOnze()){
            handleMaoDeOnze();
            if (hand.hasWinner()) return hand;
        }

        playRound();
        if (hand.hasWinner()) return hand;

        playRound();
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterSecondRound();
        if (hand.hasWinner()) return hand;

        playRound();
        if (hand.hasWinner()) return hand;

        hand.checkForWinnerAfterThirdRound();
        return hand;
    }

    private void handleMaoDeOnze() {
        Player playerInMaoDeOnze = game.getPlayer1().getScore() == 11? game.getPlayer1() : game.getPlayer2();
        Player otherPlayer = game.getPlayer1().getScore() == 11? game.getPlayer2() : game.getPlayer1();

        if(playerInMaoDeOnze.getMaoDeOnzeResponse())
            hand.setScore(HandScore.of(3));
        else
            hand.setResult(new HandResult(otherPlayer, HandScore.of(1)));
    }

    private void playRound() {
        hand.playNewRound();
    }
}
