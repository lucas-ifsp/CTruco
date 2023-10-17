/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.spi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;

class CardToPlayTest {

    @Test
    @DisplayName("Should correctly create CardToPlay from card")
    void shouldCorrectlyCreateCardToPlayFromCard() {
        final TrucoCard card = TrucoCard.of(TWO, CLUBS);
        final CardToPlay sut = CardToPlay.of(card);
        assertAll(
                () -> assertEquals(card, sut.value()),
                () -> assertFalse(sut.isDiscard())
        );
    }

    @Test
    @DisplayName("Should correctly create discard")
    void shouldCorrectlyCreateDiscard() {
        final TrucoCard card = TrucoCard.of(TWO, CLUBS);
        final CardToPlay sut = CardToPlay.discard(card);
        assertAll(
                () -> assertEquals(TrucoCard.closed(), sut.value()),
                () -> assertTrue(sut.isDiscard())
        );
    }

    @Test
    @DisplayName("Should content of non discard be the underlying TrucoCard")
    void shouldContentOfNonDiscardBeTheUnderlyingTrucoCard() {
        final TrucoCard card = TrucoCard.of(TWO, CLUBS);
        final CardToPlay sut = CardToPlay.of(card);
        assertEquals(card, sut.content());
    }

    @Test
    @DisplayName("Should content of discard be the underlying TrucoCard")
    void shouldContentOfDiscardBeTheUnderlyingTrucoCard() {
        final TrucoCard card = TrucoCard.of(TWO, CLUBS);
        final CardToPlay sut = CardToPlay.discard(card);
        assertEquals(card, sut.content());
    }

    @Test
    @DisplayName("Should throw if tries to create a card to played from a null TrucoCard")
    void shouldThrowIfTriesToCreateACardToPlayedFromANullTrucoCard() {
        assertThrows(NullPointerException.class, () -> CardToPlay.of(null));
    }

    @Test
    @DisplayName("Should throw if tries to create a card to discarded from a null TrucoCard")
    void shouldThrowIfTriesToCreateACardToDiscardedFromANullTrucoCard() {
        assertThrows(NullPointerException.class, () -> CardToPlay.discard(null));
    }

    @Test
    @DisplayName("Should CardToPlay objects be equal if bot content and side are equals")
    void shouldCardToPlayObjectsBeEqualIfBotContentAndSideAreEquals() {
        final TrucoCard card = TrucoCard.of(TWO, HEARTS);
        assertEquals(CardToPlay.of(card), CardToPlay.of(card));
    }

    @Test
    @DisplayName("Should CardToPlay objects not be equals if one is open and the other is discarded")
    void shouldCardToPlayObjectsNotBeEqualsIfOneIsOpenAndTheOtherIsDiscarded() {
        final TrucoCard card = TrucoCard.of(TWO, HEARTS);
        assertNotEquals(CardToPlay.of(card), CardToPlay.discard(card));
    }

    @Test
    @DisplayName("Should CardToPlay objects not be equals if one they hold different TrucoCard objects")
    void shouldCardToPlayObjectsNotBeEqualsIfTheyHoldDifferentTrucoCardObjects() {
        final TrucoCard card1 = TrucoCard.of(TWO, HEARTS);
        final TrucoCard card2 = TrucoCard.of(TWO, CLUBS);
        assertNotEquals(CardToPlay.of(card1), CardToPlay.discard(card2));
    }
}