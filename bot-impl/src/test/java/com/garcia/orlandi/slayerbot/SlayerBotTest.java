package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlayerBotTest {
    List<GameIntel.RoundResult> roundResults;
    TrucoCard vira;
    List<TrucoCard> openCards;
    List<TrucoCard> cards;
    GameIntel.StepBuilder stepBuilder;
    SlayerBotUtils utils;

    @Test
    @DisplayName("If second to play with 2 manilhas play non-manilha to win if possible, otherwise play the weakest manilha to guarantee the round")
    void shouldPlayNonManilhaToWinFirstRoundIfPossibleOtherwiseWeakestManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        GameIntel gameIntel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(opponentCard), vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .build();

        SlayerBot bot = new SlayerBot();

        CardToPlay cardToPlay = bot.chooseCard(gameIntel);

        TrucoCard playedCard = cardToPlay.value();

        if (TrucoCard.of(CardRank.KING, CardSuit.HEARTS).compareValueTo(opponentCard, vira) > 0) {
            // se a nao manilha vencer, deve ser escolhida
            assertThat(playedCard).isEqualTo(TrucoCard.of(CardRank.KING, CardSuit.HEARTS));
        } else {
            // caso constrario, joga a manilha mais fraca
            assertThat(playedCard).isEqualTo(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
        }
    }
}
