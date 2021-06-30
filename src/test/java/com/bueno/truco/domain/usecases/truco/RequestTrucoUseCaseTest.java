package com.bueno.truco.domain.usecases.truco;

import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestTrucoUseCaseTest {

    private RequestTrucoUseCase sut;

    @Mock
    private Player p1;
    @Mock
    private Player p2;

    @BeforeEach
    void setUp() {
        sut = new RequestTrucoUseCase();
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    void shouldCallerBeWinnerOfOnePoint(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(anyInt())).thenReturn(-1);
        TrucoResult result = sut.handle(p1, p2, 1);
        Assertions.assertEquals(new TrucoResult(1, p1), result);
    }

    @Test
    void shouldCorrectlyToStringTrucoResultHavingWinner(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(anyInt())).thenReturn(-1);
        TrucoResult result = sut.handle(p1, p2, 1);
        Assertions.assertEquals("Points=1, winner=p1",result.toString());
    }

    @Test
    void shouldCorrectlyToStringTrucoResultHavingNoWinner(){
        when(p1.requestTruco()).thenReturn(false);
        TrucoResult result = sut.handle(p1, p1, 1);
        Assertions.assertEquals("Points=1, winner=No winner",result.toString());
    }

    @Test
    void shouldCallerBeWinnerOfThreePoints(){
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(anyInt())).thenReturn(-1);
        TrucoResult result = sut.handle(p1, p2, 3);
        Assertions.assertEquals(new TrucoResult(3, p1), result);
    }

    @Test
    void shouldReturnNoWinnerForCallStatingOn12(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(p1, p2, 12);
        Assertions.assertEquals(new TrucoResult(12, null), result);
    }

    @Test
    void shouldThrowIfHandPointsAreInvalid(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.handle(p1, p2, 2));
    }

    @Test
    void shouldThrowIfRequesterIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.handle(null, p2, 1));
    }

    @Test
    void shouldThrowIfResponderIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.handle(p1, null, 1));
    }

    @Test
    void shouldLimitHandPointsAccordingToLosingPlayer(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(anyInt())).thenReturn(1);
        when(p1.getScore()).thenReturn(5);

        when(p2.getTrucoResponse(anyInt())).thenReturn(1);
        when(p2.getScore()).thenReturn(8);

        TrucoResult result = sut.handle(p1, p2, 1);
        Assertions.assertEquals(new TrucoResult(9, null), result);
    }

    @Test
    void shouldLoseForCallingWith11Points(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getScore()).thenReturn(11);
        TrucoResult result = sut.handle(p1, p2, 1);
        Assertions.assertEquals(new TrucoResult(12, p2), result);
    }

    @Test
    void shouldReturnNoWinnerIfNoOneIsCalling(){
        TrucoResult result = sut.handle(p1, p2, 3);
        Assertions.assertEquals(new TrucoResult(3, null), result);
    }

    @Test
    void shouldReturnNoWinnerAndMatchWorthSixPoints(){
        when(p1.requestTruco()).thenReturn(true);
        TrucoResult result = sut.handle(p1, p2, 3);
        Assertions.assertEquals(new TrucoResult(6, null), result);
    }

    @Test
    void shouldReturnNoWinnerAndMatchWorth12Points(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(anyInt())).thenReturn(1);
        when(p2.getTrucoResponse(anyInt())).thenReturn(1);
        TrucoResult result = sut.handle(p1, p2, 3);
        Assertions.assertEquals(new TrucoResult(12, null), result);
    }

    @Test
    void shouldReturnWinnerAfterCallAndThenRun(){
        when(p1.requestTruco()).thenReturn(true);
        when(p1.getTrucoResponse(anyInt())).thenReturn(-1);
        when(p2.getTrucoResponse(anyInt())).thenReturn(1);
        TrucoResult result = sut.handle(p1, p2, 3);
        Assertions.assertEquals(new TrucoResult(6, p2), result);
    }
}