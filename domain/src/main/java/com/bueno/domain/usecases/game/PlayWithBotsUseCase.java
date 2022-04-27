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

import com.bueno.domain.usecases.bot.BotUseCase;
import com.bueno.domain.usecases.game.model.CreateForBotsRequest;
import com.bueno.domain.usecases.game.model.PlayWithBotsResponse;

import java.util.Objects;

public class PlayWithBotsUseCase {

    private final CreateGameUseCase createGameUseCase;
    private final FindGameUseCase findGameUseCase;
    private final BotUseCase botUseCase;

    public PlayWithBotsUseCase(CreateGameUseCase createGameUse, FindGameUseCase findGameUseCase, BotUseCase botUseCase) {
        this.createGameUseCase = Objects.requireNonNull(createGameUse);
        this.findGameUseCase = Objects.requireNonNull(findGameUseCase);
        this.botUseCase = Objects.requireNonNull(botUseCase);
    }

    public PlayWithBotsResponse playWithBots(CreateForBotsRequest requestModel){
        createGameUseCase.createForBots(requestModel);

        final var game = findGameUseCase.loadUserGame(requestModel.getBot1Uuid()).orElseThrow();
        final var intel = botUseCase.playWhenNecessary(game);
        final var winnerUUID = intel.gameWinner().orElseThrow();
        final var winnerName = winnerUUID.equals(requestModel.getBot1Uuid()) ?
                requestModel.getBot1Name() : requestModel.getBot2Name();

        return new PlayWithBotsResponse(winnerUUID, winnerName);
    }
}
