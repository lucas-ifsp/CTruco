package com.brito.macena.boteco.intel.trucoResponder;

import com.brito.macena.boteco.intel.trucoCaller.PassiveTrucoCaller;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TrucoResponder Tests")
public class TrucoResponderTest {
    @BeforeAll
    static void setUpAll() { System.out.println("Starting TrucoResponder tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing TrucoResponder tests..."); }

    @Nested
    @DisplayName("AggressiveTrucoCaller method tests")
    class AggressiveTrucoCallerMethodTests {
        @Test
        @DisplayName("Should accept raise when status is MEDIUM and score difference is greater than -4")
        void shouldReturnZeroWhenStatusIsMediumAndScoreDifferenceIsGreaterThanMinusFour() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 7)
                    .opponentScore(10)
                    .build();

            AggressiveTrucoResponder trucoResponder = new AggressiveTrucoResponder();
            int response = trucoResponder.getRaiseResponse(intel, Status.MEDIUM);

            assertEquals(0, response);
        }

        @Test
        @DisplayName("Should reject raise when status is BAD with 3 cards")
        void shouldRejectRaiseWhenStatusIsBadWithThreeCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(5)
                    .build();

            AggressiveTrucoResponder trucoResponder = new AggressiveTrucoResponder();
            int response = trucoResponder.getRaiseResponse(intel, Status.BAD);

            assertEquals(-1, response);
        }

        @Test
        @DisplayName("Should increase when the state is GOOD and the bot has won the first round")
        void shouldIncreaseWhenStatusIsGoodAndWonFirstRound() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(7)
                    .build();

            AggressiveTrucoResponder trucoResponder = new AggressiveTrucoResponder();
            int response = trucoResponder.getRaiseResponse(intel, Status.GOOD);

            assertEquals(1, response);
        }
    }

    @Nested
    @DisplayName("PassiveTrucoCaller method tests")
    class PassiveTrucoCallerMethodTests {
        @Test
        @DisplayName("Should return -1 when status is BAD with 3 cards")
        void shouldReturnMinusOneWhenStatusIsBadWithThreeCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(5)
                    .build();

            PassiveTrucoResponder trucoResponder = new PassiveTrucoResponder();
            int response = trucoResponder.getRaiseResponse(intel, Status.BAD);

            assertEquals(-1, response);
        }

        @Test
        @DisplayName("Should return 0 when won first round and has 2 cards")
        void shouldReturnZeroWhenWonFirstRoundWithTwoCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(botHand, 8)
                    .opponentScore(6)
                    .build();

            PassiveTrucoResponder trucoResponder = new PassiveTrucoResponder();
            int response = trucoResponder.getRaiseResponse(intel, Status.BAD);

            assertEquals(0, response);
        }

        @Test
        @DisplayName("Should return 1 when status is GOOD and score difference is -6 or worse with 2 cards")
        void shouldReturnOneWhenStatusIsGoodAndScoreDifferenceIsMinusSixOrWorseWithTwoCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 6)
                    .opponentScore(12)
                    .build();

            PassiveTrucoResponder trucoResponder = new PassiveTrucoResponder();
            int response = trucoResponder.getRaiseResponse(intel, Status.GOOD);

            assertEquals(1, response);
        }
    }

    @Nested
    @DisplayName("SneakyTrucoCaller method tests")
    class SneakyTrucoCallerMethodTests {
        @Test
        @DisplayName("Should return 1 when status is EXCELLENT")
        void shouldReturnOneWhenStatusIsExcellent() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 10)
                    .opponentScore(5)
                    .build();

            SneakyTrucoResponder trucoResponder = new SneakyTrucoResponder();
            int response = trucoResponder.getRaiseResponse(intel, Status.EXCELLENT);

            assertEquals(1, response);
        }
    }
}
