package com.renato.DarthVader;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class DarthVaderTest {

    private DarthVader darthVader;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setUp() {
        darthVader = new DarthVader();
    }

    @Nested
    @DisplayName("Tests for the decideIfRaises method")
    class DecideIfRaisesMethod {
        @DisplayName("Should not accept if you don't have any manilha and don't have any high cards")
        @Test
        public void ShouldNotAcceptIfYouDontHaveAnyManilhaAndDontHaveAnyHighCards()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertFalse(darthVader.decideIfRaises(stepBuilder.build()));
        }

        @DisplayName("Should ask if I have two or more shackles")
        @Test
        public void shouldAskIfIhaveTwoOrMoreShackles()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 8).
                    opponentScore(5);

            assertTrue(darthVader.decideIfRaises(stepBuilder.build()));
        }

        @DisplayName("Should return true if I am four points ahead of my opponent")
        @Test
        public void shouldReturnTrueIfIAmFourPointsAheadOfMyOpponent()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 7).
                    opponentScore(3);

            assertTrue(darthVader.decideIfRaises(stepBuilder.build()));
        }

        @DisplayName("Should return true if i have one or more good cards")
        @Test
        public void shouldReturnTrueIfIHaveOneOrMoreGoodsCards()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 7).
                    opponentScore(3);

            assertTrue(darthVader.decideIfRaises(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Tests to get mão de onze")
    class testsToGetMeoDeOnze
    {
        @Test
        @DisplayName("Should return true if i have two or more shackles")
        public void shouldReturnTrueIfIHaveTwoOrShackles()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(3);

            assertTrue(darthVader.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return true If i have one or more shackle and one or more good card")
        public void shouldReturnTrueIfIHaveOneOrMoreShackleandOneOrMoreGoodCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(3);

            assertTrue(darthVader.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return true If i have two or more good cards")
        public void shouldReturnTrueIfIHaveTwoOrMoreGoodCards()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(3);

            assertTrue(darthVader.getMaoDeOnzeResponse(stepBuilder.build()));
        }



    }
    @Nested
    @DisplayName("Tests to choose a certain card")
    class ChooseCardMethod {

        @DisplayName("Should return the lowest card")
        @Test
        public void shouldReturnTheLowestCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.FIVE,CardSuit.SPADES),darthVader.getSmallerCard(stepBuilder.build()));
        }

        @DisplayName("Should return the lowest card with the lowest suit")
        @Test
        public void shouldReturnTheLowestCardWithTheLowestSuit()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.THREE,CardSuit.HEARTS),darthVader.getSmallerCard(stepBuilder.build()));
        }

        @DisplayName("Should return the lowest card with the lowest suit if the cards are the same")
        @Test
        public void shouldReturnTheLowestCardWithTheLowestSuitIfTheCardAreTheSame()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.THREE,CardSuit.SPADES),darthVader.getSmallerCard(stepBuilder.build()));
        }

        @DisplayName("Should choose the strongest card")
        @Test
        public void shouldChooseTheStrongestCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.TWO,CardSuit.SPADES),darthVader.getStrongCard(stepBuilder.build()));
        }

        @DisplayName("Should choose the strongest card with the strongest suit")
        @Test
        public void shouldChooseTheStrongestCardWithTheStrongestSuit()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.ACE,CardSuit.CLUBS),darthVader.getStrongCard(stepBuilder.build()));
        }

        @DisplayName("Should return the medium card")
        @Test
        public void shouldReturnTheMediumCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),darthVader.getMediumCard(stepBuilder.build()));
        }


        @DisplayName("Should choose the strongest card with the lowest suit")
        @Test
        public void shouldChooseTheStrongestCardWithTheLowestSuit()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.THREE,CardSuit.HEARTS),darthVader.getStrongCardwithTheLowestSuit(stepBuilder.build()));
        }


        @DisplayName("Should return true if I have two or three repeated cards")
        @Test
        public void shouldReturnTrueIfIHaveTwoOrThreeRepeatedCards()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertTrue(darthVader.checkduplicateCard(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Tests to check the shackles")

    class checkShackles
    {
        @DisplayName("Should return the weakest shackle if I have two shackles")
        @Test
        public void shouldReturnTheWeakestShackleIfIhaveTwoShackles()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.TWO,CardSuit.SPADES),darthVader.getTheSmallestManilha(stepBuilder.build()));
        }

        @DisplayName("Should return the weakest shackle if I have one shackles")
        @Test
        public void shouldReturnTheWeakestShackleIfIhaveOneShackles()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.TWO,CardSuit.SPADES),darthVader.getTheSmallestManilha(stepBuilder.build()));
        }

        @DisplayName("Should return the number of shackles I have")
        @Test
        public void shouldReturnTheNumberOfShacklesIHave()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(1,darthVader.getNumberManilhas(stepBuilder.build()));
        }

        @DisplayName("Should return the strongest shackle I have")
        @Test
        public void shouldReturnTheStrongestShackleIHave()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.TWO,CardSuit.CLUBS),darthVader.getTheStrongestManilha(stepBuilder.build()));
        }
    }


    @Nested
    @DisplayName("Tests to check if the opponent's card is bad")

    class checkOpponentsCard
    {

        @DisplayName("Should return GOOD if the opponent's card is good")
        @Test
        public void shouldReturnGOODIfTheOpponentsCardIsGood()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(DarthVader.CardClassification.GOOD,darthVader.classifyOpponentCard(stepBuilder.build()));
        }

        @DisplayName("Should return Average if the opponent's card is Average")
        @Test
        public void shouldReturnAverageIfTheOpponentsCardIsAverage()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(DarthVader.CardClassification.AVERAGE,darthVader.classifyOpponentCard(stepBuilder.build()));
        }

        @DisplayName("Should return BAD if the opponent's card is bad")
        @Test
        public void shouldReturnBADIfTheOpponentsCardIsBad()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(DarthVader.CardClassification.BAD,darthVader.classifyOpponentCard(stepBuilder.build()));
        }

        @DisplayName("Should return VERY_GOOD if the opponent's card is Manilha")
        @Test
        public void shouldReturnVERY_GOODIfTheOpponentsCardIsManilha()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(DarthVader.CardClassification.VERY_GOOD,darthVader.classifyOpponentCard(stepBuilder.build()));
        }


    }

    @Nested
    @DisplayName("Tests for choose better card than opponent")

    class testChooseBetterCardThanOpponent
    {
        @Test
        @DisplayName("Should be return the smallest card that is higher than the opponent's card")
        public void shouldReturnTheSmallestCardThatIsHigherThanTheOpponentsCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(TrucoCard.of(CardRank.KING,CardSuit.HEARTS),darthVader.chooseTheMinorCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should throw an exception if the opponent's card was not set")
        public void shouldThrowAnExceptionIfTheOpponentsCardWasNotSet()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            assertThrows(NoSuchElementException.class, () -> darthVader.chooseTheMinorCard(stepBuilder.build()));
        }


        @Test
        @DisplayName("Should play the card from the next group that is bigger than my opponent. If the next group is a good card and I don't have a shackle, return the worst card")
        public void ShouldPlayTheCardFromTheNextGroupThatIsBiggerThanMyOpponentIfTheNextGroupIsAGoodCardAndIDontHaveAShackleReturnTheWorstCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            TrucoCard opponentCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(TrucoCard.of(CardRank.FOUR,CardSuit.SPADES),darthVader.chooseTheMinorCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return the highest card if I have a good hand")
        public void ShouldReturnTheHighestCardIfIHaveAGoodHand()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            TrucoCard opponentCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(TrucoCard.of(CardRank.TWO,CardSuit.CLUBS),darthVader.chooseTheMinorCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return the smallest card that is stronger than the opponent")
        public void shouldReturnTheSmallestCardThatIsStrongerThanTheOpponent()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            TrucoCard opponentCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(TrucoCard.of(CardRank.TWO,CardSuit.CLUBS),darthVader.chooseTheMinorCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return the weakest card if there is none greater than the opponent")
        public void shouldReturnTheWeakestCardIfThereIsNoneGreaterThanOpponent()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            assertEquals(TrucoCard.of(CardRank.FOUR,CardSuit.SPADES),darthVader.chooseTheMinorCard(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Tests to classify my cards")
    class testClassifyMyCards{


        @DisplayName("Should return a map with all the classifications of my cards")
        @Test
        public void shouldReturnaMapWithAllTheClassificationsOfMyCards()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5).
                    opponentCard(opponentCard);

            Map<TrucoCard, DarthVader.CardClassification> expectedClassifications = new HashMap<>();
            expectedClassifications.put(trucoCards.get(0), DarthVader.CardClassification.GOOD);
            expectedClassifications.put(trucoCards.get(1), DarthVader.CardClassification.GOOD);
            expectedClassifications.put(trucoCards.get(2), DarthVader.CardClassification.VERY_GOOD);


            assertEquals(expectedClassifications,darthVader.classifyMyCards(stepBuilder.build()));

        }


        @Test
        @DisplayName("Should return the number of cards of each type that I have")
        public void shouldReturnTheNumberOfCardsOfEachType()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            Map<DarthVader.CardClassification, Integer> expectedClassifications = new HashMap<>();

            expectedClassifications.put(DarthVader.CardClassification.AVERAGE, 0);
            expectedClassifications.put(DarthVader.CardClassification.VERY_GOOD, 1);
            expectedClassifications.put(DarthVader.CardClassification.BAD, 0);
            expectedClassifications.put(DarthVader.CardClassification.GOOD, 2);

            assertEquals(expectedClassifications, darthVader.countCardClassifications(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Tests for choose card in each round")
    class testChooseCardInEachRound{


        @DisplayName("Should return true if I have a good hand")
        @Test
        public void RoundOne()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);



            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            assertEquals(true,darthVader.verifyMyHand(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return if it's my turn to play")

        public void shouldReturnIfItsMyTurntoPlay()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);



            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            assertEquals(true,darthVader.verifyIfMyTurnToPlay(stepBuilder.build()));
        }


        @Test
        @DisplayName("Should return the smallest card")
        public void shouldReturnTheSmallestCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);
            assertEquals(CardToPlay.of(TrucoCard.of(CardRank.FOUR,CardSuit.SPADES)),darthVader.firstRoundCard(stepBuilder.build()));
        }

    }
    @Nested
    @DisplayName("Tests for the method decides whether to increase")
    class testIncrease{

        @Test
        @DisplayName("Should return 1 if I have two or more shackles")
        public void shouldReturn1IfIHaveTwoOrMoreShackles()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            assertEquals(1,darthVader.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return zero if I have a good hand")
        public void shouldReturnZeroIfIhaveAGoodHand()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            assertEquals(0,darthVader.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return minus one if I have a bad hand")
        public void shouldReturnMinusOneIfIhaveabadHand()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 5).
                    opponentScore(5);

            assertEquals(-1,darthVader.getRaiseResponse(stepBuilder.build()));
        }

    }


    @Nested
    @DisplayName("Tests for count the number of cards")

    class testCountCards{

        @DisplayName("Should return the number of good cards I have")
        @Test
        public void shouldReturnTheNumberOfGoodCardIhave()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1).
                    botInfo(trucoCards, 7).
                    opponentScore(3);

            assertEquals(2,darthVader.getCountGoodCards(stepBuilder.build()));
        }
    }

}
