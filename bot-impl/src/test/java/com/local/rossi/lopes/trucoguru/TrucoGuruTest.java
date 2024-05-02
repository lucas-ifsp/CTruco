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

// Authors: Juan Rossi e Guilherme Lopes

package com.local.rossi.lopes.trucoguru;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrucoGuruTest {
    @Nested
    @DisplayName("DecideIfRaisesTests")
    class DecideIfRaisesTestes {
        TrucoGuru trucoGuru = new TrucoGuru();

        @Test
        @DisplayName("Should raise If won last round and has a strong card")
        void shouldRaiseIfWonLastRoundAndHasStrongCardTest() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.decideIfRaises(intel)).isTrue();
        }

        @Test
        @DisplayName("Should not raise If won last round but does not have a strong card")
        void shouldNotRaiseIfWonLastRoundAndDoesNotHaveStrongCardTest() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("Should not raise if bot score is 11")
        void shouldNotRaiseIfBotScoreIsElevenTest() {
          TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
          GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(List.of(), List.of(), vira, 1)
            .botInfo(List.of(), 11)
            .opponentScore(0)
            .build();

          assertThat(trucoGuru.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("Should not raise if opponent score is 11")
        void shouldNotRaiseIfOpponentIsElevenTest() {
          TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
          GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(List.of(), List.of(), vira, 1)
            .botInfo(List.of(), 0)
            .opponentScore(11)
            .build();

          assertThat(trucoGuru.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("Should raise if has cama de gato on second round")
        void shouldRaiseIfHasCamaDeGatoOnSecondRoundTest() {
          TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
          List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST);

          List<TrucoCard> openCards = List.of(vira);
          List<TrucoCard> botCards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

          GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(roundResults, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .build();

          assertThat(trucoGuru.decideIfRaises(intel)).isTrue();
        }

        @Test
        @DisplayName("Should not raise on first round")
        void shouldNotRaiseOnFirstRoundTest() {
          TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
          List<GameIntel.RoundResult> roundResults = List.of();

          List<TrucoCard> openCards = List.of(vira);
          List<TrucoCard> botCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

          GameIntel intel = GameIntel.StepBuilder.with()
            .gameInfo(roundResults, openCards, vira, 1)
            .botInfo(botCards, 0)
            .opponentScore(0)
            .build();

          assertThat(trucoGuru.decideIfRaises(intel)).isFalse();
        }
    }

    @Nested
    @DisplayName("GetRaiseResponseTests")
    class GetRaiseResponseTests{
        TrucoGuru trucoGuru = new TrucoGuru();
        @Test
        @DisplayName("Should accept if hand points is twelve")
        void shouldAcceptIfHandPointsIsTwelveTest(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 12)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.getRaiseResponse(intel)).isZero();
        }

        @Test
        @DisplayName("Should raise if has casal maior")
        void shouldRaiseIfHasCasalMaiorTest(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.getRaiseResponse(intel)).isOne();
        }

        @Test
        @DisplayName("Should raise if has casal menor")
        void shouldRaiseIfHasCasalMenorTest(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.getRaiseResponse(intel)).isOne();
        }

        @Test
        @DisplayName("Should accept if the opponent's score is 11 by dafault")
        void shouldAcceptIfOpponentScoreIs11Test(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 5)
                    .botInfo(botCards, 0)
                    .opponentScore(11)
                    .build();

            assertThat(trucoGuru.getRaiseResponse(intel)).isZero();
        }

        @Test
        @DisplayName("Should decline if hand is not strong")
        void shouldDeclineIfHandIsNotStrongTest(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.getRaiseResponse(intel)).isNegative();
        }

        @Test
        @DisplayName("Should raise if hand is strong")
        void shouldRaiseIfHandIsStrongTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.getRaiseResponse(intel)).isOne();
        }

        @Test
        @DisplayName("Should accept when win the first round or last round and has an attack card")
        void shouldAcceptWhenWinTheFirstRoundOrLastRoundAndHasAttackCardTest(){
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            assertThat(trucoGuru.getRaiseResponse(intel)).isZero();
        }
    }

    @Nested
    @DisplayName("GetMaoDeOnzeResponseTests")
    class GetMaoDeOnzeResponseTests{
        TrucoGuru trucoGuru = new TrucoGuru();
        @Test
        @DisplayName("Should accept mao de onze if has casal maior")
        public void shouldAcceptMaoDeOnzeIfHasCasalMaiorTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(3)
                    .build();

            assertThat(trucoGuru.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should accept mao de onze if has casal menor")
        public void shouldAcceptMaoDeOnzeIfHasCasalMenorTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(3)
                    .build();

            assertThat(trucoGuru.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should decline mao de onze when hand is weak")
        public void shouldDeclineMaoDeOnzeWhenHandIsWeakTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(3)
                    .build();

            assertThat(trucoGuru.getMaoDeOnzeResponse(intel)).isFalse();
        }

        @Test
        @DisplayName("Should accept mao de onze when winning by 7 or more and have strong card")
        public void shouldAcceptMaoDeOnzeWhenWinningBy7orMoreAndHaveStrongCardTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(2)
                    .build();

            assertThat(trucoGuru.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should accept mao de onze when winning by 4 or less and have strong hand")
        public void shouldAcceptMaoDeOnzeWhenWinningBy4orLessAndHaveStrongHandTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(8)
                    .build();

            assertThat(trucoGuru.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should accept mao de onze when score equals to opponent")
        public void shouldAcceptMaoDeOnzeWhenScoresEqualsToOpponentTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(11)
                    .build();

            assertThat(trucoGuru.getMaoDeOnzeResponse(intel)).isTrue();
        }

        @Test
        @DisplayName("Should decline mao de onze when winning by 5 or less when has no double manilhas")
        public void shouldOnlyAcceptMaoDeOnzeWhenWinningBy5OrLessWhenHasDoubleManilhasTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(8)
                    .build();

            assertThat(trucoGuru.getMaoDeOnzeResponse(intel)).isTrue();
        }
    }

    @Nested
    @DisplayName("ChooseCard")
    class ChooseCardTest {
        TrucoGuru trucoGuru = new TrucoGuru();

        @Test
        @DisplayName("Should use weakest card on first round if have casal maior")
        void shouldRaiseIfWonLastRoundAndHasStrongCardTest() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

            final CardToPlay card = trucoGuru.chooseCard(intel);
            assertThat(intel.getRoundResults().size()).isZero();
            assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), card.content());
        }

        @Test
        @DisplayName("Should use strongest card if is first round and is first to play")
        void shouldUseStrongestCardIfIsFirstRoundAndIsFirstToPlay() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();

            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            final CardToPlay card = trucoGuru.chooseCard(intel);
            assertThat(intel.getRoundResults().size()).isZero();
            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), card.content());
        }

        @Test
        @DisplayName("Should use weakest card that wins the round if lost the first round")
        void shouldUseWeakestCardThatWinsTheRounfIfLostFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.LOST);
            TrucoCard opponentCard =  TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            final CardToPlay card = trucoGuru.chooseCard(intel);
            assertThat(intel.getRoundResults().size()).isOne();
            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), card.content());
        }

        @Test
        @DisplayName("Should use weakest card if won first round")
        void shouldUseWeakestCardIfWonFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON);
            List<TrucoCard> openCards = List.of(vira);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .build();

            final CardToPlay card = trucoGuru.chooseCard(intel);
            assertThat(intel.getRoundResults().size()).isOne();
            assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), card.content());
        }

        @Test
        @DisplayName("Should use weakest card to win if starts second in first round")
        void shouldUseWeakestCardToWinIfStartsSecondInFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);
            TrucoCard opponentCard =  TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            final CardToPlay card = trucoGuru.chooseCard(intel);
            assertThat(intel.getRoundResults().size()).isZero();
            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), card.content());
        }

        @Test
        @DisplayName("Should try to draw first round if does not have card to win")
        void shouldTryToDrawFirstRoundIfDoesNotHaveCardToWin() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            List<GameIntel.RoundResult> roundResults = List.of();
            List<TrucoCard> openCards = List.of(vira);
            TrucoCard opponentCard =  TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
            );

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(roundResults, openCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            final CardToPlay card = trucoGuru.chooseCard(intel);
            assertThat(intel.getRoundResults().size()).isZero();
            assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), card.content());
        }
    }
}
