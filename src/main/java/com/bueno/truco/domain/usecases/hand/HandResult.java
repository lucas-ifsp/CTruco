package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.player.Player;

import java.util.Optional;

public class HandResult {

    private final Player winner;
    private final int points;

    public HandResult(Player winner, int points) {
        this.winner = winner;
        this.points = points;
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "winner=" + winner +  ", points=" + points;
    }
}
