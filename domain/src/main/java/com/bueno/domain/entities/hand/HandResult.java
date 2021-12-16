/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.domain.entities.hand;

import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.truco.TrucoResult;

import java.util.Objects;
import java.util.Optional;

public class HandResult {

    private final Player winner;
    private final HandScore score;

    public HandResult() {
        winner = null;
        score = null;
    }

    public HandResult(Player winner, HandScore handScore) {
        this.winner = Objects.requireNonNull(winner, "Player must not be null!");
        this.score = Objects.requireNonNull(handScore, "Hand score must not be null!");
    }

    public HandResult(TrucoResult result){
        this(result.getWinner().orElse(null), result.getScore());
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public HandScore getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "HandResult{" +
                "winner=" + winner +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandResult that = (HandResult) o;
        return Objects.equals(winner, that.winner) && score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(winner, score);
    }
}
