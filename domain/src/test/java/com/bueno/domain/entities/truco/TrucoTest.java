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

package com.bueno.domain.entities.truco;

import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.player.util.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrucoTest {

    private Truco sut;
    @Mock
    private Player p1;
    @Mock
    private Player p2;

    @BeforeAll
    static void init(){
        Logger.getLogger(Truco.class.getName()).setLevel(Level.OFF);
    }

    @BeforeEach
    void setUp() {
        sut = new Truco(p1, p2);
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should caller be winner of one point")
    void shouldCallerBeWinnerOfOnePoint(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(any())).thenReturn(-1);
        TrucoResult result = sut.handle(HandScore.ONE);
        assertEquals(new TrucoResult(HandScore.ONE, p1), result);
    }

    @Test
    @DisplayName("Should caller be winner of three points")
    void shouldCallerBeWinnerOfThreePoints(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(any())).thenReturn(-1);
        TrucoResult result = sut.handle(HandScore.THREE);
        assertEquals(new TrucoResult(HandScore.THREE, p1), result);
    }

    @Test
    @DisplayName("Should return not winner and match worth six points")
    void shouldReturnNoWinnerAndMatchWorthSixPoints(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(HandScore.THREE);
        assertAll(
                () -> assertEquals(HandScore.SIX, result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return not winner and match worth 12 points")
    void shouldReturnNoWinnerAndMatchWorth12Points(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(any())).thenReturn(1);
        when(p2.getTrucoResponse(any())).thenReturn(1);
        TrucoResult result = sut.handle(HandScore.THREE);
        assertAll(
                () -> assertEquals(HandScore.TWELVE, result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return winner after call and then run")
    void shouldReturnWinnerAfterCallAndThenRun(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(any())).thenReturn(-1);
        when(p2.getTrucoResponse(any())).thenReturn(1);
        TrucoResult result = sut.handle(HandScore.THREE);
        assertEquals(new TrucoResult(HandScore.SIX, p2), result);
    }

    @Test
    @DisplayName("Should correctly toString() TrucoResult having winner")
    void shouldCorrectlyToStringTrucoResultHavingWinner(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(any())).thenReturn(-1);
        TrucoResult result = sut.handle(HandScore.ONE);
        assertEquals("Points=1, winner=p1",result.toString());
    }

    @Test
    @DisplayName("Should corretly toString() TrucoResult having no winner")
    void shouldCorrectlyToStringTrucoResultHavingNoWinner(){
        when(p1.requestTruco()).thenReturn(false);
        sut = new Truco(p1, p1);
        TrucoResult result = sut.handle(HandScore.ONE);
        assertEquals("Points=1, winner=No winner",result.toString());
    }


    @Test
    @DisplayName("Should throw if requester is null")
    void shouldThrowIfRequesterIsNull(){
        assertThrows(NullPointerException.class, () -> new Truco(null, p2));
    }

    @Test
    @DisplayName("Should throw if responder is null")
    void shouldThrowIfResponderIsNull(){
        assertThrows(NullPointerException.class, () -> new Truco(p1, null));
    }

    @Test
    @DisplayName("Should limit hand points according to losing player")
    void shouldLimitHandPointsAccordingToLosingPlayer(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(any())).thenReturn(1);
        when(p1.getScore()).thenReturn(5);

        when(p2.getTrucoResponse(any())).thenReturn(1);
        when(p2.getScore()).thenReturn(8);

        TrucoResult result = sut.handle(HandScore.ONE);
        assertAll(
                () -> assertEquals(HandScore.NINE, result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return no winner if any player has 11 points")
    void shouldReturnNoWinnerIfAnyPlayerHas11Points(){
        when(p1.getScore()).thenReturn(11);
        TrucoResult result = sut.handle(HandScore.ONE);
        assertEquals(new TrucoResult(HandScore.ONE, null), result);
    }

    @Test
    @DisplayName("Should return no winner of call starting on 12")
    void shouldReturnNoWinnerForCallStartingOn12(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(HandScore.TWELVE);
        assertAll(
                () -> assertEquals(HandScore.TWELVE, result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return no winner if no one is asking for truco")
    void shouldReturnNoWinnerIfNoOneIsAskingForTruco(){
        TrucoResult result = sut.handle(HandScore.THREE);
        assertEquals(new TrucoResult(HandScore.THREE, null), result);
    }
}