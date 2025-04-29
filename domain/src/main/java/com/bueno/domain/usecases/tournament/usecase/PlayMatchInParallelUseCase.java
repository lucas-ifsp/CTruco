package com.bueno.domain.usecases.tournament.usecase;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayMatchInParallelUseCase {
    private final PlayTournamentMatchesUseCase playTournamentMatchesUseCase;
    private final RefreshTournamentUseCase refreshUseCase;

    public PlayMatchInParallelUseCase(PlayTournamentMatchesUseCase playTournamentMatchesUseCase,
                                      RefreshTournamentUseCase refreshUseCase) {
        this.playTournamentMatchesUseCase = playTournamentMatchesUseCase;
        this.refreshUseCase = refreshUseCase;
    }

    @Async("taskExecutor")
    public void execute(UUID tournamentUuid, int chosenMatchNumber, int numberOfSimulations) {
        playTournamentMatchesUseCase.playOne(tournamentUuid, chosenMatchNumber, numberOfSimulations);
        refreshUseCase.refresh(tournamentUuid);
        System.out.println("Refreshed BD");
    }

}
