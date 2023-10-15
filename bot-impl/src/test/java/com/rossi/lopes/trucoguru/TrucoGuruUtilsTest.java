package com.rossi.lopes.trucoguru;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
}