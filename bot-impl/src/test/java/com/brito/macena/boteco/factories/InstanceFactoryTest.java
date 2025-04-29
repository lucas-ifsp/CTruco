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

package com.brito.macena.boteco.factories;

import com.local.brito.macena.boteco.factories.InstanceFactory;
import com.local.brito.macena.boteco.intel.analyze.Pattern;
import com.local.brito.macena.boteco.intel.analyze.Trucador;
import com.local.brito.macena.boteco.intel.profiles.Agressive;
import com.local.brito.macena.boteco.intel.profiles.Passive;
import com.local.brito.macena.boteco.intel.trucoCaller.PassiveTrucoCaller;
import com.local.brito.macena.boteco.intel.trucoCaller.SneakyTrucoCaller;
import com.local.brito.macena.boteco.intel.trucoResponder.PassiveTrucoResponder;
import com.local.brito.macena.boteco.intel.trucoResponder.SneakyTrucoResponder;
import com.local.brito.macena.boteco.interfaces.Analyzer;
import com.local.brito.macena.boteco.interfaces.ProfileBot;
import com.local.brito.macena.boteco.interfaces.TrucoCaller;
import com.local.brito.macena.boteco.interfaces.TrucoResponder;
import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Instance Factory Tests")
public class InstanceFactoryTest {

    @Nested
    @DisplayName("AnalyzerBot Creation Tests")
    class CreateAnaliseBotTests {
        @Test
        @DisplayName("Should return Trucador instance when losing by more than 6 points")
        void shouldReturnTrucadorWhenLosingByMoreThanSixPoints() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.ACE, CardSuit.SPADES), 1)
                    .botInfo(List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)), 0)
                    .opponentScore(10)
                    .build();

            Analyzer analyzer = InstanceFactory.createAnaliseInstance(intel);

            assertTrue(analyzer instanceof Trucador);
        }

        @Test
        @DisplayName("Should return Pattern instance when score is close")
        void shouldReturnPatternWhenScoreIsClose() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS), 1)
                    .botInfo(List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)), 10)
                    .opponentScore(9)
                    .build();

            Analyzer analyzer = InstanceFactory.createAnaliseInstance(intel);

            assertTrue(analyzer instanceof Pattern);
        }

        @Test
        @DisplayName("Should return Pattern instance when leading by any margin")
        void shouldReturnPatternWhenLeading() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.KING, CardSuit.CLUBS), 1)
                    .botInfo(List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)), 12)
                    .opponentScore(5)
                    .build();

            Analyzer analyzer = InstanceFactory.createAnaliseInstance(intel);

            assertTrue(analyzer instanceof Pattern);
        }
    }

    @Nested
    @DisplayName("ProfileBot Creation Tests")
    class CreateProfileBotTests {
        @Test
        @DisplayName("should create Passive bot when opponent score is high and score difference is less than -6")
        void shouldCreatePassiveBotWhenOpponentScoreIsHigh() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(9)
                    .build();

            ProfileBot bot = InstanceFactory.createProfileBot(step, Status.MEDIUM);

            assertThat(bot).isInstanceOf(Passive.class);
        }

        @Test
        @DisplayName("should create Aggressive bot when score difference is not less than -6")
        void shouldCreateAggressiveBotWhenScoreDifferenceIsNotLessThanNegativeSix() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(5)
                    .build();

            ProfileBot bot = InstanceFactory.createProfileBot(step, Status.MEDIUM);

            assertThat(bot).isInstanceOf(Agressive.class);
        }
    }

    @Nested
    @DisplayName("TrucoCaller Creation Tests")
    class TrucoCallerCreationTests {
        @Test
        @DisplayName("should create SneakyTrucoCaller when score distance is less than -4")
        void shouldCreateSneakyTrucoCallerWhenScoreDistanceIsLessThanMinusFour() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(5)
                    .build();
            TrucoCaller caller = InstanceFactory.createTrucoCallerInstance(step);

            assertThat(caller).isInstanceOf(SneakyTrucoCaller.class);
        }

        @Test
        @DisplayName("should create PassiveTrucoCaller when score distance is greater than or equal to -4")
        void shouldCreatePassiveTrucoCallerWhenScoreDistanceIsGreaterThanOrEqualToMinusFour() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
            );
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 3)
                    .opponentScore(5)
                    .build();
            TrucoCaller caller = InstanceFactory.createTrucoCallerInstance(step);

            assertThat(caller).isInstanceOf(PassiveTrucoCaller.class);
        }
    }

    @Nested
    @DisplayName("TrucoResponder Creation Tests")
    class TrucoResponderCreationTests {
        @Test
        @DisplayName("should create SneakyTrucoResponder when opponent's score is greater and score difference is less than or equal to -6")
        void shouldCreateSneakyTrucoResponderWhenOpponentScoreIsGreaterAndScoreDifferenceIsLessThanOrEqualToMinusSix() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 5)
                    .opponentScore(12)
                    .build();

            TrucoResponder responder = InstanceFactory.createTrucoResponder(step);

            assertThat(responder).isInstanceOf(SneakyTrucoResponder.class);
        }

        @Test
        @DisplayName("should create PassiveTrucoResponder when opponent's score is less than or equal to player's score")
        void shouldCreatePassiveTrucoResponderWhenOpponentScoreIsLessThanOrEqualToPlayerScore() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            GameIntel step = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 8)
                    .opponentScore(6)
                    .build();

            TrucoResponder responder = InstanceFactory.createTrucoResponder(step);

            assertThat(responder).isInstanceOf(PassiveTrucoResponder.class);
        }
    }
}
