package com.bueno.application.standalone;

import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.PlayWithBotsUseCase;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EvaluateBot {
    private final UUID uuidBotToEvaluate = UUID.randomUUID();
    private String botToEvaluateName;
    private UUID uuidChallengedBot;
    private String challengedBotName;
    final int times = 30;

    public void botTester(){
        final var prompt = new UserPrompt();
        final var botNames = BotProviders.availableBots();

        prompt.printAvailableBots(botNames);

        final var botToEvaluatePosition = prompt.scanBotOption(botNames);

        botToEvaluateName = botNames.get(botToEvaluatePosition - 1);


        Long evaluatedBotWins = 0L;
        int numberOfGames = 0;
        final long start = System.currentTimeMillis();
        for(String bot:botNames){
            challengedBotName = bot;
            numberOfGames += 30;
            if (! challengedBotName.equals(botToEvaluateName)){

                uuidChallengedBot = UUID.randomUUID();

                var results = runSimulations();

                evaluatedBotWins += resultAccumulator(results);
            }
        }
        final long end = System.currentTimeMillis();
        prompt.printResultEvaluateBot(numberOfGames,(end-start),botToEvaluateName,evaluatedBotWins);
    }

    private List<PlayWithBotsDto> runSimulations(){
        final var useCase = new PlayWithBotsUseCase(uuidBotToEvaluate, botToEvaluateName, uuidChallengedBot, challengedBotName);
        return useCase.playManyInParallel(times);
    }

    private Long resultAccumulator(List<PlayWithBotsDto> results){
        final PlayWithBotsDto bot1 = results.get(0);
        Map<PlayWithBotsDto, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collectResults.get(bot1);
    }
}
