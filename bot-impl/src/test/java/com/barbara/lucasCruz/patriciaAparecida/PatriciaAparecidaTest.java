package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatriciaAparecidaTest {
    @Mock
    GameIntel intel;
    private PatriciaAparecida patricia;

    @BeforeEach
    void setUp() {
        patricia = new PatriciaAparecida();
    }

    @Nested
    @DisplayName("Check the variables to calculate the probability")
    class checkVariables {

        @Nested
        @DisplayName("Number of remainder cards")
        class RemainderCards{

            @Test
            @DisplayName("Start Of the Game")
            public void startOfTheGame() {

                List<TrucoCard> randomBotCards = generateTrucoCardToPlayList(3);
                List<TrucoCard> openCards = generateTrucoCardToPlayList(1);

                when(intel.getCards()).thenReturn(randomBotCards);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfRemainderCards(intel),36);
            }

            @Test
            @DisplayName("Last Round")
            public void lastRound () {

                List<TrucoCard> botCards = generateTrucoCardToPlayList(1);
                List<TrucoCard> openCards = generateTrucoCardToPlayList(6);

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfRemainderCards(intel),33);
            }

        }

        @Nested
        @DisplayName("Number of best cards known")
        class BestCardsKnown{
            private TrucoCard openCard;
            private List<TrucoCard> botCards;

            @BeforeEach
            void setCards(){
                botCards =  List.of(TrucoCard.of(ACE,CLUBS),
                        TrucoCard.of(ACE,HEARTS),
                        TrucoCard.of(ACE,SPADES));
                openCard = TrucoCard.of(ACE,DIAMONDS);
            }

            @Test
            @DisplayName("Have no better cards known")
            public void ShouldReturnZeroIfHaveNoBetterCardsKnown(){
                TrucoCard vira = TrucoCard.of(FOUR,SPADES);
                List<TrucoCard> openCards = List.of(vira, openCard);

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfBestCardsKnown(botCards.get(1),intel),0);
            }

            @Test
            @DisplayName("Have better cards known")
            public void ShouldReturnANumberOfBetterCardsKnown(){
                TrucoCard vira = TrucoCard.of(THREE,SPADES);
                List<TrucoCard> openCards = List.of(vira, openCard);

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);

                assertEquals(patricia.getNumberOfBestCardsKnown(botCards.get(1), intel),1);
            }
        }

        @Nested
        @DisplayName("Number of best cards unknown")
        class BestCardsUnkown {

            private TrucoCard vira;
            private List<TrucoCard> botCards;
            private List<TrucoCard> openCards;

            @BeforeEach
            void setCards(){
                botCards = List.of(
                        TrucoCard.of(FOUR, SPADES),
                        TrucoCard.of(FOUR, DIAMONDS),
                        TrucoCard.of(KING, DIAMONDS));
                vira = TrucoCard.of(THREE,SPADES);
                openCards = List.of(
                        vira,
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(FOUR, CLUBS));
            }

            @Test
            @DisplayName("Case is Manilha")
            public void ShouldReturnTheNumberOfBetterCardsUnknownInCaseOfCardIsManilha() {

                when(intel.getOpenCards()).thenReturn(openCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(botCards);

                assertEquals(patricia.getNumberOfBestCardsUnknown(botCards.get(1),intel),0);
            }

            @Test
            @DisplayName("Case isnt Manilha")
            public void ShouldReturnTheNumberOfBetterCardsUnknownInCaseOfCardIsntManilha() {

                when(intel.getOpenCards()).thenReturn(openCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(botCards);

                assertEquals(patricia.getNumberOfBestCardsUnknown(botCards.get(2),intel), 7);
            }
        }

        @Nested
        @DisplayName("Should return the number of opponent's cards ")
        class CardsOfOpponent {

            @Test
            @DisplayName("Case win the last round")
            public void ShouldReturnTheNumberOfTheOpponentCardsCaseWinLastRound() {
                when(intel.getRoundResults()).thenReturn(List.of(LOST,WON));
                assertEquals(patricia.getNumberOfOpponentsCards((intel)),1);
            }

            @Test
            @DisplayName("Case lost the last round")
            public void ShouldReturnTheNumberOfTheOpponentCardsCaseLostLastRound(){
                when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
                assertEquals(patricia.getNumberOfOpponentsCards(intel),0);
            }
        }
    }

    @Nested
    @DisplayName("Check the probability")
    class Probability{

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        @DisplayName("Should calculate the probability of each bot's cards")
        public void ShouldCalculateTheProbOfEachBotCardsWhen3cards(int qnt) {
            List<TrucoCard> botCards = generateTrucoCardToPlayList(qnt);
            TrucoCard vira = generateRandomCardToPlay();

            when(intel.getCards()).thenReturn(botCards);
            when(intel.getVira()).thenReturn(vira);

            assertEquals(patricia.listProbAllCards(intel).size(),botCards.size());
        }

        @Nested
        @DisplayName("Check values")
        class ProbValues{

            private TrucoCard vira;
            private List<TrucoCard> openCards;


            @BeforeEach
            void setCards(){
                vira = TrucoCard.of(FIVE, CLUBS);
                openCards = List.of(vira, TrucoCard.of(SIX, SPADES));

            }

            @Test
            @DisplayName("Should return the same prob when Know all better cards")
            public void ShouldReturnTheSameProbWhenKnowAllBetterCards () {
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SIX, CLUBS),
                        TrucoCard.of(SIX, HEARTS),
                        TrucoCard.of(SIX, DIAMONDS));

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);

                List<Double> probList = patricia.listProbAllCards(intel);

                assertEquals(probList.get(0), probList.get(1), probList.get(2));
            }

            @Test
            @DisplayName("Should return proportional prob to card rank")
            public void ShouldReturnProportionalProbToCardRank () {
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SIX, CLUBS),
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(KING, DIAMONDS));

                when(intel.getCards()).thenReturn(botCards);
                when(intel.getVira()).thenReturn(vira);
                when(intel.getOpenCards()).thenReturn(openCards);

                List<Double> probList = patricia.listProbAllCards(intel);

                assertTrue(intel.getCards().get(0).compareValueTo(intel.getCards().get(2),vira)>0);
                assertTrue(intel.getCards().get(2).compareValueTo(intel.getCards().get(1),vira)>0);
                assertTrue(probList.get(0) < probList.get(2));
                assertTrue(probList.get(2) < probList.get(1));
            }
        }
    }


    @Nested
    @DisplayName("Exception Thrown by Methods Tests")
    class ExceptionThrownByMethodsTests{
        @Nested
        @DisplayName("Mão de Onze Exception Tests")
        class MaoDeOnzeExceptions{

            @Test
            @DisplayName("Should Throw exception if bot accepts mão de onze without 11 points")
            public void shouldThrowExceptionInMaoDeOnzeWhenBotDoesntHave11Points(){
                when(intel.getScore()).thenReturn(10);
                assertThrows(IllegalArgumentException.class , () -> patricia.getMaoDeOnzeResponse(intel)) ;   }
        }
        @Nested
        @DisplayName("Decide if Raises Tests")
        class DecideIfRaisesExceptions{

            @Test
            @DisplayName("Should Throw exception if bot raises beyond 12 points")
            public void shouldThrowExceptionInDecideIfRaisesIfBotRaiseAfter12Points() {
                when(intel.getHandPoints()).thenReturn(13);
                assertThrows(IllegalArgumentException.class, () -> patricia.decideIfRaises(intel));
            }
        }
        @Nested
        @DisplayName("ChoiceCardExceptions")
        class chooseCardExceptions{

            @Test
            @DisplayName("Should Throw exception if bot chooses null as card")
            public void shouldThrowExceptionIfDecideToChooseNullCard() {
                assertThrows(IllegalStateException.class, () -> patricia.chooseCard(intel));
            }
        }
    }

    @Nested
    @DisplayName("choose card tests ")
    class ChooseCardTest{
        @Nested
        @DisplayName("Unknown Opponent Card Tests")
        class UnknownOpponentClassTests{
            private TrucoCard vira;
            @BeforeEach
            void setUpCardsUnknownOpponentCardCase(){
                vira = TrucoCard.of(THREE,DIAMONDS);
            }
            @Test
            @DisplayName("Choose Card With Highest Chance of Winning")
            public void ChooseHighestProbCard(){
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(TWO,SPADES),
                        TrucoCard.of(ACE, HEARTS),
                        TrucoCard.of(TWO,CLUBS)));
                when(intel.getVira()).thenReturn(vira);
                assertEquals(CardToPlay.of(TrucoCard.of(TWO,CLUBS)).value(),patricia.chooseCard(intel).value());

            }

            @Test
            @DisplayName("Choose weakest card of strong cards if it has no stronger")
            public void ChooseWeakestCardOfStrongInHand(){
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(FIVE, HEARTS), //2
                        TrucoCard.of(FOUR,CLUBS), // 3
                        TrucoCard.of(FOUR,HEARTS)));
                when(intel.getVira()).thenReturn(vira);
                assertEquals(CardToPlay.of(TrucoCard.of(FOUR,HEARTS)).value(),patricia.chooseCard(intel).value());
            }
            @Test
            @DisplayName("Plays weakest card if it has no stronger card")
            public void ChooseWeakestCardIfHasAllStrongest(){
                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(FOUR,CLUBS),
                        TrucoCard.of(FOUR,SPADES),
                        TrucoCard.of(FOUR,HEARTS)));
                when(intel.getVira()).thenReturn(vira);
                assertEquals(CardToPlay.of(TrucoCard.of(FOUR,SPADES)).value(),patricia.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Play card that draws if probability is lower than card that wins")
            public void ChooseCardThatDrawsIfHigherChance(){

                when(intel.getCards()).thenReturn(List.of(TrucoCard.of(FOUR,CLUBS),
                        TrucoCard.of(FIVE,DIAMONDS)));

                when(intel.getVira()).thenReturn(TrucoCard.of(FIVE,HEARTS));

                when(intel.getOpenCards()).thenReturn(List.of(TrucoCard.of(FIVE,HEARTS),
                        TrucoCard.of(FIVE,CLUBS),
                        TrucoCard.of(FIVE,SPADES)));

                when(intel.getRoundResults()).thenReturn(List.of(WON));
                assertEquals(CardToPlay.of(TrucoCard.of(FOUR,CLUBS)).value(), patricia.chooseCard(intel).value());
                when(intel.getRoundResults()).thenReturn(List.of(DREW));
                assertEquals(CardToPlay.of(TrucoCard.of(FOUR,CLUBS)).value(), patricia.chooseCard(intel).value());
            }
        }

        @Nested
        @DisplayName("known Opponent Card Tests")
        class KnownOpponentClassTests{
            private List<TrucoCard> botCards;
            private TrucoCard vira;
            @BeforeEach
            void setUpCardsKnownOpponentCardCase(){
                botCards = List.of(TrucoCard.of(KING, HEARTS), //2
                        TrucoCard.of(FOUR,CLUBS), // 3
                        TrucoCard.of(TWO,HEARTS)); // 1

                vira = TrucoCard.of(FIVE,CLUBS);
            }

            @Test
            @DisplayName("Choose the weakest card that wins the hand")
            public void ChooseWeakestCardThatWinsHand(){
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(TrucoCard.of(QUEEN, DIAMONDS)));

                assertEquals(CardToPlay.of(TrucoCard.of(KING, HEARTS)).content() ,patricia.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Draws if can't win and can draw")
            public void DrawIfCantWin(){
                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(TrucoCard.of(TWO, CLUBS)));

                assertEquals(CardToPlay.of(TrucoCard.of(TWO,HEARTS)).value() ,patricia.chooseCard(intel).value());
            }

            @Test
            @DisplayName("Discard the weakest card that loses the hand")
            public void DiscardWeakestCardThatLosesHand(){

                when(intel.getVira()).thenReturn(vira);
                when(intel.getCards()).thenReturn(botCards);
                when(intel.getRoundResults()).thenReturn(List.of(LOST));
                when(intel.getOpponentCard()).thenReturn(Optional.ofNullable(TrucoCard.of(SIX, CLUBS)));
                assertEquals(CardToPlay.of(TrucoCard.of(FOUR,CLUBS)).content() ,patricia.chooseCard(intel).content());
                assertEquals(TrucoCard.closed(),patricia.chooseCard(intel).value());
            }
        }
    }

    @Nested
    @DisplayName("Get Raise response tests")
    class RaiseREsponseTest{
        @Test
        @DisplayName("Should return the round even the list isEmpty")
        public void ShouldReturnTheNumberOfRoundsCaseListIsEmpty() {
            when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
            assertEquals(patricia.getNumberOfRounds(intel),1);
        }

        @Nested
        @DisplayName("Round 3")
        class RaiseRespondeRound3{
            @Nested
            @DisplayName("Opponent start")
            class OpponentStart {
                @Test
                @DisplayName("Should re-raise when can win")
                public void ShouldReRaiseWhenCanWin() {

                    when(intel.getRoundResults()).thenReturn(List.of(WON, LOST));
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(FOUR, SPADES)));
                    when(intel.getVira()).thenReturn(TrucoCard.of(TWO, SPADES));
                    when(intel.getCards()).thenReturn(List.of(TrucoCard.of(FIVE, SPADES)));

                    assertEquals(1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should re-raise when can draw")
                public void ShouldReRaiseWhenCanDraw() {
                    when(intel.getRoundResults()).thenReturn(List.of(WON, LOST));
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(FOUR, SPADES)));
                    when(intel.getVira()).thenReturn(TrucoCard.of(FOUR, DIAMONDS));
                    when(intel.getCards()).thenReturn(List.of(TrucoCard.of(FIVE, SPADES)));

                    assertEquals(1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit raise response if we cant win")
                public void ShouldQuitRaiseResponseIfWeCantWin () {
                    when(intel.getRoundResults()).thenReturn(List.of(WON,LOST));
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(FIVE,SPADES)));
                    when(intel.getVira()).thenReturn(TrucoCard.of(TWO,SPADES));
                    when(intel.getCards()).thenReturn(List.of(TrucoCard.of(FOUR,SPADES)));

                    assertEquals(-1, patricia.getRaiseResponse(intel));
                }
            }

            @Nested
            @DisplayName("We start")
            class WeStart {

                @Test
                @DisplayName("Should re-raise when prob < 60 ")
                public void ShouldReRaiseWhenProbLowerThan60() {

                    List<TrucoCard> botCards = List.of(TrucoCard.of(JACK, DIAMONDS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(LOST, WON));
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());


                    assertEquals(1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should accept when 60 < prob < 70 ")
                public void ShouldAcceptWhenProbBetween60and70() {

                    List<TrucoCard> botCards = List.of(TrucoCard.of(QUEEN, SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(LOST, WON));
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(0, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit if prob > 70 ")
                public void ShouldQuitWhenProbBigger70(){
                    List<TrucoCard> botCards = List.of(TrucoCard.of(SIX,HEARTS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX,SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(LOST,WON));
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(-1,patricia.getRaiseResponse(intel));
                }
            }

        }

        @Nested
        @DisplayName("Round 1")
        class raiseResponseRound1 {
            @Nested
            @DisplayName("We Start")
            class WeStart {
                @Test
                @DisplayName("Should re-raise when prob < 60 to min 2 Cards")
                public void ShouldReRaisetWhenProbLower60ToMin2Cards() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(SEVEN, SPADES),
                            TrucoCard.of(SEVEN, HEARTS),
                            TrucoCard.of(FOUR, SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should accept when (60 < prob < 70) To Min 2 Cards")
                public void ShouldAcceptWhenProbBetween60and70ToMin2Cards() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(SEVEN, DIAMONDS),
                            TrucoCard.of(KING, CLUBS),
                            TrucoCard.of(KING, SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(FIVE, SPADES));
                    when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(0, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit when prob > 70 to min 2 cards")
                public void ShouldQuitWhenProbBiggerThan70(){
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(FOUR,HEARTS),
                            TrucoCard.of(FOUR,SPADES),
                            TrucoCard.of(QUEEN,HEARTS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SEVEN, SPADES));
                    when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(-1,patricia.getRaiseResponse(intel));
                }
            }

            @Nested
            @DisplayName("Opponent Start")
            class OpponentStart {
                @Test
                @DisplayName("Should re-raise when can win and prob < 60 to another card")
                public void ShouldReRaiseWhenCanWinAndProbLowerThan60ToAnotherCard() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(SEVEN, CLUBS),
                            TrucoCard.of(THREE, HEARTS),
                            TrucoCard.of(FOUR,SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(TWO,SPADES)));

                    assertEquals(1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should accept when can win and (70 < prob < 60) to another card")
                public void ShouldAcceptWhenCanWinAndProbBetween70And60ToAnotherCard() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(KING, HEARTS),
                            TrucoCard.of(KING,SPADES),
                            TrucoCard.of(TWO, CLUBS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(ACE,SPADES)));

                    assertEquals(0, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit when cant win")
                public void ShouldQuitWhenCantWin(){
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(ACE, CLUBS),
                            TrucoCard.of(TWO, HEARTS),
                            TrucoCard.of(FOUR,SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(THREE,SPADES)));

                    assertEquals(-1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit when can win and prob > 70 to another card")
                public void ShouldQuitWhenCanWinAndProbBiggerThan70ToAnotherCard(){
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(SEVEN, CLUBS),
                            TrucoCard.of(FOUR, HEARTS),
                            TrucoCard.of(FOUR,SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(Collections.EMPTY_LIST);
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(THREE,SPADES)));

                    assertEquals(-1, patricia.getRaiseResponse(intel));
                }
            }
        }

        @Nested
        @DisplayName("Round 2")
        class raiseResponseRound2{

            @Nested
            @DisplayName("We Start")
            class WeStart {

                @Test
                @DisplayName("Should re-raise when prob < 60 to min 1 card")
                public void ShouldReRaiseWhenProbLowerThan60ToMin1Card() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(SEVEN, CLUBS),
                            TrucoCard.of(FOUR,SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(WON));
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should accept when (60 < prob < 70) to min 1 card")
                public void ShouldAcceptWhenProbBetween60and70ToMin1Card() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(KING, CLUBS),
                            TrucoCard.of(KING,SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(WON));
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(0, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit when prob > 70 to all cards")
                public void ShouldQuitWhenProbLowerThan70ToAllCArds() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(FOUR, CLUBS),
                            TrucoCard.of(FOUR,SPADES));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(WON));
                    when(intel.getOpponentCard()).thenReturn(Optional.empty());

                    assertEquals(-1, patricia.getRaiseResponse(intel));
                }

            }

            @Nested
            @DisplayName("Opponent Start")
            class OpponentStart{
                @Test
                @DisplayName("Should re-raise when can win and prob < 60 to another card")
                public void ShouldReRaiseWhenCanWinAndProbLowerThan60ToAnotherCard() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(SEVEN, CLUBS),
                            TrucoCard.of(THREE, HEARTS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(LOST));
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(TWO,SPADES)));

                    assertEquals(1, patricia.getRaiseResponse(intel));
                }


                @Test
                @DisplayName("Should accept when can win and (60 < prob < 70) to another card")
                public void ShouldAcceptWhenCanWinAndProbBetween60And70ToAnotherCard() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(SIX, DIAMONDS),
                            TrucoCard.of(SEVEN, CLUBS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(TWO, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(LOST));
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(FIVE,SPADES)));

                    assertEquals(0, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit when can't win")
                public void ShouldQuitWhenCantWin() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(TWO, CLUBS),
                            TrucoCard.of(TWO, HEARTS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(LOST));
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(THREE,SPADES)));

                    assertEquals(-1, patricia.getRaiseResponse(intel));
                }

                @Test
                @DisplayName("Should quit when can win and prob > 70")
                public void ShouldQuitWhenCanWinAndProbBiggerThan70ToAnotherCard() {
                    List<TrucoCard> botCards = List.of(
                            TrucoCard.of(TWO, CLUBS),
                            TrucoCard.of(FOUR, HEARTS));
                    when(intel.getCards()).thenReturn(botCards);
                    when(intel.getVira()).thenReturn(TrucoCard.of(SIX, SPADES));
                    when(intel.getRoundResults()).thenReturn(List.of(LOST));
                    when(intel.getOpponentCard()).thenReturn(Optional.of(TrucoCard.of(ACE,SPADES)));

                    assertEquals(-1, patricia.getRaiseResponse(intel));
                }

            }
        }
    }


    public GameIntel.RoundResult generateRandomRoundResult() {
        GameIntel.RoundResult[] values = GameIntel.RoundResult.values();

        Random random = new Random();
        int index = random.nextInt(values.length);

        return values[index];
    }

    public CardRank generateRandomCardRank() {
        CardRank[] values = CardRank.values();
        Random random = new Random();
        int index = random.nextInt(values.length);
        return values[index];
    }

    public CardSuit generateRandomCardSuit() {
        CardSuit[] values = CardSuit.values();
        Random random = new Random();
        int index = random.nextInt(values.length);

        while (values[index] == CardSuit.HIDDEN) {
            index = random.nextInt(values.length);
        }

        return values[index];
    }

    public TrucoCard generateRandomCard(){ //assegura que CardRank.HIDDEN só receba CardSuit.HIDDEN
        CardRank cardRank = generateRandomCardRank();
        if(cardRank == CardRank.HIDDEN){
            return TrucoCard.of(cardRank,CardSuit.HIDDEN);
        }
        return TrucoCard.of(cardRank,generateRandomCardSuit());
    }

    public TrucoCard generateRandomCardToPlay(){ //sem cartas viradas (hidden)
        TrucoCard cardToPlay = generateRandomCard();
        while (cardToPlay.getSuit() == CardSuit.HIDDEN) {
            cardToPlay = generateRandomCard();
        }
        return cardToPlay;
    }

    public List<TrucoCard> generateTrucoCardToPlayList(int qnt){
        List<TrucoCard> cardToPlayList = new ArrayList<>();
        for(int i = 0; i < qnt ; i++){
            cardToPlayList.add(generateRandomCard());
        }
        return cardToPlayList;
    }

}