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

import com.bueno.domain.entities.game.GameRuleViolationException;

import java.util.Arrays;

public enum HandPoints {
    ZERO(0), ONE(1), THREE(3), SIX(6), NINE(9), TWELVE(12);

    private final int points;

    HandPoints(int points) {
        this.points = points;
    }

    public HandPoints increase() {
        return switch (this){
            case ONE -> THREE;
            case THREE -> SIX;
            case SIX -> NINE;
            case NINE -> TWELVE;
            case ZERO, TWELVE -> throw new GameRuleViolationException("Can not increase points from " + this);
        };
    }

    public int get() {
        return points;
    }

    public static HandPoints fromIntValue(Integer points){
        return Arrays.stream(values())
                .filter(value -> value.points == points)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Illegal point value" + points));
    }

    @Override
    public String toString() {
        return "Hand points = " + points;
    }
}
