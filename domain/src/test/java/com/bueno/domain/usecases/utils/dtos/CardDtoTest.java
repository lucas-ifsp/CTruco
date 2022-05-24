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

package com.bueno.domain.usecases.utils.dtos;

import com.bueno.domain.usecases.intel.dtos.CardDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CardDtoTest {

    @ParameterizedTest
    @CsvSource({"A,C", "Q,S", "K,H", "J,D", "2,H", "X,x"})
    @DisplayName("Should not throw if parameters are valid")
    void shouldNotThrowIfParametersAreValid(String rank, String suit) {
        assertThatNoException().isThrownBy(() -> new CardDto(rank, suit));
    }

    @Test
    @DisplayName("Should throw if rank is null")
    void shouldThrowIfRankNull() {
        assertThrows(NullPointerException.class, () -> new CardDto(null, "c"));
    }

    @ParameterizedTest
    @CsvSource({"Aa,C", "8,H", "1,S"})
    @DisplayName("Should throw if rank is invalid")
    void shouldThrowIfRankIsInvalid(String rank) {
        assertThatIllegalArgumentException().isThrownBy(() -> new CardDto(rank, "C"));
    }

    @Test
    @DisplayName("Should throw if suit is null")
    void shouldThrowIfSuitIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new CardDto("a", null));
    }

    @ParameterizedTest
    @CsvSource({"A,Cc", "Q,W", "K,"})
    @DisplayName("Should throw if suit is invalid")
    void shouldThrowIfSuitIsInvalid(String suit) {
        assertThatIllegalArgumentException().isThrownBy(() -> new CardDto("A", suit));
    }

    @Test
    @DisplayName("Should correctly create closed card")
    void shouldCorrectlyCreateClosedCard() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(CardDto.closed().rank()).isEqualTo("X");
        softly.assertThat(CardDto.closed().suit()).isEqualTo("X");
        softly.assertAll();
    }
}