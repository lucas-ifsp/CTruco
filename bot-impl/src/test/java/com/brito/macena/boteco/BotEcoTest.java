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

package com.brito.macena.boteco;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BotEcoTest {

    private BotEco botEco;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting BotEco tests.");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("All BotEco tests finished.");
    }

    @BeforeEach
    void setUp() {
        botEco = new BotEco();
    }

    @Nested
    @DisplayName("Mão de Onze tests")
    class MaoDeOnzeTests {

        @Test
        @DisplayName("Should accept hand of eleven when hand power is greater or equal to 17")
        void shouldAcceptMaoDeOnzeWhenHandPowerIsGreaterOrEqual17() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .build();

            boolean response = botEco.getMaoDeOnzeResponse(intel);

            assertTrue(response);
        }

        @Test
        @DisplayName("Should accept Mão de Onze when hand power is 20 or more")
        void acceptMaoDeOnzeWhenHandPowerIs20OrMore() {
            GameIntel intel = mock(GameIntel.class);
            List<TrucoCard> hand = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
            );
            when(intel.getCards()).thenReturn(hand);

            boolean response = botEco.getMaoDeOnzeResponse(intel);

            assertTrue(response);
        }

        @Test
        @DisplayName("Should handle empty hand gracefully")
        void handleEmptyHandGracefully() {
            GameIntel intel = mock(GameIntel.class);
            List<TrucoCard> hand = List.of();
            when(intel.getCards()).thenReturn(hand);

            boolean response = botEco.getMaoDeOnzeResponse(intel);

            assertFalse(response);
        }
    }

    @Nested
    @DisplayName("Decide if raises tests")
    class DecideIfRaisesTests {

    }

    @Nested
    @DisplayName("Raise response tests")
    class RaiseResponseTests {
        @Test
        @DisplayName("Should increase truco when hand power is 17 or more and score difference is greater than 6")
        void shouldIncreaseTrucoWhenHandPowerIsSufficientAndScoreDifferenceIsHigh() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON),
                            List.of(
                                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                            ), vira, 1)
                    .botInfo(botEcoHand, 2)
                    .opponentScore(9)
                    .build();

            int response = botEco.getRaiseResponse(intel);

            assertThat(response).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Choose card tests")
    class ChooseCard {
        @Test
        @DisplayName("Should select the smallest card necessary to win")
        void selectSmallestCardToWin() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            );
            TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0).opponentCard(opponentCard)
                    .build();

            CardToPlay selectedCard = botEco.chooseCard(step);
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)));
        }

        @Test
        @DisplayName("Should avoid using manilhas in the first round")
        void avoidUsingManilhasFirstRound() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );
            TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0).opponentCard(opponentCard)
                    .build();

            CardToPlay selectedCard = botEco.chooseCard(step);
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)));
        }

        @Test
        @DisplayName("Should play best card when no opponent card and hand is medium")
        void shouldPlayBestCardWhenNoOpponentCardAndMediumHand() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .build();

            CardToPlay selectedCard = botEco.chooseCard(step);
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS)));
        }

        @Test
        @DisplayName("Should play worst card if it beats opponent's card in first round with 3 cards")
        void shouldPlayWorstCardIfItBeatsOpponentsCardWithThreeCards() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
            );
            TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            CardToPlay selectedCard = botEco.chooseCard(step);
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
        }

        @Test
        @DisplayName("Should play second best card and not play manilha in first round if hand has multiple strong cards")
        void shouldPlaySecondBestCardAndNotPlayManilhaInFirstRoundIfMultipleStrongCards() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .build();

            CardToPlay selectedCard = botEco.chooseCard(step);
            assertThat(selectedCard).isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)));
        }
    }

    @Nested
    @DisplayName("Calculate hand power tests")
    class CalculateHandPowerTests {
        @Test
        @DisplayName("Should calculate hand power correctly for a given hand")
        void calculateHandPowerCorrectly() {
            List<TrucoCard> hand = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            int handPower = botEco.calculateHandPower(hand);

            assertThat(handPower).isEqualTo(9 + 10 + 6);
        }

        @Test
        @DisplayName("Should return zero for an empty hand")
        void returnZeroForEmptyHand() {
            List<TrucoCard> hand = List.of();

            int handPower = botEco.calculateHandPower(hand);

            assertThat(handPower).isEqualTo(0);
        }

        @Test
        @DisplayName("Should calculate hand power correctly for a hand with one card")
        void calculateHandPowerForSingleCardHand() {
            List<TrucoCard> hand = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            int handPower = botEco.calculateHandPower(hand);

            assertThat(handPower).isEqualTo(7);
        }
    }
}
