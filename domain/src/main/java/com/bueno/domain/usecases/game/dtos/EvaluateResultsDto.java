package com.bueno.domain.usecases.game.dtos;

public record EvaluateResultsDto(long computingTime, long numberOfGames, long evaluatedBotWins, double winRate, double percentile,long matchWins) {
}
