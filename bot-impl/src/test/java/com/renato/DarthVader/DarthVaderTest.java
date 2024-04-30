package com.renato.DarthVader;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            assertFalse(darthVader.getMaoDeOnzeResponse(stepBuilder.build()));
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

    }




}
