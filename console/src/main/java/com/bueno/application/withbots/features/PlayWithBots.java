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

import com.bueno.application.withbots.commands.*;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsResultsDto;
import com.bueno.domain.usecases.game.usecase.PlayWithBotsUseCase;

import java.util.List;
import java.util.UUID;

public class PlayWithBots {

    private static final UUID uuidBot1 = UUID.randomUUID();
    private static final UUID uuidBot2 = UUID.randomUUID();

    private final RemoteBotRepository repository;
    private final RemoteBotApi botApi;
    private final BotManagerService providerService;

    private String bot1Name;
    private String bot2Name;
    private int times;

    public PlayWithBots(RemoteBotRepository repository, RemoteBotApi botApi, BotManagerService providerService) {
        this.repository = repository;
        this.botApi = botApi;
        this.providerService = providerService;
    }

    public void playWithBotsConsole() {
        final var botNames = providerService.providersNames();

        printAvailableBots(botNames);

        final var bot1 = scanBotOption(botNames);
        final var bot2 = scanBotOption(botNames);
        times = scanNumberOfSimulations();

        showWaitingMessage();

        bot1Name = botNames.get(bot1 - 1);
        bot2Name = botNames.get(bot2 - 1);

        final PlayWithBotsResultsDto results = playBotsStarter(providerService);

        printResult(results);
    }

    private int scanNumberOfSimulations() {
        NumberOfSimulationsReader scanSimulations = new NumberOfSimulationsReader();
        return scanSimulations.execute();
    }

    private PlayWithBotsResultsDto playBotsStarter(BotManagerService botManagerService) {
        final PlayWithBotsUseCase useCase = new PlayWithBotsUseCase(repository, botApi, botManagerService);
        return useCase.playWithBots(uuidBot1, bot1Name, uuidBot2, bot2Name, times);
    }

    private void printAvailableBots(List<String> botNames) {
        BotsAvailablePrinter printer = new BotsAvailablePrinter(botNames);
        printer.execute();
    }

    private int scanBotOption(List<String> botNames) {
        BotOptionReader scanOptions = new BotOptionReader(botNames);
        return scanOptions.execute();
    }

    private void printResult(PlayWithBotsResultsDto result) {
        PlayWithBotsPrinter printer = new PlayWithBotsPrinter(result.times(), result.timeToExecute(), result.info());
        printer.execute();
    }

    private void showWaitingMessage() {
        WaitingMessagePrinter messagePrinter = new WaitingMessagePrinter();
        messagePrinter.execute();
    }
}
