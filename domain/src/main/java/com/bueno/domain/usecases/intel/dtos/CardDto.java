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

package com.bueno.domain.usecases.intel.dtos;


import java.util.Objects;
import java.util.regex.Pattern;

public record CardDto(String rank, String suit) {

    public CardDto(String rank, String suit) {
        this.rank = Objects.requireNonNull(rank, "Rank must not be null.").toUpperCase();
        this.suit = Objects.requireNonNull(suit, "Suit must not be null.").toUpperCase();
        final boolean isValidRank = Pattern.compile("[A2-7QJKX]").matcher(this.rank).matches();
        final boolean isValidSuit = Pattern.compile("[DCHSX]").matcher(this.suit).matches();
        if (!isValidRank) throw new IllegalArgumentException("Rank value is not valid: " + this.rank);
        if (!isValidSuit) throw new IllegalArgumentException("Suit value is not valid: " + this.suit);
    }

    public static CardDto closed() {
        return new CardDto("X", "X");
    }

    @Override
    public String toString() {
        return rank+""+suit;
    }
}