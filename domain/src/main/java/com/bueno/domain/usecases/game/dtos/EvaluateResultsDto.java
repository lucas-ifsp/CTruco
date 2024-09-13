package com.bueno.domain.usecases.game.dtos;

public record EvaluateResultsDto(
        String botName,
        long computingTime,
        long numberOfGames,
        long evaluatedBotWins,
        double winRate,
        double percentile,
        long defeatedOpponents,
        long numberOfOpponents) {
}
