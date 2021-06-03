package com.bueno.truco.domain.usecases.hand;

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
}
