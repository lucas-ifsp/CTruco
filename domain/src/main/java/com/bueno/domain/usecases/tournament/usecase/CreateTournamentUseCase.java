package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.tournament.converter.TournamentConverter;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class CreateTournamentUseCase {
    private final TournamentRepository tournamentRepository;
    private final TournamentConverter tournamentConverter;

    public CreateTournamentUseCase(TournamentRepository tournamentRepository, TournamentConverter tournamentConverter) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentConverter = tournamentConverter;
    }

    public TournamentDTO createTournament(List<String> bots, int size) {
        List<String> participants = new ArrayList<>(bots);
        Collections.shuffle(participants);
        Tournament tournament = new Tournament(participants, size);
        tournamentRepository.save(tournamentConverter.toDTO(tournament));
        return tournamentConverter.toDTO(tournament);
    }


}
