package com.bueno.domain.usecases.tournament.dtos;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TournamentDTO(UUID uuid, List<String> participantsNames, Map<UUID, MatchDTO> matchesDto, int size) {

    @Override
    public String toString() {
        return "TournamentDTO{" +
               "uuid=" + uuid +
               ", participantsNames=" + participantsNames +
               ", size=" + size +
               ", matchesDto=" + matchesDto +
               '}';
    }
}
