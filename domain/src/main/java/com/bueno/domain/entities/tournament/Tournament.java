package com.bueno.domain.entities.tournament;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;

import java.util.List;
import java.util.UUID;

public class Tournament {
    private final UUID tournamentUUID;
    private final int size;
    private final List<String> participantNames;
    private final int times;
    private List<Match> matches;

    public Tournament(List<String> participantNames, int size, int times) {
        this.times = times;
        this.tournamentUUID = UUID.randomUUID();
        this.size = size;
        this.participantNames = participantNames;
    }

    public Tournament(UUID tournamentUUID, List<String> participantNames, int size, int times) {
        this.tournamentUUID = tournamentUUID;
        this.size = size;
        this.participantNames = participantNames;
        this.times = times;
    }

    public void playAllAvailable(RemoteBotApi api, RemoteBotRepository repository, BotManagerService botManagerService) {
        for (Match m : matches) {
            if (m.isAvailable()) {
                m.play(repository, api, botManagerService, times);
            }
        }

    }

    public void playByMatchUuid(UUID matchUuid, RemoteBotApi api, BotManagerService botManagerService, RemoteBotRepository repository) {
        matches.stream()
                .filter(match -> match.getId().equals(matchUuid))
                .findFirst()
                .ifPresentOrElse(match -> match.play(repository, api, botManagerService, times),
                        () -> System.out.println("No match found"));
    }

    public void setAvailableMatches() {
        for (Match match : matches) {
            if (match.isAvailable() && match.getWinnerName() != null) {
                match.setAvailable(false);
            }
            if (match.getP1Name() != null && match.getP2Name() != null && match.getWinnerName() == null) {
                match.setAvailable(true);
            }
        }
    }

    public UUID getTournamentUUID() {
        return tournamentUUID;
    }

    public int getSize() {
        return size;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<String> getParticipantNames() {
        return participantNames;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public int getTimes() {
        return times;
    }

    @Override
    public String toString() {
        return "Tournament{" +
               "tournamentUUID=" + tournamentUUID +
               ", size=" + size +
               ", matches=" + (matches == null ? " null" : matches.stream().map(match -> "\n\t" + match.toString()).toList()) +
               '}';
    }
}
