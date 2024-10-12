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

    public TournamentController(@Qualifier("matchesRepositoryMongoImpl") MatchRepository matchRepository,
                                @Qualifier("tournamentRepositoryMongoImpl") TournamentRepository tournamentRepository,
                                CreateTournamentUseCase tournamentProvider,
                                RefreshTournamentUseCase refreshUseCase,
                                PlayTournamentMatchesUseCase playTournamentMatchesUseCase, GetTournamentUseCase getTournamentUseCase, GetMatchUseCase getMatchUseCase) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentProvider = tournamentProvider;
        this.refreshUseCase = refreshUseCase;
        this.playTournamentMatchesUseCase = playTournamentMatchesUseCase;
        this.getTournamentUseCase = getTournamentUseCase;
        this.getMatchUseCase = getMatchUseCase;
    }


    @PostMapping
    public ResponseEntity<?> createTournament(@RequestBody TournamentRequestDTO request) {
        TournamentDTO dto = tournamentProvider.createTournament(request.participants(), request.participants().size(), request.times());
        List<MatchDTO> matchesDTO = getMatchUseCase.byTournamentUuid(dto.uuid());

        TournamentResponseDTO response = new TournamentResponseDTO(dto.uuid(),
                dto.participantsNames(),
                matchesDTO,
                dto.size(),
                dto.times(),
                dto.winnerName());

        return new ResponseBuilder(HttpStatus.CREATED)
                .addEntry(new ResponseEntry("payload", response))
                .addTimestamp()
                .build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getTournamentByUuid(@PathVariable UUID uuid) {
        TournamentDTO dto = tournamentRepository.findTournamentById(uuid).orElseThrow();

        return new ResponseBuilder(HttpStatus.OK)
                .addEntry(new ResponseEntry("payload", dto))
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

    @GetMapping("match/{uuid}")
    public ResponseEntity<?> getOneMatch(@PathVariable UUID uuid) {
        MatchDTO matchDTO = matchRepository.findById(uuid).orElseThrow();

        return new ResponseBuilder(HttpStatus.OK)
                .addEntry(new ResponseEntry("payload", matchDTO))
                .addTimestamp()
                .build();
    }

    // TODO - Resolver problema de persistencia
    @PostMapping("{tournamentUuid}/match/{chosenMatchNumber}/{numberOfSimulations}")
    public ResponseEntity<?> playMatch(@PathVariable UUID tournamentUuid, @PathVariable int chosenMatchNumber, @PathVariable int numberOfSimulations) {
        if (tournamentUuid == null) return new ResponseBuilder(HttpStatus.BAD_REQUEST)
                .addEntry(new ResponseEntry("payload", "invalid tournament uuid"))
                .addTimestamp()
                .build();

        playTournamentMatchesUseCase.playOne(tournamentUuid, chosenMatchNumber, numberOfSimulations);
        refreshUseCase.refresh(tournamentUuid);

        TournamentDTO dto = getTournamentUseCase.byUuid(tournamentUuid);
        List<MatchDTO> matchesDTO = getMatchUseCase.byTournamentUuid(dto.uuid());

        TournamentResponseDTO response = new TournamentResponseDTO(dto.uuid(),
                dto.participantsNames(),
                matchesDTO,
                dto.size(),
                dto.times(),
                dto.winnerName());


        return new ResponseBuilder(HttpStatus.OK)
                .addEntry(new ResponseEntry("payload", response))
                .addTimestamp()
                .build();

    }


}
