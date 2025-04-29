package com.bueno.domain.usecases.tournament.usecase;

import com.bueno.domain.entities.tournament.Match;
import com.bueno.domain.entities.tournament.Tournament;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.tournament.converter.MatchConverter;
import com.bueno.domain.usecases.tournament.converter.TournamentConverter;
import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlayTournamentMatchesUseCase {
    private final TournamentConverter tournamentConverter;
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi api;
    private final BotManagerService provider;
    private final MatchConverter matchConverter;

    public PlayTournamentMatchesUseCase(TournamentConverter tournamentConverter, RemoteBotRepository remoteBotRepository, RemoteBotApi api, BotManagerService botManagerService, MatchConverter matchConverter) {
        this.tournamentConverter = tournamentConverter;
        this.remoteBotRepository = remoteBotRepository;
        this.api = api;
        this.provider = botManagerService;
        this.matchConverter = matchConverter;
    }

    public TournamentDTO playAll(TournamentDTO dto) {
        Tournament tournament = tournamentConverter.fromDTO(dto);
        tournament.playAllAvailable(api, remoteBotRepository, provider);
        tournament.setAvailableMatches();
        return tournamentConverter.toDTO(tournament);
    }

    // TODO - colocar este método no findMatchUseCase
    public List<MatchDTO> getAllAvailableMatches(TournamentDTO dto) {
        Tournament tournament = tournamentConverter.fromDTO(dto);
        tournament.setAvailableMatches();
        return tournament.getMatches().stream()
                .filter(Match::isAvailable)
                .map(matchConverter::toDTO)
                .toList();
    }

    public TournamentDTO playOne(TournamentDTO dto, UUID matchUuuid) {
        Tournament tournament = tournamentConverter.fromDTO(dto);
        tournament.playByMatchUuid(matchUuuid, api, provider, remoteBotRepository);
        tournament.setAvailableMatches();
        System.out.println(tournament);
        return tournamentConverter.toDTO(tournament);
    }
}
