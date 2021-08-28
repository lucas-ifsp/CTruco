package com.bueno.truco.domain.entities.truco;

import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;

import java.util.Objects;
import java.util.Optional;

public class TrucoResult {
    private final HandScore score;
    private final Player winner;
    private final Player lastRequester;

    public TrucoResult(HandScore score) {
        this(score, null, null);
    }

    public TrucoResult(HandScore score, Player winner) {
        this(score, winner, null);
    }

    public TrucoResult(HandScore score, Player winner, Player lastRequester) {
        this.score = score;
        this.winner = winner;
        this.lastRequester = lastRequester;
    }

    public HandScore getScore() {
        return score;
    }

    public boolean hasWinner() {
        return winner != null;
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public Optional<Player> getLastRequester() {
        return Optional.ofNullable(lastRequester);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrucoResult that = (TrucoResult) o;
        return Objects.equals(score, that.score) && Objects.equals(winner, that.winner) && Objects.equals(lastRequester, that.lastRequester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, winner, lastRequester);
    }

    @Override
    public String toString() {
        return "Points=" + score.get() + ", winner=" + (winner != null? winner : "No winner");
    }
}
