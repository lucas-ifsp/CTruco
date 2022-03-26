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

import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThirdRoundStrategyTest {

    @Mock private GameIntel intel;
    private ThirdRoundStrategy sut;

    @Nested
    @DisplayName("When playing ")
    class PlayCardTest {
        @Test
        @DisplayName("Should use remaining card to open round")
        void shouldUseRemainingCardToOpenRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            sut = new ThirdRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should use remaining card if can win round")
        void shouldUseRemainingCardIfCanWinRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            sut = new ThirdRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should use remaining card if can tie round")
        void shouldUseRemainingCardIfCanTieRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            sut = new ThirdRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should discard if loses anyway")
        void shouldDiscardIfLosesAnyway() {
            final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            sut = new ThirdRoundStrategy(intel);
            assertEquals(TrucoCard.closed(), sut.chooseCard().value());
        }
    }

    @Nested
    @DisplayName("When responding to a raise score request ")
    class ResponseRequestTest {

        @Test
        @DisplayName("Should quit in call for 3 if user card value is lower than 8 in third round")
        void shouldQuitInCallFor3IfUserCardValueIsLowerThan8InThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            sut = new ThirdRoundStrategy(intel);
            assertEquals(-1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should quit in call for 6 if card value is lower than 11 in third round")
        void shouldQuitInCallFor6IfCardValueIsLowerThan11InThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            sut = new ThirdRoundStrategy(intel);
            assertEquals(-1, sut.getRaiseResponse(6));
        }


        @Test
        @DisplayName("Should accept call for 3 if user card value is between 8 and 10 in third round")
        void shouldQuitInCallFor3IfUserCardValueIsBetween8And10InThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            sut = new ThirdRoundStrategy(intel);
            assertEquals(0, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should request to raise any call if player has the highest card in third round")
        void shouldResquestToRaiseAnyCallIfPlayedHasHighestCardInThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            sut = new ThirdRoundStrategy(intel);
            assertEquals(1, sut.getRaiseResponse(3));
        }
    }

    @Nested
    @DisplayName("When deciding on hand score ")
    class RequestScoreRaiseTest {

        @Test
        @DisplayName("Should request truco in third round if plays first and have card of value 9 or higher")
        void shouldRequestTrucoInThirdRoundIfPlaysFirstAndHaveCardOfValue9OrHigher() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.empty());

            sut = new ThirdRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request to raise in third round if plays first and have card of value 11 or higher")
        void shouldRequestToRaiseInThirdRoundIfPlaysFirstAndHaveCardOfValue11OrHigher() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getHandPoints()).thenReturn(3);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.empty());

            sut = new ThirdRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request to raise in third round if can win")
        void shouldRequestToRaiseInThirdRoundIfCanWin() {
            final var botCards = List.of(TrucoCard.of(CardRank.KING, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON, LOST));
            when(intel.getHandPoints()).thenReturn(3);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)));

            sut = new ThirdRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request to raise in third if won first and is now tying ")
        void shouldRequestToRaiseInThirdIfWonFirstAndIsNowTying() {
            final var botCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getHandPoints()).thenReturn(3);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));

            sut = new ThirdRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }

        @Test
        @DisplayName("Should request to raise in third round if opponent card value is lower than 4")
        void shouldRequestToRaiseInThirdRoundIfOpponentCardValueIsLowerThan5() {
            final var botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));

            sut = new ThirdRoundStrategy(intel);
            assertTrue(sut.decideIfRaises());
        }
    }
}