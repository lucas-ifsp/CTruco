package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.utils.exceptions.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SaveTournamentUseCase {
    private final TournamentRepository tournamentRepository;

    public SaveTournamentUseCase(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public void save(TournamentDTO dto) {
        try {
            Objects.requireNonNull(dto.uuid());
            Objects.requireNonNull(dto.matchUUIDs());
            Objects.requireNonNull(dto.participantsNames());
            tournamentRepository.save(dto);
        } catch (Exception e) {
            throw new InvalidRequestException("cannot save a invalid tournament dto");
        }
    }
}
