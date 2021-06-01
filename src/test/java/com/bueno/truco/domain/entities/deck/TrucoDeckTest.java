package com.bueno.truco.domain.entities.deck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class TrucoDeckTest {

    private TrucoDeck deck;

    @BeforeEach
    void setUp() {
        deck = new TrucoDeck();
    }

    @AfterEach
    void tearDown() {
        deck = null;
    }

    @Test
    void shouldCreateDeckWith40Cards() {
        Assertions.assertEquals(40, deck.size());
    }

    @Test
    void shouldCreateSortedDeck() {
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        Assertions.assertEquals(firstEight, deck.take(8));
    }

    @Test
    void shouldGetShuffledCards() {
        deck.shuffle();
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        Assertions.assertNotEquals(firstEight, deck.take(8));
    }

    @Test
    void shouldNotContainEightsNinesTens() {
        List<Card> fullDeck = deck.take(40);
        Assertions.assertFalse(fullDeck.contains(new Card(8, Suit.SPADES)));
        Assertions.assertFalse(fullDeck.contains(new Card(9, Suit.CLUBS)));
        Assertions.assertFalse(fullDeck.contains(new Card(10, Suit.HEARTS)));
    }
}