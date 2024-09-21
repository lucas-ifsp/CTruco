package com.bueno.domain.usecases.tournament.dtos;

import com.bueno.domain.entities.tournament.Match;

import java.util.Comparator;
import java.util.UUID;

public record MatchDTO(UUID id,
                       int matchNumber,
                       String p1Name,
                       String p2Name,
                       boolean available,
                       String winnerName,
                       int p1Score,
                       int p2Score,
                       UUID next) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchDTO matchDTO = (MatchDTO) o;
        return id.equals(matchDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
