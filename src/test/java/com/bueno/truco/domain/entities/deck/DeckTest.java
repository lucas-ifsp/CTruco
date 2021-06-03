package com.bueno.truco.domain.entities.deck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class DeckTest {

    private Deck trucoDeck;

    @BeforeEach
    void setUp() {
        trucoDeck = new Deck();
    }

    @AfterEach
    void tearDown() {
        trucoDeck = null;
    }

    @Test
    void shouldCreateDeckWith40Cards() {
        Assertions.assertEquals(40, trucoDeck.size());
    }

    @Test
    void shouldCreateSortedDeck() {
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        Assertions.assertEquals(firstEight, trucoDeck.take(8));
    }

    @Test
    void shouldGetShuffledCards() {
        trucoDeck.shuffle();
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        Assertions.assertNotEquals(firstEight, trucoDeck.take(8));
    }

}