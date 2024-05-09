package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;
import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;

public class EvaluateBotsPrinter implements Command<Void> {
    long numberOfGames;
    long computingTime;
    String botName;
    Long botWins;
    double winRate;
    double percentile;

    public EvaluateBotsPrinter(EvaluateResultsDto resultsDto, String botName) {
        this.numberOfGames = resultsDto.numberOfGames();
        this.computingTime = resultsDto.computingTime();
        this.botName = botName;
        this.botWins = resultsDto.evaluatedBotWins();
        this.winRate = resultsDto.winRate();
        this.percentile = resultsDto.percentile();
    }

    @Override
    public Void execute() {
        System.out.println("================================================================");
        System.out.println("Time to compute " + numberOfGames + " games: " + computingTime + "ms.\n");
        System.out.println("Wins of " + botName + ": " + botWins + "/" + numberOfGames);
        System.out.printf("Win rate of all games: %.2f%%\n", winRate);
        System.out.printf("Win rate against each bot: %.2f%%\n", percentile);
        System.out.println("================================================================");
        return null;
    }
}
