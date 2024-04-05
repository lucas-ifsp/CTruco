package com.bueno.domain.usecases.game.service;

import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class WinsAccumulatorService {
    public static Long getWins(List<PlayWithBotsDto> results, String botToEvaluateName, int times) {
        Map<String, Long> collectResults = results.stream()
                .collect(Collectors.groupingBy(PlayWithBotsDto::name, Collectors.counting()));
        if (!collectResults.containsKey(botToEvaluateName)) return 0L;
        if (collectResults.get(botToEvaluateName) > (times / 2)) return 1L;
        return 0L;
    }
}
