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

package com.bueno.domain.entities.player.mineirobot;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.hand.Intel;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.round.Round;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MineiroBotTest {

    @Mock
    private Intel intel;
    @Mock
    private Round round;
    @Mock
    private Player opponent;

    private MineiroBot sut;

    @BeforeAll
    static void init() {
        Logger.getLogger(Player.class.getName()).setLevel(Level.OFF);
    }

    @BeforeEach
    void setUp() {
        sut = new MineiroBot();
        sut.setIntel(intel);
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
                sut.setCards(List.of(new Card(7, Suit.CLUBS), new Card('A', Suit.CLUBS), new Card(4, Suit.CLUBS)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(6, Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(2, Suit.CLUBS));
                assertEquals(new Card(7, Suit.CLUBS), sut.playCard());
            }

            @Test
            @DisplayName("Should draw if cannot win")
            void shouldDrawIfCannotWin() {
                sut.setCards(List.of(new Card(7, Suit.CLUBS), new Card('A', Suit.CLUBS), new Card(4, Suit.CLUBS)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card('A', Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(2, Suit.CLUBS));
                assertEquals(new Card('A', Suit.CLUBS), sut.playCard());
            }

            @Test
            @DisplayName("Should discard if loses anyway")
            void shouldDiscardIfLosesAnyway() {
                sut.setCards(List.of(new Card(7, Suit.CLUBS), new Card('A', Suit.CLUBS), new Card(4, Suit.CLUBS)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(2, Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(2, Suit.CLUBS));
                assertEquals(Card.getClosedCard(), sut.playCard());
            }

            @Test
            @DisplayName("Should discard having two winning cards")
            void shouldDiscardHavingTwoWinningCards() {
                sut.setCards(List.of(new Card(7, Suit.CLUBS), new Card('A', Suit.CLUBS), new Card(7, Suit.SPADES)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card('K', Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(6, Suit.CLUBS));
                assertEquals(Card.getClosedCard(), sut.playCard());
            }

            @Test
            @DisplayName("Should use best card to open round if has no manilha")
            void shouldUseBestCardToOpenRoundIfHasNoManilha() {
                sut.setCards(List.of(new Card(7, Suit.CLUBS), new Card('A', Suit.CLUBS), new Card(3, Suit.CLUBS)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
                assertEquals(new Card(3, Suit.CLUBS), sut.playCard());
            }

            @Test
            @DisplayName("Should use best card to open round if has only diamonds as manilha")
            void shouldUseBestCardToOpenRoundIfHasOnlyDiamondsAsManilha() {
                sut.setCards(List.of(new Card(7, Suit.CLUBS), new Card('A', Suit.DIAMONDS), new Card(3, Suit.CLUBS)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(new Card('K', Suit.CLUBS));
                assertEquals(new Card('A', Suit.DIAMONDS), sut.playCard());
            }

            @Test
            @DisplayName("Should use manilha to open round if has one manilha other than diamonds and nothing more")
            void shouldUseManilhaToOpenRoundIfHasOneManilhaOtherThanDiamondsAndNothingMore() {
                sut.setCards(List.of(new Card(7, Suit.CLUBS), new Card('A', Suit.SPADES), new Card('K', Suit.CLUBS)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(new Card('K', Suit.CLUBS));
                assertEquals(new Card('A', Suit.SPADES), sut.playCard());
            }

            @Test
            @DisplayName("Should discard to open round if has winning cards")
            void ShouldDiscardToOpenRoundIfHasWinningCards() {
                sut.setCards(List.of(new Card(7, Suit.HEARTS), new Card('A', Suit.CLUBS), new Card(7, Suit.SPADES)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card('K', Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(6, Suit.CLUBS));
                assertEquals(Card.getClosedCard(), sut.playCard());
            }

            @Test
            @DisplayName("Should use second best card to open round if has one manilha other than diamonds")
            void shouldUseSecondBestCardToOpenRoundIfHasOneManilhaOtherThanDiamonds() {
                sut.setCards(List.of(new Card(7, Suit.SPADES), new Card(6, Suit.HEARTS), new Card(3, Suit.CLUBS)));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(new Card(6, Suit.CLUBS));
                assertEquals(new Card(3, Suit.CLUBS), sut.playCard());
            }
        }

        @Nested
        @DisplayName("in second round ")
        class SecondRound {
            @Test
            @DisplayName("Should use just enough card to win round after losing first if play last")
            void shouldUseJustEnoughCardToWinRoundAfterLosingFirstIfPlayLast() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES), new Card(2, Suit.HEARTS)));

                when(round.getWinner()).thenReturn(Optional.of(opponent));
                when(intel.getRoundsPlayed()).thenReturn(List.of(round));

                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(7, Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

                assertEquals(new Card('Q', Suit.SPADES), sut.playCard());
            }

            @Test
            @DisplayName("Should discard if cannot win round after losing first")
            void shouldDiscardIfCannotWinRoundAfterLosingFirst() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES), new Card(2, Suit.HEARTS)));

                when(round.getWinner()).thenReturn(Optional.of(opponent));
                when(intel.getRoundsPlayed()).thenReturn(List.of(round));

                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(3, Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

                assertEquals(Card.getClosedCard(), sut.playCard());
            }

            @Test
            @DisplayName("Should use best card to open round after tying first")
            void shouldUseBestCardToOpenRoundAfterTyingFirst() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES), new Card(2, Suit.HEARTS)));

                when(round.getWinner()).thenReturn(Optional.empty());
                when(intel.getRoundsPlayed()).thenReturn(List.of(round));

                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(3, Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

                assertEquals(new Card(2, Suit.HEARTS), sut.playCard());
            }

            @Test
            @DisplayName("Should discard worst card to open round after winning first if has card of value higher than 8")
            void shouldDiscardWorstCardToOpenRoundAfterWinningFirstIfHasCardOfValueHigherThan8() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES), new Card(2, Suit.HEARTS)));

                when(round.getWinner()).thenReturn(Optional.of(sut));
                when(intel.getRoundsPlayed()).thenReturn(List.of(round));

                when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

                assertEquals(Card.getClosedCard(), sut.playCard());
            }

            @Test
            @DisplayName("Should use best card to open round after winning first if remaining cards have values lower than 8")
            void shouldUseBestCardToOpenRoundAfterWinningFirstIfRemainingCardsHaveValuesLowerThan8() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES), new Card('A', Suit.HEARTS)));

                when(round.getWinner()).thenReturn(Optional.of(sut));
                when(intel.getRoundsPlayed()).thenReturn(List.of(round));

                when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

                assertEquals(new Card('A', Suit.HEARTS), sut.playCard());
            }
        }

        @Nested
        @DisplayName("in third round ")
        class ThirdRound {
            @Test
            @DisplayName("Should use remaining card to open round")
            void shouldUseRemainingCardToOpenRound() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES)));
                when(intel.getVira()).thenReturn(new Card(4, Suit.CLUBS));
                when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());
                assertEquals(new Card('Q', Suit.SPADES), sut.playCard());
            }

            @Test
            @DisplayName("Should use remaining card if can win round")
            void shouldUseRemainingCardIfCanWinRound() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES)));

                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(7, Suit.CLUBS)));
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

                assertEquals(new Card('Q', Suit.SPADES), sut.playCard());
            }

            @Test
            @DisplayName("Should discard if loses anyway")
            void shouldDiscardIfLosesAnyway() {
                sut.setCards(List.of(new Card('Q', Suit.SPADES)));

                when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(6, Suit.CLUBS)));
                when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
                when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));

                assertEquals(Card.getClosedCard(), sut.playCard());
            }
        }
    }

    @Nested
    @DisplayName("When responding to a raise score request ")
    class ResponseRequestTest {

        @Test
        @DisplayName("Should request raise if is sure about winning in first round")
        void shouldRequestRaiseIfIsSureAboutWinningInFirstRound() {
            sut.setCards(List.of(new Card(6, Suit.CLUBS), new Card(6, Suit.HEARTS), new Card(3, Suit.CLUBS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            assertEquals(1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should run if has less than one card of value 9 and espadilha among three cards")
        void shouldRunIfHasLessThanOneCardOfValue9AndEspadilhaAmongThreeCards() {
            sut.setCards(List.of(new Card(6, Suit.CLUBS), new Card(5, Suit.HEARTS), new Card(2, Suit.CLUBS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            assertEquals(-1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should accept if has at least one card of value 9 and espadilha among three cards")
        void shouldAcceptIfHasAtLeastOneCardOfValue9AndEspadilhaAmongThreeCards() {
            sut.setCards(List.of(new Card(6, Suit.SPADES), new Card(5, Suit.HEARTS), new Card(3, Suit.CLUBS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            assertEquals(0, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should quit if played card with value lower than 9 and have less than value 17 in first round")
        void shouldQuitIfPlayedCardWithValueLowerThan9AndHaveLessThanValue17InFirstRound() {
            sut.setCards(List.of(new Card(3, Suit.SPADES), new Card('A', Suit.CLUBS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(2, Suit.CLUBS)));
            assertEquals(-1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should quit if lost first and already played second and remaining card value is lower than 10")
        void shouldQuitIfLostFirstAndAlreadyPlayedSecondAndRemainingCardValueIsLowerThan10() {
            sut.setCards(List.of(new Card(2, Suit.HEARTS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(1, Suit.CLUBS), new Card(7, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(-1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should request to raise if lost first and already played second and remaining card value is lower than 10")
        void shouldRequestToRaiseIfLostFirstAndAlreadyPlayedSecondAndRemainingCardValueIsHigherThan11() {
            sut.setCards(List.of(new Card(6, Suit.HEARTS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(1, Suit.CLUBS), new Card(7, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should request to raise if if lost first and already played second and remaining card value is lower than 10")
        void shouldAcceptIfLostFirstAndAlreadyPlayedSecondAndRemainingCardValueIs10or11() {
            sut.setCards(List.of(new Card(6, Suit.SPADES)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(1, Suit.CLUBS), new Card(7, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(0, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should quit if lost first round and dont have at least cards of value 18")
        void shouldQuitIfLostFirstRoundAndDontHaveAtLeastCardsOfValue19() {
            sut.setCards(List.of(new Card(6, Suit.DIAMONDS), new Card('A', Suit.CLUBS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(2, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(-1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should quit if lost first round and dont have at least cards of value 20 for point call of 6 or more")
        void shouldQuitIfLostFirstRoundAndDontHaveAtLeastCardsOfValue20ForPointCallOf6OrMore() {
            sut.setCards(List.of(new Card(6, Suit.SPADES), new Card('2', Suit.CLUBS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(2, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(-1, sut.getTrucoResponse(HandScore.of(6)));
        }

        @Test
        @DisplayName("Should request to raise if lost first round and has at least cards of value 23")
        void shouldRequestToRaiseIfLostFirstRoundAndHasAtLeastCardsOfValue23() {
            sut.setCards(List.of(new Card(6, Suit.HEARTS), new Card(6, Suit.SPADES)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(2, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(1, sut.getTrucoResponse(HandScore.of(6)));
        }

        @Test
        @DisplayName("Should quit if tied first and untying card value is lower than 10")
        void shouldQuitIfTiedFirstAndUntyingCardValueIsLowerThan10() {
            sut.setCards(List.of(new Card(3, Suit.HEARTS), new Card(5, Suit.SPADES)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(2, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.empty());
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(-1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should request to raise if tied first and untying card value is higher than 11")
        void shouldRequestToRaiseIfTiedFirstAndUntyingCardValueIsHigherThan11() {
            sut.setCards(List.of(new Card(3, Suit.HEARTS), new Card(6, Suit.HEARTS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(2, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.empty());
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should accept call if tied first and untying card value is between 10 and 11")
        void shouldAcceptCallIfTiedFirstAndUntyingCardValueIsBetween10And11() {
            sut.setCards(List.of(new Card(3, Suit.HEARTS), new Card(6, Suit.DIAMONDS)));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(5, Suit.CLUBS), new Card(2, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.empty());
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));

            assertEquals(0, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should quit in call for 3 if user card value is lower than 9 in third round")
        void shouldQuitInCallFor3IfUserCardValueIsLowerThan9InThirdRound() {
            sut.setCards(List.of());

            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(2, Suit.CLUBS)));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));

            assertEquals(-1, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should quit in call for 6 if user card value is lower than 12 in third round")
        void shouldQuitInCallFor6IfUserCardValueIsLowerThan12InThirdRound() {
            sut.setCards(List.of());

            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(6, Suit.SPADES)));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));

            assertEquals(-1, sut.getTrucoResponse(HandScore.of(6)));
        }


        @Test
        @DisplayName("Should accept call for 3 if user card value is between 9 and 11 in third round")
        void shouldQuitInCallFor3IfUserCardValueIsBetween9And11InThirdRound() {
            sut.setCards(List.of());

            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(3, Suit.CLUBS)));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));

            assertEquals(0, sut.getTrucoResponse(HandScore.of(3)));
        }

        @Test
        @DisplayName("Should request to raise any call if player has the highest card in third round")
        void shouldResquestToRaiseAnyCallIfPlayedHasHighestCardInThirdRound() {
            sut.setCards(List.of());
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getOpenCards()).thenReturn(List.of(new Card(6, Suit.CLUBS)));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));
            assertEquals(1, sut.getTrucoResponse(HandScore.of(9)));
        }
    }

    @Nested
    @DisplayName("When deciding on hand score ")
    class RequestScoreRaiseTest {
        @Test
        @DisplayName("Should not request score raise in first round")
        void shouldNotRequestScoreRaiseInFirstRound() {
            sut.setCards(List.of());
            when(intel.getRoundsPlayed()).thenReturn(List.of());
            assertFalse(sut.requestTruco());
        }

        @Test
        @DisplayName("Should not request score raise in second round if won first")
        void shouldNotRequestScoreRaiseInSecondRoundIfWonFirst() {
            sut.setCards(List.of(new Card(5, Suit.CLUBS), new Card(6, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.of(sut));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

            assertFalse(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request truco if tied first and is untying with a card of value 10 or higher")
        void shouldRequestTrucoIfTiedFirstAndIsUntyingWithCardOfValue10OrHigher() {
            sut.setCards(List.of(new Card(6, Suit.HEARTS)));

            when(round.getWinner()).thenReturn(Optional.empty());
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should not request truco if tied first and is untying with a card of value lower than 10")
        void shouldNotRequestTrucoIfTiedFirstAndIsUntyingWithCardOfValueLowerThan10() {
            sut.setCards(List.of(new Card(1, Suit.SPADES)));

            when(round.getWinner()).thenReturn(Optional.empty());
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

            assertFalse(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request any raise if tied first and can win second")
        void shouldRequestAnyRaiseIfTiedFirstAndCanWinSecond() {
            sut.setCards(List.of(new Card(1, Suit.SPADES)));

            when(round.getWinner()).thenReturn(Optional.empty());
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));
            when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card('K', Suit.SPADES)));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request any raise if tied first and has card of value 13")
        void shouldRequestAnyRaiseIfTiedFirstAndHasCardOfValue13() {
            sut.setCards(List.of(new Card(6, Suit.CLUBS)));

            when(round.getWinner()).thenReturn(Optional.empty());
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should not request to raise in second round if lost first and hand score is higher than 1")
        void shouldNotRequestToRaiseInSecondRoundIfLostFirstHandScoreIsHigherThan1() {
            sut.setCards(List.of(new Card(6, Suit.SPADES)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));
            when(intel.getHandScore()).thenReturn(HandScore.of(3));

            assertFalse(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request truco if lost first but can win second and have card of value 10 or higher for the third")
        void shouldRequestTrucoIfLostFirstButCanWinSecondAndHaveCardOfValue9OrHigherForTheThird() {
            sut.setCards(List.of(new Card(6, Suit.CLUBS), new Card(6, Suit.SPADES)));

            when(round.getWinner()).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(6, Suit.HEARTS)));

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request truco in third round if plays first and have card of value 10 or higher")
        void shouldRequestTrucoInThirdRoundIfPlaysFirstAndHaveCardOfValue10OrHigher() {
            sut.setCards(List.of(new Card(6, Suit.DIAMONDS)));

            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request to raise in third round if plays first and have card of value 12 or higher")
        void shouldRequestToRaiseInThirdRoundIfPlaysFirstAndHaveCardOfValue12OrHigher() {
            sut.setCards(List.of(new Card(6, Suit.HEARTS)));

            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));
            when(intel.getHandScore()).thenReturn(HandScore.of(3));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getCardToPlayAgainst()).thenReturn(Optional.empty());

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request to raise in third round if can win")
        void shouldRequestToRaiseInThirdRoundIfCanWin() {
            sut.setCards(List.of(new Card('K', Suit.HEARTS)));

            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));
            when(intel.getHandScore()).thenReturn(HandScore.of(3));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card('J', Suit.CLUBS)));

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request to raise in third if won first and is now tying ")
        void shouldRequestToRaiseInThirdIfWonFirstAndIsNowTying() {
            sut.setCards(List.of(new Card(5, Suit.HEARTS)));

            when(round.getWinner()).thenReturn(Optional.of(sut)).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));
            when(intel.getHandScore()).thenReturn(HandScore.of(3));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(5, Suit.CLUBS)));

            assertTrue(sut.requestTruco());
        }

        @Test
        @DisplayName("Should request to raise in third round if opponent card value is lower than 5")
        void shouldRequestToRaiseInThirdRoundIfOpponentCardValueIsLowerThan5() {
            sut.setCards(List.of(new Card(4, Suit.HEARTS)));

            when(round.getWinner()).thenReturn(Optional.of(sut)).thenReturn(Optional.of(opponent));
            when(intel.getRoundsPlayed()).thenReturn(List.of(round, round));
            when(intel.getHandScore()).thenReturn(HandScore.of(1));
            when(intel.getVira()).thenReturn(new Card(5, Suit.CLUBS));
            when(intel.getCardToPlayAgainst()).thenReturn(Optional.of(new Card(5, Suit.CLUBS)));

            assertTrue(sut.requestTruco());
        }
    }

    @Nested
    @DisplayName("When deciding if plays ")
    class MaoDeOnzeTest {

        @Test
        @DisplayName("Should accept mao de onze if has more than 28 in card value and opponent has less than 8 points")
        void shouldAcceptMaoDeOnzeIfHasMoreThan28InCardValueAndOpponentHasLessThan8Points() {
            sut.setCards(List.of(new Card(3, Suit.HEARTS), new Card(3, Suit.DIAMONDS), new Card(2, Suit.SPADES)));
            when(intel.getVira()).thenReturn(new Card('A', Suit.CLUBS));
            when(intel.getOpponentScore(sut)).thenReturn(4);
            assertTrue(sut.getMaoDeOnzeResponse());
        }

        @Test
        @DisplayName("Should accept mao de onze if has one card higher than 10 and remaining worth at least 15 if opponent has 8 or more points")
        void shouldAcceptMaoDeOnzeIfHasOneCardHigherThan10AndRemainingWorthAtLeast15IfOpponentHas8OrMorePoints() {
            sut.setCards(List.of(new Card(3, Suit.HEARTS), new Card(1, Suit.DIAMONDS), new Card(5, Suit.SPADES)));
            when(intel.getVira()).thenReturn(new Card(4, Suit.CLUBS));
            when(intel.getOpponentScore(sut)).thenReturn(8);
            assertTrue(sut.getMaoDeOnzeResponse());
        }

        @Test
        @DisplayName("Should not accept mao de onze otherwise")
        void shouldNotAcceptMaoDeOnzeOtherwise() {
            when(intel.getVira()).thenReturn(new Card(4, Suit.CLUBS));
            assertAll(
                    () -> {
                        sut.setCards(List.of(new Card(3, Suit.HEARTS), new Card(1, Suit.DIAMONDS), new Card(5, Suit.SPADES)));
                        when(intel.getOpponentScore(sut)).thenReturn(5);
                        assertFalse(sut.getMaoDeOnzeResponse());
                    },
                    () -> {
                        sut.setCards(List.of(new Card(3, Suit.HEARTS), new Card(1, Suit.DIAMONDS), new Card(3, Suit.SPADES)));
                        when(intel.getOpponentScore(sut)).thenReturn(10);
                        assertFalse(sut.getMaoDeOnzeResponse());
                    }
            );
        }
    }
}