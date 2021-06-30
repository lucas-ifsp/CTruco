package com.bueno.truco.domain.entities.deck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class CardTest {

    @Test
    void shouldCreateValidCard(){
        Card card = new Card(7, Suit.SPADES);
        Assertions.assertEquals(7, card.getRank());
        Assertions.assertEquals(Suit.SPADES, card.getSuit());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 8, 9, 10, 14})
    void shouldNotCreateInvalidCard(int rank){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Card(rank, Suit.CLUBS));
    }

    @Test
    void shouldNotAcceptNullSuit(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Card(7, null));
    }

    @ParameterizedTest
    @CsvSource({"7,7", "A,1", "Q,11", "J,12", "K,13", "k,13"})
    void shouldCreateCardFromValidRankName(char rankName, int rankValue){
        Card card = new Card(rankName, Suit.SPADES);
        Assertions.assertEquals(rankValue, card.getRank());
    }

    @Test
    void shouldNotCreateCardFromInvalidRankName(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Card('P', Suit.CLUBS));
    }

    @ParameterizedTest
    @CsvSource({"7,[7\u2665]", "1,[A\u2665]", "11,[Q\u2665]", "12,[J\u2665]", "13,[K\u2665]"})
    void shouldCorrectlyToStringOpenCard(int rank, String toString){
        Assertions.assertEquals(toString, new Card(rank, Suit.HEARTS).toString());
    }

    @Test
    void shouldCorrectlyToStringClosedCard() {
        Assertions.assertEquals("[Xx]", Card.getClosedCard().toString());
    }

    @Test
    void shouldClosedCardWorthLessThanWorstCard() {
        Card worstCard = new Card(4, Suit.DIAMONDS);
        Card vira = new Card(4, Suit.CLUBS);
        Assertions.assertEquals(-1, Card.getClosedCard().compareValueTo(worstCard, vira));
    }

    @Test
    void shouldSameCardBeEquals(){
        Assertions.assertEquals(new Card(1, Suit.DIAMONDS), new Card(1, Suit.DIAMONDS));
    }
}