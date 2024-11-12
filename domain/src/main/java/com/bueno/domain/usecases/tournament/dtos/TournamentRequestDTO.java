package com.bueno.domain.usecases.tournament.dtos;

import java.util.List;

public record TournamentRequestDTO(List<String> participants, int times, int finalAndThirdPlaceMatchTimes) {
}
