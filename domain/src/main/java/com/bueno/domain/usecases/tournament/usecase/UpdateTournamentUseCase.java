package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateTournamentUseCase {
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final UpdateMatchUseCase updateMatchUseCase;

    public UpdateTournamentUseCase(TournamentRepository tournamentRepository, MatchRepository matchRepository, UpdateMatchUseCase updateMatchUseCase) {
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
        this.updateMatchUseCase = updateMatchUseCase;
    }

    public void updateFromDTO(TournamentDTO dto) {
        tournamentRepository.update(dto);
        updateMatchUseCase.updateAll(matchRepository.findMatchesByTournamentId(dto.uuid()));
    }
}
