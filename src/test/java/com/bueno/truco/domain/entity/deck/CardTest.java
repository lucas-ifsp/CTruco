package com.bueno.truco.domain.entity.deck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldCreateACard(){
        Card card = new Card(7, Suit.SPADES);
        Assertions.assertEquals(card.getRank(), 7);
        Assertions.assertEquals(card.getSuit(), Suit.SPADES);
    }

    @Test
    void shouldConvertToString(){
        Assertions.assertEquals(new Card(7, Suit.SPADES).toString(), "7 of Spades");
    }
}