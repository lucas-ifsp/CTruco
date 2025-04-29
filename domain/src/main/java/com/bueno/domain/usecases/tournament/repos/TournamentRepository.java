package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRepository {

    Optional<TournamentDTO> findTournamentById(UUID uuid) throws SQLException;

    Map<UUID, TournamentDTO> getTournaments();

    void save(TournamentDTO dto);
}
