package com.bueno.truco.domain.entities.hand;

import com.bueno.truco.domain.entities.truco.TrucoResult;
import com.bueno.truco.domain.entities.player.Player;

import java.util.Optional;

public class HandResult {

    private final Player winner;
    private final HandScore score;

    public HandResult() {
        winner = null;
        score = null;
    }

    public HandResult(Player winner, HandScore handScore) {
        if(winner == null || handScore == null)
            throw new NullPointerException("Parameters must not be null!");
        this.winner = winner;
        this.score = HandScore.of(handScore);
    }

    public HandResult(TrucoResult result){
        this(result.getWinner().get(), HandScore.of(result.getScore()));
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public HandScore getScore() {
        return score;
    }
}
