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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FirstRoundStrategyTest {

    @Mock private GameIntel intel;
    private FirstRoundStrategy sut;

    @Nested
    @DisplayName("When playing ")
    class PlayCardTest {
        @Test
        @DisplayName("Should use just enough card to win while playing last")
        void shouldUseJustEnoughCardToWinWhilePlayingLast() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should draw if cannot win")
        void shouldDrawIfCannotWin() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should discard if loses anyway")
        void shouldDiscardIfLosesAnyway() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.closed(), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should discard having two winning cards")
        void shouldDiscardHavingTwoWinningCards() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.closed(), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should use best card to open round if has no manilha")
        void shouldUseBestCardToOpenRoundIfHasNoManilha() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should use best card to open round if has only diamonds as manilha")
        void shouldUseBestCardToOpenRoundIfHasOnlyDiamondsAsManilha() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should use manilha to open round if has one manilha other than diamonds and nothing more")
        void shouldUseManilhaToOpenRoundIfHasOneManilhaOtherThanDiamondsAndNothingMore() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES), TrucoCard.of(CardRank.KING, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.SPADES), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should discard to open round if has winning cards")
        void ShouldDiscardToOpenRoundIfHasWinningCards() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.closed(), sut.chooseCard().value());
        }

        @Test
        @DisplayName("Should use second best card to open round if has one manilha other than diamonds")
        void shouldUseSecondBestCardToOpenRoundIfHasOneManilhaOtherThanDiamonds() {
            final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpponentCard()).thenReturn(Optional.empty());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), sut.chooseCard().value());
        }
    }

    @Nested
    @DisplayName("When responding to a raise score request ")
    class ResponseRequestTest {

        @Test
        @DisplayName("Should request raise if has cards of value higher than 27 and zap or copas")
        void shouldRequestRaiseIfHasCardsOfValueHigherThan27AndZapOrCopas() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(1, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should request raise if already played and has cards of value higher than 27 and zap or copas")
        void shouldRequestRaiseIfAlreadyPlayedAndHasCardsOfValueHigherThan27AndZapOrCopas() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            final var vira = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
            final var openCards = List.of(vira, TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(vira);

            sut = new FirstRoundStrategy(intel);
            assertEquals(1, sut.getRaiseResponse(3));
        }


        @Test
        @DisplayName("Should accept if has cards of value equal or higher than 24")
        void shouldAcceptIfHasCardsOfValueEqualOrHigherThan24() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES), TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(0, sut.getRaiseResponse(3));
        }

        @Test
        @DisplayName("Should quit if has cards of value lower than 24")
        void shouldQuitIfHasCardsOfValueLowerThan24() {
            final var botCards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES), TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            sut = new FirstRoundStrategy(intel);
            assertEquals(-1, sut.getRaiseResponse(3));
        }
    }

    @Nested
    @DisplayName("When deciding on hand score ")
    class RequestScoreRaiseTest {
        @Test
        @DisplayName("Should not request score raise in first round")
        void shouldNotRequestScoreRaiseInFirstRound() {
            sut = new FirstRoundStrategy(intel);
            assertFalse(sut.decideIfRaises());
        }
    }
}