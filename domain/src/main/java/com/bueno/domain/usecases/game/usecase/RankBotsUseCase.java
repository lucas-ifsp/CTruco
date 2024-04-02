package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.PlayManyInParallelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Long botWins = results.stream().mapToLong(opponent -> winsAccumulator(opponent, botName, uuidBotToEvaluate))
                .sum();
        rankMap.put(botName, botWins);
    }

    private boolean isNotEvaluatedBot(String opponentName, String botToEvaluateName) {
        return !opponentName.equals(botToEvaluateName);
    }

    private List<PlayWithBotsDto> runSimulations(String challengedBotName, String botToEvaluateName, UUID uuidBotToEvaluate) {
        UUID opponentUuid = UUID.randomUUID();

        final var playManyService = new PlayManyInParallelService(uuidBotToEvaluate, botToEvaluateName, opponentUuid, challengedBotName);
        return playManyService.playManyInParallel(TIMES); // TODO adicionar ao Service (mudar o nome para SimulationService.runInParallel(TIMES))
    }

    private Long winsAccumulator(List<PlayWithBotsDto> results, String botToEvaluateName, UUID uuidBotToEvaluate) {
        final PlayWithBotsDto bot1 = new PlayWithBotsDto(uuidBotToEvaluate, botToEvaluateName);
        Map<PlayWithBotsDto, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if (!collectResults.containsKey(bot1)) return 0L;
        if (collectResults.get(bot1) > (TIMES / 2)) return 1L;
        return 0L;
    }

}
