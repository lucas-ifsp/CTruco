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

package com.bueno.application.standalone;

import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.PlayWithBotsUseCase;
import com.bueno.domain.usecases.game.dtos.CreateForBotsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.repos.GameRepoDisposableImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlayWithBots {

    private final UUID uuidBot1;
    private final UUID uuidBot2;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final var main = new PlayWithBots();
        final var prompt = new UserPrompt();

        final var botNames = BotProviders.availableBots();
        prompt.printAvailableBots(botNames);

        final var bot1 = prompt.scanBotOption(botNames);
        final var bot2 = prompt.scanBotOption(botNames);
        final var times = prompt.scanNumberOfSimulations();

        final var results = main.playManyInParallel(times, botNames.get(bot1 - 1), botNames.get(bot2 - 1));

        //final var results = main.playMany(times, botNames.get(bot1 - 1), botNames.get(bot2 - 1));

        results.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((bot, wins) -> System.out.println(bot.name() + " (" + bot.uuid() + "): " + wins));
    }

    public PlayWithBots(){
        this.uuidBot1 = UUID.randomUUID();
        this.uuidBot2 = UUID.randomUUID();
    }


    public List<PlayWithBotsDto> playMany(int times, String bot1Name, String bot2Name) {
        final List<PlayWithBotsDto> result = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            final var responseModel = playGame(bot1Name, bot2Name);
            result.add(responseModel);
            final var winnerUuid = responseModel.uuid();
            final var winnerName = winnerUuid.equals(uuidBot1) ? bot1Name : bot2Name;
            System.err.printf("Winner: %s (%s).\n", winnerName, winnerUuid);
        }
        return result;
    }

    private PlayWithBotsDto playGame(String bot1Name, String bot2Name) {
        final var repo = new GameRepoDisposableImpl();
        final var useCase = new PlayWithBotsUseCase(repo);
        final var requestModel = new CreateForBotsDto(uuidBot1, bot1Name, uuidBot2, bot2Name);
        return useCase.playWithBots(requestModel);
    }

    public List<PlayWithBotsDto> playManyInParallel(int times, String bot1Name, String bot2Name) throws InterruptedException, ExecutionException {
        final int numberOfThreads = Math.max(1, times / 10000);
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final List<Callable<PlayWithBotsDto>> games = new ArrayList<>();

        for (int i = 0; i < times; i++) {
            games.add(() -> playGame(bot1Name, bot2Name));
        }

        final List<PlayWithBotsDto> result = new ArrayList<>();
        for (Future<PlayWithBotsDto> future : executor.invokeAll(games)) {
            result.add(future.get());
        }

        executor.shutdown();
        return result;
    }
}
