package com.bueno.domain.usecases.tournament.converter;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentConverter {
    private final MatchConverter matchConverter;

    public TournamentConverter(MatchConverter matchConverter) {
        this.matchConverter = matchConverter;
    }

    public Tournament fromDTO(TournamentDTO dto) {
        Tournament tournament = new Tournament(dto.uuid(), dto.participantsNames(), dto.size(), dto.times());
        tournament.setMatches(dto.matchesDTO().stream().map(matchConverter::fromDTO).sorted().toList());
        return tournament;
    }

    public TournamentDTO toDTO(Tournament tournament) {
        List<MatchDTO> matchList = matchesToDto(tournament.getMatches());
        MatchDTO lastMatch = matchList.stream().filter(matchDTO -> matchDTO.next() == null).findFirst().orElse(null);
        TournamentDTO dto = new TournamentDTO(tournament.getTournamentUUID(),
                tournament.getParticipantNames(),
                matchList,
                tournament.getSize(), tournament.getTimes(), null);
        if (lastMatch != null) {
            if (lastMatch.winnerName() != null) {
                dto = new TournamentDTO(tournament.getTournamentUUID(),
                        tournament.getParticipantNames(),
                        matchList,
                        tournament.getSize(), tournament.getTimes(), lastMatch.winnerName());
            }
        }
        return dto;
    }

    private List<MatchDTO> matchesToDto(List<Match> matches) {
        if (matches == null) return List.of();
        return matches.stream().map(matchConverter::toDTO).toList();
    }
}
