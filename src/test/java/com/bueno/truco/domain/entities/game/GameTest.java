package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {

    private Game sut;
    @Mock
    private Player player1;
    @Mock
    private Player player2;
    @Mock
    private Hand hand;
    @Mock
    private HandResult handResult;

    @BeforeEach
    void setUp() {
        sut = new Game(player1, player2);
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    void shouldCorrectlyCreateGame(){
        Assertions.assertEquals(0, sut.getHands().size());
        Assertions.assertEquals(player1, sut.getPlayer1());
        Assertions.assertEquals(player2, sut.getPlayer2());
    }

    @Test
    void shouldCorrectlyDealCards(){
        sut.dealCards();
        Assertions.assertEquals(player1, sut.getFirstToPlay());
        Assertions.assertEquals(player2, sut.getLastToPlay());
        Assertions.assertFalse(sut.getFirstToPlay().equals(sut.getLastToPlay()));
        Assertions.assertNotNull(sut.getCurrentVira());
    }

    @Test
    void shouldCorrectlyAddHand(){
        when(handResult.getPoints()).thenReturn(6);
        when(handResult.getWinner()).thenReturn(Optional.of(player1)).thenReturn(Optional.of(player2));
        when(hand.getResult()).thenReturn(Optional.of(handResult));
        sut.updateGameWithLastHand(hand);
        sut.updateGameWithLastHand(hand);
        Assertions.assertEquals(2, sut.getHands().size());
    }

    @Test
    void shouldGetWinnerIfGameHasEnded(){
        when(player1.getScore()).thenReturn(12);
        Assertions.assertEquals(player1, sut.getWinner().get());
    }

    @Test
    void shouldGetNoWinnerDuringGame(){
        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(11);
        Assertions.assertTrue(sut.getWinner().isEmpty());
    }
}