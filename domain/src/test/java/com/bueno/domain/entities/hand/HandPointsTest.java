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

import static com.bueno.domain.entities.hand.HandPoints.*;
import static com.bueno.domain.entities.hand.HandPoints.TWELVE;
import static com.bueno.domain.entities.hand.HandPoints.ZERO;
import static org.assertj.core.api.Assertions.*;


class HandPointsTest {
    @Test
    @DisplayName("Should increase from 1 to 3")
    void shouldIncreaseFrom1To3() {
        assertThat(ONE.increase()).isEqualTo(THREE);
    }

    @Test
    @DisplayName("Should increase from 3 to 6")
    void shouldIncreaseFrom3To6() {
        assertThat(THREE.increase()).isEqualTo(SIX);
    }

    @Test
    @DisplayName("Should increase from 6 to 9")
    void shouldIncreaseFrom6To9() {
        assertThat(SIX.increase()).isEqualTo(NINE);
    }

    @Test
    @DisplayName("Should increase from 9 to 12")
    void shouldIncreaseFrom9To12() {
        assertThat(NINE.increase()).isEqualTo(TWELVE);
    }

    @Test
    @DisplayName("Should throw if increases from 12")
    void shouldThrowIfIncreasesFrom12() {
        assertThatExceptionOfType(GameRuleViolationException.class).isThrownBy(TWELVE::increase);
    }

    @Test
    @DisplayName("Should throw if increases from 0")
    void shouldThrowIfIncreasesFrom0() {
        assertThatExceptionOfType(GameRuleViolationException.class).isThrownBy(ZERO::increase);
    }

    @Test
    @DisplayName("Should create hand point from valid int value")
    void shouldCreateHandPointFromValidIntValue() {
        assertThat(fromIntValue(1)).isEqualTo(ONE);
    }

    @Test
    @DisplayName("Should throw if try to create hand point from invalid int value")
    void shouldThrowIfTryToCreateHandPointFromInvalidIntValue() {
        assertThatIllegalArgumentException().isThrownBy(() -> fromIntValue(4));
    }

    @Test
    @DisplayName("Should get correct int value from hand point")
    void shouldGetCorrectIntValueFromHandPoint() {
        assertThat(NINE.get()).isEqualTo(9);
    }

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        assertThat(ONE.toString()).isEqualTo("Hand points = 1");
    }
}