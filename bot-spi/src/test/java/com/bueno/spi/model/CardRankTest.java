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

package com.bueno.spi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardRank.KING;
import static org.junit.jupiter.api.Assertions.*;

class CardRankTest {

    @Test
    @DisplayName("Should next rank of hidden be hidden")
    void shouldNextRankOfHiddenBeHidden() {
        assertEquals(HIDDEN, HIDDEN.next());
    }

    @Test
    @DisplayName("Should next rank of 3 be 4")
    void shouldNextRankOf3Be4() {
        assertEquals(FOUR, THREE.next());
    }

    @Test
    @DisplayName("Should next rank of King be Ace")
    void shouldNextRankOfKingBeAce() {
        assertEquals(ACE, KING.next());
    }

    @Test
    @DisplayName("should correctly toString")
    void shouldCorrectlyToString() {
        assertEquals("A", ACE.toString());
    }

    @Test
    @DisplayName("Should correctly get rank value")
    void shouldCorrectlyGetRankValue() {
        assertEquals(1, FOUR.value());
    }

    @Test
    @DisplayName("Should create enum rank from valid symbol")
    void shouldCreateEnumRankFromValidSymbol() {
        assertEquals(CardRank.ofSymbol("K"), KING);
    }

    @Test
    @DisplayName("Should throw if tries to create enum rank from invalid symbol")
    void shouldThrowIfTriesToCreateEnumRankFromInvalidSymbol() {
        assertThrows(IllegalArgumentException.class, () -> CardRank.ofSymbol("Z"));
    }
}