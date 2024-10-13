package com.brito.macena.boteco.utils;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Game Tests")
public class GameTest {

    private GameIntel intel;

    @BeforeAll
    static void setupAll() { System.out.println("Starting Game tests..."); }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finishing Game tests...");
    }

    @Nested
    @DisplayName("Tests for wonFirstRound method")
    class WonFirstRoundTests {
        @Test
        @DisplayName("Test wonFirstRound when first round is won")
        public void testWonFirstRoundWhenFirstRoundIsWon() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.wonFirstRound(intel);
            assertThat(result).isTrue();
        }
    }
}
