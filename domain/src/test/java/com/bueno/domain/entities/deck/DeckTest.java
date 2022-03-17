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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @AfterEach
    void tearDown() {
        deck = null;
    }

    @Test
    @DisplayName("Should create truco deck with 40 cards")
    void shouldCreateDeckWith40Cards() {
        assertEquals(40, deck.size());
    }

    @Test
    @DisplayName("Should create sorted deck")
    void shouldCreateSortedDeck() {
        List<Card> firstEight = List.of(
                Card.of(Rank.FOUR, Suit.DIAMONDS), Card.of(Rank.FOUR, Suit.SPADES), Card.of(Rank.FOUR, Suit.HEARTS), Card.of(Rank.FOUR, Suit.CLUBS),
                Card.of(Rank.FIVE, Suit.DIAMONDS), Card.of(Rank.FIVE, Suit.SPADES), Card.of(Rank.FIVE, Suit.HEARTS), Card.of(Rank.FIVE, Suit.CLUBS));
        assertEquals(firstEight, deck.take(8));
    }

    @Test
    @DisplayName("Should get shuffled cards after shuffling")
    void shouldGetShuffledCardsAfterShuffling() {
        deck.shuffle();
        List<Card> firstEight = List.of(
                Card.of(Rank.ACE, Suit.DIAMONDS), Card.of(Rank.ACE, Suit.SPADES), Card.of(Rank.ACE, Suit.HEARTS), Card.of(Rank.ACE, Suit.CLUBS),
                Card.of(Rank.TWO, Suit.DIAMONDS), Card.of(Rank.TWO, Suit.SPADES), Card.of(Rank.TWO, Suit.HEARTS), Card.of(Rank.TWO, Suit.CLUBS));
        assertNotEquals(firstEight, deck.take(8));
    }

    @Test
    @DisplayName("Should be able to deal multiple cards ")
    void shouldDealMultipleCorrectly() {
        List<Card> cards = deck.take(3);
        assertAll(
                () -> assertEquals(3, cards.size()),
                () -> assertEquals(37, deck.size())
        );
    }

    @Test
    @DisplayName("Should be able to deal a single card")
    void shouldDealSingleCard() {
        assertNotNull(deck.takeOne());
    }
}