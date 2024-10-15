package com.francisco.bruno.pedrohenriquebot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
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




    }





    @Nested
    @DisplayName("Testing chooseCard")
    class ChooseCardTest {
        @Test
        @DisplayName("Should Return Card")
        void shouldReturnCard() {

        }

    }

    @Nested
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
    }

    @Test
    void chooseCard() {
    }

    @Test
    void getRaiseResponse() {
    }
}