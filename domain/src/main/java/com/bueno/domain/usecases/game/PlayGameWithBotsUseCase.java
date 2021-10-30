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

package com.bueno.domain.usecases.game;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.hand.PlayHandUseCase;

public class PlayGameWithBotsUseCase {
    private final Player bot1;
    private final Player bot2;

    public PlayGameWithBotsUseCase(Player bot1, Player bot2){
        this.bot1 = bot1;
        this.bot2 = bot2;
    }

    public Player play(){
        Game game = new Game(bot1, bot2);
        while (game.getWinner().isEmpty()) {
            new PlayHandUseCase(game).play();
            game.updateScores();
        }
        return game.getWinner().get();
    }
}
