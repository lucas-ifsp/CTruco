package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.SimulationService;
import com.bueno.domain.usecases.game.service.WinsAccumulatorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RankBotsUseCase {
    private final int TIMES = 7;
    private final Map<String, Long> rankMap = new HashMap<>();
    private final List<String> botNames = BotProviders.availableBots();

    public Map<String, Long> rankAll() {
        botNames.forEach(this::playAgainstAll);
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
        final var simulator = new SimulationService(uuidBotToEvaluate, botToEvaluateName, challengedBotName);
        return simulator.runInParallel(TIMES);
    }

}
