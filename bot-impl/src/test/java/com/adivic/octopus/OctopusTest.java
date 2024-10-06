package com.adivic.octopus;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class OctopusTest {
    private Octopus octopus;

    private GameIntel.StepBuilder stepBuilder;

    private GameIntel.StepBuilder createStepBuilder(List<TrucoCard> ourCards, Optional<List<TrucoCard>> openCards, TrucoCard vira, int botScore, int opponentScore, int handPoints) {
        List<TrucoCard> updatedOpenCards = Stream.concat(
                openCards.orElse(List.of()).stream(),
                Stream.of(vira)
        ).toList();

        return GameIntel.StepBuilder.with()
                .gameInfo(List.of(), updatedOpenCards, vira, handPoints)
                .botInfo(ourCards, botScore)
                .opponentScore(opponentScore);
    }

    @BeforeEach
    public void setUp() {
        octopus = new Octopus();
    }

    @Nested
    @DisplayName("Testing the functions to identify manilhas")
    class ManilhaCardsTest {
        @Test
        @DisplayName("Return if the hand contains manilhas")
        void returnIfTheHandContainsManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES)
            );
            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 0, 0, 0);

            assertThat(octopus.hasManilha(step.build())).isFalse();
        }

        @Test
        @DisplayName("Return the number of manilhas in the hand")
        void returnTheNumberOfManilhasInTheHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 4, 7, 1);

            assertThat(octopus.numberOfManilhas(step.build())).isEqualTo(2);
        }

        @Test
        @DisplayName("Return which manilhas have in hand")
        void returnWhichManilhasHaveInHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 4, 4, 1);

            List<TrucoCard> expectedManilhas = List.of(
                    TrucoCard.of(vira.getRank().next(), CardSuit.DIAMONDS),
                    TrucoCard.of(vira.getRank().next(), CardSuit.SPADES),
                    TrucoCard.of(vira.getRank().next(), CardSuit.HEARTS),
                    TrucoCard.of(vira.getRank().next(), CardSuit.CLUBS));

            assertThat(octopus.listOfManilhas(step.build())).containsAnyElementsOf(expectedManilhas);
        }
    }

    @Nested
    @DisplayName("Testing the functions to identify strongest cards")
    class StrongestCardsTest {
        @Test
        @DisplayName("Return if the hand contains THREE")
        void returnIfTheHandContainsThree() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 1, 1, 1);

            assertThat(octopus.hasThree(step.build())).isTrue();
        }

        @Test
        @DisplayName("Return the number of three cards in the hand")
        void returnTheNumberOfThreeCardsInTheHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 9, 10, 3);

            assertThat(octopus.numberOfThreeCards(step.build())).isEqualTo(3);
        }

        @Test
        @DisplayName("Return if the hand contains TWO")
        void returnIfTheHandContainsTwo() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 5, 0, 1);

            assertThat(octopus.hasTwo(step.build())).isTrue();
        }

        @Test
        @DisplayName("Return the number of two cards in the hand")
        void returnTheNumberOfTwoCardsInTheHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 8, 5, 1);

            assertThat(octopus.numberOfTwoCards(step.build())).isEqualTo(2);
        }

        @Test
        @DisplayName("Return if the hand contains ACE")
        void returnIfTheHandContainsAce() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 5, 0, 1);

            assertThat(octopus.hasAce(step.build())).isTrue();
        }

        @Test
        @DisplayName("Return the number of ace cards in the hand")
        void returnTheNumberOfAceCardsInTheHand() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 10, 10, 1);

            assertThat(octopus.numberOfAceCards(step.build())).isEqualTo(1);
        }

        @Test
        @DisplayName("Return the number of strong cards in the hand")
        void returnTheNumberOfStrongCardsInTheHand() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 10, 10, 1);

            assertThat(octopus.numberOfStrongCards(step.build())).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Testing the functions to identify game advantages")
    class AdvantagesGameTest {
        @Test
        @DisplayName("Returns if there is at least a three-point advantage")
        void returnsIfThereIsAtLeastAThreePointAdvantage() {

            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 7, 1, 1);

            assertThat(octopus.hasThreePointAdvantage(step.build())).isTrue();
        }

        @Test
        @DisplayName("Returns if there is at least a six-point advantage")
        void returnsIfThereIsAtLeastASixPointAdvantage() {

            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 11, 4, 1);

            assertThat(octopus.hasSixPointAdvantage(step.build())).isTrue();
        }
    }

    @Nested
    @DisplayName("Testing the methods to win the first round")
    class WinFirstRound {
        @Test
        @DisplayName("Return the better card to win the first round")
        void returnTheBetterCardToWinTheFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
            TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = new ArrayList<>();
            openCards.add(opponentCard);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.of(openCards), vira, 1, 4, 1);
            assertThat(octopus.chooseBetterCardToWinFirstRound(step.build())).isEqualTo(ourCards.get(1));
        }
    }

    @Nested
    @DisplayName("Testing the functions to choose better plays")
    class ChooseBetterPlays {
        @Test
        @DisplayName("Return an array with our cards sorted in ascending order")
        void returnAnArrayWithOurCardsSortedInAscendingOrder() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.CLUBS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
            );

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 1, 4, 1);

            assertThat(octopus.sortCards(step.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                            TrucoCard.of(CardRank.KING, CardSuit.HEARTS));
        }

        @Test
        @DisplayName("Return who won the round")
        void returnWhoWonTheRound() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(opponentCard), vira, 1)
                    .botInfo(ourCards, 1)
                    .opponentScore(1)
                    .opponentCard(opponentCard);

            assertThat(octopus.whoWonTheRound(stepBuilder.build())).isEqualTo(GameIntel.RoundResult.WON);
        }

        @Test
        @DisplayName("Returns the first player")
        void returnsTheFirstPlayer() {
            GameIntel.StepBuilder step = createStepBuilder(List.of(), Optional.empty(),
                    TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN),6,
                    4, 1);
            assertThat(octopus.checkIfWeAreFirstToPlay(step.build())).isTrue();
        }
    }

    @Nested
    @DisplayName("Testing override methods")
    class OverrideMethods {
        @Test
        @DisplayName("Return getMaoDeOnzeResnponse")
        void returnGetMaoDeOnzeResponse() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 11, 8, 3);
            assertThat(octopus.getMaoDeOnzeResponse(step.build())).isTrue();
        }

        @Test
        @DisplayName("Return decideIfRaises")
        void returnDecideIfRaises() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(ourCards, 9)
                    .opponentScore(8);
            assertThat(octopus.decideIfRaises(stepBuilder.build())).isTrue();
        }

        @Test
        @DisplayName("Test getRaiseResponse with two or more strong cards")
        void testGetRaiseResponseWithTwoOrMoreStrongcards() {
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );
            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS), 5, 5, 1);

            assertThat(octopus.getRaiseResponse(step.build())).isEqualTo(1);
        }

        @Test
        @DisplayName("Test getRaiseResponse with one strong card and six-point advantage")
        void testGetRaiseResponseWithOneStrongCardAndSixPointAdvantage() {
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );
            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS), 12, 5, 1);

            assertThat(octopus.getRaiseResponse(step.build())).isEqualTo(1);
        }

        @Test
        @DisplayName("Test getRaiseResponse with one strong card or three-point advantage")
        void testGetRaiseResponseWithOneStrongCardOrThreePointAdvantage() {
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );
            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), 6, 3, 1);

            assertThat(octopus.getRaiseResponse(step.build())).isEqualTo(0);
        }

        @Test
        @DisplayName("Test getRaiseResponse with no strong cards and no advantages")
        void testGetRaiseResponseWithNoStrongCardsAndNoAdvantages() {
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS), 2, 5, 1);

            assertThat(octopus.getRaiseResponse(step.build())).isEqualTo(-1);
        }
    }

    @Nested
    @DisplayName("Testing choose cards methods")
    class ChooseCardsMethods {
        @Test
        @DisplayName("Return case one when none of the cards are strong in the hand")
        void returnCaseOneWhenNoneOfTheCardsAreStrongInTheHand() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(), vira, 11, 8, 3);
            assertThat(octopus.caseOneWhenNoneOfTheCardsAreStrong(step.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));
        }
        @Test
        @DisplayName("Return case two when one of the cards is strong and we win the first round")
        void returnCaseTwoWhenOneOfTheCardsIsStrongAndWeWinTheFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(ourCards, 9)
                    .opponentScore(8);
            assertThat(octopus.caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));
        }

        @Test
        @DisplayName("Return case two when one of the cards is strong and we lose the first round")
        void returnCaseTwoWhenOneOfTheCardsIsStrongAndWeLoseTheFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), vira, 1)
                    .botInfo(ourCards, 9)
                    .opponentScore(8);
            assertThat(octopus.caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));
        }
        @Test
        @DisplayName("Return case two when one of the cards is manilha and we win first round")
        void returnCaseTwoWhenOneOfTheCardsIsManilhaAndWeWinFirstRound(){
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(ourCards, 1)
                    .opponentScore(2);
            assertThat(octopus.caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
        }

        @Test
        @DisplayName("Return case two when one of the cards is manilha and we lose first round")
        void returnCaseTwoWhenOneOfTheCardsIsManilhaAndWeLoseFirstRound(){
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), vira, 1)
                    .botInfo(ourCards, 1)
                    .opponentScore(2);
            assertThat(octopus.caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
        }

        @Test
        @DisplayName("Return case three when two of the cards are trumps and we win the first round")
        void returnCaseThreeWhenTwoOfTheCardsAreTrumpsAndWeWinFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
            );

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(ourCards, 2)
                    .opponentScore(1);

            assertThat(octopus.caseThreeWhenTwoOfTheCardsAreManilha(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                    );
        }

        @Test
        @DisplayName("Return case three when two of the cards are trumps and we lose the first round")
        void returnCaseThreeWhenTwoOfTheCardsAreTrumpsAndWeLoseFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), vira, 1)
                    .botInfo(ourCards, 1)
                    .opponentScore(3);

            assertThat(octopus.caseThreeWhenTwoOfTheCardsAreManilha(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
                    );
        }
        @Test
        @DisplayName("Return case four when all of the cards are strong and we win first round")
        void returnCaseFourWhenAllOfTheCardsAreStrongAndWeWinFirstRound(){
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(ourCards, 6)
                    .opponentScore(7);
            assertThat(octopus.caseFourWhenAllOfTheCardsAreStrong(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));
        }
        @Test
        @DisplayName("Return case four when all of the cards are strong and we lost first round")
        void returnCaseFourWhenAllOfTheCardsAreStrongAndWeLostFirstRound(){
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), vira, 1)
                    .botInfo(ourCards, 3)
                    .opponentScore(9);
            assertThat(octopus.caseFourWhenAllOfTheCardsAreStrong(stepBuilder.build()))
                    .containsExactly(
                            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));
        }
    }
    @Nested
    @DisplayName("Testing choose card for first round")
    class ChooseCardForFirstRound {
        @Test
        @DisplayName("Return card to play in first round when have zero strong cards")
        void returnCardToPlayInFirstRoundWhenHaveZeroStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.CLUBS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(),
                    vira, 2, 5, 1);
            assertThat(octopus.cardToPlayFirstRoundWhenZeroStrongCards(step.build()))
                    .isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)));
        }

        @Test
        @DisplayName("Return card to play in first round when have one strong cards")
        void returnCardToPlayInFirstRoundWhenHaveOneStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.CLUBS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(ourCards, 1)
                    .opponentScore(2);

            assertThat(octopus.cardToPlayFirstRoundWhenOneStrongCard(stepBuilder.build()))
                    .isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
        }

        @Test
        @DisplayName("Return card to play in first round when have two strong cards an one is manilha")
        void returnCardToPlayInFirstRoundWhenHaveTwoStrongCardsandOneIsManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(ourCards, 1)
                    .opponentScore(2);

            assertThat(octopus.cardToPlayFirstRoundWhenTwoStrongCards(stepBuilder.build()))
                    .isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)));
        }

        @Test
        @DisplayName("Return card to play in first round when have three strong cards")
        void returnCardToPlayInFirstRoundWhenHaveThreeStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(ourCards, 1)
                    .opponentScore(2);

            assertThat(octopus.cardToPlayFirstRoundWhenThreeStrongCards(stepBuilder.build()))
                    .isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)));
        }

        @Nested
        @DisplayName("Testing choose cards for second round")
        class ChooseCardForSecondRound {
            @Test
            @DisplayName("Return card to play in second round when have zero strong cards")
            void returnCardToPlayInSecondRoundWhenHaveZeroStrongCards() {
                TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.CLUBS);

                List<TrucoCard> ourCards = List.of(
                        TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

                GameIntel.StepBuilder step = createStepBuilder(ourCards, Optional.empty(),
                        vira, 2, 5, 1);
                assertThat(octopus.cardToPlaySecondRoundWhenZeroStrongCards(step.build()))
                        .isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)));
            }

            @Test
            @DisplayName("Return the card to play in the second round when there is one or two strong cards and there is a manilha and WON in the first round")
            void returnCardToPlayInSecondRoundWhenOneOrTwoStrongCardAndManilhandWonInTheFirstRound() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);

                List<TrucoCard> ourCards = List.of(
                        TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

                GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                        .botInfo(ourCards, 1)
                        .opponentScore(2);
                assertThat(octopus.cardToPlaySecondRoundWhenOneStrongCards(stepBuilder.build()))
                        .isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
            }

            @Test
            @DisplayName("Return the card to be played in the second round when there is one or two strong card and when there is not manilha and WON in the first round")
            void returnCardToPlayInSecondRoundWhenHaveOneOrTwoStrongCardsAndNotManilhaAndWonInTheFirstRound() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);

                List<TrucoCard> ourCards = List.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

                GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                        .botInfo(ourCards, 1)
                        .opponentScore(2);
                assertThat(octopus.cardToPlaySecondRoundWhenOneStrongCards(stepBuilder.build()))
                        .isEqualTo(CardToPlay.of(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)));
            }

        }
    }
}