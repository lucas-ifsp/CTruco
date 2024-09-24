package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeMatchRepository implements MatchRepository {
    private final Map<UUID, MatchDTO> matches;

    public FakeMatchRepository() {
        this.matches = new HashMap<>();
    }

    @Override
    public Optional<MatchDTO> findById(UUID uuid) {
        if (uuid == null) return Optional.empty();
        return Optional.of(matches.get(uuid));
    }

    @Override
    public Map<UUID, MatchDTO> findAll() {
        return matches;
    }

    @Override
    public List<MatchDTO> findByTournamentUUID(UUID tournamentUUID) {
        return List.of();
    }

    @Override
    public void save(MatchDTO matchDTO) {
        matches.put(matchDTO.id(), matchDTO);
    }
}
