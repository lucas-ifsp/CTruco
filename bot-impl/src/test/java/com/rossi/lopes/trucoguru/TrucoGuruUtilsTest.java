package com.rossi.lopes.trucoguru;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TrucoGuruUtilsTest {
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