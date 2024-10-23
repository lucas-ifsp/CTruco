package com.local.yuri.impl;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.*;
import static org.junit.jupiter.api.Assertions.*;

class MockRoundTest {

    private final static TrucoCard vira = TrucoCard.of(THREE, CLUBS);

    private final static TrucoCard cardA0 = TrucoCard.of(TWO, CLUBS);
    private final static TrucoCard cardA1 = TrucoCard.of(TWO, HEARTS);
    private final static TrucoCard cardA2 = TrucoCard.of(TWO, SPADES);

    private final static TrucoCard cardB0 = TrucoCard.of(ACE, CLUBS);
    private final static TrucoCard cardB1 = TrucoCard.of(ACE, HEARTS);
    private final static TrucoCard cardB2 = TrucoCard.of(ACE, SPADES);

    private final static TrucoCard cardA3 = TrucoCard.of(KING, CLUBS);
    private final static TrucoCard cardB3 = TrucoCard.of(KING, CLUBS);

    @Test
    void shouldBuildIntel_FirstRound_NobodyPlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            .giveA(cardA0)
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB0)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(),
                List.of(vira),
                vira,
                1
            )
            .botInfo(
                List.of(cardA0, cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_FirstRound_APlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            .giveA(cardA0).play()
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB0)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(),
                List.of(vira, cardA0),
                vira,
                1
            )
            .botInfo(
                List.of(cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_FirstRound_BPlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            .giveA(cardA0)
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB0).play()
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(),
                List.of(vira, cardB0),
                vira,
                1
            )
            .botInfo(
                List.of(cardA0, cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(cardB0)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_SecondRound_NobodyPlayed_AWon() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveA(SEVEN, CLUBS).play()
            .giveB(SIX, CLUBS).play()
            //
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(WON),
                List.of(vira, TrucoCard.of(SEVEN, CLUBS), TrucoCard.of(SIX, CLUBS)),
                vira,
                1
            )
            .botInfo(
                List.of(cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_SecondRound_NobodyPlayed_ADrew() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveA(SEVEN, CLUBS).play()
            .giveB(SEVEN, CLUBS).play()
            //
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(DREW),
                List.of(vira, TrucoCard.of(SEVEN, CLUBS), TrucoCard.of(SEVEN, CLUBS)),
                vira,
                1
            )
            .botInfo(
                List.of(cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_SecondRound_NobodyPlayed_ALost() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveA(SIX, CLUBS).play()
            .giveB(SEVEN, CLUBS).play()
            //
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(LOST),
                List.of(vira, TrucoCard.of(SIX, CLUBS), TrucoCard.of(SEVEN, CLUBS)),
                vira,
                1
            )
            .botInfo(
                List.of(cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_SecondRound_APlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveB(cardB0).play()
            .giveA(cardA0).play()
            // Second Round
            .giveA(cardA1).play()
            //
            .giveA(cardA2)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(WON),
                List.of(vira, cardB0, cardA0, cardA1),
                vira,
                1
            )
            .botInfo(
                List.of(cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_SecondRound_BPlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveA(cardA0).play()
            .giveB(cardB0).play()
            // Second Round
            .giveB(cardB1).play()
            //
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(WON),
                List.of(vira, cardA0, cardB0, cardB1),
                vira,
                1
            )
            .botInfo(
                List.of(cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(cardB1)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_ThirdRound_NobodyPlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveA(cardA0).play()
            .giveB(cardB0).play()
            // Second Round
            .giveB(cardB1).play()
            .giveA(cardA1).play()
            //
            .giveA(cardA2)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(WON, WON),
                List.of(vira, cardA0, cardB0, cardB1, cardA1),
                vira,
                1
            )
            .botInfo(
                List.of(cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_ThirdRound_APlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveA(cardA0).play()
            .giveB(cardB0).play()
            // Second Round
            .giveB(cardB1).play()
            .giveA(cardA1).play()
            // Third Round
            .giveA(cardA2).play()
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(WON, WON),
                List.of(vira, cardA0, cardB0, cardB1, cardA1, cardA2),
                vira,
                1
            )
            .botInfo(
                List.of(),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_ThirdRound_BPlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveB(cardB0).play()
            .giveA(cardA0).play()
            // Second Round
            .giveA(cardA1).play()
            .giveB(cardB1).play()
            // Third Round
            .giveB(cardB2).play()
            .giveA(cardA2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(WON, WON),
                List.of(vira, cardB0, cardA0, cardA1, cardB1, cardB2),
                vira,
                1
            )
            .botInfo(
                List.of(cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(cardB2)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_AllCardsPlayed() {
        GameIntel intel = MockRound
            .vira(vira)
            // First Round
            .giveB(cardB0).play()
            .giveA(cardA0).play()
            // Second Round
            .giveA(cardA1).play()
            .giveB(cardB1).play()
            // Third Round
            .giveB(cardB2).play()
            .giveA(cardA2).play()
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(WON, WON, WON),
                List.of(vira, cardB0, cardA0, cardA1, cardB1, cardB2, cardA2),
                vira,
                1
            )
            .botInfo(
                List.of(),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldNotBuildIntel_WithLessThenThreeCards() {
        RuntimeException e0 = assertThrows(RuntimeException.class, () -> {
            GameIntel intel = MockRound
                .vira(vira)
                .giveA(cardA0)
                .giveA(cardA1)
                .giveB(cardB0)
                .giveB(cardB1)
                .giveB(cardB2)
                .build();
        });

        assertEquals(MockRound.INVALID_NUMBER_OF_CARDS_MSG, e0.getMessage());

        RuntimeException e1 = assertThrows(RuntimeException.class, () -> {
            GameIntel intel = MockRound
                .vira(vira)
                .giveA(cardA0)
                .giveA(cardA1)
                .giveA(cardA2)
                .giveB(cardB0)
                .giveB(cardB1)
                .build();
        });

        assertEquals(MockRound.INVALID_NUMBER_OF_CARDS_MSG, e1.getMessage());
    }

    @Test
    void shouldNotBuildIntel_WithMoreThenThreeCards() {
        RuntimeException e0 = assertThrows(RuntimeException.class, () -> {
            GameIntel intel = MockRound
                .vira(vira)
                .giveA(cardA0)
                .giveA(cardA1)
                .giveA(cardA2)
                .giveA(cardA3)
                .giveB(cardB0)
                .giveB(cardB1)
                .giveB(cardB2)
                .build();
        });

        assertEquals(MockRound.INVALID_NUMBER_OF_CARDS_MSG, e0.getMessage());

        RuntimeException e1 = assertThrows(RuntimeException.class, () -> {
            GameIntel intel = MockRound
                .vira(vira)
                .giveA(cardA0)
                .giveA(cardA1)
                .giveA(cardA2)
                .giveB(cardB0)
                .giveB(cardB1)
                .giveB(cardB2)
                .giveB(cardB3)
                .build();
        });

        assertEquals(MockRound.INVALID_NUMBER_OF_CARDS_MSG, e1.getMessage());
    }

    @Test
    void shouldNotBuildIntel_WithInvalidPlays_PlayBeforeCard() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            GameIntel intel = MockRound
                .vira(vira)
                .play()
                .giveA(cardA0)
                .giveA(cardA1)
                .giveA(cardA2)
                .giveB(cardB0)
                .giveB(cardB1)
                .giveB(cardB2)
                .build();
        });

        assertEquals(MockRound.INVALID_PLAY_STATE_MSG, e.getMessage());
    }

    @Test
    void shouldNotBuildIntel_WithInvalidPlays_PlayTwice() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            GameIntel intel = MockRound
                .vira(vira)
                .giveA(cardA0).play()
                .giveA(cardA1).play()
                .giveA(cardA2)
                .giveB(cardB0)
                .giveB(cardB1)
                .giveB(cardB2)
                .build();
        });

        assertEquals(MockRound.INVALID_PLAY_STATE_MSG, e.getMessage());
    }

    @Test
    void shouldBuildIntel_WithBotScore() {
        GameIntel intel = MockRound
            .vira(vira)
            .giveScoreA(5)
            .giveB(cardB0)
            .giveA(cardA0)
            .giveA(cardA1)
            .giveB(cardB1)
            .giveB(cardB2)
            .giveA(cardA2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(),
                List.of(vira),
                vira,
                1
            )
            .botInfo(
                List.of(cardA0, cardA1, cardA2),
                5
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_WithOpponentScore() {
        GameIntel intel = MockRound
            .vira(vira)
            .giveScoreB(5)
            .giveA(cardA0)
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB0)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(),
                List.of(vira),
                vira,
                1
            )
            .botInfo(
                List.of(cardA0, cardA1, cardA2),
                0
            )
            .opponentScore(5)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }

    @Test
    void shouldBuildIntel_WithHandPoints() {
        GameIntel intel = MockRound
            .vira(vira)
            .hand(3)
            .giveA(cardA0)
            .giveA(cardA1)
            .giveA(cardA2)
            .giveB(cardB0)
            .giveB(cardB1)
            .giveB(cardB2)
            .build();

        GameIntel expect = GameIntel.StepBuilder.with()
            .gameInfo(
                List.of(),
                List.of(vira),
                vira,
                3
            )
            .botInfo(
                List.of(cardA0, cardA1, cardA2),
                0
            )
            .opponentScore(0)
            .opponentCard(null)
            .build();

        assertEquals(expect, intel);
    }
}