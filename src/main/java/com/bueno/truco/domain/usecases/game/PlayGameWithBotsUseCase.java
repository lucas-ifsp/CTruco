/*
 * Copyright (C) 2021 Lucas B. R. de Oliveira
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
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.player.dummybot.DummyBot;
import com.bueno.truco.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.truco.domain.entities.player.util.Player;
import com.bueno.truco.domain.usecases.hand.PlayHandUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.LogManager;

//TODO Resolver problema de pegar a mesma referencia
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
