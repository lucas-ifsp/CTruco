/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.domain.entities.round;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.truco.Truco;
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
    @CsvSource({"FOUR,FIVE,SIX,FIVE", "FIVE,FOUR,SIX,FIVE", "SEVEN,ACE,FIVE,ACE", "ACE,THREE,FIVE,THREE"})
    @DisplayName("Should return correct winner for non manilhas")
    void shouldReturnCorrectWinnerCardForNonManilhas(Rank card1Rank, Rank card2Rank, Rank viraRank, Rank winnerRank) {
        when(p1.playCard()).thenReturn(Card.of(card1Rank, Suit.SPADES));
        when(p2.playCard()).thenReturn(Card.of(card2Rank, Suit.SPADES));
        when(hand.getVira()).thenReturn(Card.of(viraRank, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertEquals(Card.of(winnerRank, Suit.SPADES), round.getHighestCard().orElse(null));
    }

    @ParameterizedTest
    @CsvSource({"FOUR,DIAMONDS,FOUR,HEARTS,THREE,FOUR,HEARTS", "FIVE,CLUBS,FIVE,HEARTS,FOUR,FIVE,CLUBS",
            "KING,SPADES,KING,HEARTS,JACK,KING,HEARTS", "KING,SPADES,JACK,HEARTS,JACK,KING,SPADES"})
    @DisplayName("Should return correct winner card for manilhas")
    void shouldReturnCorrectWinnerCardForManilhas(Rank card1Rank, Suit card1Suit, Rank card2Rank, Suit card2Suit, Rank viraRank, Rank winnerRank, Suit winnerSuit) {
        when(p1.playCard()).thenReturn(Card.of(card1Rank, card1Suit));
        when(p2.playCard()).thenReturn(Card.of(card2Rank, card2Suit));
        when(hand.getVira()).thenReturn(Card.of(viraRank, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertEquals(Card.of(winnerRank, winnerSuit), round.getHighestCard().orElse(null));
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
        when(p1.playCard()).thenReturn(Card.of(Rank.FOUR, Suit.SPADES));
        when(p2.playCard()).thenReturn(Card.of(Rank.FOUR, Suit.CLUBS));
        when(hand.getVira()).thenReturn(Card.of(Rank.SIX, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertTrue(round.getWinner().isEmpty());
    }

    @Test
    @DisplayName("Should not draw when comparing equal manilha ranks")
    void shouldNotDrawWhenComparingEqualManilhaRanks() {
        when(p1.playCard()).thenReturn(Card.of(Rank.FOUR, Suit.SPADES));
        when(p2.playCard()).thenReturn(Card.of(Rank.FOUR, Suit.CLUBS));
        when(hand.getVira()).thenReturn(Card.of(Rank.THREE, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        round.play();
        assertEquals(p2, round.getWinner().orElseThrow());
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
    @CsvSource({"FOUR,FOUR,FIVE", "FOUR,FIVE,FOUR", "FIVE,FOUR,FOUR"})
    @DisplayName("Should throw if round hs duplicated cards")
    void shouldThrowIfRoundHasDuplicatedCard(Rank card1Rank, Rank card2Rank, Rank viraRank) {
        when(p1.playCard()).thenReturn(Card.of(card1Rank, Suit.SPADES));
        when(p2.playCard()).thenReturn(Card.of(card2Rank, Suit.SPADES));
        when(hand.getVira()).thenReturn(Card.of(viraRank, Suit.SPADES));
        when(hand.getScore()).thenReturn(HandScore.of(1));

        var round = new Round(p1, p2, hand);
        assertThrows(GameRuleViolationException.class, round::play);
    }
}