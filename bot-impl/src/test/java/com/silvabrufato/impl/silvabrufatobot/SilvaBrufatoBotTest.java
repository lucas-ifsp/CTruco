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

package com.silvabrufato.impl.silvabrufatobot;

import java.util.List;
import java.util.Optional;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
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
    @DisplayName("First round")
    class FirstRoundTests{
        @Test
        @DisplayName("Should win the first round if possible")
        public void ShouldWinTheFirstRoundIfPossible() {
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
            
            assertThat(sut.chooseCard(gameIntel).content().
                    compareValueTo(
                            gameIntel.getOpponentCard().get(),
                            gameIntel.getVira())
            ).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should throw the lowest card when start the First hand")
        public void ShouldThrowTheLowestCardWhenStartTheFirstHand() {
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
    }

    @Nested
    @DisplayName("Second hand")
    class SecondRoundTests{
        @Test
        @DisplayName("ShouldWinTheSecondRoundIfPossible")
        void shouldWinTheSecondHandIfPossible() {
            when(gameIntel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS))
            );
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            assertThat(sut.chooseCard(gameIntel).content().
                    compareValueTo(
                            gameIntel.getOpponentCard().get(),
                            gameIntel.getVira())
            ).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Third round")
    class ThirdRoundTests {
        @Test
        @DisplayName("ShouldWinTheThreeRoundIfPossible")
        void shouldWinTheThreeRoundIfPossible() {
            when(gameIntel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST));
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES))
            );
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
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
}
