package com.francisco.bruno.pedrohenriquebot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.model.CardToPlay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.*;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.*;

class PedroHenriqueBotTest {

    private PedroHenriqueBot sut;
    private GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp() {
        sut = new PedroHenriqueBot();
    }

    @Test
    @DisplayName("Should return correct bot name")
    void shouldReturnCorrectBotName() {
        assertEquals("PedroHenrique", sut.getName());
    }

    @Nested
    @DisplayName("Testing getMaoDeOnzeResponse")
    class GetMaoDeOnzeResponseTest {
        @Test
        @DisplayName("Should accept Mao de Onze with 2 manilhas")
        void shouldAcceptMaoDeOnzeWith2Manilhas() {
            TrucoCard vira = TrucoCard.of(ACE, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(ACE, DIAMONDS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards,11)
                    .opponentScore(8);

            assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Decline Mao de Onze with weak hand")
        void declineMaoDeOnzeWithWeakHand() {
            TrucoCard vira = TrucoCard.of(SEVEN, DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards,11)
                    .opponentScore(6);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Accept Mao de Onze when opponent is close to winning")
        void acceptMaoDeOnzeWhenOpponentIsCloseToWinning() {
            TrucoCard vira = TrucoCard.of(QUEEN, HEARTS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(JACK, HEARTS),
                    TrucoCard.of(THREE, SPADES),
                    TrucoCard.of(ACE, DIAMONDS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira),vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(10);

            assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Decline Mão de Onze when hand strength is low even if opponent is close to winning")
        void declineMaoDeOnzeWithWeakHandEvenIfOpponentClose() {
            TrucoCard vira = TrucoCard.of(KING, CLUBS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(10);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Accept Mão de Onze with 1 manilha and 2 high cards")
        void acceptMaoDeOnzeWithOneManilhaAndTwoHighCards() {
            TrucoCard vira = TrucoCard.of(JACK, HEARTS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(THREE, DIAMONDS),
                    TrucoCard.of(KING, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(7);

            assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Decline Mão de Onze when hand strength average is low")
        void declineMaoDeOnzeWithLowHandStrengthAverage() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(SEVEN, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, DIAMONDS)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(9);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }

    }

    @Nested
    @DisplayName("Testing chooseCard")
    class ChooseCardTest {

        @Nested
        @DisplayName("First Round")
        class FirstRound {
            @Test
            @DisplayName("Should use intermediate Card when first to play")
            void shouldUseIntermediateCardWhenFirstToPlay() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(SEVEN, CLUBS),
                        TrucoCard.of(THREE, DIAMONDS)
                );
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira),vira,1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);
                CardToPlay chosenCard = sut.chooseCard(intel.build());
                assertEquals(CardToPlay.of(botCards.get(1)), chosenCard);
            }

            @Test
            @DisplayName("Should play minimal card to win when opponent plays first")
            void shouldPlayMinimalCardToWinWhenOpponentPlaysFirst() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(SEVEN, CLUBS),
                        TrucoCard.of(THREE, DIAMONDS)
                );
                TrucoCard opponentCard = TrucoCard.of(FIVE, CLUBS);
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(Collections.emptyList(), new ArrayList<>(), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                CardToPlay chosenCard = sut.chooseCard(intel.build());

                Optional<TrucoCard> expectedCardOptional = botCards.stream()
                        .filter(card -> card.relativeValue(vira) > opponentCard.relativeValue(vira))
                        .min(Comparator.comparingInt(card -> card.relativeValue(vira)));

                TrucoCard expectedCard = expectedCardOptional
                        .orElseGet(() -> botCards.stream()
                                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                                .orElseThrow(() -> new IllegalStateException("Sem cartas disponíveis")));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }

            @Test
            @DisplayName("Should choose weakest card if cannot win opponent")
            void shouldChooseWeakestCardIfCannotWinOpponent() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(FIVE, CLUBS),
                        TrucoCard.of(SIX, DIAMONDS)
                );
                TrucoCard opponentCard = TrucoCard.of(THREE, CLUBS);
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(),Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);
                CardToPlay chosenCard = sut.chooseCard(intel.build());

                TrucoCard expectedCard = botCards.stream()
                        .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                        .orElse(botCards.get(0));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }

            @Test
            @DisplayName("Should play Strongest Card when strong hand")
            void shouldPlayStrongestCardWhenStrongHand() {
                TrucoCard vira = TrucoCard.of(FOUR, HEARTS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(ACE, SPADES),
                        TrucoCard.of(TWO, CLUBS),
                        TrucoCard.of(THREE, DIAMONDS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(),Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);
                CardToPlay chosenCard = sut.chooseCard(intel.build());

                TrucoCard expectedCard = botCards.stream()
                        .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                        .orElse(botCards.get(0));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }
        }
        @Nested
        @DisplayName("Second Round")
        class SecondRound {
            @Test
            @DisplayName("Should play weak card if winner of the first round")
            void shouldPlayWeakCardIfWinnerOfTheFirstRound() {
                TrucoCard vira = TrucoCard.of(KING, CLUBS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(SEVEN, HEARTS),
                        TrucoCard.of(SIX, SPADES)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(WON), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                CardToPlay chosenCard = sut.chooseCard(intel.build());
                TrucoCard expectedCard = botCards.stream()
                        .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                        .orElse(botCards.get(0));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }

        }

        @Nested
        @DisplayName("Second Round")
        class ThirdRound {

        }

    }

    @Nested
    @DisplayName("Testing decideIfRaises")
    class decideIfRaises {
        @Test
        @DisplayName("Raise when has strong hand in first round")
        void raiseFirstRoundStrongHand() {
            TrucoCard vira = TrucoCard.of(THREE, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(THREE, DIAMONDS)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,3)
                    .botInfo(botCards, 0)
                    .opponentScore(5);

            assertTrue(sut.decideIfRaises(intel.build()));
        }

        @Test
        @DisplayName("Do not raise with weak hand in first round")
        void doNotRaiseFirstRoundWeakHand() {
            TrucoCard vira = TrucoCard.of(SEVEN, DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,3)
                    .botInfo(botCards, 0)
                    .opponentScore(5);

            assertFalse(sut.decideIfRaises(intel.build()));
        }

        @Test
        @DisplayName("Raise when opponent is close to winning")
        void raiseFirstRoundOpponentCloseToWinning() {
            TrucoCard vira = TrucoCard.of(KING, CLUBS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(QUEEN, HEARTS),
                    TrucoCard.of(JACK, CLUBS),
                    TrucoCard.of(KING, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,3)
                    .botInfo(botCards, 0)
                    .opponentScore(10);

            assertTrue(sut.decideIfRaises(intel.build()));
        }

        @Test
        @DisplayName("Raise with manilha and high card")
        void raiseWithManilhaAndHighCard() {
            TrucoCard vira = TrucoCard.of(ACE, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(THREE, SPADES),
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(SEVEN, HEARTS)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,3)
                    .botInfo(botCards, 0)
                    .opponentScore(5);

            assertTrue(sut.decideIfRaises(intel.build()));
        }

        @Test
        @DisplayName("Do not raise when own score is high and ahead")
        void doNotRaiseWhenBotAhead() {
            TrucoCard vira = TrucoCard.of(QUEEN, HEARTS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(THREE, CLUBS),
                    TrucoCard.of(ACE, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,3)
                    .botInfo(botCards, 10)
                    .opponentScore(8);

            assertFalse(sut.decideIfRaises(intel.build()));
        }
    }
}