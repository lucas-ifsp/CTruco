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

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayWithBots {

    private static final UUID uuidBot1 = UUID.randomUUID();
    private static final UUID uuidBot2 = UUID.randomUUID();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final var main = new PlayWithBots();
        final var prompt = new UserPrompt();

        final var botNames = BotProviders.availableBots();
        prompt.printAvailableBots(botNames);

        final var bot1 = prompt.scanBotOption(botNames);
        final var bot2 = prompt.scanBotOption(botNames);
        final var times = prompt.scanNumberOfSimulations();

        final long start = System.currentTimeMillis();
        final var results = main.playManyInParallel2(times, botNames.get(bot1 - 1), botNames.get(bot2 - 1));
        final long end = System.currentTimeMillis();
        System.out.println("Time to compute " + times + " games: " + (end - start) + "ms.");

        results.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((bot, wins) -> System.out.println(bot.name() + " (" + bot.uuid() + "): " + wins));
    }

    public List<PlayWithBotsDto> playManyInParallel2(int times, String bot1Name, String bot2Name) throws InterruptedException, ExecutionException {
        Callable<PlayWithBotsDto> gameCall = () -> playGame(bot1Name, bot2Name);
        return Stream.generate(() -> gameCall)
                .limit(times)
                .parallel()
                .map(tryToPlay(bot1Name, bot2Name))
                .filter(Objects::nonNull)
                .toList();
    }

    private static PlayWithBotsDto playGame(String bot1Name, String bot2Name) {
        final var repo = new GameRepoDisposableImpl();
        final var useCase = new PlayWithBotsUseCase(repo);
        final var requestModel = new CreateForBotsDto(uuidBot1, bot1Name, uuidBot2, bot2Name);
        return useCase.playWithBots(requestModel);
    }

    private static Function<Callable<PlayWithBotsDto>, PlayWithBotsDto> tryToPlay(String bot1Name, String bot2Name) {
        return callable -> {
            try {
                final var result = playGame(bot1Name, bot2Name);
                final var winnerUuid = result.uuid();
                final var winnerName = winnerUuid.equals(uuidBot1) ? bot1Name : bot2Name;
                System.err.printf("Winner: %s (%s).\n", winnerName, winnerUuid);
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
