package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;

public class EvaluateBotsPrinter implements Command<Void> {
    long numberOfGames;
    long computingTime;
    String bot;
    Long botWins;
    double winRate;
    double percentil;

    public EvaluateBotsPrinter(long numberOfGames, long computingTime, String bot, Long botWins, double winRate, double percentil) {
        this.numberOfGames = numberOfGames;
        this.computingTime = computingTime;
        this.bot = bot;
        this.botWins = botWins;
        this.winRate = winRate;
        this.percentil = percentil;
    }

    @Override
    public Void execute() {
        System.out.println("\n================================================================");
        System.out.println("Time to compute " + numberOfGames + " games: " + computingTime + "ms.\n");
        System.out.println("Wins of " + bot + ": " + botWins);
        System.out.printf("Win rate of all games: %.2f%%\nWin rate against each Bot: %.2f%%\n", winRate, percentil);
        System.out.println("================================================================");
        return null;
    }
}
