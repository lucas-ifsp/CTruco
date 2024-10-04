package com.kayky.waleska.kwtruco;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Nested
    class getMaoDeOnzeResponse {

        @Test
        @DisplayName("Return false when opponent has Zap")
        void shouldReturnFalseWhenOpponentHasZap() {

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);
            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(6);
            boolean response = kwtrucoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(response);
        }

        @Test
        @DisplayName("Return true when opponent has 11 points")
        void shouldReturnTrueWhenOpponentHasElevenPoints() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(11);
            boolean response = kwtrucoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertTrue(response);
        }

        @Test
        @DisplayName("Return false for mão de onze when bot has low rank cards")
        void shouldReturnFalseWhenBOtHasLowRankCards() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            List<TrucoCard> badCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES));
            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(badCards, 11)
                    .opponentScore(8);

            boolean response = kwtrucoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(response);
        }



       
        @Nested
        class chooseCard {
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

            @Test
            @DisplayName("Should play a stronger manilha than the opponent's if the bot possesses manilhas")
            public void shouldPlayStrongerManilhaThanTheOpponentIfTheBotPossessesManilhas() {
                TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
                );

                GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 9)
                        .opponentScore(4)
                        .opponentCard(opponentCard);

                CardToPlay selectedBotCard = kwtrucoBot.chooseCard(stepBuilder.build());

                assertEquals(CardRank.THREE, selectedBotCard.content().getRank());
            }

            @Test
            @DisplayName("Selects the Diamonds manilha when available in hand")
            void selectsTheDiamondsManilhaWhenAvailableInHand() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES));

                GameIntel gameIntel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                        .botInfo(botCards, 9)
                        .opponentScore(4)
                        .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS))
                        .build();

                CardToPlay cardToPlay = kwtrucoBot.chooseCard(gameIntel);

                assertEquals(CardSuit.DIAMONDS, cardToPlay.content().getSuit());
            }

            @Test
            @DisplayName("Discards when opponent plays stronger card and no manilhas in hand")
            void discardsWhenOpponentPlaysStrongerCardAndNoManilhasInHand() {
                TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

                GameIntel gameIntel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                        .botInfo(botCards, 9)
                        .opponentScore(4)
                        .opponentCard(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS))
                        .build();

                CardToPlay cardToPlay = kwtrucoBot.chooseCard(gameIntel);

                assertFalse(cardToPlay.isDiscard());
            }

            @Test
            @DisplayName("If possible Plays the highest card available to win the round")
            void playsHighestCardAvailableToWinRoundIfPossible() {
                TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

                GameIntel gameIntel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                        .botInfo(botCards, 9)
                        .opponentScore(4)
                        .opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS))
                        .build();

                CardToPlay cardToPlay = kwtrucoBot.chooseCard(gameIntel);

                assertEquals(CardRank.THREE, cardToPlay.content().getRank());
            }




            }

        }
    }


