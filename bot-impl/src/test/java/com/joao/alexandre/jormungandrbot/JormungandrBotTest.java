package com.joao.alexandre.jormungandrbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JormungandrBotTest {
    private JormungandrBot jormungandrBot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void config() {
        jormungandrBot = new JormungandrBot();

    }

    @Nested
    @DisplayName("Testing getLowestCardInHand() function")
    class GetLowestCardInHandTest {

        @Test
        @DisplayName("When player has one card in hand, should return that one card as lowest card")
        void shouldReturnCardInHandIfHandOnlyHasOneCard() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0);

            assertEquals(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    jormungandrBot.getLowestCardInHand(stepBuilder.build())
            );
        }

        @Test
        @DisplayName("With 3 cards in hand, should return the lowest card")
        void shouldReturnLowestCardInHand() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0);

            assertEquals(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    jormungandrBot.getLowestCardInHand(stepBuilder.build())
            );
        }

        @Test
        @DisplayName("Should return lowest card even accounting for manilha value")
        void shouldReturnLowestCardInHandEvenWhenManilhaIsLow() {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0);

            assertEquals(
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    jormungandrBot.getLowestCardInHand(stepBuilder.build())
            );
        }
    }

    @Nested
    @DisplayName("Testing getHighestCardInHand() function")
    class GetHighestCardInHandTest {

        @Test
        @DisplayName("When player has one card in hand, should return that one card as highest card")
        void shouldReturnCardInHandIfHandOnlyHasOneCard() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0);

            assertEquals(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    jormungandrBot.getHighestCardInHand(stepBuilder.build())
            );
        }

        @Test
        @DisplayName("With 3 cards in hand, should return the highest card")
        void shouldReturnHighestCardInHand() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0);

            assertEquals(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    jormungandrBot.getHighestCardInHand(stepBuilder.build())
            );
        }

        @Test
        @DisplayName("Should return highest card even accounting for manilha value")
        void shouldReturnHighestCardInHandEvenWhenManilhaIsLow() {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0);

            assertEquals(
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    jormungandrBot.getHighestCardInHand(stepBuilder.build())
            );
        }
    }

    @Nested
    @DisplayName("Testing getCardToTieOpponentsCard() function")
    class GetCardToTieOpponentsCardTest {

        @Test
        @DisplayName("When player only has higher cards than opponent, should return empty")
        void shouldReturnEmptyWhenSelfOnlyHasHigherCardThanOpponent() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            );

            TrucoCard opponentCard = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertTrue(jormungandrBot.getCardToTieOpponentsCard(stepBuilder.build()).isEmpty());
        }
    }
}