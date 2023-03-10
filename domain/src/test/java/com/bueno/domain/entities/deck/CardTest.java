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

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CardTest {

    @Nested
    @DisplayName("Should allow")
    class ShouldAllow {
        @Test
        @DisplayName("Creating a card with valid rank and suit")
        void creatingCardWithValidRankAndSuit() {
            Card card = Card.of(Rank.SEVEN, Suit.SPADES);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(card.getRank()).as("Card Rank").isEqualTo(Rank.SEVEN);
            softly.assertThat(card.getSuit()).as("Card Suit").isEqualTo(Suit.SPADES);
            softly.assertAll();
        }

        @Test
        @DisplayName("creating closed card")
        void creatingClosedCard() {
            Card card = Card.closed();
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(card.getRank()).as("Card Rank").isEqualTo(Rank.HIDDEN);
            softly.assertThat(card.getSuit()).as("Card Suit").isEqualTo(Suit.HIDDEN);
            softly.assertAll();
        }

        @Test
        @DisplayName("create card from symbols")
        void createCardFromSymbols() {
            final Rank rank = Rank.ofSymbol("A");
            final Suit suit = Suit.ofSymbol("C");
            assertThat(Card.of(Rank.ACE, Suit.CLUBS)).isEqualTo(Card.of(rank, suit));
        }
    }

    @Nested
    @DisplayName("Should not allow")
    class ShouldNotAllow {
        @Test
        @DisplayName("creating a card with null rank")
        void creatingCardWithNullRank() {
            assertThatNullPointerException().isThrownBy(() -> Card.of(null, Suit.CLUBS));
        }

        @Test
        @DisplayName("creating a card with null suit")
        void creatingCardWithNullSuit() {
            assertThatNullPointerException().isThrownBy(() -> Card.of(Rank.ACE, null));
        }

        @Test
        @DisplayName("creating card with only rank or suit hidden")
        void creatingCardWithOnlyRankOrSuitHidden() {
            SoftAssertions softly = new SoftAssertions();
            softly.assertThatThrownBy(() -> Card.of(Rank.HIDDEN, Suit.CLUBS))
                    .as("Rank is hidden but Suit is not")
                    .isInstanceOf(IllegalArgumentException.class);
            softly.assertThatThrownBy(() -> Card.of(Rank.SEVEN, Suit.HIDDEN))
                    .as("Suit is hidden but Rank is not")
                    .isInstanceOf(IllegalArgumentException.class);
            softly.assertAll();
        }
    }

    @ParameterizedTest(name = "[{index}]: rank {0} and suit {1} = {2}")
    @DisplayName("Should correctly toString() open cards")
    @CsvSource({"SEVEN,DIAMONDS,[7D]", "ACE,HEARTS,[AH]", "QUEEN,CLUBS,[QC]", "JACK,SPADES,[JS]", "KING,SPADES,[KS]"})
    void shouldCorrectlyToStringOpenCard(Rank rank, Suit suit, String output) {
        assertThat(Card.of(rank, suit).toString()).isEqualTo(output);
    }

    @Test
    @DisplayName("Should next rank of hidden be hidden")
    void shouldNextRankOfHiddenBeHidden() {
        assertThat(Rank.HIDDEN.next()).isEqualTo(Rank.HIDDEN);
    }

    @Test
    @DisplayName("Should next rank of 3 be 4")
    void shouldNextRankOf3Be4() {
        assertThat(Rank.THREE.next()).isEqualTo(Rank.FOUR);
    }

    @Test
    @DisplayName("Should next rank of King be Ace")
    void shouldNextRankOfKingBeAce() {
        assertThat(Rank.KING.next()).isEqualTo(Rank.ACE);
    }

    @Test
    @DisplayName("Should correctly toString() closed cards")
    void shouldCorrectlyToStringClosedCard() {
        assertThat(Card.closed().toString()).isEqualTo("[XX]");
    }

    @Test
    @DisplayName("Should a closed card worth less than worst possible card")
    void shouldClosedCardWorthLessThanWorstCard() {
        Card worstCard = Card.of(Rank.FOUR, Suit.DIAMONDS);
        Card vira = Card.of(Rank.FOUR, Suit.CLUBS);
        assertThat(Card.closed().compareValueTo(worstCard, vira)).isNegative();
    }

    @Test
    @DisplayName("Should same cards be equals")
    void shouldSameCardsBeEquals() {
        assertThat(Card.of(Rank.TWO, Suit.DIAMONDS)).isEqualTo(Card.of(Rank.TWO, Suit.DIAMONDS));
    }

    @Test
    @DisplayName("Should cards of different suit not be equals")
    void shouldCardsOfDifferentSuitNotBeEquals() {
        assertThat(Card.of(Rank.TWO, Suit.CLUBS)).isNotEqualTo(Card.of(Rank.TWO, Suit.DIAMONDS));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv", numLinesToSkip = 1)
    @DisplayName("Should correctly get the next rank value")
    void shouldCorrectlyGetTheNextRankValue(Rank current, Rank next) {
        assertThat(current.next()).isEqualTo(next);
    }

    @Test
    @DisplayName("Should cards of same rank worth the same if they are not manilhas")
    void shouldCardsOfSameRankWorthTheSameIfTheyAreNotManilhas() {
        final Card vira = Card.of(Rank.ACE, Suit.CLUBS);
        final Card card1 = Card.of(Rank.THREE, Suit.DIAMONDS);
        final Card card2 = Card.of(Rank.THREE, Suit.SPADES);
        assertThat(card1.compareValueTo(card2, vira)).isZero();
    }

    @ParameterizedTest
    @CsvSource({"FOUR,THREE", "SEVEN,QUEEN", "QUEEN,JACK", "JACK,KING", "KING,ACE"})
    @DisplayName("Should return correct winner for non manilhas")
    void shouldReturnCorrectWinnerCardForNonManilhas(Rank lowerRank, Rank higherRank) {
        final Card lowerCard = Card.of(lowerRank, Suit.SPADES);
        final Card higherCard = Card.of(higherRank, Suit.SPADES);
        final Card vira = Card.of(Rank.FIVE, Suit.SPADES);
        assertThat(lowerCard.compareValueTo(higherCard, vira)).isNegative();
    }

    @Test
    @DisplayName("Should espadilha worth more than ouros")
    void shouldEspadilhaWorthMoreThanOuros() {
        final Card vira = Card.of(Rank.TWO, Suit.CLUBS);
        final Card ouros = Card.of(Rank.THREE, Suit.DIAMONDS);
        final Card espadilha = Card.of(Rank.THREE, Suit.SPADES);
        assertThat(espadilha.compareValueTo(ouros, vira)).isPositive();
    }

    @Test
    @DisplayName("Should copas worth more than espadilha")
    void shouldCopasWorthMoreThanEspadilha() {
        final Card vira = Card.of(Rank.TWO, Suit.CLUBS);
        final Card espadilha = Card.of(Rank.THREE, Suit.SPADES);
        final Card copas = Card.of(Rank.THREE, Suit.HEARTS);
        assertThat(copas.compareValueTo(espadilha, vira)).isPositive();
    }

    @Test
    @DisplayName("Should zap worth more than copas")
    void shouldZapWorthMoreThanCopas() {
        final Card vira = Card.of(Rank.TWO, Suit.CLUBS);
        final Card copas = Card.of(Rank.THREE, Suit.HEARTS);
        final Card zap = Card.of(Rank.THREE, Suit.CLUBS);
        assertThat(zap.compareValueTo(copas, vira)).isPositive();
    }
}