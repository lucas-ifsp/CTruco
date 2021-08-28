package com.bueno.truco.domain.entities.round;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.hand.Hand;
import com.bueno.truco.domain.entities.hand.HandScore;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.entities.truco.Truco;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoundTest {

    @Mock
    private Player p1;
    @Mock
    private Player p2;
    @Mock
    private Hand hand;

    @BeforeEach
     void setUp(){
        Logger.getLogger(Round.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Truco.class.getName()).setLevel(Level.OFF);
    }

    @ParameterizedTest
    @CsvSource({"4,5,6,5", "5,4,6,5", "7,1,5,1", "1,3,5,3"})
    @DisplayName("Should return correct winner for non manilhas")
    void shouldReturnCorrectWinnerCardForNonManilhas(int card1, int card2, int vira, int winner) {
        when(p1.playCard()).thenReturn(new Card(card1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(card2, Suit.SPADES));
        when(hand.getVira()).thenReturn(new Card(vira, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertEquals(new Card(winner, Suit.SPADES), round.getHighestCard().orElse(null));
    }

    @ParameterizedTest
    @CsvSource({"4,DIAMONDS,4,HEARTS,3,4,HEARTS", "5,CLUBS,5,HEARTS,4,5,CLUBS",
            "13,SPADES,13,HEARTS,12,13,HEARTS", "13,SPADES,12,HEARTS,12,13,SPADES"})
    @DisplayName("Should return correct winner card for manilhas")
    void shouldReturnCorrectWinnerCardForManilhas(int rank1, Suit suit1, int rank2, Suit suit2, int vira, int winnerRank, Suit winnerSuit) {
        when(p1.playCard()).thenReturn(new Card(rank1, suit1));
        when(p2.playCard()).thenReturn(new Card(rank2, suit2));
        when(hand.getVira()).thenReturn(new Card(vira, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertEquals(new Card(winnerRank, winnerSuit), round.getHighestCard().orElse(null));
    }

    @Test
    @DisplayName("Should have round winner card if opponent runs")
    void shouldHaveRoundWinnerIfOpponentRuns() {
        when(hand.getScore()).thenReturn(HandScore.of(1));
        when(p1.requestTruco()).thenReturn(true);
        when(p2.getTrucoResponse(HandScore.of(3))).thenReturn(-1);

        var round = new Round(p1, p2, hand);
        round.play();
        assertNotNull(round.getWinner());
    }

    @Test
    @DisplayName("Should draw when comparing equal non manilha ranks")
    void shouldDrawWhenComparingEqualNonManilhaRanks() {
        when(p1.playCard()).thenReturn(new Card(4, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4, Suit.CLUBS));
        when(hand.getVira()).thenReturn(new Card(6, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertTrue(round.getWinner().isEmpty());
    }

    @Test
    @DisplayName("Should not draw when comparing equal manilha ranks")
    void shouldNotDrawWhenComparingEqualManilhaRanks() {
        when(p1.playCard()).thenReturn(new Card(4, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(4, Suit.CLUBS));
        when(hand.getVira()).thenReturn(new Card(3, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertEquals(p2, round.getWinner().get());
    }

    @Test
    @DisplayName("Should throw if constructor parameter  is null")
    void shouldThrowIfConstructorParameterIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Round(p1, p2, null));
    }

    @Test
    @DisplayName("Should throw if round card is null")
    void shouldThrowIfRoundCardIsNull() {
        when(hand.getScore()).thenReturn(HandScore.of(1));
        var round = new Round(p1, p2, hand);
        assertThrows(GameRuleViolationException.class, round::play);
    }

    @ParameterizedTest
    @CsvSource({"4,4,5", "4,5,4", "5,4,4"})
    @DisplayName("Should throw if round hs duplicated cards")
    void shouldThrowIfRoundHasDuplicatedCard(int rank1, int rank2, int rankVira) {
        when(p1.playCard()).thenReturn(new Card(rank1, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(rank2, Suit.SPADES));
        when(hand.getVira()).thenReturn(new Card(rankVira, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        assertThrows(GameRuleViolationException.class, round::play);
    }
}