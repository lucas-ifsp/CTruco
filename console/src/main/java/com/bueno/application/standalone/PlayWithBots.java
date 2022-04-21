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

import com.bueno.domain.usecases.bot.BotUseCase;
import com.bueno.domain.usecases.game.*;
import com.bueno.persistence.inmemory.InMemoryGameRepository;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class PlayWithBots {

    private final UUID uuidBot1;
    private final UUID uuidBot2;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LogManager.getLogManager().reset();

        final var main = new PlayWithBots();

        final var botNames = BotUseCase.availableBots();
        printAvailableBots(botNames);

        final var bot1 = scanBotOption(botNames);
        final var bot2 = scanBotOption(botNames);
        final var times = scanNumberOfSimulations();

        final var results = main
                .playInParallel(botNames.get(bot1 - 1), botNames.get(bot2 - 1), times);

        //final List<ResponseModel> results = main.play(botNames.get(bot1 - 1), botNames.get(bot2 - 1), times);

        results.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((bot, wins) -> System.out.println(bot.getName() + " (" + bot.getUuid() + "): " + wins));
    }

    public PlayWithBots(){
        this.uuidBot1 = UUID.randomUUID();
        this.uuidBot2 = UUID.randomUUID();
    }

    private static void printAvailableBots(List<String> botNames) {
        for (int i = 0; i < botNames.size(); i++){
            System.out.print("[" + (i + 1) + "] " + botNames.get(i) + "\t");
        }
        System.out.print("\n");
    }

    private static Integer scanBotOption(List<String> botNames) {
        var scanner = new Scanner(System.in);
        Integer botNumber;
        while (true){
            System.out.print("Select a bot by number: ");
            botNumber = Ints.tryParse(scanner.nextLine());
            if(botNumber == null || botNumber < 1 || botNumber > botNames.size()){
                System.out.println("Invalid input!");
                continue;
            }
            break;
        }
        return botNumber;
    }

    private static int scanNumberOfSimulations() {
        final var scanner = new Scanner(System.in);
        System.out.print("Number of simulations: ");
        final int times = scanner.nextInt();
        System.out.println("Starting simulation... it may take a while: ");
        return times;
    }

    public List<PlayWithBotsResponseModel> play(String bot1Name, String bot2Name, int times) throws InterruptedException {
        final List<PlayWithBotsResponseModel> result = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            final var useCase = createNewGameSettings();
            final var requestModel = new CreateForBotsRequestModel(uuidBot1, bot1Name, uuidBot2, bot2Name);
            final var responseModel = useCase.playWithBots(requestModel);
            result.add(responseModel);
            final var winnerUuid = responseModel.getUuid();
            final var winnerName = winnerUuid.equals(uuidBot1) ? bot1Name : bot2Name;
            System.err.printf("Winner: %s (%s).\n", winnerName, winnerUuid);
            TimeUnit.SECONDS.sleep(1);
        }
        return result;
    }

    private PlayWithBotsUseCase createNewGameSettings() {
        final var repo = new InMemoryGameRepository();
        final var createGameUseCase = new CreateGameUseCase(repo, null);
        final var findGameUseCase = new FindGameUseCase(repo);
        final var botUseCase = new BotUseCase(repo);
        return new PlayWithBotsUseCase(createGameUseCase, findGameUseCase, botUseCase);
    }

    public List<PlayWithBotsResponseModel> playInParallel(String bot1Name, String bot2Name, int times) throws InterruptedException, ExecutionException {
        final int numberOfThreads = Math.max(1, times / 10000);
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final List<Callable<PlayWithBotsResponseModel>> games = new ArrayList<>();

        final Callable<PlayWithBotsResponseModel> game = () -> {
            final var useCase = createNewGameSettings();
            final var requestModel = new CreateForBotsRequestModel(uuidBot1, bot1Name, uuidBot2, bot2Name);
            return useCase.playWithBots(requestModel);
        };

        for (int i = 0; i < times; i++) {
            games.add(game);
        }

        final List<PlayWithBotsResponseModel> result = new ArrayList<>();
        for (Future<PlayWithBotsResponseModel> future : executor.invokeAll(games)) {
            result.add(future.get());
        }

        executor.shutdown();
        return result;
    }
}
