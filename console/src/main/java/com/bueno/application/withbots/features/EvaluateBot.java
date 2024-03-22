package com.bueno.application.withbots.features;

import com.bueno.application.withbots.commands.UserPrompt;
import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.usecase.PlayWithBotsUseCase;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EvaluateBot {
    public static final int NUMBER_OF_GAMES = 31;
    private final UUID uuidBotToEvaluate = UUID.randomUUID();
    private String botToEvaluateName;
    final int times = 31;

    public void againstAll() {
        final var prompt = new UserPrompt();
        final var botNames = BotProviders.availableBots();

        prompt.printAvailableBots(botNames);
        final var botToEvaluatePosition = prompt.scanBotOption(botNames);
        botToEvaluateName = botNames.get(botToEvaluatePosition - 1);

        final long start = System.currentTimeMillis();
        final long numberOfGames = botNames.stream().filter(this::isNotEvaluatedBot).count() * NUMBER_OF_GAMES;
        var results = botNames.stream().filter(this::isNotEvaluatedBot).map(this::runSimulations).toList();
        final long evaluatedBotWins = results.stream().mapToLong(this::resultAccumulator).sum();
        final long gameWins = results.stream().mapToLong(this::winsAccumulator).sum();
        final long end = System.currentTimeMillis();
        double winRate = ((double) evaluatedBotWins / numberOfGames) * 100;
        double percentil = (((double) gameWins/(botNames.size() - 1))*100);
        prompt.printResultEvaluateBot(numberOfGames, (end - start), botToEvaluateName, evaluatedBotWins, winRate,percentil);
    }

    private boolean isNotEvaluatedBot(String opponentName) {
        return !opponentName.equals(botToEvaluateName);
    }

    private List<PlayWithBotsDto> runSimulations(String challengedBotName) {
        UUID opponentUuid = UUID.randomUUID();
        final var useCase = new PlayWithBotsUseCase(uuidBotToEvaluate, botToEvaluateName, opponentUuid, challengedBotName);
        return useCase.playManyInParallel(times);
    }

    private Long resultAccumulator(List<PlayWithBotsDto> results) {
        final PlayWithBotsDto bot1 = results.get(0);
        Map<PlayWithBotsDto, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collectResults.get(bot1);
    }
    private Long winsAccumulator(List<PlayWithBotsDto> results) {
        final PlayWithBotsDto bot1 = results.get(0);
        Map<PlayWithBotsDto, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if (collectResults.get(bot1)>(NUMBER_OF_GAMES/2)) return 1L;
        return 0L;
    }


}
