package com.bueno.controllers;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentRequestDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentResponseDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.tournament.usecase.*;
import com.bueno.responses.ResponseBuilder;
import com.bueno.responses.ResponseEntry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/tournament")
public class TournamentController {
    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final CreateTournamentUseCase tournamentProvider;
    private final RefreshTournamentUseCase refreshUseCase;
    private final PlayTournamentMatchesUseCase playTournamentMatchesUseCase;
    private final GetTournamentUseCase getTournamentUseCase;
    private final GetMatchUseCase getMatchUseCase;
    private final PlayMatchInParallelUseCase playMatchInParallelUseCase;

    public TournamentController(@Qualifier("matchesRepositoryMongoImpl") MatchRepository matchRepository,
                                @Qualifier("tournamentRepositoryMongoImpl") TournamentRepository tournamentRepository,
                                CreateTournamentUseCase tournamentProvider,
                                RefreshTournamentUseCase refreshUseCase,
                                PlayTournamentMatchesUseCase playTournamentMatchesUseCase,
                                GetTournamentUseCase getTournamentUseCase,
                                GetMatchUseCase getMatchUseCase,
                                PlayMatchInParallelUseCase playMatchInParallelUseCase) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentProvider = tournamentProvider;
        this.refreshUseCase = refreshUseCase;
        this.playTournamentMatchesUseCase = playTournamentMatchesUseCase;
        this.getTournamentUseCase = getTournamentUseCase;
        this.getMatchUseCase = getMatchUseCase;
        this.playMatchInParallelUseCase = playMatchInParallelUseCase;
    }


    @PostMapping
    public ResponseEntity<?> createTournament(@RequestBody TournamentRequestDTO request) {
        tournamentRepository.deleteAll();
        matchRepository.deleteAll();
        TournamentDTO dto = tournamentProvider.createTournament(request.participants(), request.participants().size(), request.times(), request.finalAndThirdPlaceMatchTimes());
        List<MatchDTO> matchesDTO = getMatchUseCase.byTournamentUuid(dto.uuid());

        TournamentResponseDTO response = new TournamentResponseDTO(dto.uuid(),
                dto.participantsNames(),
                matchesDTO,
                dto.size(),
                dto.times(),
                dto.finalAndThirdPlaceMatchTimes(),
                dto.winnerName());

        return new ResponseBuilder(HttpStatus.CREATED)
                .addEntry(new ResponseEntry("payload", response))
                .addTimestamp()
                .build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getTournamentByUuid(@PathVariable UUID uuid) {
        Optional<TournamentDTO> dtoOpt = tournamentRepository.findTournamentById(uuid);

        if (dtoOpt.isEmpty()) return new ResponseBuilder(HttpStatus.BAD_REQUEST)
                .addEntry(new ResponseEntry("error", "tournament doesn't exist"))
                .addTimestamp()
                .build();

        List<MatchDTO> matchesDTO = matchRepository.findMatchesByTournamentId(uuid);
        TournamentDTO dto = dtoOpt.get();

        TournamentResponseDTO response = new TournamentResponseDTO(dto.uuid(),
                dto.participantsNames(),
                matchesDTO,
                dto.size(),
                dto.times(),
                dto.finalAndThirdPlaceMatchTimes(),
                dto.winnerName()
        );

        return new ResponseBuilder(HttpStatus.OK)
                .addEntry(new ResponseEntry("payload", response))
                .addTimestamp()
                .build();
    }

    @GetMapping("{uuid}/match")
    public ResponseEntity<?> getMatchesFromTournament(@PathVariable UUID uuid) {
        List<MatchDTO> matches = getMatchUseCase.byTournamentUuid(uuid);


        return new ResponseBuilder(HttpStatus.OK)
                .addEntry(new ResponseEntry("payload", matches))
                .addTimestamp()
                .build();
    }

//    @GetMapping("{tournamentUuid}/match/{chosenMatchNumber}")
//    public ResponseEntity<?> getOneMatch(@PathVariable UUID tournamentUuid, @PathVariable int chosenMatchNumber) {
//        MatchDTO matchDTO = matchRepository.findById(tournamentUuid).orElseThrow();
//
//        return new ResponseBuilder(HttpStatus.OK)
//                .addEntry(new ResponseEntry("payload", matchDTO))
//                .addTimestamp()
//                .build();
//    }

    @PostMapping("{tournamentUuid}/match/{chosenMatchNumber}/{numberOfSimulations}")
    public ResponseEntity<?> playMatch(@PathVariable UUID tournamentUuid, @PathVariable int chosenMatchNumber, @PathVariable int numberOfSimulations) {
        if (tournamentUuid == null) return new ResponseBuilder(HttpStatus.BAD_REQUEST)
                .addEntry(new ResponseEntry("payload", "invalid tournament uuid"))
                .addTimestamp()
                .build();
        if (numberOfSimulationsIsEven(numberOfSimulations)) numberOfSimulations = numberOfSimulations + 1;
        playMatchInParallelUseCase.execute(tournamentUuid, chosenMatchNumber, numberOfSimulations);

        return new ResponseBuilder(HttpStatus.OK)
                .addEntry(new ResponseEntry("Success", "The server is playing the match"))
                .addTimestamp()
                .build();
    }

    private boolean numberOfSimulationsIsEven(int numberOfSimulations) {
        return numberOfSimulations % 2 == 0;
    }
}