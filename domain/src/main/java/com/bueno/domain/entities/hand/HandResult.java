/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

import com.bueno.domain.entities.player.Player;

import java.util.Objects;
import java.util.Optional;

public class HandResult {

    private final Player winner;
    private final HandPoints points;

    private HandResult(Player winner, HandPoints handPoints) {
        this.winner = winner;
        this.points = handPoints;
    }

    public static HandResult of(Player winner, HandPoints handPoints){
        Objects.requireNonNull(winner, "Player must not be null!");
        Objects.requireNonNull(handPoints, "Hand score must not be null!");

        if(handPoints.equals(HandPoints.ZERO))
            throw new IllegalArgumentException("Points of an untied hand must not be zero.");

        return new HandResult(winner, handPoints);
    }

    public static HandResult ofDraw(){
        return new HandResult(null, HandPoints.ZERO);
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public HandPoints getPoints() {
        return points;
    }

    @Override
    public String toString() {
        String result = winner == null ? "draw" : (winner.getUsername() + " (" + winner.getUuid() + ") won");
        return "HandResult = " + result + " " + points.get() + " point(s)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandResult that = (HandResult) o;
        return Objects.equals(winner, that.winner) && points == that.points;
    }
}
