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
}
