package com.bueno.application.withbots.features;

import com.bueno.application.withbots.commands.UserPrompt;
import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;
import com.bueno.domain.usecases.game.usecase.EvaluateBotUseCase;

import java.util.UUID;

public class EvaluateBot {
    public static final int TIMES = 31;
    private final UUID uuidBotToEvaluate = UUID.randomUUID();

    public void againstAll() {
        final var prompt = new UserPrompt();
        final var botNames = BotProviders.availableBots();

        prompt.printAvailableBots(botNames);
        final var botToEvaluatePosition = prompt.scanBotOption(botNames);
        String botToEvaluateName = botNames.get(botToEvaluatePosition - 1);

        EvaluateBotUseCase useCase = new EvaluateBotUseCase(botToEvaluateName,TIMES);
        EvaluateResultsDto results = useCase.getResults(botNames);

        prompt.printResultEvaluateBot(results.numberOfGames(), (results.end() - results.start()), botToEvaluateName, results.evaluatedBotWins(), results.winRate(), results.percentil());
    }




}
