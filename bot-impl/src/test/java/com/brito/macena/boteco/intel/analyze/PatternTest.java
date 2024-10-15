package com.brito.macena.boteco.intel.analyze;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pattern Tests")
public class PatternTest {
    @BeforeAll
    static void setupAll() { System.out.println("Starting Pattern tests..."); }

    @AfterAll
    static void tearDownAll() { System.out.println("Finishing Pattern tests..."); }

    @Nested
    @DisplayName("threeCardsHandler method tests")
    class ThreeCardsHandlerMethodTests {
        @Test
        @DisplayName("Returns EXCELLENT when having at least two manilhas")
        void returnsExcellentWhenHavingAtLeastTwoManilhas() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                    .botInfo(List.of(
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
                    ), 0)
                    .opponentScore(0)
                    .build();

            Pattern pattern = new Pattern(intel);
            Status result = pattern.threeCardsHandler(intel.getCards());

            assertThat(result).isEqualTo(Status.EXCELLENT);
        }
    }

    @Test
    @DisplayName("Returns GOOD when having one manilha and second best card value is at least 5")
    void returnsGoodWhenHavingOneManilhaAndSecondBestCardValueIsAtLeast5() {
        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), 0)
                .botInfo(List.of(
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
                ), 0)
                .opponentScore(0)
                .build();

        Pattern pattern = new Pattern(intel);
        Status result = pattern.threeCardsHandler(intel.getCards());

        assertThat(result).isEqualTo(Status.GOOD);
    }
}
