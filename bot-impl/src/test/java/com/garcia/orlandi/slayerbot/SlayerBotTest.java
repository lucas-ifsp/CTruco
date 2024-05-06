package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SlayerBotTest {
    List<GameIntel.RoundResult> roundResults;
    TrucoCard vira;
    List<TrucoCard> openCards;
    List<TrucoCard> cards;
    GameIntel.StepBuilder stepBuilder;
    SlayerBotUtils utils;

    @Test
    @DisplayName("If second to play with 2 manilhas play non-manilha to win if possible, otherwise play the weakest manilha to guarantee the round")
    void shouldPlayNonManilhaToWinOrWeakestManilhaIfCannotWin() {
        List<GameIntel.RoundResult> roundResults = List.of();
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
        );
        TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        List<TrucoCard> openCards = List.of(opponentCard);

        GameIntel gameIntel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .build();

        SlayerBot bot = new SlayerBot();
        CardToPlay card = bot.chooseCard(gameIntel);
        TrucoCard chosenCard = card.value();

        if (chosenCard.compareValueTo(opponentCard, vira) > 0) {
            assertNotEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), chosenCard, "Should not play the weakest manilha if non-manilha can win");
            assertEquals(TrucoCard.of(CardRank.KING, CardSuit.HEARTS), chosenCard, "Should play the strong non-manilha to win");
        } else {
            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), chosenCard, "Should play the weakest manilha to guarantee the round");
        }
    }
}
