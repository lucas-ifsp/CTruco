/*
 *  Copyright (C) 2023 João Pedro da Silva and Renan Brufato
 *  Contact: jps <dot> spj <at> gmail <dot> com 
 *  Contact: brufato17 <dot> renan <at> gmail <dot> com
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

package com.local.silvabrufato.impl.silvabrufatobot;

import java.util.List;
import java.util.Optional;

import com.bueno.spi.model.*;
import com.bueno.spi.model.GameIntel.RoundResult;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

public class SilvaBrufatoBotTest {

    private static SilvaBrufatoBot sut;
    private static GameIntel gameIntel;

    @BeforeAll
    public static void setSut() {
        if (sut == null)
            sut = new SilvaBrufatoBot();
    }

    @BeforeEach
    public void setGameIntel() {
        gameIntel = mock(GameIntel.class);
    }

    @Nested
    @DisplayName("chooseCard first round")
    class FirstRoundChooseCardTests{
        @Test
        @DisplayName("Should win the first round if possible using the highest card when opponent start the round")
        public void ShouldWinTheFirstRoundIfPossibleUsingTheHighestCardWhenTheOpponentStartTheRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));

            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            );
        }

        @Test
        @DisplayName("Should win the first round if possible using the lowest card when opponent start the round")
        public void ShouldWinTheFirstRoundIfPossibleUsingTheLowestCardWhenTheOpponentStartTheRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );
        }

        @Test
        @DisplayName("Should win the first round if possible using the middle card")
        public void ShouldWinTheFirstRoundIfPossibleUsingTheMiddleCardWhenTheOpponentStartTheRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );
        }

        @Test
        @DisplayName("Should win the first round if possible using a manilha when opponent start the round")
        public void ShouldWinTheFirstRoundIfPossibleUsingAManilhaWhenTheOpponentStartTheRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            );
        }

        @Test
        @DisplayName("Should win the first round if possible using the lowest manilha when opponent start the round")
        public void ShouldWinTheFirstRoundIfPossibleUsingTheLowestManilhaWhenTheOpponentStartTheRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
            );
        }

        @Test
        @DisplayName("Should loose the first round throwing the lowest card when opponent start the round")
        public void ShouldLooseTheFirstRoundThrowingThelowestCardWhenTheOpponentStartTheRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            );
        }

        @Test
        @DisplayName("Should throw the lowest card if is not possible to win the first round when opponent start the round")
        public void shouldThrowTheLowetsCardIfisNotPossibleToWinTheFirstRoundWhenTheOpponentStartTheRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );
        }

        @Test
        @DisplayName("Should draw the first round when opponent start the round")
        public void ShouldDrawTheFisrtRoundWhenTheOpponentStartTheRound() {
             when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
            );
        }

        @Test
        @DisplayName("Should throw the lowest card when start the first round and not have a manilha")
        public void ShouldThrowTheLowestCardWhenStartTheFirstRoundAndNotHaveAManilnha() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.empty());
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
        }

        @Test
        @DisplayName("Should start the first round using the lowest manilha if have one or more")
        public void shouldStartTheFirsRoundUsingTheLowestManilhaIfHaveOneOrMore() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.empty());
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)      
            ));
            
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES));
        }

    }

    @Nested
    @DisplayName("chooseCard second round")
    class SecondRoundChooseCardTests{
        @Test
        @DisplayName("Should win the second round if possible")
        public void shouldWinTheSecondRoundIfPossible() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            ));
            assertThat(sut.chooseCard(gameIntel).content().
                    compareValueTo(
                            gameIntel.getOpponentCard().get(),
                            gameIntel.getVira())
            ).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should throw a hidden card if start the round and will not raise")
        public void shouldThrowAHiddenCardIfStartTheRoundAndWillNotRaise() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
            when(gameIntel.getOpponentCard()).thenReturn(Optional.empty());
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            ));
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                CardToPlay.discard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)).content()
                );
        }

        @Test
        @DisplayName("Should throw a manilha if have and start the second round")
        public void shouldThrowAManilhaIfHaveAndStartTheSecondRound() {
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));
            when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
            when(gameIntel.getOpponentCard()).thenReturn(Optional.empty());
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            ));
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(
                CardToPlay.discard(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)).content()
                );
        }
    }

    @Nested
    @DisplayName("chooseCard third round")
    class ThirdRoundChooseCardTests {
        @Test
        @DisplayName("ShouldWinTheThreeRoundIfPossible")
        void shouldWinTheThreeRoundIfPossible() {
            when(gameIntel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES))
            );
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES))
            );
            assertThat(sut.chooseCard(gameIntel).content().
                    compareValueTo(
                            gameIntel.getOpponentCard().get(),
                            gameIntel.getVira())
            ).isGreaterThan(0);
        }

    }

    @Nested
    @DisplayName("Response to the hand of eleven")
    class ResponseToTheHandOfEleven{
        @Test
        @DisplayName("TheReturnMustBeDifferentFromNull")
        void theReturnMustBeDifferentFromNull() {
            assertThat(sut.getMaoDeOnzeResponse(gameIntel)).isNotNull();
        }

        @ParameterizedTest
        @ValueSource(ints = {9,10})
        @DisplayName("botMustRunFromTheHandOfElevenIfTheOpponentHasNineOrMorePoints")
        void botMustRunFromTheHandOfElevenIfTheOpponentHasNineOrMorePoints(int points) {
            when(gameIntel.getOpponentScore()).thenReturn(points);
            assertThat(sut.getMaoDeOnzeResponse(gameIntel)).isFalse();
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 4 ,6 ,8})
        @DisplayName("botMustAcceptHandOfElevenIfItHasManilhaAndAtLeastOneCardGreaterThanOrEqualToACEAndTheOpponentHasLessThanNinePoints")
        void botMustAcceptHandOfElevenIfItHasManilhaAndAtLeastOneCardGreaterThanOrEqualToACEAndTheOpponentHasLessThanNinePoints(int points) {
            when(gameIntel.getOpponentScore()).thenReturn(points);
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK,CardSuit.SPADES));
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            ));
            assertThat(sut.getMaoDeOnzeResponse(gameIntel)).isTrue();
        }

        @Test
        @DisplayName("shouldAcceptHandOfElevenIfHaveTwoCardsGreaterThanOrEqualToACEandDoNotHaveAShackle")
        void shouldAcceptHandOfElevenIfHaveTwoCardsGreaterThanOrEqualToACEandDoNotHaveAShackle() {
            when(gameIntel.getOpponentScore()).thenReturn(7);
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR,CardSuit.CLUBS));
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            ));
            assertThat(sut.getMaoDeOnzeResponse(gameIntel)).isTrue();
        }

    }

    @Nested
    @DisplayName("Response to getRaiseResponse")
    class ResponseToGetRaiseResponse{
        @Nested
        @DisplayName("First round")
        class FirstRound{
            @Test
            @DisplayName("theReturnMustBeBetweenMinusOneAndOne")
            void theReturnMustBeBetweenMinusOneAndOne() {
                when(gameIntel.getRoundResults()).thenReturn(List.of());
                assertThat(sut.getRaiseResponse(gameIntel)).isIn(-1,0,1);
            }

            @Test
            @DisplayName("inTheFirstRoundShouldNotAskForAPointIncrease")
            void inTheFirstRoundShouldNotAskForAPointIncrease() {
                when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
                when(gameIntel.getRoundResults()).thenReturn(List.of());
                when(gameIntel.getCards()).thenReturn(List.of(
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES)));
                assertThat(sut.getRaiseResponse(gameIntel)).isNotPositive();
            }

            @Test
            @DisplayName("inTheFirstRoundShouldReturnZeroIfHaveZAPAndThree")
            void inTheFirstRoundShouldReturnZeroIfHaveZAPAndThree() {
                when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
                when(gameIntel.getRoundResults()).thenReturn(List.of());
                when(gameIntel.getCards()).thenReturn(List.of(
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)));
                assertThat(sut.getRaiseResponse(gameIntel)).isZero();
            }

            @Test
            @DisplayName("inTheFirstRoundShouldReturnZeroIfHaveCOPASAndThree")
            void inTheFirstRoundShouldReturnZeroIfHaveCopasAndThree() {
                when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
                when(gameIntel.getRoundResults()).thenReturn(List.of());
                when(gameIntel.getCards()).thenReturn(List.of(
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                assertThat(sut.getRaiseResponse(gameIntel)).isZero();
            }

            @Test
            @DisplayName("inTheFirstRoundShouldReturnZeroIfHaveManilhaAndManilha")
            void inTheFirstRoundShouldReturnZeroIfHaveManilhaAndManilha() {
                when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
                when(gameIntel.getRoundResults()).thenReturn(List.of());
                when(gameIntel.getCards()).thenReturn(List.of(
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                assertThat(sut.getRaiseResponse(gameIntel)).isZero();
            }
        }

        @Nested
        @DisplayName("Second round")
        class SecondRound{
            @Nested
            @DisplayName("If lose round one")
            class IfLoseRoundOne{
                @Test
                @DisplayName("theReturnMustBeBetweenMinusOneAndOne")
                void theReturnMustBeBetweenMinusOneAndOneIfLostRoundOne() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST));
                    assertThat(sut.getRaiseResponse(gameIntel)).isIn(-1,0,1);
                }

                @Test
                @DisplayName("shouldReturnOneIfYouHaveHeartsAndSpadesAndMissedTheFirst")
                void shouldReturnOneIfYouHaveHeartsAndSpadesAndMissedTheFirst() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isOne();
                }

                @Test
                @DisplayName("shouldReturnZeroIfThereAreHeartsAndDiamonds")
                void shouldReturnZeroIfThereAreHeartsAndDiamonds() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }
            }
            @Nested
            @DisplayName("If won round one")
            class IfWonRoundOne{
                @Test
                @DisplayName("theReturnMustBeBetweenMinusOneAndOneIfWonRoundOne")
                void theReturnMustBeBetweenMinusOneAndOneIfWonRoundOne() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    assertThat(sut.getRaiseResponse(gameIntel)).isIn(-1,0,1);
                }

                @Test
                @DisplayName("shouldReturnOneIfItHasHearts")
                void shouldReturnOneIfItHasHearts() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isOne();
                }

                @Test
                @DisplayName("shouldReturnOneIfItHasManilhaAndManilha")
                void shouldReturnOneIfItHasManilhaAndManilha() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isOne();
                }

                @Test
                @DisplayName("shouldReturnZeroIfItHasManilhaAndThree")
                void shouldReturnZeroIfItHasManilhaAndThree() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }

                @Test
                @DisplayName("shouldReturnZeroIfItHasTwoThree")
                void shouldReturnZeroIfItHasTwoThree() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }
            }
        }

        @Nested
        @DisplayName("Third round")
        class ThirdRound{
            @Nested
            @DisplayName("If lose round one")
            class IfLoseRoundOne{
                @Test
                @DisplayName("theReturnMustBeBetweenMinusOneAndOne")
                void theReturnMustBeBetweenMinusOneAndOne() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST,RoundResult.WON));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isIn(-1,0,1);
                }

                @Test
                @DisplayName("theReturnMustBeZeroIfHaveSpadesAndOuros")
                void theReturnMustBeZeroIfHaveSpades() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST,RoundResult.WON));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }

                @Test
                @DisplayName("theReturnMustBeZeroIfHaveOuros")
                void theReturnMustBeZeroIfHaveOuros() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST,RoundResult.WON));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }
            }

            @Nested
            @DisplayName("If won round one")
            class IfWonRoundOne{
                @Test
                @DisplayName("theReturnMustBeBetweenMinusOneAndOne")
                void theReturnMustBeBetweenMinusOneAndOne() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON,RoundResult.LOST));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isIn(-1,0,1);
                }

                @Test
                @DisplayName("theReturnMustBeZeroIfHaveSpades")
                void theReturnMustBeZeroIfHaveSpades() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON,RoundResult.LOST));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }

                @Test
                @DisplayName("theReturnMustBeZeroIfHaveOuros")
                void theReturnMustBeZeroIfHaveOuros() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON,RoundResult.LOST));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }

                @Test
                @DisplayName("theReturnMustBeZeroIfHaveThree")
                void theReturnMustBeZeroIfHaveThree() {
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON,RoundResult.LOST));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
                    assertThat(sut.getRaiseResponse(gameIntel)).isZero();
                }
            }
        }
    }

    @Nested
    @DisplayName("Response to decideIfRaises")
    class ResponsedecideIfRaises{
        @Nested
        @DisplayName("First round")
        class FirstRound{
            @Nested
            @DisplayName("if the opponent starts")
            class ifTheOpponentStarts{
                @Test
                @DisplayName("theReturnMustBeDifferentFromNull")
                void theReturnMustBeDifferentFromNull() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of());
                    assertThat(sut.decideIfRaises(gameIntel)).isNotNull();
                }

                @Test
                @DisplayName("shouldReturnTrueIfItIsPossibleWinTheOpponentCardWithACardOtherThanZapAndHasZap")
                void shouldReturnTrueIfItIsPossibleWinTheOpponentCardWithACardOtherThanZapAndHasZap() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of());
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
                    when(gameIntel.getOpenCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), //vira
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS) //opponent card
                    ));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

                @Test
                @DisplayName("shouldReturnTrueIfItIsPossibleWinTheOpponentCardWithACardOtherThanCopasAndHasCopas")
                void shouldReturnTrueIfItIsPossibleWinTheOpponentCardWithACardOtherThanCopasAndHasCopas() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of());
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
                    when(gameIntel.getOpenCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), //vira
                            TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS) //opponent card
                    ));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

                @Test
                @DisplayName("shouldReturnTrueIfItIsPossibleWinTheOpponentCardWithACardOtherThanSpadesAndHasSpades")
                void shouldReturnTrueIfItIsPossibleWinTheOpponentCardWithACardOtherThanSpadesAndHasSpades() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of());
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
                    when(gameIntel.getOpenCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), //vira
                            TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS) //opponent card
                    ));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }
            }

            @Nested
            @DisplayName("if the SilvaBrufatoBot starts")
            class ifTheSilvaBrufatoBotStarts{
                @Test
                @DisplayName("shouldReturnTrueIfBotHasZapAndCopas")
                void shouldReturnTrueIfBotHasZapAndCopas() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of());
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

                @Test
                @DisplayName("shouldReturnTrueIfBotHasZapAndSpades")
                void shouldReturnTrueIfBotHasZapAndSpades() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of());
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

                @Test
                @DisplayName("shouldReturnTrueIfBotHasCopasAndSpades")
                void shouldReturnTrueIfBotHasCopasAndSpades() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of());
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }
            }
        }

        @Nested
        @DisplayName("Second round")
        class SecondRound{
            @Nested
            @DisplayName("if the opponent starts")
            class ifTheOpponentStarts{
                @Test
                @DisplayName("shouldReturnTrueIfBotHasCopas")
                void shouldReturnTrueIfBotHasCopas() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST));
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

                @Test
                @DisplayName("shouldReturnTrueIfBotHasTwoManilhas")
                void shouldReturnTrueIfBotHasTwoManilhas() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST));
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

            }

            @Nested
            @DisplayName("if the SilvaBrufatoBot starts")
            class ifTheSilvaBrufatoBotStarts{
                @Test
                @DisplayName("shouldReturnTrueIfBotHasZap")
                void shouldReturnTrueIfBotHasZap() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

                @Test
                @DisplayName("shouldReturnTrueIfBotHasCopas")
                void shouldReturnTrueIfBotHasCopas() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }

                @Test
                @DisplayName("shouldReturnTrueIfBotHasSpades")
                void shouldReturnTrueIfBotHasSpades() {
                    when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.WON));
                    when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
                    when(gameIntel.getCards()).thenReturn(List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)));
                    assertThat(sut.decideIfRaises(gameIntel)).isTrue();
                }
            }
        }

        @Nested
        @DisplayName("Third round")
        class ThirdRound{
            @Test
            @DisplayName("shouldReturnTrueIfBotHasSpades")
            void shouldReturnTrueIfBotHasSpades() {
                when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST, RoundResult.WON));
                when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));
                when(gameIntel.getCards()).thenReturn(List.of(
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES)));
                assertThat(sut.decideIfRaises(gameIntel)).isTrue();
            }

            @Test
            @DisplayName("shouldReturnTrueIfBotHasDiamonds")
            void shouldReturnTrueIfBotHasDiamonds() {
                when(gameIntel.getRoundResults()).thenReturn(List.of(RoundResult.LOST, RoundResult.WON));
                when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));
                when(gameIntel.getCards()).thenReturn(List.of(
                        TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)));
                assertThat(sut.decideIfRaises(gameIntel)).isTrue();
            }
        }
    }
}
