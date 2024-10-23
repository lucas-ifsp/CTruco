package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import org.springframework.stereotype.Repository;

import java.util.*;

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
    public List<MatchDTO> findAll() {
        return matches.values().stream().toList();
    }

    @Override
    public void save(MatchDTO matchDTO) {
        matches.put(matchDTO.uuid(), matchDTO);
    }

    @Override
    public void saveAll(List<MatchDTO> dtos) {
        dtos.forEach(dto -> matches.put(dto.uuid(), dto));
    }

    @Override
    public void update(MatchDTO matchDTO) {

    }

    @Override
    public void updateAll(List<MatchDTO> matchDTOS) {

    }

    @Override
    public void deleteAll() {
        matches.clear();
    }

    @Override
    public List<MatchDTO> findMatchesByTournamentId(UUID uuid) {
        return List.of();
    }

}
