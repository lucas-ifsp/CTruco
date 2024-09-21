package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.tournament.converter.TournamentConverter;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.bueno.domain.usecases.tournament.usecase.RefreshTournamentUseCase.refreshMatches;


@Service
public class PrepareTournamentUseCase {
    private final TournamentConverter tournamentConverter;
    private final TournamentRepository tournamentRepository;

    public PrepareTournamentUseCase(TournamentConverter tournamentConverter, TournamentRepository tournamentRepository) {
        this.tournamentConverter = tournamentConverter;
        this.tournamentRepository = tournamentRepository;
    }


    public TournamentDTO prepareMatches(TournamentDTO dto) {
        Tournament tournament = tournamentConverter.fromDTO(dto);
        List<String> participants = tournament.getParticipantNames();
        Objects.requireNonNull(tournament);
        Objects.requireNonNull(participants);
        List<Match> matches = new ArrayList<>();

        int size = dto.size();

        insertMatches(size, matches);
        insertParticipants(participants, matches, size);
        setNextMatches(size, matches);
        tournament.setMatches(refreshMatches(matches));

        return tournamentConverter.toDTO(tournament);
    }

    private void insertMatches(int size, List<Match> matches) {
        for (int i = 0; i < size - 1; i++)
            matches.add(new Match(UUID.randomUUID(),
                    i + 1,
                    null,
                    null,
                    false,
                    null,
                    0,
                    0,
                    null));
    }

    private void insertParticipants(List<String> participants, List<Match> matches, int size) {
        int participantsIndex = 0;
        for (int i = 0; i < size - 1; i++) {
            if (i < size / 2) {
                matches.get(i).setP1Name(participants.get(participantsIndex));
                matches.get(i).setP2Name(participants.get(participantsIndex + 1));
                participantsIndex++;
                participantsIndex++;
            }
        }
    }

    private void setNextMatches(int size, List<Match> matches) {
        int next = size / 2 - 1;
        for (int i = 0; i < size - 1; i++) {
            if (i % 2 == 0) next++;
            if (i == size - 2) return;
            matches.get(i).setNext(matches.get(next));
        }
    }


}
