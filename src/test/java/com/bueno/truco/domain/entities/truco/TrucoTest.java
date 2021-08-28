package com.bueno.truco.domain.entities.truco;

import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
        TrucoResult result = sut.handle(HandScore.of(1));
        assertEquals(new TrucoResult(HandScore.of(1), p1), result);
    }

    @Test
    @DisplayName("Should caller be winner of three points")
    void shouldCallerBeWinnerOfThreePoints(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(any())).thenReturn(-1);
        TrucoResult result = sut.handle(HandScore.of(3));
        assertEquals(new TrucoResult(HandScore.of(3), p1), result);
    }

    @Test
    @DisplayName("Should return not winner and match worth six points")
    void shouldReturnNoWinnerAndMatchWorthSixPoints(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(HandScore.of(3));
        assertAll(
                () -> assertEquals(HandScore.of(6), result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return not winner and match worth 12 points")
    void shouldReturnNoWinnerAndMatchWorth12Points(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(any())).thenReturn(1);
        when(p2.getTrucoResponse(any())).thenReturn(1);
        TrucoResult result = sut.handle(HandScore.of(3));
        assertAll(
                () -> assertEquals(HandScore.of(12), result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return winner after call and then run")
    void shouldReturnWinnerAfterCallAndThenRun(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(any())).thenReturn(-1);
        when(p2.getTrucoResponse(any())).thenReturn(1);
        TrucoResult result = sut.handle(HandScore.of(3));
        assertEquals(new TrucoResult(HandScore.of(6), p2), result);
    }

    @Test
    @DisplayName("Should correctly toString() TrucoResult having winner")
    void shouldCorrectlyToStringTrucoResultHavingWinner(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(any())).thenReturn(-1);
        TrucoResult result = sut.handle(HandScore.of(1));
        assertEquals("Points=1, winner=p1",result.toString());
    }

    @Test
    @DisplayName("Should corretly toString() TrucoResult having no winner")
    void shouldCorrectlyToStringTrucoResultHavingNoWinner(){
        when(p1.requestTruco()).thenReturn(false);
        sut = new Truco(p1, p1);
        TrucoResult result = sut.handle(HandScore.of(1));
        assertEquals("Points=1, winner=No winner",result.toString());
    }


    @Test
    @DisplayName("Should throw if requester is null")
    void shouldThrowIfRequesterIsNull(){
        sut = new Truco(null, p2);
        assertThrows(IllegalArgumentException.class, () -> sut.handle(HandScore.of(1)));
    }

    @Test
    @DisplayName("Should throw if responder is null")
    void shouldThrowIfResponderIsNull(){
        sut = new Truco(p1, null);
        assertThrows(IllegalArgumentException.class, () -> sut.handle(HandScore.of(1)));
    }

    @Test
    @DisplayName("Should limit hand points according to losing player")
    void shouldLimitHandPointsAccordingToLosingPlayer(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(any())).thenReturn(1);
        when(p1.getScore()).thenReturn(5);

        when(p2.getTrucoResponse(any())).thenReturn(1);
        when(p2.getScore()).thenReturn(8);

        TrucoResult result = sut.handle(HandScore.of(1));
        assertAll(
                () -> assertEquals(HandScore.of(9), result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return no winner if any player has 11 points")
    void shouldReturnNoWinnerIfAnyPlayerHas11Points(){
        when(p1.getScore()).thenReturn(11);
        TrucoResult result = sut.handle(HandScore.of(1));
        assertEquals(new TrucoResult(HandScore.of(1), null), result);
    }

    @Test
    @DisplayName("Should return no winner of call starting on 12")
    void shouldReturnNoWinnerForCallStartingOn12(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(HandScore.of(12));
        assertAll(
                () -> assertEquals(HandScore.of(12), result.getScore()),
                () -> assertEquals(Optional.empty(), result.getWinner())
        );
    }

    @Test
    @DisplayName("Should return no winner if no one is asking for truco")
    void shouldReturnNoWinnerIfNoOneIsAskingForTruco(){
        TrucoResult result = sut.handle(HandScore.of(3));
        assertEquals(new TrucoResult(HandScore.of(3), null), result);
    }
}