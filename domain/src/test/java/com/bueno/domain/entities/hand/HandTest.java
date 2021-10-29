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

package com.bueno.domain.entities.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Deck;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.round.Round;
import com.bueno.domain.entities.truco.Truco;
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
    @Mock
    private Deck deck;

    @BeforeAll
    static void init(){
        Logger.getLogger(Truco.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Hand.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Round.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Player.class.getName()).setLevel(Level.OFF);
    }

    @BeforeEach
    void setUp() {
        when(deck.takeOne()).thenReturn(new Card(7, Suit.CLUBS));
        sut  = new Hand(p1,p2, deck);
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