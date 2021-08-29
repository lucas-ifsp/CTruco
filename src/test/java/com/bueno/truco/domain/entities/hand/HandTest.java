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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @BeforeAll
    static void init(){
        Logger.getLogger(Game.class.getName()).setLevel(Level.OFF);
    }

    @BeforeEach
    void setUp() {
        sut  = new Hand(p1,p2);//new Card(7, Suit.CLUBS)
        //when(sut.getVira()).thenReturn(new Card(7, Suit.CLUBS));
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

        sut.playNewRound();
        sut.playNewRound();
        sut.checkForWinnerAfterSecondRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should get correct last round winner")
    void shouldGetCorrectLastRoundWinner(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES));
        sut.playNewRound();
        assertEquals(p1, sut.getLastRoundWinner().orElse(null));
    }

    @Test
    @DisplayName("Should win hand tying first and winning second")
    void shouldWinHandTyingFirstAndWinningSecond(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card(4,Suit.SPADES));

        sut.playNewRound();
        sut.playNewRound();
        sut.checkForWinnerAfterSecondRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should win hand winning first and tying second")
    void shouldWinHandWinningFirstAndTyingSecond(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES)).thenReturn(new Card(3,Suit.CLUBS));

        sut.playNewRound();
        sut.playNewRound();
        sut.checkForWinnerAfterSecondRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should draw hand with three tied rounds")
    void shouldDrawHandWithThreeTiedRounds(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(2,Suit.SPADES)).thenReturn(new Card(1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card(2,Suit.CLUBS)).thenReturn(new Card(1, Suit.CLUBS));

        sut.playNewRound();
        sut.playNewRound();
        sut.playNewRound();
        sut.checkForWinnerAfterThirdRound();

        assertNull(getWinner(sut));
    }

    @Test
    @DisplayName("Should win hand by best of three")
    void shouldWinHandByBestOfThree(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(2,Suit.SPADES)).thenReturn(new Card(1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(2,Suit.CLUBS)).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card('K', Suit.CLUBS));

        sut.playNewRound();
        sut.playNewRound();
        sut.playNewRound();
        sut.checkForWinnerAfterThirdRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should win hand winning first and tying third")
    void shouldWinHandWinningFirstAndTyingThird(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(2,Suit.SPADES)).thenReturn(new Card(1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(2,Suit.CLUBS)).thenReturn(new Card(3,Suit.CLUBS)).thenReturn(new Card(1, Suit.CLUBS));

        sut.playNewRound();
        sut.playNewRound();
        sut.playNewRound();
        sut.checkForWinnerAfterThirdRound();

        assertEquals(p1, getWinner(sut));
    }

    @Test
    @DisplayName("Should throw playing a forth round")
    void shouldThrowPlayingAForthRound(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES));
        sut.playNewRound();
        sut.playNewRound();
        sut.playNewRound();
        Assertions.assertThrows(GameRuleViolationException.class, () -> sut.playNewRound());
    }

    @Test
    @DisplayName("Should store played hands")
    void shouldStorePlayedHands(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES));
        sut.playNewRound();
        sut.playNewRound();
        assertEquals(2, sut.getRoundsPlayed().size());
    }

    @Test
    @DisplayName("Should store open cards")
    void shouldStoreOpenCards(){
        when(p1.playCard()).thenReturn(new Card(3,Suit.SPADES)).thenReturn(new Card(3,Suit.HEARTS));
        when(p2.playCard()).thenReturn(new Card(4,Suit.SPADES)).thenReturn(new Card(4,Suit.HEARTS));
        sut.playNewRound();
        sut.playNewRound();
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
        return handResult.flatMap(HandResult::getWinner).orElse(null);
    }
}