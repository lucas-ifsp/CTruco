package com.zampieri.rissatti.impl.bot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.zampieri.rissatti.impl.bot.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BotTest {

    private Bot bot;
    @BeforeEach
    void setUp() {
        bot = new Bot();
    }

    GameIntel.StepBuilder builder;

    @Test
    @DisplayName("If opponent card is worst")
    void testIfOpponentCardIsWorst() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS));

        TrucoCard oponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(oponentCard);

        assertEquals(CardRank.ACE, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If have zap")
    void testIfHaveZap() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

        TrucoCard oponentCard = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(oponentCard);

        assertEquals(CardRank.SIX, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If is the first card and have Ouros")
    void testIfFirstCardAndHasOuros() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard oponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(oponentCard);

        assertEquals(CardRank.SEVEN, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If the opponent hidden first card")
    void testIfOpponentCardsHidden() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard oponentCard = TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(oponentCard);

        assertEquals(CardRank.FIVE, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If the opponent has manilha")
    void testIfOpponentHasManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

        TrucoCard oponentCard = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(oponentCard);

        assertEquals(CardRank.FOUR, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If no have strength cards")
    void testIfNoHaveStrengthCards() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard oponentCard = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(oponentCard);

        assertEquals(CardRank.FOUR, bot.chooseCard(builder.build()).content().getRank());
    }




}
