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

package com.brito.macena.boteco.intel.trucoCaller;

import com.local.brito.macena.boteco.intel.trucoCaller.AggressiveTrucoCaller;
import com.local.brito.macena.boteco.intel.trucoCaller.PassiveTrucoCaller;
import com.local.brito.macena.boteco.intel.trucoCaller.SneakyTrucoCaller;
import com.local.brito.macena.boteco.utils.MyHand;
import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TrucoCaller Tests")
public class TrucoCallerTest {

    @BeforeAll
    static void setUpAll() { System.out.println("Starting TrucoCaller tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing TrucoCaller tests..."); }

    @Nested
    @DisplayName("AggressiveTrucoCaller method tests")
    class AgessiveTrucoCallerMethodTests {
        @Test
        @DisplayName("Should call truco when status is excellent")
        void shouldCallTrucoWhenStatusIsExcellent() {
            AggressiveTrucoCaller caller = new AggressiveTrucoCaller();
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            assertTrue(caller.shouldCallTruco(intel, Status.EXCELLENT));
        }

        @Test
        @DisplayName("Should call truco when status is good")
        void shouldCallTrucoWhenStatusIsGood() {
            AggressiveTrucoCaller caller = new AggressiveTrucoCaller();
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            assertTrue(caller.shouldCallTruco(intel, Status.GOOD));
        }
    }

    @Nested
    @DisplayName("PassiveTrucoCaller method tests")
    class PassiveTrucoCallerMethodTests {
        @Test
        @DisplayName("Should call truco if losing by 9 or more and status is medium or bad with 3 cards")
        void shouldCallTrucoWhenLosingByNineOrMoreAndStatusIsMediumOrBad() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(9)
                    .build();

            PassiveTrucoCaller trucoCaller = new PassiveTrucoCaller();
            boolean shouldCall = trucoCaller.shouldCallTruco(intel, Status.MEDIUM);

            assertTrue(shouldCall);
        }

        @Test
        @DisplayName("Should dont call truco if status is excellent and there are 2 cards")
        void shouldDontCallTrucoWhenStatusIsExcellentAndTwoCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(5)
                    .build();

            PassiveTrucoCaller trucoCaller = new PassiveTrucoCaller();
            boolean shouldCall = trucoCaller.shouldCallTruco(intel, Status.MEDIUM);

            assertFalse(shouldCall);
        }
    }

    @Nested
    @DisplayName("SneakyTrucoCaller method tests")
    class SneakyTrucoCallerMethodTests {
        @Test
        @DisplayName("Should call truco if status is medium or good with 3 cards")
        void shouldCallTrucoWhenStatusIsMediumOrGoodAndThreeCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(5)
                    .build();

            SneakyTrucoCaller trucoCaller = new SneakyTrucoCaller();
            boolean shouldCall = trucoCaller.shouldCallTruco(intel, Status.MEDIUM);

            assertTrue(shouldCall);
        }

        @Test
        @DisplayName("Should dont call truco if status is excellent with 2 cards")
        void shouldDontCallTrucoWhenStatusIsExcellentAndTwoOrFewerCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(6)
                    .build();

            SneakyTrucoCaller trucoCaller = new SneakyTrucoCaller();
            boolean shouldCall = trucoCaller.shouldCallTruco(intel, Status.MEDIUM);

            assertFalse(shouldCall);
        }

        @Test
        @DisplayName("Should not call truco if status is good and second card power is less than 8")
        void shouldNotCallTrucoWhenStatusIsGoodAndSecondCardPowerLessThanEight() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(4)
                    .build();

            SneakyTrucoCaller trucoCaller = new SneakyTrucoCaller();
            boolean shouldCall = trucoCaller.shouldCallTruco(intel, Status.MEDIUM);

            assertFalse(shouldCall);
        }
    }
}
