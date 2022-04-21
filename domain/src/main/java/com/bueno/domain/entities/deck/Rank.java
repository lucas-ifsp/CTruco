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

package com.bueno.domain.entities.deck;

import java.util.Arrays;

public enum Rank {
    HIDDEN(0, 'X'), FOUR(1, '4'), FIVE(2, '5'),
    SIX(3, '6'), SEVEN(4, '7'), QUEEN(5, 'Q'),
    JACK(6, 'J'), KING(7, 'K'), ACE(8, 'A'),
    TWO(9, '2'), THREE(10, '3');

    private final int value;
    private final char symbol;

    Rank(int value, char symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    int value() {
        return value;
    }

    public Rank next() {
        return switch (value){
            case 0 -> HIDDEN;
            case 10 -> FOUR;
            default -> values()[value + 1];
        };
    }

    public static Rank ofSymbol(String symbol){
        return Arrays.stream(values())
                .filter(rank -> String.valueOf(rank.symbol).equals(symbol))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown rank symbol:" + symbol));
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
