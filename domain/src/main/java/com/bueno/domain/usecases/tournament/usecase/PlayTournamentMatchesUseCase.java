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
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayTournamentMatchesUseCase {
    private final TournamentRepository tournamentRepository;
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi api;
    private final BotManagerService provider;
    private final GetMatchUseCase getMatchUseCase;
    private final UpdateTournamentUseCase updateTournamentUseCase;
    private final UpdateMatchUseCase updateMatchUseCase;
    private final RefreshTournamentUseCase refreshUseCase;

    public PlayTournamentMatchesUseCase(TournamentRepository tournamentRepository,
                                        RemoteBotRepository remoteBotRepository,
                                        RemoteBotApi api,
                                        BotManagerService botManagerService,
                                        GetMatchUseCase getMatchUseCase,
                                        UpdateTournamentUseCase updateTournamentUseCase,
                                        UpdateMatchUseCase updateMatchUseCase,
                                        RefreshTournamentUseCase refreshUseCase) {
        this.tournamentRepository = tournamentRepository;
        this.remoteBotRepository = remoteBotRepository;
        this.api = api;
        this.provider = botManagerService;
        this.getMatchUseCase = getMatchUseCase;
        this.updateTournamentUseCase = updateTournamentUseCase;
        this.updateMatchUseCase = updateMatchUseCase;
        this.refreshUseCase = refreshUseCase;
    }

//    public TournamentDTO playAll(TournamentDTO dto) {
//        Tournament tournament = TournamentConverter.fromDTO(dto, getMatchUseCase);
//        tournament.playAllAvailable(api, remoteBotRepository, provider);
//        tournament.setAvailableMatches();
//        return TournamentConverter.toDTO(tournament);
//    }

    // TODO - colocar este método no findMatchUseCase
    public List<MatchDTO> getAllAvailableMatches(TournamentDTO dto) {
        Tournament tournament = TournamentConverter.fromDTO(dto, getMatchUseCase);
        tournament.setAvailableMatches();
        return tournament.getMatches().stream()
                .filter(Match::isAvailable)
                .map(MatchConverter::toDTO)
                .toList();
    }

    public void playOne(UUID uuid, int chosenMatchNumber, int numberOfSimulations) {
        Optional<TournamentDTO> dto = tournamentRepository.findTournamentById(uuid);

        if (dto.isEmpty()) throw new EntityNotFoundException("tournament doesn't exist");

        Optional<MatchDTO> chosenMatch = getMatchUseCase.byTournamentUuid(uuid)
                .stream()
                .filter(matchDTO -> matchDTO.matchNumber() == chosenMatchNumber)
                .findFirst();

        if (chosenMatch.isEmpty()) throw new EntityNotFoundException("the chosenMatch doesn't exist");

        UUID chosenMatchId = chosenMatch.get().uuid();

        Tournament tournament = playChosenMatch(dto.get(), chosenMatchId, numberOfSimulations);
        TournamentDTO updatedDto = TournamentConverter.toDTO(tournament);
        updateTournamentUseCase.updateFromDTO(updatedDto);
        updateMatchUseCase.updateAll(tournament.getMatches().stream().map(MatchConverter::toDTO).toList());
        if (updatedDto != null)
            refreshUseCase.refresh(updatedDto.uuid());
    }

    private Tournament playChosenMatch(TournamentDTO dto, UUID chosenMatchId, int numberOfSimulations) {
        Tournament tournament = TournamentConverter.fromDTO(dto, getMatchUseCase);
        tournament.playByMatchUuid(chosenMatchId, api, provider, remoteBotRepository, numberOfSimulations);
        return tournament;
    }


}
