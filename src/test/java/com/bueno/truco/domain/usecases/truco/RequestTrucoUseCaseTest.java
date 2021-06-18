package com.bueno.truco.domain.usecases.truco;

import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestTrucoUseCaseTest {

    private RequestTrucoUseCase sut;
    private Player runner;
    private Player accepter;
    private Player caller;
    private Player callerRunner;

    @BeforeEach
    void setUp() {
        sut = new RequestTrucoUseCase();
        runner = new RunnerMock("Runner");
        accepter = new AccepterMock("Accepter");
        caller = new CallerMock("Caller");
        callerRunner = new CallerRunnerMock("CallerRunner");
    }

    @AfterEach
    void tearDown() {
        sut = null;
        runner = null;
        accepter = null;
        caller = null;
        callerRunner = null;
    }

    @Test
    void shouldCallerBeWinnerOfOnePoint(){
        TrucoResult result = sut.handle(caller, runner, 1);
        Assertions.assertEquals(new TrucoResult(1, caller), result);
    }

    @Test
    void shouldCallerBeWinnerOfThreePoints(){
        TrucoResult result = sut.handle(caller, runner, 3);
        Assertions.assertEquals(new TrucoResult(3, caller), result);
    }

    @Test
    void shouldReturnNoWinnerForCallStatingOn12(){
        TrucoResult result = sut.handle(caller, runner, 12);
        Assertions.assertEquals(new TrucoResult(12, null), result);
    }

    @Test
    void shouldThrowIfHandPointsAreInvalid(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.handle(caller, runner, 2));
    }

    @Test
    void shouldThrowIfRequesterIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.handle(null, runner, 1));
    }

    @Test
    void shouldThrowIfResponderIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.handle(caller, null, 1));
    }

    @Test
    void shouldHandWorthSixPointsWithBestPlayerHaving6Points(){
        caller.incrementScoreBy(6);
        TrucoResult result = sut.handle(caller, caller, 1);
        Assertions.assertEquals(new TrucoResult(6, null), result);
    }

    @Test
    void shouldLoseForCallingWith11Points(){
        caller.incrementScoreBy(9);
        caller.incrementScoreBy(1);
        caller.incrementScoreBy(1);
        TrucoResult result = sut.handle(caller, accepter, 1);
        Assertions.assertEquals(new TrucoResult(12, accepter), result);
    }

    @Test
    void shouldReturnNoWinnerIfNoOneIsCalling(){
        TrucoResult result = sut.handle(runner, runner, 3);
        Assertions.assertEquals(new TrucoResult(3, null), result);
    }

    @Test
    void shouldReturnNoWinnerAndMatchWorthSixPoints(){
        TrucoResult result = sut.handle(caller, accepter, 3);
        Assertions.assertEquals(new TrucoResult(6, null), result);
    }

    @Test
    void shouldReturnNoWinnerAndMatchWorth12Points(){
        TrucoResult result = sut.handle(caller, caller, 3);
        Assertions.assertEquals(new TrucoResult(12, null), result);
    }

    @Test
    void shouldReturnWinnerAfterCallAndThenRun(){
        TrucoResult result = sut.handle(callerRunner, caller, 3);
        Assertions.assertEquals(new TrucoResult(6, caller), result);
    }
}