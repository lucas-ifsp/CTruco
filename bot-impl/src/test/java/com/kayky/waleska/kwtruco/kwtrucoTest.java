package com.kayky.waleska.kwtruco;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class kwtrucoTest {
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
    class MaoDeOnzeTests
    {
        @Test
        @Tag("MaoDeOnze")
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
        @Tag("MaoDeOnze")
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
        @Tag("MaoDeOnze")
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

        @Test
        @Tag("MaoDeOnze")
        @DisplayName("Return true for mão de onze when bot has high rank cards")
        void shouldReturnTrueWhenBotHasHighRankCards() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(8);

            boolean response = kwtrucoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertTrue(response);
        }
    }

    @Nested
    class ChooseCardTests{
        @Test
        @Tag("ChooseCard")
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
        @Tag("ChooseCard")
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
        @Tag("ChooseCard")
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
        @Tag("ChooseCard")
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
        @Tag("ChooseCard")
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
        @Tag("ChooseCard")
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
        @Tag("ChooseCard")
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
        @Tag("ChooseCard")
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



    @Nested
    class DecideIfRaises {
        @Test
        @Tag("DecideIfRaises")
        @DisplayName("Check if has good hand and specific conditions")
        public void checkIfHasGoodHandAndSpecificConditions() {
            TrucoCard trumpCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> playedCards = Arrays.asList(trumpCard, opponentCard);

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), playedCards, trumpCard, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(8)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertTrue(shouldRaise);
        }

        @Test
        @Tag("DecideIfRaises")
        @DisplayName("Check if not has good hand and specific conditions")
        public void checkIfNotHasGoodHandAndSpecificConditions() {
            TrucoCard trumpCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> playedCards = Arrays.asList(trumpCard, opponentCard);

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), playedCards, trumpCard, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(8)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertTrue(shouldRaise);
        }

        @Test
        @Tag("DecideIfRaises")
        @DisplayName("Do not raise when the bot has a weak hand")
        public void doNotRaiseWhenTheBotHasAWeakHand() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            List<TrucoCard> playedCards = Arrays.asList(
                    opponentCard,
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), playedCards, vira, 1)
                    .botInfo(botCards, 3)
                    .opponentScore(3)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertFalse(shouldRaise);
        }

        @Test
        @Tag("DecideIfRaises")
        @DisplayName("Bot should raise when it has manilha")
        public void botShouldRaiseWhenItHasManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    opponentCard);
            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(5)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertThat(shouldRaise).isTrue();
        }

        @Test
        @Tag("DecideIfRaises")
        @DisplayName("Bot should not raise if doesn't have manilha")
        public void botShouldNotRaiseIfDoesntHaveManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            List<TrucoCard> openCards = Arrays.asList(
                    vira,
                    opponentCard);
            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 9)
                    .opponentScore(5)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertFalse(botCards.stream().anyMatch(card -> card.isManilha(vira)));
        }
        @Test
        @DisplayName("Should not raise if player has only one strong card")
        public void shouldNotRaiseIfPlayerHasOnlyOneStrongCard() {
            TrucoCard goodCard = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );


            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, goodCard, 1)
                    .botInfo(botCards, 4)
                    .opponentScore(6)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertFalse(shouldRaise);
        }

        @Test
        @DisplayName("Return True if player has more the one strong card")
        public void returnTrueIfPlayerHasMoreTheOneStrongCard() {
            TrucoCard goodCard = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );


            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, goodCard, 1)
                    .botInfo(botCards, 4)
                    .opponentScore(6)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertTrue(shouldRaise);
        }

        @Test
        @DisplayName("Return False When Have Only Low Rank Cards")
        public void returnFalseWhenHaveOnlyLowRankCards() {

            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    opponentCard);

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(Arrays.asList(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS), TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)), 3)
                    .opponentScore(2)
                    .opponentCard(opponentCard);

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertFalse(shouldRaise);
        }

        @Test
        @DisplayName("Return false when opponent has strong cards")
        public void returnFalseWhenOpponentHasStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(Arrays.asList(TrucoCard.of(CardRank.KING, CardSuit.SPADES), TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)), 3)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

            boolean shouldRaise = kwtrucoBot.decideIfRaises(stepBuilder.build());

            assertFalse(shouldRaise);
        }

        @Test
        @DisplayName("Return false when 1 point is missing to win")
        public void returnFalseWhen1PointIsMissingToWin() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    opponentCard);
            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 11)
                    .opponentScore(10)
                    .opponentCard(opponentCard);

            assertFalse(kwtrucoBot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Return true if bot have zap")
        void returnTrueIfBotHaveZap() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS), TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

            List<TrucoCard> openCards = Collections.singletonList(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

            TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

            GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(botCards, 6)
                    .opponentScore(2)
                    .opponentCard(opponentCard);

            assertTrue(kwtrucoBot.decideIfRaises(stepBuilder.build()));
        }

    }

}





