package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRepository {

    Optional<TournamentDTO> findTournamentById(UUID uuid);

    Map<UUID, TournamentDTO> findAll();

    void save(TournamentDTO dto);
}
