package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.tournament.converter.MatchConverter;
import com.bueno.domain.usecases.tournament.converter.TournamentConverter;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;

import java.util.List;

public class RefreshTournamentUseCase {
    private final TournamentConverter tournamentConverter;
    private final MatchConverter matchConverter;

    public RefreshTournamentUseCase(TournamentConverter tournamentConverter, MatchConverter matchConverter) {
        this.tournamentConverter = tournamentConverter;
        this.matchConverter = matchConverter;
    }

    public TournamentDTO refresh(TournamentDTO dto) {
        Tournament tournament = tournamentConverter.fromDTO(dto);
        List<Match> matches = dto.matchesDto().values().stream().map(matchConverter::fromDTO).sorted().toList();
        refreshMatches(matches);
        tournament.setMatches(matches);
        return tournamentConverter.toDTO(tournament);
    }

    // logica de passar os vencedores de uma partida para os participantes da outra
    public static List<Match> refreshMatches(List<Match> matches) {
        for (Match match : matches) {
            if (match.getNext() != null && match.getWinnerName() != null) {
                Match next = matches.stream().filter(m -> m.getId() == match.getNext().getId()).findFirst().orElseThrow();
                if (next.getP1Name() == null) {
                    next.setP1Name(match.getWinnerName());
                    continue;
                }
                if (next.getP2Name() == null && !match.getWinnerName().equals(next.getP1Name())) {
                    next.setP2Name(match.getWinnerName());
                }
            }
            if (match.getWinnerName() == null && match.getP1Name() != null && match.getP2Name() != null) {
                match.setAvailable(true);
            }
        }
        return matches;
    }
}
