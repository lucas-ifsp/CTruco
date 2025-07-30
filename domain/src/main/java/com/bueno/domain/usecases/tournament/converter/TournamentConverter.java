package com.bueno.domain.usecases.tournament.converter;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.usecase.GetMatchUseCase;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentConverter {

    public static TournamentDTO toDTO(Tournament tournament) {
        List<MatchDTO> matchList = tournament.getMatches().stream().map(MatchConverter::toDTO).toList();
        Optional<MatchDTO> lastMatch = matchList.stream().filter(matchDTO -> matchDTO.next() == null).findFirst();
        if (lastMatch.isPresent()) {
            String tournamentWinner = lastMatch.get().winnerName();
            if (tournamentWinner != null) {
                return new TournamentDTO(tournament.getTournamentUUID(),
                        tournament.getParticipantNames(),
                        matchList.stream().map(MatchDTO::uuid).toList(),
                        tournament.getSize(), tournament.getTimes(), tournament.getFinalAndThirdPlaceMatchTimes(), tournamentWinner);
            }
            return new TournamentDTO(tournament.getTournamentUUID(),
                    tournament.getParticipantNames(),
                    matchList.stream().map(MatchDTO::uuid).toList(),
                    tournament.getSize(), tournament.getTimes(), tournament.getFinalAndThirdPlaceMatchTimes(), null);
        } else {
            //TODO - trocar isso dps
            return null;
        }
    }

    public static Tournament fromDTO(TournamentDTO dto, GetMatchUseCase getMatchUseCase) {
        List<Match> matches = dto.matchUUIDs().stream()
                .map(mUuid -> {
                    Optional<MatchDTO> matchDTO = getMatchUseCase.byUuid(mUuid);
                    if (matchDTO.isPresent())
                        return MatchConverter.fromDTO(matchDTO.get(), getMatchUseCase.byTournamentUuid(dto.uuid()));
                    throw new EntityNotFoundException("match not found");
                })
                .sorted()
                .toList();


        return new Tournament(dto.uuid(), dto.participantsNames(), matches, dto.size(), dto.times(), dto.finalAndThirdPlaceMatchTimes());
    }
}
