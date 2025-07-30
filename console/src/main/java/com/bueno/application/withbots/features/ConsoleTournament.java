package com.bueno.application.withbots.features;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.usecase.*;

import java.util.*;

public class ConsoleTournament {
    private final CreateTournamentUseCase tournamentProvider;
    private final PlayTournamentMatchesUseCase playUseCase;
    private final RefreshTournamentUseCase refreshUseCase;
    private TournamentDTO dto;

    public ConsoleTournament(CreateTournamentUseCase tournamentProvider,
                             PlayTournamentMatchesUseCase playUseCase,
                             RefreshTournamentUseCase refreshUseCase) {
        this.tournamentProvider = tournamentProvider;
        this.playUseCase = playUseCase;
        this.refreshUseCase = refreshUseCase;
    }

//    public void startTournament(List<String> bots, int times) {
//        if (bots.size() != 16 && bots.size() != 8 && bots.size() != 4) {
//            System.out.println("invalid number of participants");
//            return;
//        }
//        List<String> participants = new ArrayList<>(bots);
//        Collections.shuffle(participants);
//        dto = tournamentProvider.createTournament(participants, participants.size(), times);
//        System.out.println("ALL Matches: ");
//        dto.matchesDTO().forEach((matchDTO) -> System.out.println("Match number: " +
//                matchDTO.matchNumber() +
//                " " +
//                matchDTO.p1Name() +
//                " VS " +
//                matchDTO.p2Name()
//        ));
//    }
//
//    public void tournamentMenu() {
//        Objects.requireNonNull(dto, "tournament menu requires tournament data");
//        MatchDTO finalMatch = dto.matchesDTO()
//                .stream()
//                .filter(matchDTO -> matchDTO.next() == null).findFirst().orElseThrow();
//
//        while (finalMatch.winnerName() == null) {
//            List<MatchDTO> availableOnes = playUseCase.getAllAvailableMatches(dto);
//            System.out.println("Available matches: " + availableOnes.size());
//            availableOnes.forEach(matchDTO -> System.out.println("Match number: " +
//                    matchDTO.matchNumber() +
//                    "  " +
//                    matchDTO.p1Name() +
//                    " VS " +
//                    matchDTO.p2Name()));
//            final int matchNumber = chooseMatch();
//            playUseCase.playOne(dto.uuid(), matchNumber, dto.times());
//            refreshUseCase.refresh(dto.uuid());
//
//
//            finalMatch = dto.matchesDTO()
//                    .stream()
//                    .filter(matchDTO -> matchDTO.next() == null).findFirst().orElseThrow();
//        }
//        System.out.println("Ganhador: " + finalMatch.winnerName());
//    }

    private int chooseMatch() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the match number: ");
        return scan.nextInt();
    }
}
