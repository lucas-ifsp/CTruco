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

@DisplayName("TrucoResponder tests")
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
    }

    @Nested
    @DisplayName("PassiveTrucoCaller method tests")
    class PassiveTrucoCallerMethodTests {

    }

    @Nested
    @DisplayName("SneakyTrucoCaller method tests")
    class SneakyTrucoCallerMethodTests {

    }
}
