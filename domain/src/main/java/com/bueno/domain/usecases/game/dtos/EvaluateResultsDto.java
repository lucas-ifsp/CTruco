package com.bueno.domain.usecases.game.dtos;

public record EvaluateResultsDto(long start, long numberOfGames, long evaluatedBotWins, long end, double winRate, double percentil) {
}
