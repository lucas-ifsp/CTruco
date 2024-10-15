package com.brito.macena.boteco.intel.trucoCaller;

import com.brito.macena.boteco.utils.MyHand;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TrucoCaller tests")
public class TrucoCallerTest {

    @BeforeAll
    static void setUpAll() { System.out.println("Starting TrucoCaller tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing TrucoCaller tests..."); }

    @Nested
    @DisplayName("AggressiveTrucoCaller method tests")
    class AgessiveTrucoCallerMethodTests {
        @Test
        @DisplayName("Should call truco when status is excellent")
        void shouldCallTrucoWhenStatusIsExcellent() {
            AggressiveTrucoCaller caller = new AggressiveTrucoCaller();
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            assertTrue(caller.shouldCallTruco(intel, Status.EXCELLENT));
        }

        @Test
        @DisplayName("Should call truco when status is good")
        void shouldCallTrucoWhenStatusIsGood() {
            AggressiveTrucoCaller caller = new AggressiveTrucoCaller();
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            assertTrue(caller.shouldCallTruco(intel, Status.GOOD));
        }
    }

    @Nested
    @DisplayName("PassiveTrucoCaller method tests")
    class PassiveTrucoCallerMethodTests {
        @Test
        @DisplayName("Should call truco if losing by 9 or more and status is medium or bad with 3 cards")
        void shouldCallTrucoWhenLosingByNineOrMoreAndStatusIsMediumOrBad() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(9)
                    .build();

            PassiveTrucoCaller trucoCaller = new PassiveTrucoCaller();
            boolean shouldCall = trucoCaller.shouldCallTruco(intel, Status.MEDIUM);

            assertTrue(shouldCall);
        }

        @Test
        @DisplayName("Should dont call truco if status is excellent and there are 2 cards")
        void shouldDontCallTrucoWhenStatusIsExcellentAndTwoCards() {
            List<TrucoCard> botHand = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botHand, 0)
                    .opponentScore(5)
                    .build();

            PassiveTrucoCaller trucoCaller = new PassiveTrucoCaller();
            boolean shouldCall = trucoCaller.shouldCallTruco(intel, Status.MEDIUM);

            assertFalse(shouldCall);
        }
    }

    @Nested
    @DisplayName("SneakyTrucoCaller method tests")
    class SneakyTrucoCallerMethodTests {

    }
}
