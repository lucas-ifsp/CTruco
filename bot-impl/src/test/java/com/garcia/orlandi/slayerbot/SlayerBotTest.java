package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
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
        TrucoCard vira = TrucoCard.of(FOUR, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(JACK, CardSuit.HEARTS);
        TrucoCard zap = TrucoCard.of(CardRank.FIVE, CLUBS);
        TrucoCard winningCard = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

        List<TrucoCard> cards = Arrays.asList(zap, winningCard);

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

    @Test
    @DisplayName("Should not play a hidden card if second to play in the first round")
    void shouldNotPlayHiddenCardIfSecondToPlayInFirstRound() {
        List<GameIntel.RoundResult> roundResults = Collections.emptyList();

        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, Collections.singletonList(vira), vira, 1)
                .botInfo(cards, 1)
                .opponentScore(0)
                .opponentCard(opponentCard);

        GameIntel game = stepBuilder.build();

        SlayerBot bot = new SlayerBot();

        CardToPlay cardToPlay = bot.chooseCard(game);
        TrucoCard chosenCard = cardToPlay.value();

        assertNotEquals(TrucoCard.closed(), chosenCard, "Chosen card should not be a hidden card if second to play in the first round");

    }
    @Test
    @DisplayName("Should play the weakest card when second to play and not holding any manilha")
    void shouldPlayWeakestCardWhenSecondToPlayWithoutManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
        );

        TrucoCard opponentManilha = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
        List<TrucoCard> openCards = List.of(vira, opponentManilha);

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(Collections.emptyList(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentManilha);

        SlayerBot bot = new SlayerBot();
        CardToPlay card = bot.chooseCard(stepBuilder.build());
        TrucoCard expectedCard = TrucoCard.of(CardRank.SEVEN, SPADES);
        assertThat(card.value()).isEqualTo(expectedCard);
    }

    @Test
    @DisplayName("Should play the matching card to tie, then play the strongest card next round")
    void shouldPlayMatchingThenStrongestCard() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

        List<TrucoCard> openCards = List.of(vira, opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        CardToPlay firstPlay = bot.chooseCard(stepBuilder.build());
        TrucoCard expectedTieCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
        assertThat(firstPlay.value()).isEqualTo(expectedTieCard);

        List<TrucoCard> remainingCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
        );
        List<TrucoCard> updatedOpenCards = List.of(vira, opponentCard, expectedTieCard);
        GameIntel updatedGame = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), updatedOpenCards, vira, 1)
                .botInfo(remainingCards, 0)
                .opponentScore(0)
                .build();

        CardToPlay secondPlay = bot.chooseCard(updatedGame);
        TrucoCard expectedManilha = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
        assertThat(secondPlay.value()).isEqualTo(expectedManilha);
    }

    @Test
    @DisplayName("Should request truco if tied in first round and holding a manilha")
    void shouldRequestTrucoAfterTieIfHoldingManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> cards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
        );

        TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.DREW);

        List<TrucoCard> openCards = List.of(vira, opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        SlayerBot bot = new SlayerBot();

        boolean shouldRequestTruco = bot.decideIfRaises(stepBuilder.build());
        assertThat(shouldRequestTruco).isTrue();
    }
}

