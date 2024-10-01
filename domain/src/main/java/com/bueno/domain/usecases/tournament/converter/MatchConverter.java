package com.bueno.domain.usecases.tournament.converter;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchConverter {
    private final MatchRepository matchRepository;

    public MatchConverter(@Qualifier("matchesRepositoryMongoImpl") MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match fromDTO(MatchDTO dto) {
        Optional<MatchDTO> nextOpt = matchRepository.findById(dto.next());
        if (nextOpt.isPresent()) {
            Match next = fromDTO(nextOpt.get());
            return new Match(dto.uuid(), dto.matchNumber(), dto.p1Name(), dto.p2Name(), dto.available(), dto.winnerName(), dto.p1Score(), dto.p2Score(), next);
        } else {
            return new Match(dto.uuid(), dto.matchNumber(), dto.p1Name(), dto.p2Name(), dto.available(), dto.winnerName(), dto.p1Score(), dto.p2Score(), null);
        }
    }

    public MatchDTO toDTO(Match match) {
        return new MatchDTO(match.getId(),
                match.getMatchNumber(),
                match.getP1Name(),
                match.getP2Name(),
                match.isAvailable(),
                match.getWinnerName(),
                match.getP1Score(),
                match.getP2Score(),
                (match.getNext() == null ? null : match.getNext().getId()));
    }
}
