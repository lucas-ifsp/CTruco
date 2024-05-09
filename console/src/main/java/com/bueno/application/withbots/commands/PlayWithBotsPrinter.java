package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlayWithBotsPrinter implements Command<Void> {
    int numberOfGames;
    long computingTime;
    List<PlayWithBotsDto> results;

    public PlayWithBotsPrinter(int numberOfGames, long computingTime, List<PlayWithBotsDto> results) {
        this.numberOfGames = numberOfGames;
        this.computingTime = computingTime;
        this.results = results;
    }

    @Override
    public Void execute() {
        System.out.println("\n================================================================");
        System.out.println("Time to compute " + numberOfGames + " games: " + computingTime + "ms.\n");
        results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((bot, wins) -> System.out.println(bot.name() + ": " + wins));
        System.out.println("================================================================");
        return null;
    }
}
