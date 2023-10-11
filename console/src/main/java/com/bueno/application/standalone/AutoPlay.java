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

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoPlay {

    private static final UUID uuidBot1 = UUID.randomUUID();
    private static final UUID uuidBot2 = UUID.randomUUID();

    public static void main(String[] args) {
        final var main = new AutoPlay();
        final var prompt = new UserPrompt();
        final var botNames = BotProviders.availableBots();
        final var numBots = BotProviders.availableBots().size();
        final var times = prompt.scanNumberOfSimulations();

        for (int bot1 = 0; bot1 < numBots; bot1++) {
            for (int bot2 = bot1; bot2 < numBots; bot2++) {
                if (bot1 == bot2) continue;
                final var results = main.playManyInParallel(times, botNames.get(bot1), botNames.get(bot2));
                prompt.printResult(getResult(results));
            }
        }
    }

    private List<PlayWithBotsDto> playManyInParallel(int times, String bot1Name, String bot2Name) {
        final Callable<PlayWithBotsDto> playGame = () -> play(bot1Name, bot2Name);
        return Stream.generate(() -> playGame)
                .limit(times)
                .parallel()
                .map(executeGameCall())
                .filter(Objects::nonNull)
                .toList();
    }

    private PlayWithBotsDto play(String bot1Name, String bot2Name){
        final var repo = new GameRepoDisposableImpl();
        final var useCase = new PlayWithBotsUseCase(repo);
        final var requestModel = new CreateForBotsDto(uuidBot1, bot1Name, uuidBot2, bot2Name);
        final var result = useCase.playWithBots(requestModel);
        return result;
    }

    private static Function<Callable<PlayWithBotsDto>, PlayWithBotsDto> executeGameCall() {
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

    private static LinkedHashMap<PlayWithBotsDto, Long> getResult(List<PlayWithBotsDto> result) {
        LinkedHashMap<PlayWithBotsDto, Long> map = new LinkedHashMap<>();
         result.stream()
                 .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                 .forEach((bot, wins) -> map.put(bot, wins));
        return map;
    }

}
