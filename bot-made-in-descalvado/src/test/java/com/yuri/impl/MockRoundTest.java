package com.yuri.impl;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;

class MockRoundTest {

    private final static TrucoCard vira = TrucoCard.of(THREE, CLUBS);

    private final static TrucoCard cardA0 = TrucoCard.of(TWO, CLUBS);
    private final static TrucoCard cardA1 = TrucoCard.of(TWO, HEARTS);
    private final static TrucoCard cardA2 = TrucoCard.of(TWO, SPADES);

    private final static TrucoCard cardB0 = TrucoCard.of(ACE, CLUBS);
    private final static TrucoCard cardB1 = TrucoCard.of(ACE, HEARTS);
    private final static TrucoCard cardB2 = TrucoCard.of(ACE, SPADES);

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
    void shouldBuildIntel_SecondRound_NobodyPlayed() {

    }

    @Test
    void shouldBuildIntel_SecondRound_APlayed() {

    }

    @Test
    void shouldBuildIntel_SecondRound_BPlayed() {

    }

    @Test
    void shouldBuildIntel_ThirdRound_NobodyPlayed() {

    }

    @Test
    void shouldBuildIntel_ThirdRound_APlayed() {

    }

    @Test
    void shouldBuildIntel_ThirdRound_BPlayed() {

    }

    @Test
    void shouldBuildIntel_AllCardsPlayed() {

    }

    @Test
    void shouldNotBuildIntel_WithLessThenThreeCards() {

    }

    @Test
    void shouldNotBuildIntel_WithMoreThenThreeCards() {

    }

    @Test
    void shouldNotBuildIntel_WithInvalidPlays() {

    }
}