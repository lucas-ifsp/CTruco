package com.bueno.domain.usecases.tournament.dtos;

import java.util.UUID;

public record MatchDTO(UUID uuid,
                       int matchNumber,
                       String p1Name,
                       String p2Name,
                       boolean available,
                       String winnerName,
                       long p1Score,
                       long p2Score,
                       long timeToExecute,
                       UUID next) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchDTO matchDTO = (MatchDTO) o;
        return uuid.equals(matchDTO.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
