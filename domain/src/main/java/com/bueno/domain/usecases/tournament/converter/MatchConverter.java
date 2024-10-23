package com.bueno.domain.usecases.tournament.converter;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.usecase.GetMatchUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MatchConverter {

    public static MatchDTO toDTO(Match match) {
        return new MatchDTO(match.getId(),
                match.getMatchNumber(),
                match.getP1Name(),
                match.getP2Name(),
                match.getIsAvailable(),
                match.getWinnerName(),
                match.getP1Score(),
                match.getP2Score(),
                match.getTimeToExecute(),
                (match.getNext() == null ? null : match.getNext().getId()));
    }

    public static Match fromDTO(MatchDTO dto, List<MatchDTO> allMatches) {
        Optional<MatchDTO> next = allMatches.stream().filter(m -> m.uuid().equals(dto.next())).findFirst();
        Match match;
        match = next
                .map(matchDTO ->
                        new Match(dto.uuid(),
                                dto.matchNumber(),
                                dto.p1Name(),
                                dto.p2Name(),
                                dto.available(),
                                dto.winnerName(),
                                dto.p1Score(),
                                dto.p2Score(),
                                dto.timeToExecute(),
                                fromDTO(matchDTO, allMatches)))
                .orElseGet(() ->
                        new Match(dto.uuid(),
                                dto.matchNumber(),
                                dto.p1Name(),
                                dto.p2Name(),
                                dto.available(),
                                dto.winnerName(),
                                dto.p1Score(),
                                dto.p2Score(),
                                dto.timeToExecute(),
                                null));
        return match;
    }

}
