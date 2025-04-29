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

import com.local.brito.macena.boteco.intel.analyze.Trucador;
import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Trucador tests")
public class TrucadorTest {
    @BeforeAll
    static void setupAll() { System.out.println("Starting Trucador tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing Trucador tests..."); }

    @Nested
    @DisplayName("Three Cards Handler Tests")
    class ThreeCardsHandlerTests {

        @Test
        @DisplayName("Returns EXCELLENT when having at least two manilhas with three cards")
        void returnsExcellentWhenHavingAtLeastTwoManilhasWithThreeCards() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                    ), 0)
                    .opponentScore(0)
                    .build();
            Trucador trucador = new Trucador(intel);
            assertEquals(Status.EXCELLENT, trucador.threeCardsHandler(intel.getCards()));
        }

        @Test
        @DisplayName("Returns GOOD when having one manilha and second best card value is 9 with three cards")
        void returnsGoodWhenHavingOneManilhaAndSecondBestCardValueIsNineWithThreeCards() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                    ), 0)
                    .opponentScore(0)
                    .build();
            Trucador trucador = new Trucador(intel);
            assertEquals(Status.GOOD, trucador.threeCardsHandler(intel.getCards()));
        }

        @Test
        @DisplayName("Returns BAD when hand power is less than 11 with three cards")
        void returnsBadWhenHandPowerIsLessThanElevenWithThreeCards() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                    ), 0)
                    .opponentScore(0)
                    .build();
            Trucador trucador = new Trucador(intel);
            assertEquals(Status.BAD, trucador.threeCardsHandler(intel.getCards()));
        }
    }

    @Nested
    @DisplayName("Two Cards Handler Tests")
    class TwoCardsHandlerTests {

        @Test
        @DisplayName("Returns EXCELLENT when won first round and best card value is 9 with two cards")
        void returnsExcellentWhenWonFirstRoundAndBestCardValueIsNineWithTwoCards() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                    ), 0)
                    .opponentScore(0)
                    .build();
            Trucador trucador = new Trucador(intel);
            assertEquals(Status.EXCELLENT, trucador.twoCardsHandler(intel.getCards()));
        }


        @Test
        @DisplayName("Returns MEDIUM when lost first round and second best card value is 7 with two cards")
        void returnsMediumWhenLostFirstRoundAndSecondBestCardValueIsSevenWithTwoCards() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
                    ), 0)
                    .opponentScore(0)
                    .build();
            Trucador trucador = new Trucador(intel);
            assertEquals(Status.MEDIUM, trucador.twoCardsHandler(intel.getCards()));
        }
    }

    @Nested
    @DisplayName("One Card Handler Tests")
    class OneCardHandlerTests {

        @Test
        @DisplayName("Returns EXCELLENT when won first round and my card is higher than opponent's card with one card")
        void returnsExcellentWhenWonFirstRoundAndMyCardIsHigherThanOpponentsCardWithOneCard() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                    ), 0)
                    .opponentScore(0)
                    .build();
            Trucador trucador = new Trucador(intel);
            assertEquals(Status.EXCELLENT, trucador.oneCardHandler());
        }

        @Test
        @DisplayName("Returns BAD when lost first round and best card value is less than 3 with one card")
        void returnsBadWhenLostFirstRoundAndBestCardValueIsLessThanThreeWithOneCard() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
                    ), 0)
                    .opponentScore(0)
                    .build();
            Trucador trucador = new Trucador(intel);
            assertEquals(Status.BAD, trucador.oneCardHandler());
        }
    }
}

