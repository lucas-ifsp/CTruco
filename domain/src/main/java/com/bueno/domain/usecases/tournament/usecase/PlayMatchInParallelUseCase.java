package com.bueno.domain.usecases.tournament.usecase;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayMatchInParallelUseCase {
    private final PlayTournamentMatchesUseCase playTournamentMatchesUseCase;

    public PlayMatchInParallelUseCase(PlayTournamentMatchesUseCase playTournamentMatchesUseCase) {
        this.playTournamentMatchesUseCase = playTournamentMatchesUseCase;
    }

    @Async("taskExecutor")
    public void execute(UUID tournamentUuid, int chosenMatchNumber, int numberOfSimulations) {
        playTournamentMatchesUseCase.playOne(tournamentUuid, chosenMatchNumber, numberOfSimulations);
        System.out.println("Refreshed BD");
    }

}
