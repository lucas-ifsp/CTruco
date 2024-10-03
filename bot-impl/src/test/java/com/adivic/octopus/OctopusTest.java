package com.adivic.octopus;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OctopusTest {
    private Octopus octopus;

    private GameIntel.StepBuilder stepBuilder;

    private GameIntel.StepBuilder createStepBuilder(List<TrucoCard> ourCards, TrucoCard vira, int botScore, int opponentScore, int handpoints) {
        return GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(vira), vira, handpoints)
                .botInfo(ourCards, botScore)
                .opponentScore(opponentScore);
    }


    @BeforeEach
    public void setUp(){
        octopus = new Octopus();
    }
    @Nested
    @DisplayName("Testing the functions to identify manilhas")
    class ManilhaCardsTest{
        @Test
        @DisplayName("Return if the hand contains manilhas")
        void returnIfTheHandContainsManilha(){
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES)
            );
            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 0, 0, 0);

            assertThat(octopus.hasManilha(step.build())).isFalse();
        }

        @Test
        @DisplayName("Return the number of manilhas in the hand")
        void returnTheNumberOfManilhasInTheHand(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 4, 7, 1);

            assertThat(octopus.numberOfManilhas(step.build())).isEqualTo(2);
        }

        @Test
        @DisplayName("Return which manilhas have in hand")
        void returnWhichManilhasHaveInHand(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 4, 4, 1);

            List <TrucoCard> expectedManilhas = List.of(
                    TrucoCard.of(vira.getRank().next(), CardSuit.DIAMONDS),
                    TrucoCard.of(vira.getRank().next(), CardSuit.SPADES),
                    TrucoCard.of(vira.getRank().next(), CardSuit.HEARTS),
                    TrucoCard.of(vira.getRank().next(), CardSuit.CLUBS));

            assertThat(octopus.listOfManilhas(step.build())).containsAnyElementsOf(expectedManilhas);
        }
    }

    @Nested
    @DisplayName("Testing the functions to identify strongest cards")
    class StrongestCardsTest{
        @Test
        @DisplayName("Return if the hand contains THREE")
        void returnIfTheHandContainsThree(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 1, 1, 1);

            assertThat(octopus.hasThree(step.build())).isTrue();
        }

        @Test
        @DisplayName("Return the number of three cards in the hand")
        void returnTheNumberOfThreeCardsInTheHand(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 9, 10, 3);

            assertThat(octopus.numberOfThreeCards(step.build())).isEqualTo(3);
        }
        @Test
        @DisplayName("Return if the hand contains TWO")
        void returnIfTheHandContainsTwo(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 5, 0, 1);

            assertThat(octopus.hasTwo(step.build())).isTrue();
        }

        @Test
        @DisplayName("Return the number of two cards in the hand")
        void returnTheNumberOfTwoCardsInTheHand(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 8, 5, 1);

            assertThat(octopus.numberOfTwoCards(step.build())).isEqualTo(2);
        }

        @Test
        @DisplayName("Return if the hand contains ACE")
        void returnIfTheHandContainsAce(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(ourCards, 5)
                    .opponentScore(0);

            assertThat(octopus.hasAce(stepBuilder.build())).isTrue();
        }
    }

    @Nested
    @DisplayName("Testing the functions to identify game advantages")
    class AdvantagesGameTest{
        @Test
        @DisplayName("Returns if there is at least a three-point advantage")
        void returnsIfThereIsAtLeastAThreePointAdvantage(){

            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 7, 1, 1);

            assertThat(octopus.hasThreePointAdvantage(step.build())).isTrue();
        }
        @Test
        @DisplayName("Returns if there is at least a six-point advantage")

        void returnsIfThereIsAtLeastASixPointAdvantage(){

            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> ourCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));

            GameIntel.StepBuilder step = createStepBuilder(ourCards, vira, 11, 4, 1);

            assertThat(octopus.hasSixPointAdvantage(step.build())).isTrue();
        }
    }
}
