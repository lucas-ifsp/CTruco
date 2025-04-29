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

package com.brito.macena.boteco.intel.analyze;

import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.brito.macena.boteco.intel.analyze.Pattern;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pattern Tests")
public class PatternTest {
    @BeforeAll
    static void setupAll() { System.out.println("Starting Pattern tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing Pattern tests..."); }

    @Nested
    @DisplayName("threeCardsHandler method tests")
    class ThreeCardsHandlerMethodTests {
        @Test
        @DisplayName("Returns EXCELLENT when having at least two manilhas")
        void returnsExcellentWhenHavingAtLeastTwoManilhas() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.threeCardsHandler(intel.getCards());

            assertThat(result).isEqualTo(Status.EXCELLENT);
        }

        @Test
        @DisplayName("Returns GOOD when having one manilha and second best card value is at least 5")
        void returnsGoodWhenHavingOneManilhaAndSecondBestCardValueIsAtLeast5() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.threeCardsHandler(intel.getCards());

            assertThat(result).isEqualTo(Status.GOOD);
        }

        @Test
        @DisplayName("Returns BAD when hand power is less than 10")
        void returnsBadWhenHandPowerIsLessThan10() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.threeCardsHandler(intel.getCards());

            assertThat(result).isEqualTo(Status.BAD);
        }
    }

    @Nested
    @DisplayName("twoCardsHandler method tests")
    class TwoCardsHandlerMethodTests {
        @Test
        @DisplayName("Returns EXCELLENT when won first round and have at least one manilha")
        void returnsExcellentWhenWonFirstRoundAndHaveAtLeastOneManilha() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.twoCardsHandler(intel.getCards());

            assertThat(result).isEqualTo(Status.EXCELLENT);
        }

        @Test
        @DisplayName("Returns BAD when lost first round and no cards can beat opponent card")
        void returnsBadWhenLostFirstRoundAndNoCardsCanBeatOpponentCard() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                    ), 0)
                    .opponentScore(0)
                    .opponentCard(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES))
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.twoCardsHandler(intel.getCards());

            assertThat(result).isEqualTo(Status.BAD);
        }

        @Test
        @DisplayName("Returns MEDIUM when hand power is between 14 and 17")
        void returnsMediumWhenHandPowerIsBetween14And17() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.twoCardsHandler(intel.getCards());

            assertThat(result).isEqualTo(Status.MEDIUM);
        }
    }

    @Nested
    @DisplayName("oneCardHandler method tests")
    class OneCardHandlerMethodTests {
        @Test
        @DisplayName("Returns EXCELLENT when won first round and hand points are less than or equal to 3")
        void returnsExcellentWhenWonFirstRoundAndHandPointsAreLessThanOrEqualTo3() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 3)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.oneCardHandler();

            assertThat(result).isEqualTo(Status.EXCELLENT);
        }

        @Test
        @DisplayName("Returns BAD when lost first round and no manilhas")
        void returnsBadWhenLostFirstRoundAndNoManilhas() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.oneCardHandler();

            assertThat(result).isEqualTo(Status.BAD);
        }

        @Test
        @DisplayName("Returns GOOD when hand points are greater than 3 and best card value is 8")
        void returnsGoodWhenHandPointsAreGreaterThan3AndBestCardValueIs8() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 4)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.oneCardHandler();

            assertThat(result).isEqualTo(Status.GOOD);
        }
    }
}

