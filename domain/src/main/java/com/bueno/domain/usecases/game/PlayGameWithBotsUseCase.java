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

import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.Bot;
import com.bueno.domain.entities.player.util.BotFactory;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase.RequestModel;

import java.util.UUID;

public class PlayGameWithBotsUseCase {

    private final GameRepository repo;

    public PlayGameWithBotsUseCase(GameRepository repo) {
        this.repo = repo;
    }

    public UUID playWithBots(String bot1Name, String bot2Name){
        final Bot bot1 = BotFactory.create(bot1Name, repo);
        final Bot bot2 = BotFactory.create(bot2Name, repo);

        CreateGameUseCase gameUseCase = new CreateGameUseCase(repo, null);
        Intel intel = gameUseCase.create(bot1, bot2);
        bot1.setIntel(intel);

        PlayCardUseCase playCardUseCase = new PlayCardUseCase(repo);
        intel = playCardUseCase.playCard(new RequestModel(bot1.getUuid(), bot1.chooseCardToPlay().content()));

        return intel.gameWinner().orElseThrow();
    }
}
