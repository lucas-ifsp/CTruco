/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
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

package com.bueno.impl.mineirobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.*;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecondRoundStrategyTest {

    @Mock private GameIntel intel;
    private SecondRoundStrategy sut;

    @Nested
    @DisplayName("When playing ")
    class PlayCardTest {
        @Test
        @DisplayName("Should use just enough card to win round after losing first if play last")
        void shouldUseJustEnoughCardToWinRoundAfterLosingFirstIfPlayLast() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(LOST));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should discard if cannot win round after losing first")
        void shouldDiscardIfCannotWinRoundAfterLosingFirst() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(LOST));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertEquals(TrucoCard.closed(), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should not be able to play the same card twice in the same hand")
        void shouldNotBeAbleToPlayTheSameCardTwiceInTheSameHand() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should discard worst card to open round after winning first if has card of value higher than 8")
        void shouldDiscardWorstCardToOpenRoundAfterWinningFirstIfHasCardOfValueHigherThan8() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON));
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertEquals(TrucoCard.closed(), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should use best card to open round after winning first if remaining cards have values lower than 8")
        void shouldUseBestCardToOpenRoundAfterWinningFirstIfRemainingCardsHaveValuesLowerThan8() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON));
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should not discard in any case if both players have 11 points")
        void shouldNotDiscardInAnyCaseIfBothPlayersHave11Points() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getScore()).thenReturn(11);
            when(intel.getOpponentScore()).thenReturn(11);

            sut = new SecondRoundStrategy(intel);
            assertNotEquals(TrucoCard.closed(), sut.chooseCard().value());
        }
    }

    @Nested
    @DisplayName("When responding to a raise score request ")
    class ResponseRequestTest {

        @Test
        @DisplayName("Should quit if lost first and already played second and remaining card value is lower than 10")
        void shouldQuitIfLostFirstAndAlreadyPlayedSecondAndRemainingCardValueIsLowerThan10() {
            final var botCards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            sut = new SecondRoundStrategy(intel);
            assertEquals(-1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should accept if lost first and already played second and remaining card value is Higher than 11")
        void shouldAcceptIfLostFirstAndAlreadyPlayedSecondAndRemainingCardValueIsHigherThan11() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            sut = new SecondRoundStrategy(intel);
            assertEquals(0, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should quit if lost first round and dont have at least cards of value 18")
        void shouldQuitIfLostFirstRoundAndDontHaveAtLeastCardsOfValue18() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            sut = new SecondRoundStrategy(intel);
            assertEquals(-1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should quit if lost first round and dont have at least cards of value 20 for point call of 6 or more")
        void shouldQuitIfLostFirstRoundAndDontHaveAtLeastCardsOfValue20ForPointCallOf6OrMore() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            sut = new SecondRoundStrategy(intel);
            assertEquals(-1, sut.getRaiseResponse(6));
        }

        @Test
        @DisplayName("Should request to raise if lost first round and has at least cards of value 23")
        void shouldRequestToRaiseIfLostFirstRoundAndHasAtLeastCardsOfValue23() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            sut = new SecondRoundStrategy(intel);
            assertEquals(1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should quit if tied first and untying card value is lower than 10")
        void shouldQuitIfTiedFirstAndUntyingCardValueIsLowerThan10() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            sut = new SecondRoundStrategy(intel);
            assertEquals(-1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should request to raise if tied first and untying card value is higher than 11")
        void shouldRequestToRaiseIfTiedFirstAndUntyingCardValueIsHigherThan11() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            sut = new SecondRoundStrategy(intel);
            assertEquals(1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should accept call if tied first and untying card value is between 10 and 11")
        void shouldAcceptCallIfTiedFirstAndUntyingCardValueIsBetween10And11() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            sut = new SecondRoundStrategy(intel);
            assertEquals(0, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should request to raise if can win after tying first round")
        void shouldRequestToRaiseIfCanWinAfterTyingFirstRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            sut = new SecondRoundStrategy(intel);
            assertEquals(1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should request to raise if can win after winning first round")
        void shouldRequestToRaiseIfCanWinAfterWinningFirstRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
            when(intel.getRoundResults()).thenReturn(List.of(WON));

            sut = new SecondRoundStrategy(intel);
            assertEquals(1, sut.getRaiseResponse(3));
        }
    }

    @Nested
    @DisplayName("When deciding on hand score ")
    class RequestScoreRaiseTest {
        @Test
        @DisplayName("Should not request score raise in second round if won first")
        void shouldNotRequestScoreRaiseInSecondRoundIfWonFirst() {
            final var botCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertFalse(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request truco if tied first and is untying with a card of value 10 or higher")
        void shouldRequestTrucoIfTiedFirstAndIsUntyingWithCardOfValue10OrHigher() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should not request truco if tied first and is untying with a card of value lower than 10")
        void shouldNotRequestTrucoIfTiedFirstAndIsUntyingWithCardOfValueLowerThan10() {
            final var botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertFalse(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request any raise if tied first and can win second")
        void shouldRequestAnyRaiseIfTiedFirstAndCanWinSecond() {
            final var botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request any raise if tied first and has card of value 13")
        void shouldRequestAnyRaiseIfTiedFirstAndHasCardOfValue13() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new SecondRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should not request to raise in second round if lost first and hand score is higher than 1")
        void shouldNotRequestToRaiseInSecondRoundIfLostFirstHandScoreIsHigherThan1() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(LOST));
            when(intel.getHandPoints()).thenReturn(3);

            sut = new SecondRoundStrategy(intel);
            assertFalse(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request truco if lost first but can win second and have card of value 10 or higher for the third")
        void shouldRequestTrucoIfLostFirstButCanWinSecondAndHaveCardOfValue9OrHigherForTheThird() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(LOST));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)));

            sut = new SecondRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }
    }
}