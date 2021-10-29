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

package com.bueno.domain.entities.deck;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardTest {

    @Nested
    @DisplayName("Should allow")
    class ShouldAllow{
        @Test
        @DisplayName("Creating a card with valid rank and suit")
        void creatingCardWithValidRankAndSuit(){
            Card card = new Card(7, Suit.SPADES);
            assertEquals(7, card.getRank());
            assertEquals(Suit.SPADES, card.getSuit());
        }

        @ParameterizedTest(name = "[{index}]: {1} as rank name for {0}")
        @DisplayName("creating cards from valid ranks names")
        @CsvSource({"7,7", "A,1", "Q,11", "J,12", "K,13", "k,13"})
        void creatingCardsFromValidRankNames(char rankName, int rankValue){
            Card card = new Card(rankName, Suit.SPADES);
            assertEquals(rankValue, card.getRank());
        }
    }

    @Nested
    @DisplayName("Should not allow")
    class ShouldNotAllow{
        @ParameterizedTest(name = "[{index}]: {0} as rank")
        @DisplayName("creating cards with invalid ranks")
        @ValueSource(ints = {-1, 8, 9, 10, 14})
        void creatingCardsWithInvalidRank(int rank){
            assertThrows(IllegalArgumentException.class, () -> new Card(rank, Suit.CLUBS));
        }

        @Test
        @DisplayName("creating a card with null suit")
        void creatingCardWithNullSuit(){
            assertThrows(IllegalArgumentException.class, () -> new Card(7, null));
        }

        @Test
        @DisplayName("creating a card from invalid rank name")
        void creatingCardFromInvalidRankName(){
            assertThrows(IllegalArgumentException.class, () -> new Card('P', Suit.CLUBS));
        }
    }

    @ParameterizedTest(name = "[{index}]: rank {0} and suit {1} = {2}")
    @DisplayName("Should correctly toString() open cards")
    @CsvSource({"7,DIAMONDS,[7\u2666]", "1,HEARTS,[A\u2665]", "11,CLUBS,[Q\u2663]", "12,SPADES,[J\u2660]", "13,SPADES,[K\u2660]"})
    void shouldCorrectlyToStringOpenCard(int rank, Suit suit, String toString){
        assertEquals(toString, new Card(rank, suit).toString());
    }

    @Test
    @DisplayName("Should correctly toString() closed cards")
    void shouldCorrectlyToStringClosedCard() {
        assertEquals("[Xx]", Card.getClosedCard().toString());
    }

    @Test
    @DisplayName("Should a closed card worth less than worst possible card")
    void shouldClosedCardWorthLessThanWorstCard() {
        Card worstCard = new Card(4, Suit.DIAMONDS);
        Card vira = new Card(4, Suit.CLUBS);
        assertEquals(-1, Card.getClosedCard().compareValueTo(worstCard, vira));
    }

    @Test
    @DisplayName("Should same cards be equals")
    void shouldSameCardsBeEquals(){
        assertEquals(new Card(1, Suit.DIAMONDS), new Card(1, Suit.DIAMONDS));
    }
}