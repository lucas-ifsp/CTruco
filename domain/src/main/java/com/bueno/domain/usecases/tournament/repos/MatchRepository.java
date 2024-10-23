package com.bueno.domain.usecases.tournament.repos;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository {

    Optional<MatchDTO> findById(UUID uuid);

    List<MatchDTO> findAll();

    List<MatchDTO> findMatchesByTournamentId(UUID uuid);

    void save(MatchDTO matchDTO);

    void saveAll(List<MatchDTO> dtos);

    void update(MatchDTO matchDTO);

    void updateAll(List<MatchDTO> matchDTOS);

    void deleteAll();

}
