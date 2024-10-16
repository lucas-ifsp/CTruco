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

package com.brito.macena.boteco.interfaces;

import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Analyzer Tests")
public class AnalyzerTest {

    @BeforeAll
    static void setUpAll() { System.out.println("Starting Analyzer tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing Analyzer tests..."); }

    @Nested
    @DisplayName("myHand method tests")
    class MyHandMethodTests {
        @Test
        @DisplayName("Returns EXCELLENT when no cards")
        void returnsExcellentWhenNoCards() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();

            Analyzer analyzer = new AnalyzerImpl();
            Status result = analyzer.myHand(intel);

            assertThat(result).isEqualTo(Status.EXCELLENT);
        }

        @Test
        @DisplayName("Returns threeCardsHandler when three cards")
        void returnsThreeCardsHandlerWhenThreeCards() {
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            );
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), cards, TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), 0)
                    .botInfo(cards, 0)
                    .opponentScore(0)
                    .build();

            Analyzer analyzer = new AnalyzerImpl();
            Status result = analyzer.myHand(intel);

            assertThat(result).isEqualTo(Status.GOOD);
        }

        @Test
        @DisplayName("Returns twoCardsHandler when two cards")
        void returnsTwoCardsHandlerWhenTwoCards() {
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES)
            );
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), cards, TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), 0)
                    .botInfo(cards, 0)
                    .opponentScore(0)
                    .build();

            Analyzer analyzer = new AnalyzerImpl();
            Status result = analyzer.myHand(intel);

            assertThat(result).isEqualTo(Status.MEDIUM);
        }

        @Test
        @DisplayName("Returns oneCardHandler when one card")
        void returnsOneCardHandlerWhenOneCard() {
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), cards, TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), 0)
                    .botInfo(cards, 0)
                    .opponentScore(0)
                    .build();

            Analyzer analyzer = new AnalyzerImpl();
            Status result = analyzer.myHand(intel);

            assertThat(result).isEqualTo(Status.BAD);
        }
    }

    private static class AnalyzerImpl extends Analyzer {
        @Override
        public Status threeCardsHandler(List<TrucoCard> myCards) {
            return Status.GOOD;
        }

        @Override
        public Status twoCardsHandler(List<TrucoCard> myCards) {
            return Status.MEDIUM;
        }

        @Override
        public Status oneCardHandler() {
            return Status.BAD;
        }
    }
}