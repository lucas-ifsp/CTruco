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
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private GameIntel mockIntel(List<TrucoCard> trucoCards, TrucoCard vira) {
        GameIntel intel = mock(GameIntel.class);
        when(intel.getCards()).thenReturn(trucoCards);
        when(intel.getVira()).thenReturn(vira);
        return intel;
    }


    @Nested
    @DisplayName("getMaoDeOnzeResponse")
    class getMaoDeOnzeResponseTests
    {
        @DisplayName("Decidir jogar para 2 manilhas")
        @Test
        public void ShouldPlayTwoManilhas()
        {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @DisplayName("Decidir jogar com zap")
        @Test
        public void ShouldPlayWithZap()
        {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @DisplayName("Decidir jogar com 1 manilha e duas cartas fortes")
        @Test
        public void ShouldPlayWithManilhaStrong()
        {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getMaoDeOnzeResponse(intel)).isTrue();
        }
    }

    @Nested
    @DisplayName("decideIfRaises")
    class decideIfRaisesTests
    {
        @DisplayName("Deve aumentar a pedida de pontos caso tenha ganho um round e seja o ultimo, com carta forte")
        @Test
        public void ShouldCallWithStrongCard()
        {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @DisplayName("Deve nao aumentar a pedida de pontos caso nao tenha cartas fortes ou manilhas")
        @Test
        public void ShouldNotCallWithWeakCards()
        {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getMaoDeOnzeResponse(intel)).isFalse();
        }
    }

    @Nested
    @DisplayName("chooseCard")
    class chooseCardTests
    {
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

        @DisplayName("Escolhe a carta menor para o primeiro round caso tenha zap")
        @Test
        public void shouldChooseWeakestCard()
        {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            final CardToPlay card = zetruquero.chooseCard(intel);
            assertEquals(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), card.content());
        }

        @DisplayName("Se nao tiver manilha deve abrir o jogo com a carta mais forte")
        @Test
        public void shouldChooseStrongCardWithoutManilha()
        {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            final CardToPlay card = zetruquero.chooseCard(intel);
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), card.content());
        }

        @DisplayName("Se tiver feito um ponto, jogar a carta mais forte (para o caso sem zap)")
        @Test
        public void shouldChooseStrongCardWithoutZap()
        {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            final CardToPlay card = zetruquero.chooseCard(intel);
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), card.content());
        }

        @DisplayName("Se tiver feito um ponto, jogar a carta mais fraca (para o caso de ter zap)")
        @Test
        public void shouldChooseWeakCardWithZap()
        {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            final CardToPlay card = zetruquero.chooseCard(intel);
            assertEquals(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), card.content());
        }

        @DisplayName("Se tiver em maos o casal maior, comecar com a mais fraca")
        @Test
        public void shouldChooseWeakCardWithTwoStrongest()
        {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            final CardToPlay card = zetruquero.chooseCard(intel);
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), card.content());
        }
    }

    @Nested
    @DisplayName("getRaiseResponse")
    class getRaiseResponseTests
    {
        @DisplayName("Nao deve aceitar o aumento de pontos para caso nao tenha uma manilha ou 2 cartas bem fortes")
        @Test
        public void shouldNotAcceptWithWeakHand()
        {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getRaiseResponse(intel)).isZero();
        }

        @DisplayName("Nao aceitar a pedida se ja tenha perdido o primeiro round")
        @Test
        public void shouldNotAcceptWithLost()
        {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getRaiseResponse(intel)).isZero();
        }

        @DisplayName("Aumentar a pedida caso tenha uma vitoria e o zap")
        @Test
        public void shouldNotAcceptWithWinAndZap()
        {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getRaiseResponse(intel)).isOne();
        }

        @DisplayName("Aumentar a pedida caso seja round 2 e tenha uma vitoria e duas manilhas")
        @Test
        public void shouldNotAcceptWithWinAndTwoManilha()
        {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(zetruquero.getRaiseResponse(intel)).isOne();
        }
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

        @DisplayName("Deve retornar que a a mao atual do bot tem uma carta fote, caso sem manilha")
        @Test
        public void ShouldReturnTrueForGoodCards()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

            assertThat(zetruquero.strongCardInHand(trucoCards, vira)).isTrue();
        }

        @DisplayName("Deve retornar que a a mao atual do bot tem o casal maior")
        @Test
        public void ShouldReturnTrueForTwoStrongest()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

            assertThat(zetruquero.twoStrongestManilhas(trucoCards, vira)).isTrue();
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

    @DisplayName("Deve recusar mão de onze se tiver duas cartas fracas")
    @Test
    public void shouldRejectMaoDeOnzeWithTwoWeakCards() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
        );
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

        GameIntel intel = mockIntel(botCards, vira);
        assertThat(zetruquero.getMaoDeOnzeResponse(intel)).isFalse();
    }

    @DisplayName("Deve aumentar a pedida se tiver uma manilha e uma carta alta")
    @Test
    public void shouldRaiseWithManilhaAndHighCard() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), // Manilha
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
        );
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

        GameIntel intel = mockIntel(botCards, vira);
        assertThat(zetruquero.decideIfRaises(intel)).isTrue();
    }

    @DisplayName("Deve escolher a carta intermediária se for a segunda rodada e tiver ganho a primeira")
    @Test
    public void shouldChooseMiddleCardOnSecondRoundWithWin() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
        );
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);

        GameIntel intel = mockIntel(botCards, vira);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));

        assertThat(zetruquero.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
    }

    @DisplayName("Deve recusar aumentar a pedida com cartas medianas e já perdeu o primeiro round")
    @Test
    public void shouldNotRaiseWithMediumCardsAndFirstRoundLost() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
        );
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        GameIntel intel = mockIntel(botCards, vira);
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));

        assertThat(zetruquero.getRaiseResponse(intel)).isZero();
    }

    @DisplayName("Deve aceitar aumentar a pedida se tiver zap e manilha")
    @Test
    public void shouldRaiseWithZapAndManilha() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), // Manilha
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS) // Zap
        );
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

        GameIntel intel = mockIntel(botCards, vira);
        assertThat(zetruquero.getRaiseResponse(intel)).isOne();
    }

    @DisplayName("Deve jogar a carta mais forte se não houver manilha e tiver duas cartas fortes")
    @Test
    public void shouldPlayStrongestCardWithoutManilhaAndTwoStrongCards() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
        );
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

        GameIntel intel = mockIntel(botCards, vira);
        assertThat(zetruquero.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
    }

    @DisplayName("Deve jogar a manilha mais fraca se tiver as duas manilhas mais fortes")
    @Test
    public void shouldPlayWeakerManilhaWithTwoStrongest() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        GameIntel intel = mockIntel(botCards, vira);
        assertThat(zetruquero.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
    }


}
