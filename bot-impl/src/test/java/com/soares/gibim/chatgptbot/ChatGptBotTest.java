package com.soares.gibim.chatgptbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatGptBotTest {

    private ChatGptBot sut;

    GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp(){sut = new ChatGptBot(); }

    @Test
    @DisplayName("If its the last hand and have zap then ask truco")
    void IfItsTheLastRoundAndHaveZapThenAskTruco() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
        );

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertTrue(sut.decideIfRaises(intel.build()));
    }

    @Test
    @DisplayName("If we do the first round we order truco in the second")
    void testIfWeDoTheFirstRoundAskForTrucoInTheSecond() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertTrue(sut.decideIfRaises(intel.build()));

    }
    @Test
    @DisplayName("Decide if you accept 'truco' in the first round with a 'manilha.")
    void TestAcceptTrucoFirstRoundWithManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );
        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );

        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(0, sut.getRaiseResponse(intel.build()));
    }

}

