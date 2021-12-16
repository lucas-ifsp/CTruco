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
import com.bueno.domain.entities.hand.Intel;
import com.bueno.domain.entities.player.util.Bot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.hand.PlayHandUseCase;

public class PlayGameWithBotsUseCase {

    private final GameRepository repo;

    public PlayGameWithBotsUseCase(GameRepository repo) {
        this.repo = repo;
    }

    public Player playWithBots(Bot bot1, Bot bot2){
        CreateGameUseCase gameUseCase = new CreateGameUseCase(repo);
        gameUseCase.create((Player) bot1, (Player) bot2);
        PlayHandUseCase playHandUseCase = new PlayHandUseCase(repo);
        final Intel intel = playHandUseCase.playCard(((Player) bot1).getUuid(), bot1.playCard());
        return intel.gameWinner().orElseThrow();
    }

}
