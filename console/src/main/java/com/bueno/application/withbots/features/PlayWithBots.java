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

package com.bueno.application.withbots.features;

import com.bueno.application.withbots.commands.UserPrompt;
import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.usecase.PlayWithBotsUseCase;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;

import java.util.List;
import java.util.UUID;

public class PlayWithBots {

    private static final UUID uuidBot1 = UUID.randomUUID();
    private static final UUID uuidBot2 = UUID.randomUUID();
    private String bot1Name;
    private String bot2Name;
    private int times;


    public void playWithBotsConsole(){
        final var prompt = new UserPrompt();
        final var botNames = BotProviders.availableBots();

        prompt.printAvailableBots(botNames);

        final var bot1 = prompt.scanBotOption(botNames);
        final var bot2 = prompt.scanBotOption(botNames);
        times = prompt.scanNumberOfSimulations();

        bot1Name = botNames.get(bot1 - 1);
        bot2Name = botNames.get(bot2 - 1);

        final long start = System.currentTimeMillis();
        final var results = playBotsStarter();
        final long end = System.currentTimeMillis();
        prompt.printResult(times, (end - start), results);
    }

    private List<PlayWithBotsDto> playBotsStarter() {
        final var useCase = new PlayWithBotsUseCase(uuidBot1, bot1Name, uuidBot2, bot2Name);
        return useCase.playManyInParallel(times);
    }


}
