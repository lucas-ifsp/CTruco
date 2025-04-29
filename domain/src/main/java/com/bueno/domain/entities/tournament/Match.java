package com.bueno.domain.entities.tournament;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsResultsDto;
import com.bueno.domain.usecases.game.usecase.PlayWithBotsUseCase;

import java.util.UUID;


public class Match implements Comparable {
    private final UUID id;
    private final int matchNumber;
    private String p1Name;
    private String p2Name;
    private boolean isAvailable;
    private String winnerName;
    private long p1Score;
    private long p2Score;
    private Match next;

    public Match(UUID id, int matchNumber,
                 String p1Name,
                 String p2Name,
                 boolean isAvailable,
                 String winnerName,
                 long p1Score,
                 long p2Score,
                 Match next) {
        this.id = id;
        this.matchNumber = matchNumber;
        this.p1Name = p1Name;
        this.p2Name = p2Name;
        this.isAvailable = isAvailable;
        this.winnerName = winnerName;
        this.p1Score = p1Score;
        this.p2Score = p2Score;
        this.next = next;
    }

    public void play(RemoteBotRepository repository,
                     RemoteBotApi botApi,
                     BotManagerService botManagerService,
                     int times) {
        final PlayWithBotsUseCase useCase = new PlayWithBotsUseCase(repository, botApi, botManagerService);
        PlayWithBotsResultsDto results = useCase.playWithBots(UUID.randomUUID(), p1Name, UUID.randomUUID(), p2Name, times);
        long p1Wins = results.info().stream().filter(info -> info.name().equals(p1Name)).count();
        long p2Wins = times - p1Wins;
        p1Score = p1Wins;
        p2Score = p2Wins;
        if (p1Wins > times / 2) {
            winnerName = p1Name;
        } else
            winnerName = p2Name;
        isAvailable = false;
    }

    public void setWinnerToNextBracket() {
        if (next == null) return;
        if (winnerName == null) return;

        if (matchNumber % 2 == 0) {
            if (next.p2Name == null)
                next.p2Name = winnerName;
            return;
        }

        if (next.p1Name == null)
            next.p1Name = winnerName;
    }

    public void setAvailableState() {
        isAvailable = isPlayableMatch();
    }

    private boolean isPlayableMatch() {
        return p1Name != null && p2Name != null && winnerName == null;
    }

    public UUID getId() {
        return id;
    }

    public void setP1Name(String p1Name) {
        this.p1Name = p1Name;
    }

    public void setP2Name(String p2Name) {
        this.p2Name = p2Name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public Match getNext() {
        return next;
    }

    public void setNext(Match next) {
        this.next = next;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public String getP1Name() {
        return p1Name;
    }

    public String getP2Name() {
        return p2Name;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public long getP1Score() {
        return p1Score;
    }

    public long getP2Score() {
        return p2Score;
    }

    @Override
    public String toString() {
        return "MatchInfo{" +
               "uuid= " + id +
               ", number= " + matchNumber +
               ", p1Name='" + p1Name + '\'' +
               ", p2Name='" + p2Name + '\'' +
               ", available=" + isAvailable +
               ", winnerName='" + winnerName + '\'' +
               ", p1Score=" + p1Score +
               ", p2Score=" + p2Score +
               ", nextMatch=" + (next == null ? ("null") : (next.id.toString()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;
        return id.equals(match.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || this.getClass() != o.getClass()) throw new RuntimeException();
        Match match = (Match) o;
        return Integer.compare(this.matchNumber, match.matchNumber);
    }


}
