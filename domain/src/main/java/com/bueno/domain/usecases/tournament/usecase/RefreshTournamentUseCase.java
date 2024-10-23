package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.tournament.converter.MatchConverter;
import com.bueno.domain.usecases.tournament.converter.TournamentConverter;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTournamentUseCase {
    private final TournamentRepository tournamentRepository;
    private final GetMatchUseCase getMatchUseCase;
    private final UpdateTournamentUseCase updateTournamentUseCase;
    private final UpdateMatchUseCase updateMatchUseCase;

    public RefreshTournamentUseCase(TournamentRepository tournamentRepository,
                                    GetMatchUseCase getMatchUseCase,
                                    UpdateTournamentUseCase updateTournamentUseCase, UpdateMatchUseCase updateMatchUseCase) {
        this.tournamentRepository = tournamentRepository;
        this.getMatchUseCase = getMatchUseCase;
        this.updateTournamentUseCase = updateTournamentUseCase;
        this.updateMatchUseCase = updateMatchUseCase;
    }

    public void refresh(UUID tournamentUuid) {
        Optional<TournamentDTO> dto = tournamentRepository.findTournamentById(tournamentUuid);
        if (dto.isEmpty()) throw new EntityNotFoundException("invalid tournament uuid");
        updateTournamentUseCase.updateFromDTO(refreshMatches(dto.get()));
    }

    private TournamentDTO refreshMatches(TournamentDTO dto) {
        Tournament tournament = TournamentConverter.fromDTO(dto, getMatchUseCase);
        Map<UUID, Match> cacheUpdatedMatches = new HashMap<>();
        tournament.refreshMatches(cacheUpdatedMatches);
        updateMatchUseCase.updateAll(cacheUpdatedMatches.values().stream().map(MatchConverter::toDTO).toList());

        return TournamentConverter.toDTO(tournament);
    }

}
