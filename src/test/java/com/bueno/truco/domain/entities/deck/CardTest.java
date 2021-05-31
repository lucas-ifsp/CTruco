package com.bueno.truco.domain.entities.deck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CardTest {

    @Test
    void shouldCreateCard(){
        Card card = new Card(7, Suit.SPADES);
        Assertions.assertEquals(7, card.getRank());
        Assertions.assertEquals(Suit.SPADES, card.getSuit());
    }

    @Test
    void shouldCreateCardFromRankName(){
        Card card = new Card( '7', Suit.SPADES);
        Assertions.assertEquals(7, card.getRank());
        card = new Card( 'K', Suit.SPADES);
        Assertions.assertEquals(13, card.getRank());
    }

    @Test
    void shouldConvertToString(){
        Assertions.assertEquals("7 of Spades", new Card(7, Suit.SPADES).toString());
    }

    @Test
    void shouldToStringAQJK(){
        Assertions.assertEquals("A of Hearts", new Card(1, Suit.HEARTS).toString());
        Assertions.assertEquals("Q of Hearts", new Card(11, Suit.HEARTS).toString());
        Assertions.assertEquals("J of Hearts", new Card(12, Suit.HEARTS).toString());
        Assertions.assertEquals("K of Hearts", new Card(13, Suit.HEARTS).toString());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 14})
    void shouldNotCreateCardWithInvalidRank(int rank){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Card(rank, Suit.CLUBS));
    }

    @Test
    void shouldCorrectlyBeEquals(){
        Assertions.assertEquals(new Card(1, Suit.DIAMONDS), new Card(1, Suit.DIAMONDS));
    }
}