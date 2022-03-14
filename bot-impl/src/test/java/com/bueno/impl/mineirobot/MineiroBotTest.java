/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MineiroBotTest {

    @Mock private GameIntel intel;

    private MineiroBot sut;

    @BeforeEach
    void setUp() {
        sut = new MineiroBot();
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Nested
    @DisplayName("When playing ")
    class PlayCardTest {

        @Nested
        @DisplayName("in first round ")
        class FirstRound {

            @Test
            @DisplayName("Should use just enough card to win while playing last")
            void shouldUseJustEnoughCardToWinWhilePlayingLast() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
                assertEquals(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should draw if cannot win")
            void shouldDrawIfCannotWin() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
                assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should discard if loses anyway")
            void shouldDiscardIfLosesAnyway() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
                assertEquals(TrucoCard.closed(), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should discard having two winning cards")
            void shouldDiscardHavingTwoWinningCards() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                assertEquals(TrucoCard.closed(), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should use best card to open round if has no manilha")
            void shouldUseBestCardToOpenRoundIfHasNoManilha() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
                assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should use best card to open round if has only diamonds as manilha")
            void shouldUseBestCardToOpenRoundIfHasOnlyDiamondsAsManilha() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
                assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should use manilha to open round if has one manilha other than diamonds and nothing more")
            void shouldUseManilhaToOpenRoundIfHasOneManilhaOtherThanDiamondsAndNothingMore() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.SPADES), TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
                assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.SPADES), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should discard to open round if has winning cards")
            void ShouldDiscardToOpenRoundIfHasWinningCards() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                assertEquals(TrucoCard.closed(), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should use second best card to open round if has one manilha other than diamonds")
            void shouldUseSecondBestCardToOpenRoundIfHasOneManilhaOtherThanDiamonds() {
                final var botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES), TrucoCard.of(CardRank.SIX, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), sut.chooseCard(intel).value());
            }
        }

        @Nested
        @DisplayName("in second round ")
        class SecondRound {
            @Test
            @DisplayName("Should use just enough card to win round after losing first if play last")
            void shouldUseJustEnoughCardToWinRoundAfterLosingFirstIfPlayLast() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(LOST));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should discard if cannot win round after losing first")
            void shouldDiscardIfCannotWinRoundAfterLosingFirst() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(LOST));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.closed(), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should not be able to play the same card twice in the same hand")
            void shouldNotBeAbleToPlayTheSameCardTwiceInTheSameHand() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(DREW));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should discard worst card to open round after winning first if has card of value higher than 8")
            void shouldDiscardWorstCardToOpenRoundAfterWinningFirstIfHasCardOfValueHigherThan8() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(WON));
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.closed(), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should use best card to open round after winning first if remaining cards have values lower than 8")
            void shouldUseBestCardToOpenRoundAfterWinningFirstIfRemainingCardsHaveValuesLowerThan8() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(WON));
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should not discard in any case if both players have 11 points")
            void shouldNotDiscardInAnyCaseIfBothPlayersHave11Points() {
                final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(WON));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
                when(intel.getScore()).thenReturn(11);
                when(intel.getOpponentScore()).thenReturn(11);

                assertNotEquals(TrucoCard.closed(), sut.chooseCard(intel).value());
            }
        }

        @Nested
        @DisplayName("in third round ")
        class ThirdRound {
            @Test
            @DisplayName("Should use remaining card to open round")
            void shouldUseRemainingCardToOpenRound() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
                when(intel.getOpponentCard()).thenReturn(Optional.empty());
                assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should use remaining card if can win round")
            void shouldUseRemainingCardIfCanWinRound() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should use remaining card if can tie round")
            void shouldUseRemainingCardIfCanTieRound() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES), sut.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Should discard if loses anyway")
            void shouldDiscardIfLosesAnyway() {
                final var botCards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(LOST,WON));
                when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)));
                when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

                assertEquals(TrucoCard.closed(), sut.chooseCard(intel).value());
            }
        }
    }

    @Nested
    @DisplayName("When responding to a raise score request ")
    class ResponseRequestTest {

        @Test
        @DisplayName("Should request raise if is sure about winning in first round")
        void shouldRequestRaiseIfIsSureAboutWinningInFirstRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            assertEquals(1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should run if has less than one card of value 9 and espadilha among three cards")
        void shouldRunIfHasLessThanOneCardOfValue9AndEspadilhaAmongThreeCards() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            assertEquals(-1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should accept if has at least one card of value 9 and espadilha among three cards")
        void shouldAcceptIfHasAtLeastOneCardOfValue9AndEspadilhaAmongThreeCards() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            assertEquals(0, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should quit if played card with value lower than 9 and have less than value 17 in first round")
        void shouldQuitIfPlayedCardWithValueLowerThan9AndHaveLessThanValue17InFirstRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            assertEquals(-1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should quit if lost first and already played second and remaining card value is lower than 10")
        void shouldQuitIfLostFirstAndAlreadyPlayedSecondAndRemainingCardValueIsLowerThan10() {
            final var botCards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            assertEquals(-1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should accept if lost first and already played second and remaining card value is Higher than 11")
        void shouldAcceptIfLostFirstAndAlreadyPlayedSecondAndRemainingCardValueIsHigherThan11() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            assertEquals(0, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should quit if lost first round and dont have at least cards of value 18")
        void shouldQuitIfLostFirstRoundAndDontHaveAtLeastCardsOfValue19() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            assertEquals(-1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should quit if lost first round and dont have at least cards of value 20 for point call of 6 or more")
        void shouldQuitIfLostFirstRoundAndDontHaveAtLeastCardsOfValue20ForPointCallOf6OrMore() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getHandPoints()).thenReturn(3);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            assertEquals(-1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should request to raise if lost first round and has at least cards of value 23")
        void shouldRequestToRaiseIfLostFirstRoundAndHasAtLeastCardsOfValue23() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS), TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST));

            assertEquals(1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should quit if tied first and untying card value is lower than 10")
        void shouldQuitIfTiedFirstAndUntyingCardValueIsLowerThan10() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            assertEquals(-1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should request to raise if tied first and untying card value is higher than 11")
        void shouldRequestToRaiseIfTiedFirstAndUntyingCardValueIsHigherThan11() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            assertEquals(1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should accept call if tied first and untying card value is between 10 and 11")
        void shouldAcceptCallIfTiedFirstAndUntyingCardValueIsBetween10And11() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            final var openCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            assertEquals(0, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should request to raise if can win after tying first round")
        void shouldRequestToRaiseIfCanWinAfterTyingFirstRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
            when(intel.getRoundResults()).thenReturn(List.of(DREW));

            assertEquals(1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should request to raise if can win after winning first round")
        void shouldRequestToRaiseIfCanWinAfterWinningFirstRound() {
            final var botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
            when(intel.getRoundResults()).thenReturn(List.of(WON));

            assertEquals(1, sut.getRaiseResponse(intel));
        }


        @Test
        @DisplayName("Should quit in call for 3 if user card value is lower than 9 in third round")
        void shouldQuitInCallFor3IfUserCardValueIsLowerThan9InThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));

            assertEquals(-1, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should quit in call for 6 if card value is lower than 12 in third round")
        void shouldQuitInCallFor6IfCardValueIsLowerThan12InThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getHandPoints()).thenReturn(3);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(LOST, WON));

            assertEquals(-1, sut.getRaiseResponse(intel));
        }


        @Test
        @DisplayName("Should accept call for 3 if user card value is between 9 and 11 in third round")
        void shouldQuitInCallFor3IfUserCardValueIsBetween9And11InThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));

            assertEquals(0, sut.getRaiseResponse(intel));
        }

        @Test
        @DisplayName("Should request to raise any call if player has the highest card in third round")
        void shouldResquestToRaiseAnyCallIfPlayedHasHighestCardInThirdRound() {
            final var openCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
            when(intel.getOpenCards()).thenReturn(openCards);
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));

            assertEquals(1, sut.getRaiseResponse(intel));
        }
    }

    @Nested
    @DisplayName("When deciding on hand score ")
    class RequestScoreRaiseTest {
        @Test
        @DisplayName("Should not request score raise in first round")
        void shouldNotRequestScoreRaiseInFirstRound() {
            when(intel.getCards()).thenReturn(List.of());
            when(intel.getRoundResults()).thenReturn(List.of());
            assertFalse(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should not request score raise in second round if won first")
        void shouldNotRequestScoreRaiseInSecondRoundIfWonFirst() {
            final var botCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            assertFalse(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should request truco if tied first and is untying with a card of value 10 or higher")
        void shouldRequestTrucoIfTiedFirstAndIsUntyingWithCardOfValue10OrHigher() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            assertTrue(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should not request truco if tied first and is untying with a card of value lower than 10")
        void shouldNotRequestTrucoIfTiedFirstAndIsUntyingWithCardOfValueLowerThan10() {
            final var botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            assertFalse(sut.decideIfRaises(intel));
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

            assertTrue(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should request any raise if tied first and has card of value 13")
        void shouldRequestAnyRaiseIfTiedFirstAndHasCardOfValue13() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(DREW));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            assertTrue(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should not request to raise in second round if lost first and hand score is higher than 1")
        void shouldNotRequestToRaiseInSecondRoundIfLostFirstHandScoreIsHigherThan1() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(LOST));
            when(intel.getHandPoints()).thenReturn(3);

            assertFalse(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should request truco if lost first but can win second and have card of value 10 or higher for the third")
        void shouldRequestTrucoIfLostFirstButCanWinSecondAndHaveCardOfValue9OrHigherForTheThird() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS), TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(LOST));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)));

            assertTrue(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should request truco in third round if plays first and have card of value 10 or higher")
        void shouldRequestTrucoInThirdRoundIfPlaysFirstAndHaveCardOfValue10OrHigher() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.empty());

            assertTrue(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should request to raise in third round if plays first and have card of value 12 or higher")
        void shouldRequestToRaiseInThirdRoundIfPlaysFirstAndHaveCardOfValue12OrHigher() {
            final var botCards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
            when(intel.getHandPoints()).thenReturn(3);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.empty());

            assertTrue(sut.decideIfRaises(intel));
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

            assertTrue(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should request to raise in third if won first and is now tying ")
        void shouldRequestToRaiseInThirdIfWonFirstAndIsNowTying() {
            final var botCards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
            when(intel.getHandPoints()).thenReturn(3);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));

            assertTrue(sut.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should request to raise in third round if opponent card value is lower than 5")
        void shouldRequestToRaiseInThirdRoundIfOpponentCardValueIsLowerThan5() {
            final var botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
            when(intel.getHandPoints()).thenReturn(1);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));
            when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)));

            assertTrue(sut.decideIfRaises(intel));
        }
    }

    @Nested
    @DisplayName("When deciding if plays ")
    class MaoDeOnzeTest {

        @Test
        @DisplayName("Should accept mao de onze if the sum of the two higher cards values is 20 or more")
        void shouldAcceptMaoDeOnzeIfTheSumOfTheTwoHigherCardsValuesIs20OrMore() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.SPADES), TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(intel.getOpponentScore()).thenReturn(4);
            assertTrue(sut.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should accept mao de onze if has one card higher than 10 and remaining worth at least 15 if opponent has 8 or more points")
        void shouldAcceptMaoDeOnzeIfHasOneCardHigherThan10AndRemainingWorthAtLeast15IfOpponentHas8OrMorePoints() {
            final var botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));
            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
            when(intel.getOpponentScore()).thenReturn(8);
            assertTrue(sut.getMaoDeOnzeResponse(intel));
        }
    }
}