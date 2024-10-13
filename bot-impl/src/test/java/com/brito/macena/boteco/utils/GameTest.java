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

        @Test
        @DisplayName("Test wonFirstRound when first round is lost")
        public void testWonFirstRoundWhenFirstRoundIsLost() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.wonFirstRound(intel);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Test wonFirstRound when no rounds are played")
        public void testWonFirstRoundWhenNoRoundsArePlayed() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.wonFirstRound(intel);
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Tests for lostFirstRound method")
    class LostFirstRoundTests {
        @Test
        @DisplayName("Test lostFirstRound when first round is lost")
        public void testLostFirstRoundWhenFirstRoundIsLost() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.lostFirstRound(intel);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Test lostFirstRound when first round is won")
        public void testLostFirstRoundWhenFirstRoundIsWon() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.lostFirstRound(intel);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Test lostFirstRound when no rounds are played")
        public void testLostFirstRoundWhenNoRoundsArePlayed() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.lostFirstRound(intel);
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Tests for hasManilha method")
    class HasManilhaTests {
        @Test
        @DisplayName("Test hasManilha when there is no manilha")
        public void testHasManilhaWhenThereIsNoManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.hasManilha(intel);
            assertThat(result).isFalse();
        }
    }
}
