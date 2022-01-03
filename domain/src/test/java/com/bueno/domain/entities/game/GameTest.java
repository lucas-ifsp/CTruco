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

package com.bueno.domain.entities.game;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.player.util.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {

    private Game sut;
    @Mock private Player player1;
    @Mock private Player player2;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

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
                () -> assertEquals(1, sut.getHands().size()),
                () -> assertEquals(player1, sut.getPlayer1()),
                () -> assertEquals(player2, sut.getPlayer2())
        );
    }

    @Test
    @DisplayName("Should correctly prepare hand")
    void shouldCorrectlyPrepareHand(){
        assertAll(
                () -> assertEquals(player1, sut.getFirstToPlay()),
                () -> assertEquals(player2, sut.getLastToPlay()),
                () -> assertNotEquals(sut.getFirstToPlay(), sut.getLastToPlay()),
                () -> assertEquals(1, sut.handsPlayed())
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

    @Test
    @DisplayName("Should get intel from current hand")
    void shouldGetIntelFromCurrentHand() {
        assertNotNull(sut.getIntel());
    }

    @Test
    @DisplayName("Should get all history when base intel is null")
    void shouldGetAllHistoryWhenBaseIntelIsNull() {
        sut.prepareNewHand();
        sut.prepareNewHand();
        assertEquals(3, sut.getIntelSince(null).size());
    }

    @Test
    @DisplayName("Should correctly get intel in the same hand")
    void shouldCorrectlyGetIntelInTheSameHand() {
        when(player1.getCards()).thenReturn(List.of(Card.of(Rank.KING, Suit.CLUBS), Card.of(Rank.QUEEN, Suit.CLUBS)));

        sut = new Game(player1, player2);
        final Intel firstHandIntel = sut.getIntel();
        final Hand hand = sut.currentHand();

        hand.playFirstCard(player1, Card.of(Rank.KING, Suit.CLUBS));
        hand.playSecondCard(player2, Card.closed());
        hand.playFirstCard(player1, Card.of(Rank.QUEEN, Suit.CLUBS));
        assertEquals(3, sut.getIntelSince(firstHandIntel).size());
    }

    @Test
    @DisplayName("Should correctly get intel between hands")
    void shouldCorrectlyGetIntelBetweenHands() {
        when(player1.getCards()).thenReturn(List.of(Card.of(Rank.KING, Suit.CLUBS), Card.of(Rank.KING, Suit.SPADES)));

        sut = new Game(player1, player2);
        final Intel firstHandIntel = sut.getIntel();
        final Hand hand = sut.currentHand();

        hand.playFirstCard(player1, Card.of(Rank.KING, Suit.CLUBS));
        hand.playSecondCard(player2, Card.closed());
        hand.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
        hand.playSecondCard(player2, Card.closed());

        sut.prepareNewHand();
        final Hand newHand = sut.currentHand();
        newHand.playFirstCard(player2, Card.closed());

        assertEquals(6, sut.getIntelSince(firstHandIntel).size());
    }
}