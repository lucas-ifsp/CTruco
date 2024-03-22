package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EvaluateBotUseCase {
    private final UUID uuidBotToEvaluate = UUID.randomUUID();
    private final String botToEvaluateName;
    private final int times;

    public EvaluateBotUseCase(String botToEvaluateName,int times) {
        this.botToEvaluateName = botToEvaluateName;
        this.times = times;
    }

    public EvaluateResultsDto getResults(List<String> botNames) {
        final long start = System.currentTimeMillis();
        final long numberOfGames = botNames.stream().filter(this::isNotEvaluatedBot).count() * times;
        var results = botNames.stream().filter(this::isNotEvaluatedBot).map(this::runSimulations).toList();
        final long evaluatedBotWins = results.stream().mapToLong(this::resultAccumulator).sum();
        final long gameWins = results.stream().mapToLong(this::winsAccumulator).sum();
        final long end = System.currentTimeMillis();
        double winRate = ((double) evaluatedBotWins / numberOfGames) * 100;
        double percentil = (((double) gameWins/(botNames.size() - 1))*100);
        return new EvaluateResultsDto(start, numberOfGames, evaluatedBotWins, end, winRate, percentil);
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
        if (collectResults.get(bot1)>(times/2)) return 1L;
        return 0L;
    }
}
