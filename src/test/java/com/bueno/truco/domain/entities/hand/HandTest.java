package com.bueno.truco.domain.entities.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.hand.Hand;
import com.bueno.truco.domain.entities.hand.HandResult;
import com.bueno.truco.domain.entities.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @BeforeEach
    void setUp() {
        when(game.getPlayer1()).thenReturn(p1);
        when(game.getPlayer2()).thenReturn(p2);
        sut  = new Hand(game, new Card(7, Suit.CLUBS));
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should win hand winning first two rounds")
    void shouldWinHandWinningFirstTwoRounds(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES)).thenReturn(new Card(4,Suit.HEARTS));

        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        sut.checkForWinnerAfterSecondRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Shoudl get correct last round winner")
    void shouldGetCorrectLastRoundWinner(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES));
        sut.playNewRound(p1, p2);
        assertEquals(p1, sut.getLastRoundWinner().get());
    }

    @Test
    @DisplayName("Should win hand tying first and winning second")
    void shouldWinHandTyingFirstAndWinningSecond(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card(4,Suit.SPADES));

        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        sut.checkForWinnerAfterSecondRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should win hand winning first and tying second")
    void shouldWinHandWinningFirstAndTyingSecond(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES)).thenReturn(new Card(3,Suit.CLUBS));

        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        sut.checkForWinnerAfterSecondRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should draw hand with three tied rounds")
    void shouldDrawHandWithThreeTiedRounds(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(2,Suit.SPADES)).thenReturn(new Card(1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card(2,Suit.CLUBS)).thenReturn(new Card(1, Suit.CLUBS));

        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        sut.checkForWinnerAfterThirdRound();

        assertNull(getWinner(sut));
    }

    @Test
    @DisplayName("Should win hand by best of three")
    void shouldWinHandByBestOfThree(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(2,Suit.SPADES)).thenReturn(new Card(1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(2,Suit.CLUBS)).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card('K', Suit.CLUBS));

        sut.playNewRound(p1, p2);
        sut.playNewRound(p2, p1);
        sut.playNewRound(p1, p2);
        sut.checkForWinnerAfterThirdRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should win hand winning first and tying third")
    void shouldWinHandWinningFirstAndTyingThird(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(2,Suit.SPADES)).thenReturn(new Card(1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(2,Suit.CLUBS)).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card(1, Suit.CLUBS));

        sut.playNewRound(p1, p2);
        sut.playNewRound(p2, p1);
        sut.playNewRound(p1, p2);
        sut.checkForWinnerAfterThirdRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should throw playing a forth round")
    void shouldThrowPlayingAForthRound(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES));
        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        Assertions.assertThrows(GameRuleViolationException.class, () -> sut.playNewRound(p1, p2));
    }

    @Test
    @DisplayName("Should store played hands")
    void shouldStorePlayedHands(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES));
        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        assertEquals(2, sut.getRoundsPlayed().size());
    }

    @Test
    @DisplayName("Should store open cards")
    void shouldStoreOpenCards(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES)).thenReturn(new Card(4,Suit.HEARTS));
        sut.playNewRound(p1, p2);
        sut.playNewRound(p1, p2);
        assertEquals(5, sut.getOpenCards().size());
    }

    @Test
    @DisplayName("Should have winner if hand result has winner")
    void shouldHaveWinnerIfHandResultHasWinner(){
        sut.setResult(new HandResult(p1, HandScore.of(3)));
        Assertions.assertTrue(sut.hasWinner());
        assertEquals(p1, getWinner(sut));
    }

    private Player getWinner(Hand hand) {
        Optional<HandResult> handResult = hand.getResult();
        return handResult.map(hr -> hr.getWinner().orElse(null))
                .orElse(null);
    }
}