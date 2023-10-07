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
    @DisplayName("First hand")
    class FirstHandTests{
        @Test
        @DisplayName("Should win the first hand if possible")
        public void ShouldWinTheFirstHandIfPossible() {
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getOpponentCard()).thenReturn(Optional.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS))
            );
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));
            assertThat(sut.chooseCard(gameIntel).content().
                    compareValueTo(
                            gameIntel.getOpponentCard().get(),
                            gameIntel.getVira())
            ).isGreaterThan(0);
        }

        @Test
        @DisplayName("shouldTryToWinTheFirstHandWithoutUsingManilha")
        void shouldTryToWinTheFirstHandWithoutUsingManilha() {
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
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
        @DisplayName("mustTryToWinTheFirstRoundWithTheHighestCardIfThereIsNoManilha")
        void mustTryToWinTheFirstRoundWithTheHighestCardIfThereIsNoManilha() {
            when(gameIntel.getRoundResults()).thenReturn(List.of());
            when(gameIntel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            when(gameIntel.getCards()).thenReturn(List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
            ));
            assertThat(sut.chooseCard(gameIntel).content()).isEqualTo(TrucoCard.of(CardRank.KING, CardSuit.SPADES));
        }

    }

    @Nested
    @DisplayName("Second hand")
    class SecondHandTests{
        @Test
        @DisplayName("ShouldWinTheSecondHandIfPossible")
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
    @DisplayName("Third hand")
    class ThirdHandTests {
        @Test
        @DisplayName("ShouldWinTheThreeHandIfPossible")
        void shouldWinTheThreeHandIfPossible() {
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
}
