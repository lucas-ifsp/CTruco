package com.bueno.domain.usecases.tournament.dtos;

import java.util.List;
import java.util.UUID;

public record TournamentResponseDTO(UUID uuid, List<MatchDTO> AvailableMatches, int size) {
}
