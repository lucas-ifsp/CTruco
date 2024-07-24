package com.bueno.domain.usecases.game.dtos;

import java.util.List;

public record PlayWithBotsResultsDto(List<PlayWithBotsDto> info, long timeToExecute, int times) {
}
