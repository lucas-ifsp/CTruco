package com.brito.macena.boteco.intel.trucoCaller;

import com.brito.macena.boteco.intel.profiles.Agressive;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.*;

import java.util.List;

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

    }

    @Nested
    @DisplayName("SneakyTrucoCaller method tests")
    class SneakyTrucoCallerMethodTests {

    }
}
