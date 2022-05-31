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

public enum Suit {
    HIDDEN("X", 0),
    DIAMONDS("D", 1),
    SPADES("S", 2),
    HEARTS("H", 3),
    CLUBS("C", 4);

    private final String symbol;
    private final int ordinalValue;

    Suit(String symbol, int ordinalValue) {
        this.symbol = symbol;
        this.ordinalValue = ordinalValue;
    }

    public static Suit ofSymbol(String symbol){
        return Arrays.stream(values())
                .filter(suit -> suit.symbol.equals(symbol))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown suit symbol"));
    }

    @Override
    public String toString() {
        return symbol;
    }

    int value() {
        return ordinalValue;
    }

}
