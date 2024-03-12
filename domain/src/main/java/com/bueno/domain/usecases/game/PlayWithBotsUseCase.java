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
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.dtos.CreateForBotsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.repos.GameRepoDisposableImpl;
import com.bueno.domain.usecases.game.repos.GameRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Stream;

public class PlayWithBotsUseCase {

    private final UUID uuidBot1;
    private final String bot1Name;
    private final UUID uuidBot2;
    private final String bot2Name;

    public PlayWithBotsUseCase(UUID uuidBot1, String bot1Name, UUID uuidBot2, String bot2Name) {
        this.uuidBot1 = uuidBot1;
        this.bot1Name = bot1Name;
        this.uuidBot2 = uuidBot2;
        this.bot2Name = bot2Name;
    }

    public List<PlayWithBotsDto> playManyInParallel(int times) {
        final Callable<PlayWithBotsDto> gameWaitingForBeCreatedAndPlayed = this::playWithBots;
        return Stream.generate(() -> gameWaitingForBeCreatedAndPlayed)
                .limit(times)
                .parallel()
                .map(executeGameCall())
                .filter(Objects::nonNull)
                .toList();
    }

    private PlayWithBotsDto playWithBots(){
        GameRepository gameRepository = new GameRepoDisposableImpl();
        final var requestModel = new CreateForBotsDto(uuidBot1, bot1Name, uuidBot2, bot2Name);
        final CreateGameUseCase createGameUseCase = new CreateGameUseCase(gameRepository);
        createGameUseCase.createForBots(requestModel);
        final var game = gameRepository.findByPlayerUuid(requestModel.bot1Uuid()).map(GameConverter::fromDto).orElseThrow();
        final var botUseCase = new BotUseCase(gameRepository);

        //Plays the game
        final var intel = botUseCase.playWhenNecessary(game);

        final var winnerUUID = intel.gameWinner().orElseThrow();
        final var winnerName = winnerUUID.equals(requestModel.bot1Uuid()) ?
                requestModel.bot1Name() : requestModel.bot2Name();
        System.out.println("Winner: " + winnerName);
        return new PlayWithBotsDto(winnerUUID, winnerName);
    }


    private Function<Callable<PlayWithBotsDto>, PlayWithBotsDto> executeGameCall(){
        return gameCall -> {
            try {
                return gameCall.call();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
