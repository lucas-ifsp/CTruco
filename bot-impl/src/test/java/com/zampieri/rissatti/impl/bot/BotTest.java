package com.zampieri.rissatti.impl.bot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.ACE, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If have zap")
    void testIfHaveZap() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);
        assertTrue(bot.hasZap(builder.build()));
    }

    @Test
    @DisplayName("If is the first card and have Ouros")
    void testIfFirstCardAndHasOuros() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.SEVEN, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If the opponent hidden first card")
    void testIfOpponentCardsHidden() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.FIVE, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If the opponent has manilha")
    void testIfOpponentHasManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.FOUR, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If no have strength cards")
    void testIfNoHaveStrengthCards() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.FOUR, bot.chooseCard(builder.build()).content().getRank());
    }


    @Test
    @DisplayName("If has manilha but don`t use in first round")
    void testIfHasManilhaButDontUseInFirstRound() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.KING, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("If round drew, play the highest card")
    void testIfRoundDrewPlayTheHighestCard() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.SEVEN, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Decide if raises with manilha")
    void testDecidesIfRaisesWithManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(0, bot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Decide if raises when opponent has 9 points or more")
    void testDecidesIfRaisesWhenOpponentHas9PointsOrMore() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(9)
                .opponentCard(opponentCard);

        assertEquals(-1, bot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Decide if raises when dont have cards higher than Jack")
    void testDecidesIfRaisesWhenDontHaveCardsHigherThanJack() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Collections.singletonList(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(-1, bot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Decide if raises with good cards")
    void testDecidesIfRaisesWithGoodCards() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(0, bot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Play the highest card if the round value is six or more")
    void testPlayTheHighestCardIfTheRoundValueIsSixOrMore() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 6)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.THREE, bot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Raises if has casal")
    void testRaisesIfHasCasal() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 6)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(1, bot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Raises if won first round and has manilha")
    void testRaisesIfWonFirstRoundAndHasManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(1, bot.getRaiseResponse(builder.build()));
    }
}
