package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.SimulationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class RankBotsUseCase {
    public static final int TIMES_RANK = 7;

    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi remoteBotApi;
    private final List<String> botNames;
    private final BotManagerService botManagerService;
    private boolean isRanking = false;
    private boolean hasRank = false;
    private List<BotRankInfoDto> rank = new ArrayList<>();
    Map<String, Long> resultsMap = new HashMap<>();
    private long start;

    public RankBotsUseCase(RemoteBotRepository remoteBotRepository, RemoteBotApi remoteBotApi) {
        this.remoteBotRepository = remoteBotRepository;
        this.remoteBotApi = remoteBotApi;
        botManagerService = new BotManagerService(remoteBotRepository, remoteBotApi);
        botNames = botManagerService.providersNames();
    }

    public Map<String, Long> rankAll() {
        setRank(new ArrayList<>());
        setIsRanking(true);
        start = System.currentTimeMillis();
        System.out.println("simulando");
        botNames.forEach(this::playAgainstAll);
        resultsHandler(resultsMap, botNames);
        System.out.println("terminou");
        setIsRanking(false);
        return resultsMap;
    }

    private void resultsHandler(Map<String, Long> result, List<String> botNames) {
        var sortedRankMap = result.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        long rankNumber = 1;
        for (Map.Entry<String, Long> bot : sortedRankMap.entrySet()) {
            rank.add(new BotRankInfoDto(bot.getKey(), bot.getValue(), rankNumber));
            rankNumber++;
        }
    }

    private void playAgainstAll(String botName) {

        UUID uuidBotToEvaluate = UUID.randomUUID();
        long botWins = botNames.stream()
                .filter(opponentName -> isNotEvaluatedBot(opponentName, botName))
                .map(opponent -> runSimulations(opponent, botName, uuidBotToEvaluate))
                .flatMap(List::stream)
                .filter(result -> result.name().equals(botName))
                .count();

        resultsMap.put(botName, botWins);
    }

    private boolean isNotEvaluatedBot(String opponentName, String botToEvaluateName) {
        return !opponentName.equals(botToEvaluateName);
    }

    private List<PlayWithBotsDto> runSimulations(String challengedBotName, String botToEvaluateName, UUID uuidBotToEvaluate) {
        final var simulator = new SimulationService(remoteBotRepository, remoteBotApi, botManagerService);
        return simulator.runInParallel(uuidBotToEvaluate, botToEvaluateName, UUID.randomUUID(), challengedBotName, TIMES_RANK);
    }


    public void setIsRanking(boolean ranking) {
        isRanking = ranking;
    }

    public boolean isRanking() {
        return isRanking;
    }

    public boolean hasRank() {
        return hasRank;
    }

    public void setHasRank(boolean hasRank) {
        this.hasRank = hasRank;
    }

    public long getProcessingTime() {
        return System.currentTimeMillis() - start;
    }

    public List<BotRankInfoDto> getRank() {
        return rank;
    }

    public void setRank(List<BotRankInfoDto> rank) {
        this.rank = rank;
    }
}
