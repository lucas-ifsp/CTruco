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

import static com.bueno.spi.model.CardSuit.HEARTS;
import static com.bueno.spi.model.CardSuit.SPADES;
import static org.junit.jupiter.api.Assertions.*;

class CardSuitTest {

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        assertEquals("S", SPADES.toString());
    }

    @Test
    @DisplayName("Should get correct suit value")
    void shouldGetCorrectSuitValue() {
        assertEquals(2, SPADES.value());
    }

    @Test
    @DisplayName("Should create enum suit from valid symbol")
    void shouldCreateEnumSuitFromValidSymbol() {
        assertEquals(HEARTS, CardSuit.ofSymbol("H"));
    }

    @Test
    @DisplayName("Should throw if tries to create suit from invalid symbol")
    void shouldThrowIfTriesToCreateSuitFromInvalidSymbol() {
        assertThrows(IllegalArgumentException.class, () -> CardSuit.ofSymbol("A"));
    }
}