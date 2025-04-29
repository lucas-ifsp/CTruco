package com.bueno.domain.entities.tournament;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;

import java.util.*;

public class Tournament {
    private final UUID tournamentUUID;
    private final int size;
    private final List<String> participantNames;
    private final int times;
    private final List<Match> matches;

    public Tournament(List<String> participantNames, int size, int times) {
        this.times = times;
        this.tournamentUUID = UUID.randomUUID();
        this.size = size;
        this.participantNames = participantNames;
        this.matches = new ArrayList<>();
    }

    public Tournament(UUID tournamentUUID, List<String> participantNames, List<Match> matches, int size, int times) {
        this.tournamentUUID = tournamentUUID;
        this.size = size;
        this.matches = matches.stream().sorted().toList();
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
        matches.forEach(Match::setAvailableState);
    }

    // TODO - mudar p/ programação declarativa
    public void insertMatches() {
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

    // TODO - mudar p/ programação declarativa
    public void insertParticipants() {
        int participantsIndex = 0;
        for (int i = 0; i < size - 1; i++) {
            if (i < size / 2) {
                matches.get(i).setP1Name(participantNames.get(participantsIndex));
                matches.get(i).setP2Name(participantNames.get(participantsIndex + 1));
                participantsIndex = participantsIndex + 2;
            }
        }
    }

    // TODO - mudar p/ programação declarativa
    public void setNextMatches() {
        int next = size / 2 - 1;
        for (int i = 0; i < size - 1; i++) {
            if (i % 2 == 0) next++;
            if (i == size - 2) return;
            matches.get(i).setNext(matches.get(next));
        }
    }

    public void refreshMatches(Map<UUID, Match> cacheMatches) {
        matches.forEach(m -> {
                    m.setAvailableState();
                    boolean nextMatchChanged = m.setWinnerToNextBracket();
                    addToCache(m, cacheMatches, nextMatchChanged);
                }
        );
    }

    public void setAvailableOnes() {
        matches.forEach(Match::setAvailableState);
    }

    private void addToCache(Match match, Map<UUID, Match> cacheMatches, boolean nextMatchChanged) {
        if (nextMatchChanged) {
            nextMatchChanged = false;
            addToCache(match.getNext(), cacheMatches, nextMatchChanged);
        }
        if (!cacheMatches.containsKey(match.getId()))
            cacheMatches.put(match.getId(), match);
        else cacheMatches.get(match.getId()).setAvailableState();
    }

    public int getSize() {
        return size;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<Match> getAvailableMatches() {
        return matches.stream().filter(Match::isAvailable).toList();
    }

    public int getTimes() {
        return times;
    }

    public UUID getTournamentUUID() {
        return tournamentUUID;
    }

    public List<String> getParticipantNames() {
        return participantNames;
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
