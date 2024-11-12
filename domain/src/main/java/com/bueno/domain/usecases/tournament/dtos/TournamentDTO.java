package com.bueno.domain.usecases.tournament.dtos;

import java.util.List;
import java.util.UUID;

public record TournamentDTO(UUID uuid, List<String> participantsNames, List<UUID> matchUUIDs, int size, int times,
                            int finalAndThirdPlaceMatchTimes,
                            String winnerName) {

    @Override
    public String toString() {
        return "TournamentDTO{" +
               "uuid=" + uuid +
               ", participantsNames=" + participantsNames +
               ", size=" + size +
               ", matchesDTO=" + matchUUIDs +
               '}';
    }
}
