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

// Authors: Juan Rossi e Guilherme Lopes

package com.local.rossi.lopes.trucoguru;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TrucoGuruUtilsTest {
    @Nested
    @DisplayName("HasManilhaTest")
    class HasManilhaTest {
        @Test
        @DisplayName("Should return true if hand at least one manilha")
        void shouldReturnTrueIfHandHasManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasManilha(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no manilha")
        void shouldReturnFalseIfHandHasNoManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasManilha(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("HasZapTest")
    class HasZapTest {
        @Test
        @DisplayName("Should return true if hand has zap")
        void shouldReturnTrueIfHandHasZap() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasZap(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no zap")
        void shouldReturnFalseIfHandHasNoZap() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasZap(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("HasCopasTest")
    class HasCopasTest {
        @Test
        @DisplayName("Should return true if hand has copas")
        void shouldReturnTrueIfHandHasCopas() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasCopas(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no copas")
        void shouldReturnFalseIfHandHasNoCopas() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasZap(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("HasDoubleManilhas")
    class HasDoubleManilhasTest{
        @Test
        @DisplayName("Should return true if hand has double manilhas")
        void shouldReturnTrueIfHandHasDoubleManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasDoubleManilhas(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no double manilhas")
        void shouldReturnFalseIfHandHasNoDoubleManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasDoubleManilhas(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("HasStrongCardTest")
    class HasStrongCardTest {
        @Test
        @DisplayName("Should return true if hand has at least one strong card")
        void shouldReturnTrueIfHandHasStrongCard() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasStrongCard(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return true if hand is strong (at least 2)")
        void shouldReturnTrueIfHandIsStrong() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasStrongHand(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if hand has no strong card")
        void shouldReturnFalseIfHandHasNoStrongCard() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasStrongCard(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("HasCasalTest")
    class HasCasalTest {
        @Test
        @DisplayName("Should return true if has casal maior")
        void shouldReturnTrueIfHasCasalMaior() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasCasalMaior(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if not has casal maior")
        void shouldReturnFalseIfNotHasCasalMaior() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasCasalMaior(cards, vira)).isFalse();
        }

        @Test
        @DisplayName("Should return true if has casal menor")
        void shouldReturnTrueIfHasCasalMenor() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasCasalMenor(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false if not has casal menor")
        void shouldReturnTrueIfNotHasCasalMenor() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasCasalMenor(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("GetStrongestCard")
    class GetStrongestCardTest {
        @Test
        @DisplayName("Should return the correct strongestCard when provided with one manilha")
        void shouldReturnStrongestCardWhenProvidedWithOneManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.ACE, TrucoGuruUtils.getStrongestCard(cards, vira).getRank());
            assertEquals(CardSuit.DIAMONDS, TrucoGuruUtils.getStrongestCard(cards, vira).getSuit());
        }

        @Test
        @DisplayName("Should return the correct strongestCard when provided with more than one manilha")
        void shouldReturnStrongestCardWhenProvidedWithMoreThanOneManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.ACE, TrucoGuruUtils.getStrongestCard(cards, vira).getRank());
            assertEquals(CardSuit.CLUBS, TrucoGuruUtils.getStrongestCard(cards, vira).getSuit());
        }

        @Test
        @DisplayName("Should return the correct strongestCard when provided with no manilhas")
        void shouldReturnStrongestCardWhenProvidedWithNoManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.THREE, TrucoGuruUtils.getStrongestCard(cards, vira).getRank());
            assertEquals(CardSuit.DIAMONDS, TrucoGuruUtils.getStrongestCard(cards, vira).getSuit());
        }
    }

    @Nested
    @DisplayName("GetWeakestCard")
    class GetWeakestCardTest {
        @Test
        @DisplayName("Should return the correct weakestCard when provided with one manilha")
        void shouldReturnWeakestCardWhenProvidedWithOneManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.FOUR, TrucoGuruUtils.getWeakestCard(cards, vira).getRank());
            assertEquals(CardSuit.DIAMONDS, TrucoGuruUtils.getWeakestCard(cards, vira).getSuit());
        }

        @Test
        @DisplayName("Should return the correct weakestCard when provided with more than one manilha")
        void shouldReturnWeakestCardWhenProvidedWithMoreThanOneManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.THREE, TrucoGuruUtils.getWeakestCard(cards, vira).getRank());
            assertEquals(CardSuit.DIAMONDS, TrucoGuruUtils.getWeakestCard(cards, vira).getSuit());
        }

        @Test
        @DisplayName("Should return the correct weakestCard when provided with no manilhas")
        void shouldReturnWeakestCardWhenProvidedWithNoManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            );
            assertEquals(CardRank.SEVEN, TrucoGuruUtils.getWeakestCard(cards, vira).getRank());
            assertEquals(CardSuit.SPADES, TrucoGuruUtils.getWeakestCard(cards, vira).getSuit());
        }
    }

    @Nested
    @DisplayName("getWeakestStrongerCardTest")
    class GetWeakestStrongerCardTest {
        @Test
        @DisplayName("Should return the correct weakestCard card that wins the round")
        void shouldReturnWeakestCardWhenProvidedWithNoManilhas() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            TrucoCard openedCard = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            );
            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), TrucoGuruUtils.getWeakestStrongestCard(cards, openedCard, vira));
        }

        @Test
        @DisplayName("Should return null if no card beats the opened card")
        void shouldReturnNullIfNoCardBeatsTheOpenedCard() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
            TrucoCard openedCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            );
            assertNull(TrucoGuruUtils.getWeakestStrongestCard(cards, openedCard, vira));
        }

        @Test
        @DisplayName("Should return null if card is equal in value with opened card")
        void shouldReturnNullIfCardIsEqualInValueWithOpenedCard() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
            TrucoCard openedCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            );
            assertNull(TrucoGuruUtils.getWeakestStrongestCard(cards, openedCard, vira));
        }
    }

    @Nested
    @DisplayName("HasStrongHandTest")
    class HasStrongHandTest {
        @Test
        @DisplayName("Should return true when have strong hand")
        void shouldReturnTrueWhenHaveStrongHand() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasStrongHand(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false when have no strong hand")
        void shouldReturnFalseWhenHaveNoStrongHand() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasStrongHand(cards, vira)).isFalse();
        }
    }

    @Nested
    @DisplayName("HasHighRankTest")
    class HasHighRankTest {
        @Test
        @DisplayName("Should return true when have high rank")
        void shouldReturnTrueWhenHaveHighRank() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasHighRank(cards, vira)).isTrue();
        }

        @Test
        @DisplayName("Should return false when have low rank")
        void shouldReturnFalseWhenHaveLowRank() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            );
            assertThat(TrucoGuruUtils.hasStrongHand(cards, vira)).isFalse();
        }
    }
}
