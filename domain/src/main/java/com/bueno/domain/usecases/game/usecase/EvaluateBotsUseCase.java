package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.PlayManyInParallelService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EvaluateBotsUseCase {
    private final UUID uuidBotToEvaluate = UUID.randomUUID();
    private final String botToEvaluateName;
    public static final int TIMES = 31;

    public EvaluateBotsUseCase(String botToEvaluateName) {
        this.botToEvaluateName = botToEvaluateName;
    }

    public EvaluateResultsDto getResults(List<String> botNames) {
        final int numberOfGames = (botNames.size() - 1) * TIMES;

        final long start = System.currentTimeMillis();
        var results = botNames.stream().filter(this::isNotEvaluatedBot).map(this::runSimulations).toList();
        final long end = System.currentTimeMillis();

        final long evaluatedBotWins = results.stream().mapToLong(this::resultAccumulator).sum();
        final long gameWins = results.stream().mapToLong(this::winsAccumulator).sum();

        double winRate = ((double) evaluatedBotWins / numberOfGames) * 100;
        double percentile = (((double) gameWins / (botNames.size() - 1)) * 100);

        return new EvaluateResultsDto(start, numberOfGames, evaluatedBotWins, end, winRate, percentile, gameWins);
    }

    private boolean isNotEvaluatedBot(String opponentName) {
        return !opponentName.equals(botToEvaluateName);
    }

    private List<PlayWithBotsDto> runSimulations(String challengedBotName) {
        UUID opponentUuid = UUID.randomUUID();
        final var playManyService = new PlayManyInParallelService(uuidBotToEvaluate, botToEvaluateName, opponentUuid, challengedBotName);
        return playManyService.playManyInParallel(TIMES);
    }

    private Long resultAccumulator(List<PlayWithBotsDto> results) {
        final PlayWithBotsDto bot1 = results.stream().filter(bot -> !isNotEvaluatedBot(bot.name())).toList().get(0);
        Map<PlayWithBotsDto, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collectResults.get(bot1);
    }

    private Long winsAccumulator(List<PlayWithBotsDto> results) {
        final PlayWithBotsDto bot1 = results.stream().filter(bot -> !isNotEvaluatedBot(bot.name())).toList().get(0);
        Map<PlayWithBotsDto, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if (collectResults.get(bot1) > (TIMES / 2)) return 1L;
        return 0L;
    }
}
