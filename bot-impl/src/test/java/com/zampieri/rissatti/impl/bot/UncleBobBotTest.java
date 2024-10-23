package com.zampieri.rissatti.impl.bot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.zampieri.rissatti.impl.UncleBobBot.UncleBobBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UncleBobBotTest {

    private UncleBobBot uncleBobBot;
    @BeforeEach
    void setUp() {
        uncleBobBot = new UncleBobBot();
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

        assertEquals(CardRank.ACE, uncleBobBot.chooseCard(builder.build()).content().getRank());
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
        assertTrue(uncleBobBot.hasZap(builder.build()));
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

        assertEquals(CardRank.SEVEN, uncleBobBot.chooseCard(builder.build()).content().getRank());
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

        assertEquals(CardRank.FIVE, uncleBobBot.chooseCard(builder.build()).content().getRank());
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

        assertEquals(CardRank.FOUR, uncleBobBot.chooseCard(builder.build()).content().getRank());
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

        assertEquals(CardRank.FOUR, uncleBobBot.chooseCard(builder.build()).content().getRank());
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

        assertEquals(CardRank.KING, uncleBobBot.chooseCard(builder.build()).content().getRank());
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

        assertEquals(CardRank.SEVEN, uncleBobBot.chooseCard(builder.build()).content().getRank());
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

        assertEquals(0, uncleBobBot.getRaiseResponse(builder.build()));
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

        assertEquals(-1, uncleBobBot.getRaiseResponse(builder.build()));
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

        assertEquals(-1, uncleBobBot.getRaiseResponse(builder.build()));
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

        assertEquals(0, uncleBobBot.getRaiseResponse(builder.build()));
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

        assertEquals(CardRank.THREE, uncleBobBot.chooseCard(builder.build()).content().getRank());
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

        assertEquals(1, uncleBobBot.getRaiseResponse(builder.build()));
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

        assertEquals(1, uncleBobBot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if any card worst than a six")
    void testTestIfTheValueOfTheCardIsWorstThanSix() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertTrue(uncleBobBot.AnyCardsWithValueLowerThanSix(builder.build()));
    }

    @Test
    @DisplayName("Test if have zap and cards below 6 should not play eleven-point hand")
    void testTestIfHaveZapAndCardsBelowSixShoudntPlay() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertFalse(uncleBobBot.getMaoDeOnzeResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if draw the round")
    void testTestIfDrawTheRound() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.TWO , uncleBobBot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Test if plays the lowest card first")
    void testTestIfBotPlaysTheLowestCardFirst() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(CardRank.FOUR , uncleBobBot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Test if plays the card to win the round")
    void testTestIfPlaysTheCardToWinTheRound() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);


        assertEquals(CardRank.THREE , uncleBobBot.chooseCard(builder.build()).content().getRank());
    }
    @Test
    @DisplayName("Test if hand has 3 manilhas")
    void testTestIfHandHas3manilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(3, uncleBobBot.CountManilhas(builder.build()));
    }

    @Test
    @DisplayName("Test if hand has 2 manilhas should start a point raise request")
    void testTestIfHandHas2ManilhasStartPointRequest() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertTrue(uncleBobBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Test if hand has 1 zap and two cards above KING should start a point raise request")
    void testTestIfHandHas1ZapAnd2CardAboveKing() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertTrue(uncleBobBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Test if win first round and has a good card should start a point raise request")
    void testTestIfWinFirstRoundAndHasGoodCard() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertTrue(uncleBobBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Test if has 3 manilhas should play eleven point hand")
    void testTestIfHas3ManilhasShoulsPlayElevenPointHand() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertTrue(uncleBobBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Test if has 3 bad cards should not play eleven point hand")
    void testTestIfHas3BadCardsShouldNotPlayElevenPointHand() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.FOUR, CardSuit.SPADES), TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertFalse(uncleBobBot.getMaoDeOnzeResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if has 3 bad cards should not start a point raise request")
    void testTestIfHas3BadCardsShouldNotStartPointRaiseRequest() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), TrucoCard.of(CardRank.FOUR, CardSuit.SPADES), TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertFalse(uncleBobBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Test if has 0 manilhas should not start a a point raise request")
    void testTestIfHas0ManilhasShouldNotStartPointRaiseRequest() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.SPADES), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertFalse(uncleBobBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Test if reject raise request with not good cards")
    void testTestIfRejectRaiseRequestWithNotGoodCards() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(-1, uncleBobBot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if accept raise request with three manilhas")
    void testTestIfAcceptRaiseRequestWithThreeManilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(1, uncleBobBot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if the bot manilha is greater than opponent manilha")
    void testTestIfBotManilhaIsGreaterThanOpponentManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS), TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.TWO, uncleBobBot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Test if the bot manilha is lower than opponent manilha")
    void testTestIfBotManilhaIsLowerThanOpponentManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.SEVEN, uncleBobBot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Test to don't play manilha if the opponent hidden card")
    void testTestIfBotDontPlayManilhaVsOpponentHiddenCard() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Arrays.asList(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN));

        TrucoCard opponentCard = TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(CardRank.SEVEN, uncleBobBot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Test cant start a point raise request if eleven point hand")
    void testCantStartPointRaiseRequestIfElevenPointHand() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertFalse(uncleBobBot.decideIfRaises(builder.build()));
    }

    @Test
    @DisplayName("Test if accept raise request with good cards and win last round")
    void testIfAcceptRaiseRequestWithGoodCardAndWinLastRound() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(0 ,uncleBobBot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if accept raise request with good cards and draw last round")
    void testIfAcceptRaiseRequestWithGoodCardAndDrawLastRound() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(0 ,uncleBobBot.getRaiseResponse(builder.build()));
    }

    @Test
        @DisplayName("Test if Any card higher than two")
    void testIfAnyCardHighertThanTwo() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertTrue(uncleBobBot.AnyCardsWithValueHigherThanTwo(builder.build()));
    }

    @Test
    @DisplayName("Test if Any card higher than King")
    void testIfAnyCardHighertThanKing() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertTrue(uncleBobBot.AnyCardsWithValueHigherThanTwo(builder.build()));
    }

    @Test
    @DisplayName("Test if as mao de onze")
    void testIfAsMaoDeOnze() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 11)
                .botInfo(botCards, 11)
                .opponentScore(11);

        assertTrue(uncleBobBot.HandPointsEqualEleven(builder.build()));
    }

    @Test
    @DisplayName("Test if return the lowest card in hand")
    void testIfReturnLowestCard() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 11)
                .botInfo(botCards, 11)
                .opponentScore(11);

        assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),uncleBobBot.getLowestCard(botCards, vira));
    }

    @Test
    @DisplayName("Test if return the highest card in hand")
    void testIfReturnHighestCard() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 11)
                .botInfo(botCards, 11)
                .opponentScore(11);

        assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),uncleBobBot.getHighestCard(botCards, vira));
    }

    @Test
    @DisplayName("Test if accept raise request with manilha and draw last round")
    void testIfAcceptRaiseRequestWithManilhaAndDrawLastRound() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(1 ,uncleBobBot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if reject raise request without manilha and lost last round")
    void testIfRejectRaiseRequestWithManilhaAndDrawLastRound() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals(-1 ,uncleBobBot.getRaiseResponse(builder.build()));
    }

    @Test
    @DisplayName("Test if is the first to play and dont have good cards")
    void testIfIsFirstToPlayAndHaveLowCards() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS), TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 11)
                .botInfo(botCards, 0)
                .opponentScore(0);

        assertEquals( CardRank.FOUR, uncleBobBot.chooseCard(builder.build()).content().getRank());
    }

    @Test
    @DisplayName("Test if has 3 cards 3 should accept raise ")
    void testIfHas3Cards3ShouldAcceptRaise() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS) ,TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

        List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 11)
                .opponentScore(11);

        assertEquals(0, uncleBobBot.getRaiseResponse(builder.build()));
    }
}
