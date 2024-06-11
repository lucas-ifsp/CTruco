package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.SimulationService;
import com.bueno.domain.usecases.game.service.WinsAccumulatorService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public class EvaluateBotsUseCase {
    private static final int TIMES = 31;
    private final UUID uuidBotToEvaluate = UUID.randomUUID();
    private String botToEvaluateName;
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi botApi;
    private final BotManagerService providerService;

    public EvaluateBotsUseCase(RemoteBotRepository remoteBotRepository, RemoteBotApi botApi, BotManagerService providerService) {
        this.remoteBotRepository = remoteBotRepository;
        this.botApi = botApi;
        this.providerService = providerService;
    }

    public EvaluateResultsDto getResults(List<String> botNames,String botToEvaluateName) {
        setBotToEvaluateName(botToEvaluateName);
        final int numberOfGames = (botNames.size() - 1) * TIMES;

        final long start = System.currentTimeMillis();
        var results = botNames.stream().filter(this::isNotEvaluatedBot).map(this::runSimulations).toList();
        final long end = System.currentTimeMillis();

        final long evaluatedBotWins = results.stream().mapToLong(this::resultAccumulator).sum();
        final long gameWins = results.stream().mapToLong(match -> WinsAccumulatorService.getWins(match, botToEvaluateName, TIMES)).sum();

        double winRate = ((double) evaluatedBotWins / numberOfGames) * 100;
        double percentile = (((double) gameWins / (botNames.size() - 1)) * 100);

        return new EvaluateResultsDto((end - start), numberOfGames, evaluatedBotWins, winRate, percentile, gameWins);
    }

    private boolean isNotEvaluatedBot(String opponentName) {
        return !opponentName.equals(botToEvaluateName);
    }

    private List<PlayWithBotsDto> runSimulations(String challengedBotName) {
        final var playManyService = new SimulationService(remoteBotRepository, botApi,providerService);
        return playManyService.runInParallel(uuidBotToEvaluate, botToEvaluateName,UUID.randomUUID(), challengedBotName, TIMES);
    }

    private Long resultAccumulator(List<PlayWithBotsDto> results) {
        Map<String, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(PlayWithBotsDto::name, Collectors.counting()));
        if (!collectResults.containsKey(botToEvaluateName)) return 0L;
        return collectResults.get(botToEvaluateName);
    }

    public void setBotToEvaluateName(String botToEvaluateName) {
        this.botToEvaluateName = botToEvaluateName;
    }
}
