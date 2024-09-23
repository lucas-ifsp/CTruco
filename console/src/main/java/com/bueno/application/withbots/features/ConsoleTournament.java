package com.bueno.application.withbots.features;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.usecase.*;

import java.util.*;

public class ConsoleTournament {
    private final CreateTournamentUseCase tournamentProvider;
    private final PrepareTournamentUseCase prepareTournament;
    private final PlayTournamentMatchesUseCase playUseCase;
    private final RefreshTournamentUseCase refreshUseCase;
    private TournamentDTO dto;

    public ConsoleTournament(CreateTournamentUseCase tournamentProvider,
                             PrepareTournamentUseCase prepareTournament,
                             PlayTournamentMatchesUseCase playUseCase,
                             RefreshTournamentUseCase refreshUseCase) {
        this.tournamentProvider = tournamentProvider;
        this.prepareTournament = prepareTournament;
        this.playUseCase = playUseCase;
        this.refreshUseCase = refreshUseCase;
    }

    public void startTournament(List<String> bots) {
        if (bots.size() != 16 && bots.size() != 8 && bots.size() != 4) {
            System.out.println("invalid number of participants");
            return;
        }
        List<String> participants = new ArrayList<>(bots);
        Collections.shuffle(participants);
        dto = tournamentProvider.createTournament(participants, participants.size());
        dto = prepareTournament.prepareMatches(dto);
        System.out.println("ALL Matches: ");
        dto.matchesDto().forEach((uuid, matchDTO) -> System.out.println("Match number: " +
                                                                        matchDTO.matchNumber() +
                                                                        " " +
                                                                        matchDTO.p1Name() +
                                                                        " VS " +
                                                                        matchDTO.p2Name()
        ));
    }

    public void tournamentMenu() {
        Objects.requireNonNull(dto, "tournament menu requires tournament data");
        MatchDTO finalMatch = dto.matchesDto()
                .values()
                .stream()
                .filter(matchDTO -> matchDTO.next() == null).findFirst().orElseThrow();

        while (finalMatch.winnerName() == null) {
            List<MatchDTO> availableOnes = playUseCase.getAllAvailableMatches(dto);
            System.out.println("Available matches: " + availableOnes.size());
            availableOnes.forEach(matchDTO -> System.out.println("Match number: " +
                                                                 matchDTO.matchNumber() +
                                                                 "  " +
                                                                 matchDTO.p1Name() +
                                                                 " VS " +
                                                                 matchDTO.p2Name()));
            final UUID matchUUID = chooseMatch(dto);
            dto = playUseCase.playOne(dto, matchUUID);
            dto = refreshUseCase.refresh(dto);

            finalMatch = dto.matchesDto()
                    .values()
                    .stream()
                    .filter(matchDTO -> matchDTO.next() == null).findFirst().orElseThrow();
        }
        System.out.println("Ganhador: " + finalMatch.winnerName());
    }

    private UUID chooseMatch(TournamentDTO dto) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the match number: ");
        int nextMatchNumber = scan.nextInt();
        return dto.matchesDto().values().stream()
                .filter(matchDTO -> matchDTO.matchNumber() == nextMatchNumber)
                .findFirst()
                .orElseThrow()
                .id();
    }
}
