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

import com.bueno.domain.entities.player.util.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HandResultTest {

    @Mock
    private Player player;

    @ParameterizedTest(name = "[{index}]: with valid hand value = {0}")
    @DisplayName("Should allow creating hand result with all arguments")
    @ValueSource(ints = {0, 1, 3, 6, 9, 12})
    void shouldAllowCreatingHandResultWithValidArguments(int value){
        HandResult handResult = new HandResult(player, HandScore.of(value));
        assertEquals(player, handResult.getWinner().orElseThrow());
        assertEquals(HandScore.of(value), handResult.getScore());
    }

    @Test
    @DisplayName("Should not allow creating hand result with partial parameters")
    void shouldNotAllowCreatingHandResultWithPartialParameters() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new HandResult(null, HandScore.of(1))),
                () -> assertThrows(NullPointerException.class, () -> new HandResult(player, null)));
    }
}