package com.bueno.controllers;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentRequestDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.tournament.usecase.CreateTournamentUseCase;
import com.bueno.domain.usecases.tournament.usecase.PlayTournamentMatchesUseCase;
import com.bueno.domain.usecases.tournament.usecase.PrepareTournamentUseCase;
import com.bueno.domain.usecases.tournament.usecase.RefreshTournamentUseCase;
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
    private final PrepareTournamentUseCase prepareTournamentUseCase;
    private final PlayTournamentMatchesUseCase playUseCase;
    private final RefreshTournamentUseCase refreshUseCase;

    public TournamentController(@Qualifier("matchesRepositoryMongoImpl") MatchRepository matchRepository,
                                @Qualifier("tournamentRepositoryMongoImpl") TournamentRepository tournamentRepository,
                                CreateTournamentUseCase tournamentProvider,
                                PrepareTournamentUseCase prepareTournamentUseCase,
                                PlayTournamentMatchesUseCase playUseCase,
                                RefreshTournamentUseCase refreshUseCase) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentProvider = tournamentProvider;
        this.prepareTournamentUseCase = prepareTournamentUseCase;
        this.playUseCase = playUseCase;
        this.refreshUseCase = refreshUseCase;
    }


    @PostMapping
    public ResponseEntity<?> createTournament(@RequestBody TournamentRequestDTO request) {
        TournamentDTO dto;
        dto = tournamentProvider.createTournament(request.participants(), request.participants().size(), request.times());
        dto = prepareTournamentUseCase.prepareMatches(dto);
        tournamentRepository.save(dto);

        return new ResponseBuilder(HttpStatus.CREATED)
                .addEntry(new ResponseEntry("payload", dto))
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
        TournamentDTO dto = tournamentRepository.findTournamentById(uuid).orElseThrow();
        List<MatchDTO> matches = new ArrayList<>(dto.matchesDTO());


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
    @PostMapping("{tournamentUuid}/match/{chosenMatch}")
    public ResponseEntity<?> playMatch(@PathVariable UUID tournamentUuid, @PathVariable int chosenMatch) {
        if (tournamentUuid == null) return new ResponseBuilder(HttpStatus.BAD_REQUEST)
                .addEntry(new ResponseEntry("payload", "invalid tournament uuid"))
                .addTimestamp()
                .build();
        try {

            TournamentDTO dto = tournamentRepository.findTournamentById(tournamentUuid).orElseThrow();
            UUID chosenMatchId = dto.matchesDTO().stream().filter(matchDTO -> matchDTO.matchNumber() == chosenMatch).findFirst().orElseThrow().uuid();
            dto = playUseCase.playOne(dto, chosenMatchId);
            dto = refreshUseCase.refresh(dto);
            tournamentRepository.update(dto);

            TournamentDTO response = new TournamentDTO(dto.uuid(), dto.participantsNames(), dto.matchesDTO(), dto.size(), dto.times(), dto.winnerName());
            return new ResponseBuilder(HttpStatus.OK)
                    .addEntry(new ResponseEntry("payload", response))
                    .addTimestamp()
                    .build();

        } catch (Exception e) {
            return new ResponseBuilder(HttpStatus.NOT_FOUND)
                    .addEntry(new ResponseEntry("error", "tournament or match not found"))
                    .addTimestamp()
                    .build();
        }
    }


}
