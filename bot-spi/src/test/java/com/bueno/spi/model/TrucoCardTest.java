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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;

class TrucoCardTest {

    @Test
    @DisplayName("Should create card from valid rank and suit")
    void shouldCreateCardFromValidRankAndSuit() {
        final TrucoCard card = TrucoCard.of(THREE, DIAMONDS);
        assertAll(
                () -> assertEquals(THREE, card.getRank()),
                () -> assertEquals(DIAMONDS, card.getSuit())
        );
    }

    @Test
    @DisplayName("Should create closed card")
    void shouldCreateClosedCard() {
        final TrucoCard card = TrucoCard.closed();
        assertAll(
                () -> assertEquals(CardRank.HIDDEN, card.getRank()),
                () -> assertEquals(CardSuit.HIDDEN, card.getSuit())
        );
    }

    @Test
    @DisplayName("Should throw if try to create card if null parameters")
    void shouldThrowIfTryToCreateCardIfNullParameters() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> TrucoCard.of(THREE, null)),
                () -> assertThrows(NullPointerException.class, () -> TrucoCard.of(null, DIAMONDS))
        );
    }

    @Test
    @DisplayName("Should throw if tries to create a card if hidden rank and open suit or vice versa")
    void shouldThrowIfTriesToCreateACardIfHiddenRankAndOpenSuitOrViceVersa() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> TrucoCard.of(THREE, CardSuit.HIDDEN)),
                () -> assertThrows(IllegalArgumentException.class, () -> TrucoCard.of(CardRank.HIDDEN, DIAMONDS))
        );
    }

    @Test
    @DisplayName("Should a closed card worth less than worst possible card")
    void shouldClosedCardWorthLessThanWorstCard() {
        TrucoCard worstCard = TrucoCard.of(FOUR, DIAMONDS);
        TrucoCard vira = TrucoCard.of(FOUR, CLUBS);
        assertEquals(-1, TrucoCard.closed().compareValueTo(worstCard, vira));
    }

    @Test
    @DisplayName("Should card with rank greater than vira rank worth rank minus one")
    void shouldCardWithRankGreaterThanViraRankWorthRankMinusOne() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        final TrucoCard vira = TrucoCard.of(FOUR, HEARTS);
        assertEquals(8, card.relativeValue(vira));
    }

    @Test
    @DisplayName("Should card with rank not greater than vira rank worth card rank")
    void shouldCardWithRankNotGreaterThanViraRankWorthCardRank() {
        final TrucoCard card = TrucoCard.of(FOUR, SPADES);
        final TrucoCard vira = TrucoCard.of(FIVE, HEARTS);
        assertEquals(1, card.relativeValue(vira));
    }


    @Test
    @DisplayName("Should card with rank equal to vira worth rank value")
    void shouldCardWithRankEqualToViraWorthRankValue() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        final TrucoCard vira = TrucoCard.of(TWO, HEARTS);
        assertEquals(9, card.relativeValue(vira));
    }

    @Test
    @DisplayName("Should card with manilha rank worth manilha value")
    void shouldCardWithManilhaRankWorthManilhaValue() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        final TrucoCard vira = TrucoCard.of(ACE, HEARTS);
        assertEquals(11, card.relativeValue(vira));
    }

    @Test
    @DisplayName("Should regular cards of same rank have same the value")
    void shouldRegularCardsOfSameRankHaveSameTheValue() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        final TrucoCard otherCard = TrucoCard.of(TWO, HEARTS);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertEquals(0, card.compareValueTo(otherCard, vira));
    }

    @ParameterizedTest
    @CsvSource({"FOUR,THREE", "SEVEN,QUEEN", "QUEEN,JACK", "JACK,KING", "KING,ACE"})
    @DisplayName("Should return correct winner for non manilhas")
    void shouldReturnCorrectWinnerCardForNonManilhas(CardRank lowerRank, CardRank higherRank) {
        final TrucoCard lowerCard = TrucoCard.of(lowerRank, SPADES);
        final TrucoCard higherCard = TrucoCard.of(higherRank, SPADES);
        final TrucoCard vira = TrucoCard.of(FIVE, SPADES);
        assertTrue(lowerCard.compareValueTo(higherCard, vira) < 0);
    }

    @Test
    @DisplayName("Should manilha worth more than a regular card")
    void shouldManilhaWorthMoreThanARegularCard() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        final TrucoCard manilha = TrucoCard.of(FOUR, HEARTS);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(card.compareValueTo(manilha, vira) < 0);
    }

    @Test
    @DisplayName("Should ouros worth less than espadilha")
    void shouldOurosWorthLessThanEspadilha() {
        final TrucoCard ouros = TrucoCard.of(FOUR, DIAMONDS);
        final TrucoCard espadilha = TrucoCard.of(FOUR, SPADES);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(ouros.compareValueTo(espadilha, vira) < 0);
    }

    @Test
    @DisplayName("Should espadilha worth less than copas")
    void shouldEspadilhaWorthLessThanCopas() {
        final TrucoCard espadilha = TrucoCard.of(FOUR, SPADES);
        final TrucoCard copas = TrucoCard.of(FOUR, HEARTS);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(copas.compareValueTo(espadilha, vira) > 0);
    }

    @Test
    @DisplayName("Should copas worth less than zap")
    void shouldCopasWorthLessThanZap() {
        final TrucoCard copas = TrucoCard.of(FOUR, HEARTS);
        final TrucoCard zap = TrucoCard.of(FOUR, CLUBS);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(copas.compareValueTo(zap, vira) < 0);
    }

    @Test
    @DisplayName("Should correctly identify ouros card")
    void shouldCorrectlyIdentifyOurosCard() {
        final TrucoCard ouros = TrucoCard.of(FOUR, DIAMONDS);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(ouros.isOuros(vira));
    }

    @Test
    @DisplayName("Should correctly identify espadilha card")
    void shouldCorrectlyIdentifyEspadilhaCard() {
        final TrucoCard espadilha = TrucoCard.of(FOUR, SPADES);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(espadilha.isEspadilha(vira));
    }

    @Test
    @DisplayName("Should correctly identify copas card")
    void shouldCorrectlyIdentifyCopasCard() {
        final TrucoCard copas = TrucoCard.of(FOUR, HEARTS);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(copas.isCopas(vira));
    }

    @Test
    @DisplayName("Should correctly identify zap card")
    void shouldCorrectlyIdentifyZapCard() {
        final TrucoCard zap = TrucoCard.of(FOUR, CLUBS);
        final TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        assertTrue(zap.isZap(vira));
    }

    @Test
    @DisplayName("Should cards of same rank and suit be equals")
    void shouldCardsOfSameRankAndSuitBeEquals() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        final TrucoCard otherCard = TrucoCard.of(TWO, SPADES);
        assertEquals(card, otherCard);
    }

    @Test
    @DisplayName("Should cards of different suit or rank be not equals")
    void shouldCardsOfDifferentSuitOrRankBeNotEquals() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        final TrucoCard otherCard = TrucoCard.of(TWO, DIAMONDS);
        assertNotEquals(card, otherCard);
    }

    @Test
    @DisplayName("Should throw if any parameter is null in compareValueTo method")
    void shouldThrowIfAnyParameterIsNullInCompareValueToMethod() {
        final TrucoCard card = TrucoCard.of(TWO, SPADES);
        Assertions.assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> TrucoCard.closed().compareValueTo(null, card)),
                () -> assertThrows(NullPointerException.class,
                        () -> TrucoCard.closed().compareValueTo(card, null))

        );
    }

    @Test
    @DisplayName("Should throw if vira parameter is null while checking if the card is a manilha")
    void shouldThrowIfViraParameterIsNullWhileCheckingIfTheCardIsAManilha() {
        assertThrows(NullPointerException.class, () -> TrucoCard.closed().isEspadilha(null));
    }

    @Test
    @DisplayName("Should cards of same rank and suit be the same")
    void shouldCardsOfSameRankAndSuitBeTheSame() {
        final TrucoCard card = TrucoCard.of(ACE, SPADES);
        final TrucoCard otherCard = TrucoCard.of(ACE, SPADES);
        assertSame(card, otherCard);
    }

    @ParameterizedTest(name = "[{index}]: rank {0} and suit {1} = {2}")
    @DisplayName("Should correctly toString() open cards")
    @CsvSource({"SEVEN,DIAMONDS,[7D]", "ACE,HEARTS,[AH]", "QUEEN,CLUBS,[QC]", "JACK,SPADES,[JS]", "KING,SPADES,[KS]"})
    void shouldCorrectlyToStringOpenCard(CardRank rank, CardSuit suit, String output){
        assertEquals(output, TrucoCard.of(rank, suit).toString());
    }

    @Test
    @DisplayName("Should correctly toString() closed cards")
    void shouldCorrectlyToStringClosedCard() {
        assertEquals("[XX]", TrucoCard.closed().toString());
    }

}