package com.brito.macena.boteco.utils;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MyCardsTest {
    @Test
    @DisplayName("Should return the best card when the hand has 3 cards")
    void testGetBestCardWithThreeCards() {
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
        TrucoCard card3 = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        MyCards myCards = new MyCards(List.of(card1, card2, card3), vira);

        TrucoCard bestCard = myCards.getBestCard();
        assertThat(bestCard).isEqualTo(card1);
    }

    @Test
    @DisplayName("Should return the best card when the hand has 2 cards")
    void testGetBestCardWithTwoCards() {
        TrucoCard card1 = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

        MyCards myCards = new MyCards(List.of(card1, card2), vira);

        TrucoCard bestCard = myCards.getBestCard();
        assertThat(bestCard).isEqualTo(card2);
    }

    @Test
    @DisplayName("Should return the best card when the hand has 1 card")
    void testGetBestCardWithOneCard() {
        TrucoCard card1 = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

        MyCards myCards = new MyCards(List.of(card1), vira);

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

        MyCards myCards = new MyCards(List.of(card1, card2, card3), vira);

        TrucoCard worstCard = myCards.getWorstCard();
        assertThat(worstCard).isEqualTo(card3);
    }
}
