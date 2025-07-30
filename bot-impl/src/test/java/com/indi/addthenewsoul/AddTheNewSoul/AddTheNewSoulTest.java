/*
 *  Copyright (C) 2023 Ingrid Nery and Diego Pagotto
 *  Contact: ingrid <dot> nery <at> ifsp <dot> edu <dot> br
 *  Contact: diego <dot> pagotto <at> ifsp <dot> edu <dot> br
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
package com.indi.addthenewsoul.AddTheNewSoul;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.indi.impl.addthenewsoul.AddTheNewSoul;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AddTheNewSoulTest {
    private AddTheNewSoul addTheNewSoul;
    List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
            TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));
    TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
    private GameIntel.StepBuilder stepBuilder;
    @BeforeEach
    public void setUp(){
        addTheNewSoul = new AddTheNewSoul();
    }

    @Nested
    @DisplayName("chooseCard")
    class ChooseCardTests{
        @Test
        @DisplayName("Should play the lowest card to win")
        void shouldPlayTheLowestCardToWinTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(3)
                    .opponentCard(opponentCard);
            assertEquals(CardRank.FIVE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
        }
        @Test
        @DisplayName("It is not possible to win, discard the lowest card.")
        void itIsNotPossibleToWinDiscardTheLowestCardTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(3)
                    .opponentCard(opponentCard);

            assertEquals(CardRank.FIVE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Should play the lowest card according to the suit")
        public void shouldPlayTheLowestCardAccordingToTheSuitTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.closed());

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.closed());
            assertEquals(CardSuit.SPADES, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
        }

        @Test
        @DisplayName("Should play a manilha stronger than the player's")
        public void shouldPlayManilhaStrongerThanThePlayersTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

            List<TrucoCard> opensCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), opensCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(opponentCard);
            assertEquals(CardRank.SIX, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
        }
        @Test
        @DisplayName("Should play the smallest manilha that beats the opponent")
        public void shouldPlayTheSmallestManilhaThatBeatsTheOpponentTest(){

            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> opensCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), opensCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            assertEquals(CardSuit.HEARTS, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
        }

        @Test
        @DisplayName("Should play the smallest card even if has manilha that is not diamond when no cards have been played")
        void shouldPlayTheSmallestCardEvenIfHasManilhaThatIsNotDiamondWhenNoCardsHaveBeenPlayedTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4);
            assertEquals(CardRank.FIVE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Should always play the Diamonds manilha when in hand")
        void shouldAlwaysPlayTheDiamondsManilhaWhenInHandTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));
            assertEquals(CardSuit.DIAMONDS, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
        }

        @Test
        @DisplayName("Should discard when the opponent plays a card that Adenilso cant beat ")
        void shouldDiscardWhenTheOpponentPlaysACardThatAdenilsoCantBeatTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            assertTrue(addTheNewSoul.chooseCard(stepBuilder.build()).isDiscard());
        }

        @Test
        @DisplayName("Should play the lowest card on hand opened in the first round if cannot beat the opponent ")
        void shouldPlayTheLowestCardOnHandOpenedInTheFirstRoundIfCannotBeatTheOpponentTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            assertEquals(CardRank.FOUR, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Should play attack card if has two at least two attack cards in hand")
        void shouldPlayAttackCardIfHasTwoAtLeastTwoAttackCardsInHandTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));
            assertEquals(CardSuit.DIAMONDS, addTheNewSoul.chooseCard(stepBuilder.build()).content().getSuit());
        }

        @Test
        @DisplayName("Should play the highest card when game is amarrado")
        void shouldPlayTheHighestCardWhenGameIsAmarradoTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));
            assertEquals(CardRank.THREE, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Should always amarrar when its possible")
        void shouldAlwaysAmarrarWhenItsPossibleTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));
            assertEquals(CardRank.SEVEN, addTheNewSoul.chooseCard(stepBuilder.build()).content().getRank());

        }
    }

    @Nested
    @DisplayName("decideIfRaises")
    class DecideIfRaisesTests{
        @Test
        @DisplayName("Should not rise if the opponent has more than 8 points")
        public void shouldNotRiseIfTheOpponentHasMoreThan8pointsTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    opponentCard);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(9)
                    .opponentCard(opponentCard);
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should rise if has good cards and opponent less than 8 points")
        public void shouldRiseIfHasGoodCardsAndOpponentLessThab8pointsTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    opponentCard);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(8)
                    .opponentCard(opponentCard);
            assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should not rise if dont have good cards")
        public void shouldNotRiseIfDontHaveGoodCardsTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    opponentCard);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(3)
                    .opponentCard(opponentCard);
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));

        }

        @Test
        @DisplayName("Should raise if it has manilha and card with relative value greater than 4")
        public void shouldRaiseIfItHasManilhaAndCardWithRelativeValueGraterThan4Test(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    opponentCard);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(4)
                    .opponentCard(opponentCard);
            assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should not raise if only have manilha")
        public void shouldNotRaiseIfOnlyHaveManilhaTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 7)
                    .opponentScore(8)
                    .opponentCard(opponentCard);
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not raise if have only card good")
        public void shouldNotRaiseIfHaveOnlyCardGoodTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 4)
                    .opponentScore(6)
                    .opponentCard(opponentCard);
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should increase if sum of card values is above average")
        public void shouldIncreaseIfSumOfCardValuesIsAboveAverageTest(){
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 3)
                    .opponentScore(6)
                    .opponentCard(opponentCard);
            assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should increase have just manilhas")
        public void shouldIncreaseHaveJustManilhasTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    opponentCard);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(2)
                    .opponentCard(opponentCard);
            assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should increase if winning by more than 3 points independent of cards")
        public void shouldIncreaseIfWinningByMoreThan3PointsIndependentOfCardsTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 6)
                    .opponentScore(2);
            assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not raise if have 11 points and opponent has 10")
        public void shouldNotRaiseIfHave11PointsAndOpponentHas10Test() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    opponentCard);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(10)
                    .opponentCard(opponentCard);
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not raise if all cards are below seven")
        public void shouldNotRaiseIfAllCardsAreBelowSevenTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    opponentCard);


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(2)
                    .opponentCard(opponentCard);
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not raise if opponent has a better card than the best card")
        public void shouldNotRaiseIfOpponentHasABetterCardThanTheBestCardTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should raise if round result drew and strong hand")
        public void shouldRaiseIfRoundResultDrewAndStrongHand(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    opponentCard);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(2)
                    .opponentCard(opponentCard);
            assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should raise if round result drew and sum of card values is below average")
        public void shouldRaiseIfRoundResultDrewAndSumOfCardValuesIsBelowAverage(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    opponentCard);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(2)
                    .opponentCard(opponentCard);
            assertTrue(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not raise if round result drew and weak hand")
        public void shouldNotRaiseIfRoundResultDrewAndWeakHand(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    opponentCard);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(2)
                    .opponentCard(opponentCard);
            assertFalse(addTheNewSoul.decideIfRaises(stepBuilder.build()));
        }
    }


    @Nested
    @DisplayName("getMaoDeOnzeResponse")
    class GetMaoDeOnzeResponseTests{
        @Test
        @DisplayName("Should accept mao de onze if the cards are strong")
        public void shouldAcceptMaoDeOnzeIfTheCarsAreStrongTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(9);
            assertTrue(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should decline mao de onze if the cards are weak")
        public void ShouldDeclineMaoDeOnzeIfTheCardsAreWeakTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(9);
            assertFalse(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }


        @Test
        @DisplayName("Should accept mao de onze if opponent score is less than seven")
        public void shouldAcceptMaoDeOnzeIfOpponentScoreIsLessThanSevenTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(5);
            assertTrue(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should accept mao de onze if opponent score is six")
        public void shouldAcceptMaoDeOnzeIfOpponentScoreIsSixTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(6);
            assertTrue(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should decline mao de onze if  sum of card values is below average")
        public void shouldDeclineMaoDeOnzeIfSumOfCardValuesIsAboveAverageTest(){
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(7);
            assertFalse(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should decline mao de onze if sum of card values is above average but opponent score is greater than eight")
        public void shouldDeclineMaoDeOnzeIfSumOfCardValueIsAboveAverageButOpponentScoreIsGreaterThanEightTest(){
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(9);
            assertFalse(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should accept mao de onze if sum of card values is above average but opponent score is less than nine")
        public void shouldAcceptMaoDeOnzeIfSumOfCardValuesIsAboveAverageButOpponentScoreIsLessThanNineTest(){
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(8);
            assertTrue(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should decline mao de onze if opponent score is nine and weak hand")
        public void shouldDeclineMaoDeOnzeIfOpponentScoreIsNineAndWeakHandTest() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(9);
            assertFalse(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should accept if hand is strong and opponent score is nine")
        public void shouldAcceptIfHandIsStrongAndOpponentScoreIsNine() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(9) ;
            assertTrue(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should accept mao de onze if has zap")
        public void shouldAcceptMaoDeOnzeIfHasZap() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(9);
            assertTrue(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should accept if score is equal to opponent")
        public void shouldAcceptIfScoreIsEqualToOpponent(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(11);
            assertTrue(addTheNewSoul.getMaoDeOnzeResponse(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("getRaiseResponse")
    class GetRaiseResponseTests{
        @Test
        @DisplayName("Should not raise if hand value is twelve")
        void shouldNotRaiseIfHandValueIsTwelveTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 12)
                    .botInfo(botCards, 0)
                    .opponentScore(0);
            assertEquals(addTheNewSoul.getRaiseResponse(stepBuilder.build()), -1);
        }
        @Test
        @DisplayName("Should always accept if has a casal maior")
        void shouldAlwaysAcceptIfHasACasalMaiorTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isOne();
        }

        @Test
        @DisplayName("Should decline if hand is not strong")
        void shouldDeclineIfHandIsNotStrongTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);
            assertEquals(addTheNewSoul.getRaiseResponse(stepBuilder.build()), -1);
        }

        @Test
        @DisplayName("Should accept if won first round and has a manilha")
        void shouldRaiseIfWonFirstRoundAndHasAManilhaTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isZero();
        }

        @Test
        @DisplayName("Should accept if won first round and has an attack card")
        void shouldRaiseIfWonFirstRoundAndHasAnAttackCardTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isZero();
        }

        @Test
        @DisplayName("Should raise if has zap and at least one attack card")
        void shouldRaiseIfHasZapAndAtLeastOneAttackCardTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(botCards, 0)
                    .opponentScore(0);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isOne();
        }

        @Test
        @DisplayName("Should decline if loss means game over")
        void shouldNotRaiseIfLossMeansGameOverTest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(6);
            assertEquals(addTheNewSoul.getRaiseResponse(stepBuilder.build()), -1);
        }

        @Test
        @DisplayName("Should raise if loss means game over but has casal")
        void shouldRaiseIfLossMeansGameOverButHasCasalTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(6);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isOne();
        }

        @Test
        @DisplayName("Should accept if every manilha has been played and has attack card")
        void shouldRaiseIfEveryManilhaHasBeenPlayedAndHasAttackCardTest(){
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

            List<TrucoCard> botCards = Collections.singletonList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 6)
                    .botInfo(botCards, 0)
                    .opponentScore(3);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isZero();
        }

        @Test
        @DisplayName("Should raise if hand is strong and points difference is higher than Three")
        void shouldRaiseIfHandIsStrongAndPointsDifferenceIsHigherThanThreeTest(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 6)
                    .botInfo(botCards, 6)
                    .opponentScore(2);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isOne();
        }

        @Test
        @DisplayName("Should not raise if hand is weak and score difference is lower than two")
        void shouldNotRaiseIfHandIsWeakAndScoreDifferenceIsLowerThanTwo(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 6)
                    .botInfo(botCards, 6)
                    .opponentScore(2);
            assertThat(addTheNewSoul.getRaiseResponse(stepBuilder.build())).isOne();
        }

        @Test
        @DisplayName("Should decline if is mao de onze")
        void shouldDeclineIfIsMaoDeOnze(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(botCards, 11)
                    .opponentScore(11);
            assertEquals(addTheNewSoul.getRaiseResponse(stepBuilder.build()), -1);
        }
    }



}
