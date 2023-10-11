/*
 *  Copyright (C) 2023 Eduardo Aguliari and Ramon Peixe
 *  Contact: eduardo <dot> aguliari <at> ifsp <dot> edu <dot> br
 *  Contact: ramon <dot> peixe <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */
package com.peixe.aguliari.perdenuncabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PerdeNuncaBotTest {
    private PerdeNuncaBot perdeNuncaBot;

    TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
    private final List<TrucoCard> botCards = Arrays.asList(
            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
            TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES));

    @BeforeEach
    public void setUp() {
        perdeNuncaBot = new PerdeNuncaBot();
    }

    @Test
    @DisplayName("Chooses the smallest card when no cards have been played")
    void choosesTheSmallestCardWhenNoCardsHaveBeenPlayed() {
        // Arrange
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4);

        // Act
        CardToPlay card = perdeNuncaBot.chooseCard(stepBuilder.build());

        // Assert
        assertEquals(CardRank.FIVE, card.content().getRank());
    }

    @Test
    @DisplayName("Bot discards lowest rank card when impossible to win.")
    public void testBotDiscardsLowestRankCardWhenImpossibleToWin() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(3)
                .opponentCard(opponentCard);

        CardToPlay botCard = perdeNuncaBot.chooseCard(stepBuilder.build());

        assertEquals(CardRank.FIVE, botCard.content().getRank());
    }

    @Test
    @DisplayName("Should play the lowest manilha that beats the opponent, if possible")
    public void shouldPlayTheLowestManilhaThatBeatsTheOpponentIfPossible() {
        // Arrange
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));
        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        CardToPlay botCard = perdeNuncaBot.chooseCard(stepBuilder.build());

        assertEquals(CardSuit.HEARTS, botCard.content().getSuit());
    }

    @Test
    @DisplayName("Should play the lowest card according to the suit if the bot has no manilhas")
    public void shouldPlayTheLowestCardAccordingToTheSuitIfTheBotHasNoManilhas() {
        // Arrange
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.closed());

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.closed());

        CardToPlay botCard = perdeNuncaBot.chooseCard(stepBuilder.build());

        assertEquals(CardSuit.SPADES, botCard.content().getSuit());
    }

    @Test
    @DisplayName("Should play a manilha stronger than the player's if the bot has manilhas")
    public void shouldPlayManilhaStrongerThanThePlayersIfTheBotHasManilhas() {
        // Arrange
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(opponentCard);

        CardToPlay botCard = perdeNuncaBot.chooseCard(stepBuilder.build());

        assertEquals(CardRank.SIX, botCard.content().getRank());
    }

    @Test
    @DisplayName("Should play the card with the lowest rank to win")
    public void shouldPlayTheCardWithTheLowestRankToWinTest() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(3)
                .opponentCard(opponentCard);

        assertEquals(CardRank.FIVE, perdeNuncaBot.chooseCard(stepBuilder.build()).content().getRank());
    }

    @Test
    @DisplayName("Chooses the Diamonds manilha when in hand")
    void choosesTheDiamondsManilhaWhenInHand() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        GameIntel gameIntel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS))
                .build();

        CardToPlay cardToPlay = perdeNuncaBot.chooseCard(gameIntel);

        assertEquals(CardSuit.DIAMONDS, cardToPlay.content().getSuit());
    }

    @Test
    @DisplayName("Discards when the opponent plays a unbeatable card ")
    void discardsWhenTheOpponentPlaysAUnbeatableCard() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> openCards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));

        GameIntel gameIntel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS))
                .build();

        CardToPlay cardToPlay = perdeNuncaBot.chooseCard(gameIntel);

        assertFalse(cardToPlay.isDiscard());
    }

    @Test
    @DisplayName("Plays an attack card if has at least two attack cards in hand")
    void playsAnAttackCardIfHasAtLeastTwoAttackCardsInHand() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));

        GameIntel gameIntel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS))
                .build();

        CardToPlay cardToPlay = perdeNuncaBot.chooseCard(gameIntel);

        assertEquals(CardSuit.DIAMONDS, cardToPlay.content().getSuit());
    }

    @Test
    @DisplayName("Should not rise if opponent has specific points")
    public void shouldNotRiseIfOpponentHasSpecificPoints() {
        TrucoCard trumpCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        List<TrucoCard> playedCards = Arrays.asList(trumpCard, opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), playedCards, trumpCard, 1)
                .botInfo(botCards, 9)
                .opponentScore(9)
                .opponentCard(opponentCard);

        boolean shouldRaise = perdeNuncaBot.decideIfRaises(stepBuilder.build());

        assertFalse(shouldRaise);
    }

    @Test
    @DisplayName("Should raise if has good hand and specific conditions")
    public void shouldRaiseIfHasGoodHandAndSpecificPoints() {
        // Arrange
        TrucoCard trumpCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
        List<TrucoCard> playedCards = Arrays.asList(trumpCard, opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), playedCards, trumpCard, 1)
                .botInfo(botCards, 9)
                .opponentScore(8)
                .opponentCard(opponentCard);

        boolean shouldRaise = perdeNuncaBot.decideIfRaises(stepBuilder.build());

        assertTrue(shouldRaise);
    }

    @Test
    @DisplayName("Bot does not raise if it has a weak hand")
    public void botDoesNotRaiseIfItHasAWeakHand() {
        // Arrange
        TrucoCard trumpCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
        List<TrucoCard> playedCards = Arrays.asList(
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                opponentCard);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), playedCards, trumpCard, 1)
                .botInfo(botCards, 3)
                .opponentScore(3)
                .opponentCard(opponentCard);

        // Act
        boolean shouldRaise = perdeNuncaBot.decideIfRaises(stepBuilder.build());

        // Assert
        assertFalse(shouldRaise);
    }
}