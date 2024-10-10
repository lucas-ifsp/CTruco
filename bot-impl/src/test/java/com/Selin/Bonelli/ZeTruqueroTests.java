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

/* 'zeTruquero' bot with didactic propose. Code by Lucas Selin and Pedro Bonelli */

package com.Selin.Bonelli;
import com.Selin.Bonelli.zetruquero.Zetruquero;
import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZeTruqueroTests
{
    private Zetruquero zetruquero;

    @BeforeEach
    public void InitTestClass()
    {
        zetruquero = new Zetruquero();
    }

    @Nested
    @DisplayName("getMaoDeOnzeResponse")
    class getMaoDeOnzeResponseTests
    {

    }

    @Nested
    @DisplayName("decideIfRaises")
    class decideIfRaisesTests
    {

    }

    @Nested
    @DisplayName("chooseCard")
    class chooseCardTests
    {

    }

    @Nested
    @DisplayName("getRaiseResponse")
    class getRaiseResponseTests
    {

    }

    @Nested
    @DisplayName("Testes para analise da qualidade das cartas")
    class CardPowerAnalysis {
        @DisplayName("Deve retornar que a mao atual do bot tem um zap")
        @Test
        public void ShouldReturnZapInHands()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

            assertThat(zetruquero.zapInHand(trucoCards, vira)).isTrue();
        }

        @DisplayName("Deve retornar que a mao atual do bot tem alguma carta realeza")
        @Test
        public void ShouldReturnRoyaltyInHands()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            assertThat(zetruquero.royaltyCardInHand(trucoCards)).isTrue();
        }

        @DisplayName("Deve retornar que a mao atual do bot tem alguma carta considerada forte")
        @Test
        public void ShouldReturnStrongInHands()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
            TrucoCard vira2 = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            assertThat(zetruquero.strongHand(trucoCards, vira)).isTrue();
            assertThat(zetruquero.strongHand(trucoCards, vira2)).isFalse();
        }
    }

    @DisplayName("Deve escolher a carta mais forte ao jogar primeiro")
    @Test
    public void shouldChooseStrongestCardWhenPlayingFirst() {
        List<TrucoCard> trucoCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

        // O bot deve escolher o Rei de Espadas
        TrucoCard expectedCard = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
        assertThat(zetruquero.chooseCard(mockIntel(trucoCards, vira))).isEqualTo(CardToPlay.of(expectedCard));
    }

    private GameIntel mockIntel(List<TrucoCard> trucoCards, TrucoCard vira) {
        GameIntel intel = mock(GameIntel.class);
        when(intel.getCards()).thenReturn(trucoCards);
        when(intel.getVira()).thenReturn(vira);
        return intel;
    }

    @DisplayName("Deve escolher a carta mais fraca quando o adversário jogou uma carta forte")
    @Test
    public void shouldChooseWeakestCardWhenOpponentPlaysStrongCard() {
        List<TrucoCard> trucoCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

        GameIntel intel = mockIntel(trucoCards, vira);
        when(intel.getOpponentCard()).thenReturn(Optional.of(opponentCard));

        // Ecolhe a carta mais fraca como descarte
        TrucoCard expectedCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
        assertThat(zetruquero.chooseCard(intel)).isEqualTo(CardToPlay.of(expectedCard));
    }


}
