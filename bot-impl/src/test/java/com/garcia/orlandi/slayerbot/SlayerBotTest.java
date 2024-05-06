package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SlayerBotTest {
    List<GameIntel.RoundResult> roundResults;
    TrucoCard vira;
    List<TrucoCard> openCards;
    List<TrucoCard> cards;
    GameIntel.StepBuilder stepBuilder;
    SlayerBotUtils utils;

    @Test
    @DisplayName("Should request point raise when holding zap and a winning card")
    void shouldRequestPointRaiseWhenHoldingZapAndWinningCard() {
        TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);
        TrucoCard zap = TrucoCard.of(CardRank.FIVE, CLUBS);
        TrucoCard winningCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

        List<TrucoCard> cards = Arrays.asList(zap, winningCard);
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), Arrays.asList(opponentCard), vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        GameIntel game = stepBuilder.build();
        SlayerBot bot = new SlayerBot();

        boolean shouldRaise = bot.decideIfRaises(game);

        assertTrue(shouldRaise, "SlayerBot should request a point raise when holding zap and a winning card");
    }
}
