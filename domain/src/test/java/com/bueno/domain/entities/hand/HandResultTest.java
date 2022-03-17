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

import com.bueno.domain.entities.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandResultTest {

    @Mock Player player1;
    @Mock Player player2;


    @Test
    @DisplayName("Should allow creating result with winner and hand points")
    void shouldAllowCreatingResultWithWinnerAndHandPoints() {
        final HandResult result = HandResult.of(player1, HandPoints.ONE);
        assertAll(
                () -> assertEquals(player1, result.getWinner().orElse(null)),
                () -> assertEquals(HandPoints.ONE, result.getPoints())
        );
    }

    @Test
    @DisplayName("Should allow creating result of a draw hand")
    void shouldAllowCreatingResultOfADrawHand() {
        final HandResult result = HandResult.ofDraw();
        assertAll(
                () -> assertTrue(result.getWinner().isEmpty()),
                () -> assertEquals(HandPoints.ZERO, result.getPoints())
        );
    }

    @Test
    @DisplayName("Should not allow creating hand result with partial parameters")
    void shouldNotAllowCreatingHandResultWithPartialParameters() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> HandResult.of(null, HandPoints.ONE)),
                () -> assertThrows(NullPointerException.class, () -> HandResult.of(player1, null))
        );
    }

    @Test
    @DisplayName("Should not allow creating a result with winner and zero points")
    void shouldNotAllowCreatingAResultWithWinnerAndZeroPoints() {
        assertThrows(IllegalArgumentException.class, () -> HandResult.of(player1, HandPoints.ZERO));
    }

    @Test
    @DisplayName("Should correctly toString hand of nine points")
    void shouldCorrectlyToStringHandOfNinePoints() {
        final UUID uuid = UUID.randomUUID();
        final HandResult result = HandResult.of(player1, HandPoints.NINE);
        when(player1.getUsername()).thenReturn("p1");
        when(player1.getUuid()).thenReturn(uuid);
        String expected = "HandResult = p1 ("+ uuid + ") won 9 point(s)";
        assertEquals(expected, result.toString());
    }

    @Test
    @DisplayName("Should correctly toString drew hand")
    void shouldCorrectlyToStringDrewHand() {
        final HandResult result = HandResult.ofDraw();
        String expected = "HandResult = draw 0 point(s)";
        assertEquals(expected, result.toString());
    }

    @Test
    @DisplayName("Should hand results of same winner and points be equal")
    void shouldHandResultsOfSameWinnerAndPointsBeEqual() {
        assertEquals(HandResult.ofDraw(), HandResult.ofDraw());
    }

    @Test
    @DisplayName("Should hand results of different winner or points not be equal")
    void shouldHandResultsOfDifferentWinnerOrPointsNotBeEqual() {
        assertAll(
                () -> assertNotEquals(HandResult.of(player1, HandPoints.ONE), HandResult.of(player2, HandPoints.ONE)),
                () -> assertNotEquals(HandResult.of(player1, HandPoints.ONE), HandResult.of(player1, HandPoints.THREE))
        );
    }



}