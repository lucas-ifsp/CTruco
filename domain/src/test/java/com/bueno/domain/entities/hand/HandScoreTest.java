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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class HandScoreTest {
    @Test
    @DisplayName("Should increase from 1 to 3")
    void shouldIncreaseFrom1To3() {
        assertEquals(HandScore.THREE, HandScore.ONE.increase());
    }

    @Test
    @DisplayName("Should increase from 6 to 9")
    void shouldIncreaseFrom6To9() {
        assertEquals(HandScore.NINE, HandScore.SIX.increase());
    }

    @Test
    @DisplayName("Should throw if increases from 12")
    void shouldThrowIfIncreasesFrom12() {
        assertThrows(HandScoreException.class, HandScore.TWELVE::increase);
    }

    @Test
    @DisplayName("Should throw if increase from 0")
    void shouldThrowIfIncreasesFrom0() {
        assertThrows(HandScoreException.class, HandScore.ZERO::increase);
    }
}