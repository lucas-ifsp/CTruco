/*
 *  Copyright (C) 2023 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.application.withbots.commands;

import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.google.common.primitives.Ints;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class UserPrompt {

    private final Scanner scanner;

    public UserPrompt() {
        this.scanner = new Scanner(System.in);
    }

    public void printAvailableBots(List<String> botNames) {
        for (int i = 0; i < botNames.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + botNames.get(i));
        }
        System.out.print("\n");
    }

    public int scanBotOption(List<String> botNames) {
        Integer botNumber;
        while (true) {
            System.out.print("Select a bot by number: ");
            botNumber = Ints.tryParse(scanner.nextLine());
            if (botNumber == null || botNumber < 1 || botNumber > botNames.size()) {
                System.out.println("Invalid input!A");
                continue;
            }
            break;
        }
        return botNumber;
    }

    public int scanNumberOfSimulations() {
        final var scanner = new Scanner(System.in);
        System.out.print("Number of simulations: ");
        final int times = scanner.nextInt();
        System.out.println("\nStarting simulation... it may take a while: ");
        return times;
    }

    public void printResult(int numberOfGames, long computingTime, List<PlayWithBotsDto> results) {
        System.out.println("\n================================================================");
        System.out.println("Time to compute " + numberOfGames + " games: " + computingTime + "ms.\n");
        results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((bot, wins) -> System.out.println(bot.name() + ": " + wins));
        System.out.println("================================================================");
    }

    public void printResultEvaluateBot(long numberOfGames, long computingTime, String bot, Long botWins, double winRate,double percentil) {
        System.out.println("\n================================================================");
        System.out.println("Time to compute " + numberOfGames + " games: " + computingTime + "ms.\n");
        System.out.println("Wins of " + bot + ": " + botWins);
        System.out.printf("Win rate of all: %.2f%%\nWin rate against each Bot: %.2f%%\n", winRate,percentil);
        System.out.println("================================================================");
    }
}
