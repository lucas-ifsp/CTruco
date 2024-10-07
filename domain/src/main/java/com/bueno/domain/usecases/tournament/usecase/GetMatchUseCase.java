package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetMatchUseCase {
    private final MatchRepository matchRepository;

    public GetMatchUseCase(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Optional<MatchDTO> byUuid(UUID uuid) {
        return matchRepository.findById(uuid);
    }

    public List<MatchDTO> byTournamentUuid(UUID uuid) {
        return matchRepository.findMatchesByTournamentId(uuid);
    }
}
