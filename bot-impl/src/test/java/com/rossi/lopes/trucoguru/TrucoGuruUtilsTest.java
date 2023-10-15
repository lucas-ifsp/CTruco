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
    @DisplayName("Should return true if hand has manilha")
    void shouldReturnTrueIfHandHasManilha() {
        TrucoGuruUtils trucoGuruUtils = new TrucoGuruUtils();

        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );
        assertThat(trucoGuruUtils.hasManilha(cards, vira)).isTrue();
    }
}