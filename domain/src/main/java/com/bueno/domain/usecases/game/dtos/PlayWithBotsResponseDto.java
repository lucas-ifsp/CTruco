package com.bueno.domain.usecases.game.dtos;

public record PlayWithBotsResponseDto(String bot1Name, long bot1Wins, String bot2Name, long bot2Wins, int gamesPlayed,long timeToExecute) {

}
