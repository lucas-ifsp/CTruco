package com.bueno.domain.usecases.tournament.dtos;

import java.util.List;
import java.util.UUID;

public record TournamentResponseDTO(UUID uuid, List<String> participantsNames, List<MatchDTO> matchesDTO, int size,
                                    int times, int finalAndThirdPlaceMatchTimes,
                                    String winnerName) {
}
