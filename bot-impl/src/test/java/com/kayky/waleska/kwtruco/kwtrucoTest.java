package com.kayky.waleska.kwtruco;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class
kwtrucoTest {
    private kwtruco kwtrucoBot;

    private final TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
    private final List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
            TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
    );

    @BeforeEach
    public void setUp() {
        kwtrucoBot = new kwtruco();
    }

    @Test
    @DisplayName("Select the lowest card at the beginning of the game")
    void SelectTheLowestCardAtTheBeginningOfTheGame() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 7)
                .opponentScore(2);

        CardToPlay cardToPlay = kwtrucoBot.chooseCard(stepBuilder.build());

        assertEquals(CardRank.FOUR, cardToPlay.content().getRank());
    }

    @Test
    @DisplayName("Bot discards the lowest ranked card when the opponent has a stronger card")
    void botDiscardsLowestRankedCardWhenOpponentHasStrongerCard() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(3)
                .opponentCard(opponentCard);

        CardToPlay botCard = kwtrucoBot.chooseCard(stepBuilder.build());

        assertEquals(CardRank.TWO, botCard.content().getRank());


    }

    @Test
    @DisplayName("Should select the lowest card by suit when the bot lacks manilhas")
    public void shouldSelectTheLowestCardBySuitWhenTheBotLacksManilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.closed());

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.closed());

        CardToPlay botCard = kwtrucoBot.chooseCard(stepBuilder.build());

        assertEquals(CardSuit.HEARTS, botCard.content().getSuit());
    }

    @Test
    @DisplayName("Should select the lowest ranked card that can defeat the opponent")
    public void ShouldSelectTheLowestRankedCardThatCanDefeatTheOpponent() {
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(3)
                .opponentCard(opponentCard);

        assertEquals(CardRank.FOUR, kwtrucoBot.chooseCard(stepBuilder.build()).content().getRank());
    }
}

