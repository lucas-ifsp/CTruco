package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;

import java.util.Optional;

public class HandResult {

    private final Player winner;
    private final int points;

    public HandResult(Player winner, int points) {
        if(!isValidHandValue(points))
            throw new GameRuleViolationException("Illegal hand value!");
        this.winner = winner;
        this.points = points;
    }

    public HandResult(TrucoResult result){
        this(result.getWinner().get(), result.getPoints());
    }

    private boolean isValidHandValue(int points) {
        return points == 0 || points == 1 || points == 3 || points == 6 || points == 9 || points == 12;
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public int getPoints() {
        return points;
    }
}
