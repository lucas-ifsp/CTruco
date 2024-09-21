package com.bueno.domain.usecases.tournament.converter;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TournamentConverter {
    private final MatchConverter matchConverter;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    public TournamentConverter(MatchConverter matchConverter, TournamentRepository tournamentRepository, MatchRepository matchRepository) {
        this.matchConverter = matchConverter;
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
    }

    public Tournament fromDTO(TournamentDTO dto) {
        Tournament tournament = new Tournament(dto.participantsNames(), dto.size());
        tournament.setMatches(dto.matchesDto().values().stream().map(matchConverter::fromDTO).sorted().toList());
        return tournament;
    }

    public TournamentDTO toDTO(Tournament tournament) {
        System.out.println(tournament.toString());
        Map<UUID, MatchDTO> matchAndNextOneMap = matchesToDto(tournament.getMatches());
        TournamentDTO dto = new TournamentDTO(tournament.getTournamentUUID(),
                tournament.getParticipantNames(),
                matchAndNextOneMap,
                tournament.getSize());
        tournamentRepository.save(dto);
        dto.matchesDto().values().forEach(matchRepository::save);
        return dto;
    }

    private Map<UUID, MatchDTO> matchesToDto(List<Match> matches) {
        if (matches == null) return Map.of();
        return matches.stream()
                .collect(Collectors.toMap(
                                Match::getId,
                                matchConverter::toDTO
                        )
                );
    }
}
