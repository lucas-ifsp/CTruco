package com.daniel.therover;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TheRoverTest {
    private TheRover theRover;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setup() {
        theRover = new TheRover();
    }

    @Nested
    @DisplayName("isPlayingFirst Tests")
    class isPlayingFirstTest {

        @Test
        @DisplayName("Should return false if player is playing second")
        void ShouldReturnFalseIfPlayerIsPlayingSecond() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertFalse(theRover.isPlayingFirst(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return true if player is playing first")
        void ShouldReturnTrueIfPlayerIsPlayingFirst() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(List.of(), 0)
                    .opponentScore(0);

            assertTrue(theRover.isPlayingFirst(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("getCurrentRound tests")
    class getCurrentRoundTest {

        @Test
        @DisplayName("Should return 1 if in the first round")
        void ShouldReturnOneIfInFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(1, theRover.getCurrentRound(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return 2 if in the second round")
        void ShouldReturnTwoIfInSecondRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(2, theRover.getCurrentRound(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return 3 if in the third round")
        void ShouldReturnThreeIfInThirdRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(3, theRover.getCurrentRound(stepBuilder.build()));
        }
    }

}
