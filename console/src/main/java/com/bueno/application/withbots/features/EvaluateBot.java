package com.bueno.application.withbots.features;

import com.bueno.application.withbots.commands.BotsAvailablePrinter;
import com.bueno.application.withbots.commands.BotOptionReader;
import com.bueno.application.withbots.commands.EvaluateBotsPrinter;
import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;
import com.bueno.domain.usecases.game.usecase.EvaluateBotsUseCase;

import java.util.List;

public class EvaluateBot {
    public static final int TIMES = 31;

    public void againstAll() {
        final var botNames = BotProviders.availableBots();

        printAvailableBots(botNames);
        final var botToEvaluatePosition = scanBotOption(botNames);
        String botToEvaluateName = botNames.get(botToEvaluatePosition - 1);

        EvaluateBotsUseCase useCase = new EvaluateBotsUseCase(botToEvaluateName, TIMES);
        EvaluateResultsDto results = useCase.getResults(botNames);

        printResultEvaluateBot(
                results.numberOfGames(),
                (results.end() - results.start()),
                botToEvaluateName,
                results.evaluatedBotWins(),
                results.winRate(),
                results.percentil()
        );
    }

    private void printAvailableBots(List<String> botNames) {
        BotsAvailablePrinter printer = new BotsAvailablePrinter(botNames);
        printer.execute();
    }

    private int scanBotOption(List<String> botNames) {
        BotOptionReader scanOptions = new BotOptionReader(botNames);
        return scanOptions.execute();
    }

    private void printResultEvaluateBot(long numberOfGames, long computingTime, String bot, Long botWins, double winRate, double percentil) {
        EvaluateBotsPrinter printer = new EvaluateBotsPrinter(numberOfGames, computingTime, bot, botWins, winRate, percentil);
        printer.execute();
    }
}
