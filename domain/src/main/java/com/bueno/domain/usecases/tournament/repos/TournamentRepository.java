package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRepository {

    Optional<TournamentDTO> findTournamentById(UUID uuid);

    List<TournamentDTO> findAll();

    void save(TournamentDTO dto);

    void update(TournamentDTO dto);

    void deleteAll();
}
