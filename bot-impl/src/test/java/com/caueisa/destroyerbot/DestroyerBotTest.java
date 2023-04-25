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

// Authors: Cauê Gastaldi and Isabela Forti

package com.caueisa.destroyerbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DestroyerBotTest {

    @Mock
    private GameIntel intel;
    private final BotServiceProvider sut = new DestroyerBot();

    private TrucoCard vira;
    private List<TrucoCard> cards;
    private Optional<TrucoCard> opponentCard;
    private List<GameIntel.RoundResult> results;

    @Nested
    @DisplayName("When in first round")
    class FirstRoundTest {
        @Nested
        @DisplayName("When playing a card")
        class ChooseCardTest {
            @Test
            @DisplayName("Should play the lowest card that is stronger than the opponent card")
            void shouldPlayTheLowestCardThatIsStrongerThanOpponentCard() {
                vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
                opponentCard = Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getOpponentCard()).thenReturn(opponentCard);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
            }

            @Test
            @DisplayName("Should play the lowest card between all cards available to be played if the bot doesn't have "
                       + "a card that beats the opponent card")
            void shouldPlayTheLowestCardBetweenAllCardsAvailableToBePlayed() {
                vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
                opponentCard = Optional.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getOpponentCard()).thenReturn(opponentCard);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
            }

            @Test
            @DisplayName("Should play a card with rank equals to Three if it has it in hand and it is the first to play.")
            void shouldPlayACardWithRankEqualsToThreeInTheFirstRoundIfItIsTheFirstToPlay(){
                vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            }

            @Test
            @DisplayName("Should play the lowest rank manilha if it has at least two of them in hand in the first round " +
                    "and it is the first to play.")
            void shouldPlayLowestRankManilhaIfItHasAtLeastTwoOfThemInHandInTheFirstRoundAndItIsTheFirstToPlay(){
                vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
                cards = List.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
            }

            @Test
            @DisplayName("Should play the lowest card between all cards available to be played in the first round if it is the first to play.")
            void shouldPlayTheHighestRankCardBetweenAllCardsAvailableToBePlayed(){
                vira = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
                cards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
            }

            @Test
            @DisplayName("Should play the manilha in the first round if it only has one and if it is the first to play.")
            void shouldPlayTheManilhaInTheFirstRoundIfItIsTheOnlyOneAndIfItIsTheFirstToPlay(){
                vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
                cards = List.of(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
            }
        }

        @Nested
        @DisplayName("When requesting a point raise")
        class DecidesIfRaisesTest {
            @Test
            @DisplayName("Should ask for a raise request if bot has two above rank two")
            void shouldAskForARaiseRequestIfHasTwoCardsAboveRankTwo(){
                vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getOpponentScore()).thenReturn(8);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }

            @Test
            @DisplayName("Should ask for a raise request if bot has three cards above rank two")
            void shouldAskForARaiseRequestIfHasThreeCardsAboveRankTwo(){
                vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                                TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getOpponentScore()).thenReturn(8);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("When in second round")
    class SecondRoundTest {
        @Nested
        @DisplayName("When get a point raise request")
        class DecidesIfRaisesTest {
            @Test
            @DisplayName("Should ask for a point raise request if it has only cards above rank ace and won " +
                         "the first round")
            void shouldAskForAPointRaiseRequestIfHasOnlyCardsAboveRankAceAndWonTheFirstRound() {
                results = List.of(GameIntel.RoundResult.WON);
                vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getOpponentScore()).thenReturn(8);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }

            @Test
            @DisplayName("Should ask for a raise request if bot has one card above rank two")
            void shouldAskForARaiseRequestIfHasOneCardAboveRankTwo(){
                results = List.of(GameIntel.RoundResult.LOST);
                vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getOpponentScore()).thenReturn(8);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }

            @Test
            @DisplayName("Should ask for a raise request if bot has two cards above rank two")
            void shouldAskForARaiseRequestIfHasTwoCardsAboveRankTwo(){
                results = List.of(GameIntel.RoundResult.LOST);
                vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getOpponentScore()).thenReturn(8);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }
        }

        @Nested
        @DisplayName("When playing a card")
        class ChooseCardTest {
            @Test
            @DisplayName("Should play the lowest card that is stronger than the opponent card")
            void shouldPlayTheLowestCardThatIsStrongerThanOpponentCard() {
                results = List.of(GameIntel.RoundResult.LOST);
                vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
                opponentCard = Optional.of(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getOpponentCard()).thenReturn(opponentCard);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
            }

            @Test
            @DisplayName("Should play the card that is equal to the opponent card if the bot doesn't have a card " +
                         "stronger than the opponent card")
            void shouldPlayTheCardThatIsEqualToTheOpponentCard() {
                results = List.of(GameIntel.RoundResult.DREW);
                vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
                opponentCard = Optional.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getOpponentCard()).thenReturn(opponentCard);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));
            }

            @Test
            @DisplayName("Should discard the lowest rank manilha in the second round if it has two of them and " +
                    "it is the first to play.")
            void shouldDiscardLowestRankManilhaInTheSecondRoundIfItHasTwoOfThemAndItIsTheFirstToPlay(){
                vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
                cards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));
            }

            @Test
            @DisplayName("Should play the manilha in second round if it only has one and it is the first to play.")
            void shouldPlayTheManilhaInTheSecondRoundIfItIsTheOnlyOneAndIfItIsTheFirstToPlay(){
                vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.KING, CardSuit.CLUBS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));
            }

            @Test
            @DisplayName("Should discard the lowest rank card available if it has zap in the second round and it is the first to play.")
            void shouldDiscardLowestRankCardAvailableIfItHasZapInTheSecondRoundAndIfItIsTheFirstToPlay(){
                results = List.of(GameIntel.RoundResult.WON);
                vira = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
                cards = List.of(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).value()).isEqualTo(CardToPlay.discard(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)).value());
            }

            @Test
            @DisplayName("Should play the card with rank equals to Three in the second round if it is the first to play.")
            void shouldPlayCardWithRankEqualsToThreeInTheSecondRoundIfItIsTheFirstToPlay(){
                results = List.of(GameIntel.RoundResult.WON);
                vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
                cards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            }

            @Test
            @DisplayName("Should play the highest rank card available in the second round if it is the first to play.")
            void shouldPlayTheHighestRankCardAvailableInTheSecondRoundIfItIsTheFirstToPlay(){
                results = List.of(GameIntel.RoundResult.WON);
                vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
                cards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
            }
        }
    }

    @Nested
    @DisplayName("When in third round")
    class ThirdRoundTest {
        @Nested
        @DisplayName("When get a point raise request")
        class DecidesIfRaisesTest {
            @Test
            @DisplayName("Should ask for a point raise request if it has one card above rank ace")
            void shouldAskForAPointRaiseRequestIfHasOneCardAboveRankAce() {
                results = List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST);
                vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getOpponentScore()).thenReturn(10);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }
        }

        @Nested
        @DisplayName("When playing a card")
        class ChooseCardTest {
            @Test
            @DisplayName("Should play the only card available.")
            void shouldPlayTheOnlyCardAvailable() {
                results = List.of(GameIntel.RoundResult.DREW, GameIntel.RoundResult.DREW);
                vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS));

                when(intel.getRoundResults()).thenReturn(results);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
            }
        }
    }

    @Nested
    @DisplayName("When in any round")
    class AnyRoundTest {
        @Nested
        @DisplayName("When get a point raise request")
        class GetRaiseResponseTest {

            @ParameterizedTest
            @CsvSource({"9", "10"})
            @DisplayName("Should ask for point raise if hand points equals to three and opponent has nine or more points.")
            void shouldAskForPointRaiseIfHandPointsEqualsToThreeAndOpponentHasNineOrMorePoints(int opponentScore){
                when(intel.getHandPoints()).thenReturn(3);
                when(intel.getOpponentScore()).thenReturn(opponentScore);

                assertThat(sut.getRaiseResponse(intel)).isEqualTo(1);
            }

            @ParameterizedTest
            @CsvSource({"6", "11"})
            @DisplayName("Should ask for point raise if hand points equals to six and opponent has six or more points.")
            void shouldAskForPointRaiseIfHandPointsEqualsToSixAndOpponentHasSixOrMorePoints(){
                when(intel.getHandPoints()).thenReturn(6);
                when(intel.getOpponentScore()).thenReturn(6);

                assertThat(sut.getRaiseResponse(intel)).isEqualTo(1);
            }

            @ParameterizedTest
            @CsvSource({"3", "4"})
            @DisplayName("Should accept point raise request if hand points equals to nine and opponent has three or more points.")
            void shouldAcceptPointRaiseRequestIfHandPointsEqualsToNineAndOpponentHasThreeOrMorePoints(){
                when(intel.getHandPoints()).thenReturn(9);
                when(intel.getOpponentScore()).thenReturn(4);

                assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
            }

            @ParameterizedTest
            @CsvSource({"6, 3", "5, 3", "4, 3"})
            @DisplayName("Should accept a point raise request if it has only cards above rank seven and is winning " +
                         "the game by until three points of difference")
            void shouldAcceptPointRaiseRequestIfHasOnlyCardsAboveRankSevenAndIsWinningByUntilThreePoints
                 (int botScore, int opponentScore)
            {
                vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

                when(intel.getScore()).thenReturn(botScore);
                when(intel.getOpponentScore()).thenReturn(opponentScore);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
            }

            @ParameterizedTest
            @CsvSource({"5, 9", "4, 9"})
            @DisplayName("Should accept a point raise request if it is losing the game by four or more " +
                         "points of difference and has only manilhas")
            void shouldAcceptPointRaiseRequestIfIsLosingByFourOrMorePointsAndHasOnlyManilhas
                 (int botScore, int opponentScore)
            {
                vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

                when(intel.getScore()).thenReturn(botScore);
                when(intel.getOpponentScore()).thenReturn(opponentScore);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.getRaiseResponse(intel)).isEqualTo(0);
            }

            @ParameterizedTest
            @CsvSource({"10, 4", "10, 5", "10, 6"})
            @DisplayName("Should re-raise a point raise request if it is winning the game by until six " +
                         "points of difference and has only cards above rank seven")
            void shouldReRaiseAPointRaiseRequestIfIsWinningByUntilSixPointsAndHasOnlyCardsAboveRankSeven
                 (int botScore, int opponentScore)
            {
                vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));

                when(intel.getScore()).thenReturn(botScore);
                when(intel.getOpponentScore()).thenReturn(opponentScore);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.getRaiseResponse(intel)).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("When in mao de onze")
        class GetMaoDeOnzeResponse {
            @Test
            @DisplayName("Should play mao de onze if has at least one manilha and two cards above ace rank and if the opponent score is less than 9 points.")
            void shouldPlayMaoDeOnzeIfHasAtLeastOneManilhaAndTwoCardsAboveAceRankAndTheOpponentHasLessThanNinePoints() {
                vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
                cards = List.of(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

                when(intel.getOpponentScore()).thenReturn(6);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(vira);

                assertThat(sut.getMaoDeOnzeResponse(intel)).isTrue();
            }

            @Test
            @DisplayName("Should accept mao de onze if opponent is also in mao de onze.")
            void shouldAcceptMaoDeOnzeIfOpponentIsAlsoInMaoDeOnze(){
                when(intel.getOpponentScore()).thenReturn(11);
                assertThat(sut.getMaoDeOnzeResponse(intel)).isTrue();
            }

            @ParameterizedTest
            @CsvSource({"8", "10"})
            @DisplayName("Should accept mao de onze if opponent score is greater than 7 and bot has all cards " +
                    "above king rank")
            void shouldAcceptMaoDeOnzeIfOpponentScoreIsGreaterThanSevenAndBotHasAllCardsAboveKingRank(int opponentScore){
                vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
                cards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

                when(intel.getOpponentScore()).thenReturn(opponentScore);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getVira()).thenReturn(vira);

                assertThat(sut.getMaoDeOnzeResponse(intel)).isTrue();
            }
        }

        @Nested
        @DisplayName("When playing a card")
        class ChooseCardTest {
            @Test
            @DisplayName("Should play the lowest card between all cards available to be played if the opponent card " +
                         "is hidden")
            void shouldPlayTheLowestCardBetweenAllCardsAvailableToBePlayedIfOpponentCardIsHidden() {
                vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));
                opponentCard = Optional.of(TrucoCard.closed());

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getOpponentCard()).thenReturn(opponentCard);

                assertThat(sut.chooseCard(intel).content()).isEqualTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
            }
        }

        @Nested
        @DisplayName("When requesting a point raise")
        class DecidesIfRaisesTest {
            @ParameterizedTest
            @CsvSource({"8, 5", "8, 6", "8,7"})
            @DisplayName("Should ask for point raise if is winning the game by until three points of difference")
            void shouldAskForPointRaiseIfIsWinningTheGameByUntilThreePoints(int botScore, int opponentScore){
                when(intel.getScore()).thenReturn(botScore);
                when(intel.getOpponentScore()).thenReturn(opponentScore);
                assertThat(sut.decideIfRaises(intel)).isTrue();
            }

            @ParameterizedTest
            @CsvSource({"5,8", "6,8", "7,8"})
            @DisplayName("Should ask for point raise if is losing the game by until three points of difference " +
                         "and has cards above rank ACE")
            void shouldAskForPointRaiseIfIsLosingTheGameByThreePointsAndHasCardsAboveRankTwo(int botScore, int opponentScore){
                vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
                cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

                when(intel.getScore()).thenReturn(botScore);
                when(intel.getOpponentScore()).thenReturn(opponentScore);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }

            @Test
            @DisplayName("Should ask for point raise if opponent has eleven points")
            void shouldAskForPointRaiseIfOpponentHasElevenPoints() {
                when(intel.getOpponentScore()).thenReturn(11);
                assertThat(sut.decideIfRaises(intel)).isTrue();
            }

            @Test
            @DisplayName("Should not ask for point raise if bot has eleven points.")
            void shouldNotAskForPointRaiseIfHasElevenPointsAndOpponentHasLessThanElevenPoints() {
                when(intel.getScore()).thenReturn(11);
                assertThat(sut.decideIfRaises(intel)).isFalse();
            }

            @Test
            @DisplayName("Should ask for point raise if it has zap and copas and the opponent is winning by six points.")
            void shouldAskForPointRaiseIfItContainsZapAndCopasAndTheOpponentIsWinningBySixPoints(){
                vira = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
                cards = List.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(cards);
                when(intel.getScore()).thenReturn(2);
                when(intel.getOpponentScore()).thenReturn(9);

                assertThat(sut.decideIfRaises(intel)).isTrue();
            }

            @ParameterizedTest
            @CsvSource({"5", "7"})
            @DisplayName("Should ask for a point raise if opponent score is equal or less than 7.")
            void shouldAskForPointRaiseIfOpponentScoreIsEqualOrLessThanSeven(int opponentScore){
                when(intel.getOpponentScore()).thenReturn(opponentScore);
                assertThat(sut.decideIfRaises(intel)).isTrue();
            }


        }
    }

}
