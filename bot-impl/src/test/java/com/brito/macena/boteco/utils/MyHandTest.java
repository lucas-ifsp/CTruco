package com.brito.macena.boteco.utils;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyHandTest {
    @Test
    @DisplayName("Should return the best card when the hand has 3 cards")
    void testGetBestCardWithThreeCards() {
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        MyHand myCards = new MyHand(List.of(card1, card2, card3), vira);

        TrucoCard bestCard = myCards.getBestCard();
        assertThat(bestCard).isEqualTo(card1);
    }

    @Test
    @DisplayName("Should return the best card when the hand has 2 cards")
    void testGetBestCardWithTwoCards() {
        TrucoCard card1 = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

        MyHand myCards = new MyHand(List.of(card1, card2), vira);

        TrucoCard bestCard = myCards.getBestCard();
        assertThat(bestCard).isEqualTo(card1);
    }

    @Test
    @DisplayName("Should return the best card when the hand has 1 card")
    void testGetBestCardWithOneCard() {
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        MyHand myCards = new MyHand(List.of(card1), vira);

        TrucoCard bestCard = myCards.getBestCard();
        assertThat(bestCard).isEqualTo(card1);
    }

    @Test
    @DisplayName("Should return the worst card when the hand has 3 cards")
    void testGetWorstCardWithThreeCards() {
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        MyHand myCards = new MyHand(List.of(card1, card2, card3), vira);

        TrucoCard worstCard = myCards.getWorstCard();
        assertThat(worstCard).isEqualTo(card2);
    }

    @Test
    @DisplayName("Should return the second-best card when the hand has 3 cards")
    void testGetSecondBestCardWithThreeCards() {
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        MyHand myCards = new MyHand(List.of(card1, card2, card3), vira);

        TrucoCard secondBestCard = myCards.getSecondBestCard();
        assertThat(secondBestCard).isEqualTo(card3);
    }

    @Test
    @DisplayName("Should return the best card when the hand has mixed cards and a vira")
    void testGetBestCardWithVira() {
        TrucoCard card1 = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
        TrucoCard card2 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

        MyHand myCards = new MyHand(List.of(card1, card2, card3), vira);

        TrucoCard bestCard = myCards.getBestCard();
        assertThat(bestCard).isEqualTo(card2);
    }

    @Test
    @DisplayName("Should return power of worst card")
    void shouldReturnPowerOfWorstCard() {
        List<TrucoCard> botHand = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );

        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(botHand, 0)
                .opponentScore(0)
                .build();

        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());

        assertEquals(1, myHand.powerOfCard(intel, 2));
    }
}
