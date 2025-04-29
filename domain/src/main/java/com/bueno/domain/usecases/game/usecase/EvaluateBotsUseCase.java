package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.SimulationService;
import com.bueno.domain.usecases.game.service.WinsAccumulatorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EvaluateBotsUseCase {
    public static final int EVALUATE_TIMES = 31;
    private final UUID uuidBotToEvaluate = UUID.randomUUID();
    private String botToEvaluateName;
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi botApi;
    private final BotManagerService providerService;

    public EvaluateBotsUseCase(RemoteBotRepository remoteBotRepository,
                               RemoteBotApi botApi,
                               BotManagerService providerService
    ) {
        this.remoteBotRepository = remoteBotRepository;
        this.botApi = botApi;
        this.providerService = providerService;
    }

    public EvaluateResultsDto evaluate(List<String> botNames, String botToEvaluateName) {
        return executeSimulations(botToEvaluateName, botNames);
    }

    public EvaluateResultsDto evaluateWithAll(String botToEvaluateName) {
        List<String> botNames = providerService.providersNames();
        return executeSimulations(botToEvaluateName, botNames);
    }

    private EvaluateResultsDto executeSimulations(String botToEvaluateName, List<String> botNames) {
        setBotToEvaluateName(botToEvaluateName);
        final long numberOfGames = (long) (botNames.size() - 1) * EVALUATE_TIMES;

        final long start = System.currentTimeMillis();
        List<List<PlayWithBotsDto>> results = botNames.stream().filter(this::isNotEvaluatedBot).map(this::run).toList();
        final long end = System.currentTimeMillis();

        final long computingTime = (end - start);

        final long evaluatedBotWins = results.stream().mapToLong(this::resultAccumulator).sum();
        final long defeatedOpponents = results.stream().mapToLong(match -> WinsAccumulatorService.getWins(match, botToEvaluateName, EVALUATE_TIMES)).sum();
        final long numberOfOpponents = results.size();

        double winRate = ((double) evaluatedBotWins / numberOfGames) * 100;
        double percentile = (((double) defeatedOpponents / (botNames.size() - 1)) * 100);

        return new EvaluateResultsDto(botToEvaluateName,computingTime, numberOfGames, evaluatedBotWins, winRate, percentile, defeatedOpponents, numberOfOpponents);
    }

    private boolean isNotEvaluatedBot(String opponentName) {
        return !opponentName.equals(botToEvaluateName);
    }

    private List<PlayWithBotsDto> run(String challengedBotName) {
        final var playManyService = new SimulationService(remoteBotRepository, botApi, providerService);
        return playManyService.runInParallel(uuidBotToEvaluate, botToEvaluateName, UUID.randomUUID(), challengedBotName, EVALUATE_TIMES);
    }

    private Long resultAccumulator(List<PlayWithBotsDto> results) {
        Map<String, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(PlayWithBotsDto::name, Collectors.counting()));
        if (!collectResults.containsKey(botToEvaluateName)) return 0L;
        return collectResults.get(botToEvaluateName);
    }

    private void setBotToEvaluateName(String botToEvaluateName) {
        this.botToEvaluateName = botToEvaluateName;
    }
}
