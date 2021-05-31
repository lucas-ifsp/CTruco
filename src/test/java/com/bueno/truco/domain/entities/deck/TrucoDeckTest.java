package com.bueno.truco.domain.entities.deck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TrucoDeckTest {

    @Test
    void shouldCreateDeckWith40Cards() {
        TrucoDeck deck = new TrucoDeck();
        Assertions.assertEquals(40, deck.size());
    }

    @Test
    void shouldCreateSortedDeck() {
        TrucoDeck deck = new TrucoDeck();
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        Assertions.assertEquals(firstEight, deck.take(8));
    }

    @Test
    void shouldGetShuffledCards() {
        TrucoDeck deck = new TrucoDeck();
        deck.shuffle();
        List<Card> firstEight = List.of(
                new Card(1, Suit.DIAMONDS), new Card(1, Suit.SPADES), new Card(1, Suit.HEARTS), new Card(1, Suit.CLUBS),
                new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(2, Suit.HEARTS), new Card(2, Suit.CLUBS));
        Assertions.assertNotEquals(firstEight, deck.take(8));
    }

    @Test
    void shouldNotContainEightsNinesTens() {
        TrucoDeck deck = new TrucoDeck();
        List<Card> fullDeck = deck.take(40);
        Assertions.assertFalse(fullDeck.contains(new Card(8, Suit.SPADES)));
        Assertions.assertFalse(fullDeck.contains(new Card(9, Suit.CLUBS)));
        Assertions.assertFalse(fullDeck.contains(new Card(10, Suit.HEARTS)));
    }
}