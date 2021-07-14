package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrucoTest {

    private Truco sut;
    @Mock
    private Player p1;
    @Mock
    private Player p2;

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
        when(p2.getTrucoResponse(anyInt())).thenReturn(-1);
        TrucoResult result = sut.handle(1);
        assertEquals(new TrucoResult(1, p1), result);
    }

    @Test
    @DisplayName("Should caller be winner of three points")
    void shouldCallerBeWinnerOfThreePoints(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(anyInt())).thenReturn(-1);
        TrucoResult result = sut.handle(3);
        assertEquals(new TrucoResult(3, p1), result);
    }

    @Test
    @DisplayName("Should return not winner and match worth six points")
    void shouldReturnNoWinnerAndMatchWorthSixPoints(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(3);
        assertEquals(new TrucoResult(6, null), result);
    }

    @Test
    @DisplayName("Should return not winner and match worth 12 points")
    void shouldReturnNoWinnerAndMatchWorth12Points(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(anyInt())).thenReturn(1);
        when(p2.getTrucoResponse(anyInt())).thenReturn(1);
        TrucoResult result = sut.handle(3);
        assertEquals(new TrucoResult(12, null), result);
    }

    @Test
    @DisplayName("Should return winner after call and then run")
    void shouldReturnWinnerAfterCallAndThenRun(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(anyInt())).thenReturn(-1);
        when(p2.getTrucoResponse(anyInt())).thenReturn(1);
        TrucoResult result = sut.handle(3);
        assertEquals(new TrucoResult(6, p2), result);
    }

    @Test
    @DisplayName("Should correctly toString() TrucoResult having winner")
    void shouldCorrectlyToStringTrucoResultHavingWinner(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(anyInt())).thenReturn(-1);
        TrucoResult result = sut.handle(1);
        assertEquals("Points=1, winner=p1",result.toString());
    }

    @Test
    @DisplayName("Should corretly toString() TrucoResult having no winner")
    void shouldCorrectlyToStringTrucoResultHavingNoWinner(){
        when(p1.requestTruco()).thenReturn(false);
        sut = new Truco(p1, p1);
        TrucoResult result = sut.handle(1);
        assertEquals("Points=1, winner=No winner",result.toString());
    }

    @Test
    @DisplayName("Should throw if hand points are invalid")
    void shouldThrowIfHandPointsAreInvalid(){
        assertThrows(IllegalArgumentException.class, () -> sut.handle(2));
    }

    @Test
    @DisplayName("Should throw if requester is null")
    void shouldThrowIfRequesterIsNull(){
        sut = new Truco(null, p2);
        assertThrows(IllegalArgumentException.class, () -> sut.handle(1));
    }

    @Test
    @DisplayName("Should throw if responder is null")
    void shouldThrowIfResponderIsNull(){
        sut = new Truco(p1, null);
        assertThrows(IllegalArgumentException.class, () -> sut.handle(1));
    }

    @Test
    @DisplayName("Should limit hand points according to losing player")
    void shouldLimitHandPointsAccordingToLosingPlayer(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(anyInt())).thenReturn(1);
        when(p1.getScore()).thenReturn(5);

        when(p2.getTrucoResponse(anyInt())).thenReturn(1);
        when(p2.getScore()).thenReturn(8);

        TrucoResult result = sut.handle(1);
        assertEquals(new TrucoResult(9, null), result);
    }

    @Test
    @DisplayName("Should return no winner if any player has 11 points")
    void shouldReturnNoWinnerIfAnyPlayerHas11Points(){
        when(p1.getScore()).thenReturn(11);
        TrucoResult result = sut.handle(1);
        assertEquals(new TrucoResult(1, null), result);
    }

    @Test
    @DisplayName("Should return no winner of call starting on 12")
    void shouldReturnNoWinnerForCallStatingOn12(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(12);
        assertEquals(new TrucoResult(12, null), result);
    }

    @Test
    @DisplayName("Should return no winner if no one is asking for truco")
    void shouldReturnNoWinnerIfNoOneIsAskingForTruco(){
        TrucoResult result = sut.handle(3);
        assertEquals(new TrucoResult(3, null), result);
    }
}