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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


class HandScoreTest {

    @ParameterizedTest
    @ValueSource(ints = {1,3,6,9,12})
    @DisplayName("Should accept valid score values")
    void shouldAcceptValidScoreValues(int scoreValue) {
        final HandScore score  = HandScore.of(scoreValue);
        assertEquals(scoreValue, score.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {-3,2,13,15})
    @DisplayName("Should not accept invalid score values")
    void shouldThrowForInvalidScore(int scoreValue) {
        assertThrows(HandScoreException.class, () -> HandScore.of(scoreValue));
    }

    @Test
    @DisplayName("Should increase from 1 to 3")
    void shouldIncreaseFrom1To3() {
        final HandScore score = HandScore.of(1);
        assertEquals(3, score.increase().get());
    }

    @Test
    @DisplayName("Should increase from 6 to 9")
    void shouldIncreaseFrom6To9() {
        final HandScore score = HandScore.of(1);
        assertEquals(3, score.increase().get());
    }

    @Test
    @DisplayName("Should throw if increases from 12")
    void shouldThrowIfIncreasesFrom12() {
        final HandScore score = HandScore.of(12);
        assertThrows(HandScoreException.class, score::increase);
    }

    @Test
    @DisplayName("Should throw if increase from 0")
    void shouldThrowIfIncreasesFrom0() {
        final HandScore score = HandScore.of(0);
        assertThrows(HandScoreException.class, score::increase);
    }

    @Test
    @DisplayName("Can increase if is lower than 12")
    void canIncreaseIfIsLowerThan12() {
        assertTrue(HandScore.of(9).canIncrease());
    }

    @Test
    @DisplayName("Cannot increase if score is equal to 12")
    void cannotIncreaseIfIsScore12() {
        assertFalse(HandScore.of(12).canIncrease());
    }

    @Test
    @DisplayName("Cannot increase if score is equal to 0")
    void cannotIncreaseIfScoreIs0() {
        assertFalse(HandScore.of(0).canIncrease());
    }

}