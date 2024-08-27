package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.SimulationService;
import com.bueno.domain.usecases.game.service.WinsAccumulatorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RankBotsUseCase {
    private final int TIMES = 7;

    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi remoteBotApi;
    private final Map<String, Long> rankMap;
    private final List<String> botNames;
    private final BotManagerService botManagerService;
    private boolean isRanking = false;
    private boolean hasRank = false;
    private List<BotRankInfoDto> rank;

    public RankBotsUseCase(RemoteBotRepository remoteBotRepository, RemoteBotApi remoteBotApi) {
        rankMap = new HashMap<>();
        this.remoteBotRepository = remoteBotRepository;
        this.remoteBotApi = remoteBotApi;
        botManagerService = new BotManagerService(remoteBotRepository, remoteBotApi);
        botNames = botManagerService.providersNames();
    }

    public Map<String, Long> rankAll() {
        setIsRanking(true);
        setHasRank(false);
        botNames.forEach(this::playAgainstAll);
        //TODO - deixar só rank, ou seja, retirar esse rankMap
        setRank(rankMapToBotRankInfoDto(rankMap));
        setIsRanking(false);
        setHasRank(true);
        return rankMap;
    }

    private void playAgainstAll(String botName) {
        UUID uuidBotToEvaluate = UUID.randomUUID();
        var results = botNames.stream()
                .filter(opponentName -> isNotEvaluatedBot(opponentName, botName))
                .map(opponent -> runSimulations(opponent, botName, uuidBotToEvaluate))
                .toList();

        Long botWins = results.stream().mapToLong(match -> WinsAccumulatorService.getWins(match, botName, TIMES))
                .sum();
        rankMap.put(botName, botWins);
    }

    private boolean isNotEvaluatedBot(String opponentName, String botToEvaluateName) {
        return !opponentName.equals(botToEvaluateName);
    }

    private List<PlayWithBotsDto> runSimulations(String challengedBotName, String botToEvaluateName, UUID uuidBotToEvaluate) {
        final var simulator = new SimulationService(remoteBotRepository, remoteBotApi, botManagerService);
        return simulator.runInParallel(uuidBotToEvaluate, botToEvaluateName, UUID.randomUUID(), challengedBotName, TIMES);
    }

    private List<BotRankInfoDto> rankMapToBotRankInfoDto(Map<String, Long> rankMap) {
        return rankMap.entrySet().stream()
                .map(entry -> new BotRankInfoDto(entry.getKey(), entry.getValue()))
                .toList();
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

    public List<BotRankInfoDto> getRank() {
        return rank;
    }

    public void setRank(List<BotRankInfoDto> rank) {
        this.rank = rank;
    }
}
