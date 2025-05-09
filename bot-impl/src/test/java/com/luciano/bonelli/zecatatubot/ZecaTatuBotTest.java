package com.luciano.bonelli.zecatatubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ZecaTatuBotTest {
    private ZecaTatuBot zecaTatuBot;
    private GameIntel intel;

    @BeforeEach
    public void setup() {
        zecaTatuBot = new ZecaTatuBot();
        intel = mock(GameIntel.class);
    }

    @Nested
    @DisplayName("Test getMaoDeOnzeResponse method")
    class GetMaoDeOnzeResponseTest {

    }

    @Nested
    @DisplayName("Test decideIfRaises method")
    class DecideIfRaisesTest {

    }


    @Nested
    @DisplayName("Test chooseCard method")
    class ChooseCardTest {

    }

    @Nested
    @DisplayName("Test getRaiseResponse method")
    class GetRaiseResponseTest {
    }

    @Test
    @DisplayName("CountHowManyManilhasInHand")
    public void countManilhas() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)));
        assertThat(zecaTatuBot.countManilha(intel)).isEqualTo(2);
    }
    @Test
    @DisplayName("ShoulCountHandValue")
    public void handValueManilha() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
        assertThat(zecaTatuBot.handValue(intel)).isEqualTo(23);
    }

    @Test
    @DisplayName("ShouldTellHighestCardInHand")
    public void HighestCard() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
        assertThat(zecaTatuBot.getHighCard(intel)).isEqualTo(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
    }

    @Test
    @DisplayName("ShouldTellHighestCardInHandWith2equalsCard")
    public void HighestCardWith2Equals() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
        assertThat(zecaTatuBot.getHighCard(intel).getRank()).isEqualTo((CardRank.TWO));
    }

    @Test
    @DisplayName("ShouldTellLowestCardInHand")
    public void LowestCard() {
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
        when(intel.getCards()).thenReturn(List.of(
                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)));
        assertThat(zecaTatuBot.getLowCard(intel)).isEqualTo(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
    }


}

