package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.hand.PlayHandUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandTest {

    private Hand sut;
    @Mock
    private Player p1;
    @Mock
    private Player p2;
    @Mock
    private Game game;
    @Mock
    private Round round;

    @BeforeEach
    void setUp() {
        sut = new Hand();
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    void shouldThrowIfExceedThreeRounds(){
        playRounds(3);
        Assertions.assertThrows(GameRuleViolationException.class, () -> sut.addPlayedRound(new Round(p1, p2, game)));
    }

   @Test
    void shouldWinHandWinningFirstTwoRounds(){
        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.of(p1));
        playRounds(2);
        sut.checkForWinnerAfterSecondRound();
        Player winner = getWinner();
        Assertions.assertEquals(p1, winner);
    }

    @Test
    void shouldGetCorrectLastRoundWinner(){
        when(round.getWinner()).thenReturn(Optional.of(p1));
        playRounds(1);
        Assertions.assertEquals(p1, sut.getLastRoundWinner().get());
    }

    @Test
    void shouldWinHandTyingFirstAndWinningSecond(){
        when(round.getWinner()).thenReturn(Optional.empty()).thenReturn(Optional.of(p1));
        playRounds(2);
        sut.checkForWinnerAfterSecondRound();
        Player winner = getWinner();
        Assertions.assertEquals(p1, winner);
    }

    @Test
    void shouldWinHandWinningFirstAndTyingSecond(){
        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.empty());
        playRounds(2);
        sut.checkForWinnerAfterSecondRound();
        Player winner = getWinner();
        Assertions.assertEquals(p1, winner);
    }

    @Test
    void shouldDrawHandWithThreeTiedRounds(){
        when(round.getWinner()).thenReturn(Optional.empty()).thenReturn(Optional.empty()).thenReturn(Optional.empty());
        playRounds(3);

        sut.checkForWinnerAfterSecondRound();
        Player winner = getWinner();
        Assertions.assertEquals(null, winner);

        sut.checkForWinnerAfterThirdRound();
        winner = getWinner();
        Assertions.assertEquals(null, winner);
    }

    @Test
    void shouldWinHandByBestOfThree(){
        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.of(p2));
        playRounds(3);

        sut.checkForWinnerAfterSecondRound();
        Player winner = getWinner();
        Assertions.assertEquals(null, winner);

        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.of(p1));
        sut.checkForWinnerAfterThirdRound();
        winner = getWinner();
        Assertions.assertEquals(p1, winner);
    }

    @Test
    void shouldWinWinningFirstAndTyingThird(){
        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.of(p2));
        playRounds(3);
        sut.checkForWinnerAfterSecondRound();
        Player winner = getWinner();
        Assertions.assertEquals(null, winner);

        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.empty());
        sut.checkForWinnerAfterThirdRound();
        winner = getWinner();
        Assertions.assertEquals(p1, winner);
    }

    private void playRounds(int number) {
        for (int i = 0; i < number; i++) {
            sut.addPlayedRound(round);
        }
    }

    private Player getWinner() {
        Optional<HandResult> handResult = sut.getResult();
        return handResult
                .map(hr -> hr.getWinner().orElse(null))
                .orElse(null);
    }
}