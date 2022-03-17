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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class HandPointsTest {
    @Test
    @DisplayName("Should increase from 1 to 3")
    void shouldIncreaseFrom1To3() {
        assertEquals(HandPoints.THREE, HandPoints.ONE.increase());
    }

    @Test
    @DisplayName("Should increase from 3 to 6")
    void shouldIncreaseFrom3To6() {
        assertEquals(HandPoints.SIX, HandPoints.THREE.increase());
    }

    @Test
    @DisplayName("Should increase from 6 to 9")
    void shouldIncreaseFrom6To9() {
        assertEquals(HandPoints.NINE, HandPoints.SIX.increase());
    }

    @Test
    @DisplayName("Should increase from 9 to 12")
    void shouldIncreaseFrom9To12() {
        assertEquals(HandPoints.TWELVE, HandPoints.NINE.increase());
    }

    @Test
    @DisplayName("Should throw if increases from 12")
    void shouldThrowIfIncreasesFrom12() {
        assertThrows(GameRuleViolationException.class, HandPoints.TWELVE::increase);
    }

    @Test
    @DisplayName("Should throw if increases from 0")
    void shouldThrowIfIncreasesFrom0() {
        assertThrows(GameRuleViolationException.class, HandPoints.ZERO::increase);
    }

    @Test
    @DisplayName("Should create hand point from valid int value")
    void shouldCreateHandPointFromValidIntValue() {
        final HandPoints handPoints = HandPoints.fromIntValue(1);
        assertEquals(HandPoints.ONE, handPoints);
    }

    @Test
    @DisplayName("Should throw if try to create hand point from invalid int value")
    void shouldThrowIfTryToCreateHandPointFromInvalidIntValue() {
        assertThrows(IllegalArgumentException.class, () -> HandPoints.fromIntValue(4));
    }

    @Test
    @DisplayName("Should get correct int value from hand point")
    void shouldGetCorrectIntValueFromHandPoint() {
        assertEquals(9, HandPoints.NINE.get());
    }

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        assertEquals("Hand points = 1", HandPoints.ONE.toString());
    }


}