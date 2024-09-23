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
    private boolean available;
    private String winnerName;
    private long p1Score;
    private long p2Score;
    private Match next;

    public Match(UUID id, int matchNumber, String p1Name, String p2Name, boolean available, String winnerName, long p1Score, long p2Score, Match next) {
        this.id = id;
        this.matchNumber = matchNumber;
        this.p1Name = p1Name;
        this.p2Name = p2Name;
        this.available = available;
        this.winnerName = winnerName;
        this.p1Score = p1Score;
        this.p2Score = p2Score;
        this.next = next;
    }

    public void play(RemoteBotRepository repository, RemoteBotApi botApi, BotManagerService botManagerService, int times) {
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
    }


    public UUID getId() {
        return id;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public String getP1Name() {
        return p1Name;
    }

    public void setP1Name(String p1Name) {
        this.p1Name = p1Name;
    }

    public String getP2Name() {
        return p2Name;
    }

    public void setP2Name(String p2Name) {
        this.p2Name = p2Name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public long getP1Score() {
        return p1Score;
    }

    public void setP1Score(long p1Score) {
        this.p1Score = p1Score;
    }

    public long getP2Score() {
        return p2Score;
    }

    public void setP2Score(long p2Score) {
        this.p2Score = p2Score;
    }

    public Match getNext() {
        return next;
    }

    public void setNext(Match next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "MatchInfo{" +
               "id= " + id +
               ", number= " + matchNumber +
               ", p1Name='" + p1Name + '\'' +
               ", p2Name='" + p2Name + '\'' +
               ", available=" + available +
               ", winnerName='" + winnerName + '\'' +
               ", p1Score=" + p1Score +
               ", p2Score=" + p2Score +
               ", nextMatch=" + (next == null ? ("null") : (next.id.toString()));
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || this.getClass() != o.getClass()) throw new RuntimeException();
        Match match = (Match) o;
        return Integer.compare(this.matchNumber, match.matchNumber);
    }
}
