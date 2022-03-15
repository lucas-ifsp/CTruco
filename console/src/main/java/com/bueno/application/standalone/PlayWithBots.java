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
import com.bueno.domain.usecases.game.PlayWithBotsUseCase;
import com.bueno.domain.usecases.game.PlayWithBotsUseCase.ResponseModel;
import com.bueno.persistence.inmemory.InMemoryGameRepository;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class PlayWithBots {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LogManager.getLogManager().reset();

        final List<String> botNames = BotUseCase.availableBots();
        printAvailableBots(botNames);

        final Integer bot1 = scanBotOption(botNames);
        final Integer bot2 = scanBotOption(botNames);
        final int times = scanNumberOfSimulations();

        final PlayWithBots main = new PlayWithBots();
        final List<ResponseModel> results = main.play(botNames.get(bot1-1), botNames.get(bot2-1), times);

        results.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((bot, wins) -> System.out.println(bot.name() + " (" + bot.uuid() + "): " + wins));
    }

    private static void printAvailableBots(List<String> botNames) {
        for (int i = 0; i < botNames.size(); i++){
            System.out.print("[" + (i + 1) + "] " + botNames.get(i) + "\t");
        }
        System.out.print("\n");
    }

    private static Integer scanBotOption(List<String> botNames) {
        Scanner scanner = new Scanner(System.in);
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Number of simulations: ");
        final int times = scanner.nextInt();
        System.out.println("Starting simulation... it may take a while: ");
        return times;
    }

/*
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            final InMemoryGameRepository repo = new InMemoryGameRepository();
            PlayGameWithBotsUseCase uc = new PlayGameWithBotsUseCase(repo);
            final UUID uuid = uc.playWithBots(new MineiroBot(repo, uuid1), new MineiroBot(repo, uuid2));
            System.err.println("Winner: " + (uuid.equals(uuid1) ? "MineiroBot1" : "MineiroBot2"));
            //TimeUnit.SECONDS.sleep(1);
        }
    }
*/

    public List<ResponseModel> play(String bot1Name, String bot2Name, int times) throws InterruptedException, ExecutionException {
        final int numberOfThreads = Math.max(1, times / 10000);
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final List<Callable<ResponseModel>> games = new ArrayList<>();

        final Callable<ResponseModel> game = () -> {
            final var repo = new InMemoryGameRepository();
            var useCase = new PlayWithBotsUseCase(repo);
            return useCase.playWithBots(bot1Name, bot2Name);
        };

        for (int i = 0; i < times; i++) {
            games.add(game);
        }

        final List<ResponseModel> result = new ArrayList<>();
        for (Future<ResponseModel> future : executor.invokeAll(games)) {
            result.add(future.get());
        }

        executor.shutdown();
        return result;
    }
}
