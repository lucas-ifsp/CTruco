/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
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

@DisplayName("MyHand Tests")
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

    @Test
    @DisplayName("Should return power of strong card")
    void shouldReturnPowerOfStrongCard() {
        List<TrucoCard> botHand = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );

        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(botHand, 0)
                .opponentScore(0)
                .build();

        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());

        assertEquals(13, myHand.powerOfCard(intel, 0));
    }
}
