package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {

    private Game sut;
    @Mock
    private Player player1;
    @Mock
    private Player player2;

    @BeforeEach
    void setUp() {
        sut = new Game(player1, player2);
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should correctly create game")
    void shouldCorrectlyCreateGame(){
        assertAll(
                () -> assertEquals(0, sut.getHands().size()),
                () -> assertEquals(player1, sut.getPlayer1()),
                () -> assertEquals(player2, sut.getPlayer2())
        );
    }

    @Test
    @DisplayName("Should correctly deal cards")
    void shouldCorrectlyDealCards(){
        sut.dealCards();
        sut.prepareNewHand();
        verify(player1).setCards(anyList());
        verify(player2).setCards(anyList());
    }

    @Test
    @DisplayName("Should correctly prepare hand")
    void shouldCorrectlyPrepareHand(){
        final Hand hand = sut.prepareNewHand();
        assertAll(
                () -> assertEquals(player1, sut.getFirstToPlay()),
                () -> assertEquals(player2, sut.getLastToPlay()),
                () -> assertNotEquals(sut.getFirstToPlay(), sut.getLastToPlay()),
                () -> assertEquals(hand, sut.getCurrentHand())
        );
    }

    @Test
    @DisplayName("Should have winner when game ends")
    void shouldGetWinnerWhenGameEnds(){
        when(player1.getScore()).thenReturn(12);
        assertEquals(player1, sut.getWinner().orElse(null));
    }

    @Test
    @DisplayName("Should have no winner before game ends")
    void shouldGetNoWinnerBeforeGameEnds(){
        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(11);
        assertTrue(sut.getWinner().isEmpty());
    }

    @Test
    @DisplayName("Should be mao de onze if only one player has 11 points")
    void shouldBeMaoDeOnzeIfOnlyOnePlayerHas11Points() {
        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(3);
        assertTrue(sut.isMaoDeOnze());
    }

    @Test
    @DisplayName("Should not be mao de onze if both players have 11 points")
    void shouldNotBeMaoDeOnzeIfBothPlayersHave11Points() {
        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(11);
        assertFalse(sut.isMaoDeOnze());
    }

    @Test
    @DisplayName("Should not be mao de onze if no player has 11 points")
    void shouldNotBeMaoDeOnzeIfNoPlayerHas11Points() {
        when(player1.getScore()).thenReturn(10);
        when(player2.getScore()).thenReturn(8);
        assertFalse(sut.isMaoDeOnze());
    }


}