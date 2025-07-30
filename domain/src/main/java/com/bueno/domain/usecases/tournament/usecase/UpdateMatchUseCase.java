package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UpdateMatchUseCase {
    private final MatchRepository matchRepository;

    public UpdateMatchUseCase(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public void updateAll(List<MatchDTO> matchDTOS) {
        matchRepository.updateAll(matchDTOS);
    }

    public void update(MatchDTO matchDTO) {
        matchRepository.update(matchDTO);
    }
}
