package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeTournamentRepository implements TournamentRepository {
    Map<UUID, TournamentDTO> tournaments;

    public FakeTournamentRepository() {
        this.tournaments = new HashMap<>();
    }

    public Optional<TournamentDTO> findTournamentById(UUID uuid) {
        if (uuid == null) return Optional.empty();
        return Optional.of(tournaments.get(uuid));
    }

    public Map<UUID, TournamentDTO> getTournaments() {
        return tournaments;
    }

    @Override
    public void save(TournamentDTO dto) {
        Objects.requireNonNull(dto);
        tournaments.put(dto.uuid(), dto);
    }
}
