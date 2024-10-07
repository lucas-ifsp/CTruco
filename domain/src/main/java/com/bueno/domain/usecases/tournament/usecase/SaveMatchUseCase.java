package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveMatchUseCase {
    private final MatchRepository matchRepository;

    public SaveMatchUseCase(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public void one(MatchDTO matchDTO) {
        matchRepository.save(matchDTO);
    }

    public void all(List<MatchDTO> dtos) {
        matchRepository.saveAll(dtos);
    }
}
