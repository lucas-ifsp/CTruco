package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindMatchUseCase {

    public static UUID findUuidByMatchNumber(TournamentDTO dto, int matchNumber) {
        return dto.matchesDto().entrySet().stream()
                .filter(set -> set.getValue().matchNumber() == matchNumber)
                .findFirst()
                .orElseThrow()
                .getKey();
    }
}
