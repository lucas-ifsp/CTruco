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

package com.bueno.domain.usecases.hand;

import com.bueno.domain.usecases.hand.dtos.PlayCardDto;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class PlayCardDtoTest {

    @Test
    @DisplayName("Should not throw if all parameters are valid")
    void shouldNotThrowIfAllParametersAreValid() {
        assertThatNoException().isThrownBy(() -> new PlayCardDto(UUID.randomUUID(), new CardDto("X", "X")));
    }

    @Test
    @DisplayName("Should throw if uuid is null")
    void shouldThrowIfUuidIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new PlayCardDto(null, new CardDto("X", "X")));
    }

    @Test
    @DisplayName("Should throw if card is null")
    void shouldThrowIfCardIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new PlayCardDto(UUID.randomUUID(), null));
    }
}
