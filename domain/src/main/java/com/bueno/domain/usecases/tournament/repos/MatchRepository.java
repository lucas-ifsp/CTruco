package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {

    Optional<MatchDTO> findById(UUID uuid);

    Map<UUID, MatchDTO> findAll();

    void save(MatchDTO matchDTO);
}
