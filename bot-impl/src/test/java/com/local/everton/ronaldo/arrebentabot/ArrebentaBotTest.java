package com.local.everton.ronaldo.arrebentabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArrebentaBotTest {
    @Mock
    public ArrebentaBot arrebentaBot;

    private GameIntel.StepBuilder stepBuilder;
    private GameIntel intel;



    @BeforeEach
    void beforeEach() {
        arrebentaBot = new ArrebentaBot();
    }

    @Nested
    class chooseCard {


        @Test
        @DisplayName("Shoul choose the higher card when tied")
        void shouldChooseTheHigherCardWhenTied() {
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9)
                    .opponentCard(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
            assertEquals(CardRank.SEVEN, arrebentaBot.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Shoul choose the higher card when won first round")
        void shouldChooseTheHigherCardWhenWonFirstRound() {
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9)
                    .opponentCard(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));
            assertEquals(CardRank.THREE, arrebentaBot.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Shoul choose middle card when opponent's card is equal or higher then smaller card and equal or smaller the higher card")
        void shouldChooseMiddleCardWhenOpponentCardIsEqualOrHigherTheSmallerCardAndEqualOrSmallerTheHigherCard() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9)
                    .opponentCard(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
            assertEquals(CardRank.TWO, arrebentaBot.chooseCard(stepBuilder.build()).content().getRank());
            assertEquals(CardSuit.SPADES, arrebentaBot.chooseCard(stepBuilder.build()).content().getSuit());
        }

        @Test
        @DisplayName("Shoul choose higher card when opponent's card higher or equal then middle card")
        void shouldChooseHigherCardWhenOpponentCardIsHigherOrEqualThenMiddleCard() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9)
                    .opponentCard(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            assertEquals(CardRank.KING, arrebentaBot.chooseCard(stepBuilder.build()).content().getRank());
            assertEquals(CardSuit.HEARTS, arrebentaBot.chooseCard(stepBuilder.build()).content().getSuit());
        }

        @Test
        @DisplayName("Shoul choose smaller card when opponent's card higher then all bot cards")
        void shouldChooseSmallerCardWhenOpponentCardIsHigherThenAllBotCards() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9)
                    .opponentCard(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));
            assertEquals(CardRank.SIX, arrebentaBot.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Shoul choose smaller card when opponent's card smaller then all bot cards")
        void shouldChooseSmallerCardWhenOpponentCardIsSmallerThenAllBotCards() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9)
                    .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));
            assertEquals(CardRank.KING, arrebentaBot.chooseCard(stepBuilder.build()).content().getRank());
        }
    }

    @Nested
    class gettingMaoDeOnzeResponse {
        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
        );
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

        @Test
        @DisplayName("Shoul not accept mao de onze when the relative value of the cards are minor then 11 and opponent score under 10")
        void shouldNotAcceptMaoDeOnzeWhemRelativeValueOfTheCardsAreMinorThenTenAndTheOpponentScoreUnderEleven() {

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 11)
                    .opponentScore(10);
            assertFalse(arrebentaBot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should accept mao de onze when opponent score is 11")
        void shouldAcceptMaoDeOnzeWhenOpponentScoreIsEleven() {

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 11)
                    .opponentScore(11);
            assertTrue(arrebentaBot.getMaoDeOnzeResponse(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not accept mao de onze when opponent has nine or more points and has weak cards")
        void ShouldNotAcceptMaoDeOnzeWhenOpponentHasNineOrMorePointsAndHasWeakCards() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertFalse(arrebentaBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should accept mao de onze when oppponent has nine or more points and have good cards")
        void ShouldAcceptMaoDeOnzeWhenOppponentHasNineOrMorePointsAndHaveGoodCards() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);
            assertTrue(arrebentaBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should accept mao de onze when oppponent has nine or more points but have casal")
        void ShouldAcceptMaoDeOnzeWhenOppponentHasNineOrMorePointsButHaveCasal() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);
            assertTrue(arrebentaBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should accept mao de onze when opponent has nine or more points but have threes")
        void ShouldAcceptMaoDeOnzeWhenOpponentHasNineOrMorePointsButHaveThrees() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE,CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);
            assertTrue(arrebentaBot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should accept mao de onze when opponent has nine or more points but have twos")
        void ShouldAcceptMaoDeOnzeWhenOpponentHasNineOrMorePointsButHaveTwos() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertTrue(arrebentaBot.getMaoDeOnzeResponse(intel));

        }
    }

    @Nested
    class getRaiseResponse {
        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

        @Test
        @DisplayName("shouls accept raise when have manilhas")
        void shouldAcceptRaiseWhemhaveManilhas() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9);
            assertEquals(arrebentaBot.getRaiseResponse(stepBuilder.build()), 1);
        }

        @Test
        @DisplayName("Shoul accept raise whem the cards value is over or iqual to 18")
        void shouldAcceptRaseWhenTheValueOfTheCardsIsOverOrEqualToFifiteen() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.SPADES)
            );

            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 3)
                    .opponentScore(3);
            assertEquals(arrebentaBot.getRaiseResponse(stepBuilder.build()), 1);
        }

        @Test
        @DisplayName("Should not accept raise when the value of the cards is minor then 15 and have no manilhas")
        void shouldNotAcceptRaiseWhenTheValueOfTheCardsIsMinorThenFifiteenAndHaveNoManilhas() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 11)
                    .opponentScore(11);
            assertEquals(arrebentaBot.getRaiseResponse(stepBuilder.build()), 0);
        }

        @Test
        @DisplayName("Should not accept raise when lost first hand and have weak cards")
        void shouldNotAcceptRaiseWhenLostFirstHandAndHaveWeakCards() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);


            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 0);
        }

        @Test
        @DisplayName("Should accept raise whe lost first hand but has manilhas")
        void shouldNotAcceptRaiseWhenLostFirstHandButHasManilhas() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 1);
        }

        @Test
        @DisplayName("Should accept raise when lost first hand but has Jack of Spades")
        void shouldAcceptRaiseWithJackOfSpades() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 1);
        }

        @Test
        @DisplayName("Should accept raise when lost first hand but has Seven of Clubs")
        void shouldAcceptRaiseWithSevenOfClubs() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 1);
        }

        @Test
        @DisplayName("Should accept raise when lost first hand but has Queen of Clubs")
        void shouldAcceptRaiseWithQueenOfClubs() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 0);
        }

        @Test
        @DisplayName("Should accept raise when lost first hand but has King of Hearts")
        void shouldAcceptRaiseWithKingOfHearts() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS), // Manilha específica
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)); // Outra carta não relacionada à manilha
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 1);
        }

        @Test
        @DisplayName("Should decline raise when lost first hand with good cards")
        void shouldDeclineRaiseWhenLostFirstHandWithGoodCards() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 1);
        }

        @Test
        @DisplayName("Should accept raise when has excellent cards")
        void shouldAcceptRaiseWhenHasExcellentCards() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 0);
        }

        @Test
        @DisplayName("Should accept raise when won first hand with good cards")
        void shouldAcceptRaiseWhenWonFirstHandWithGoodCards() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertEquals(arrebentaBot.getRaiseResponse(intel), 0);
        }

        @Test
        @DisplayName("Should accept raise whe lost first hand but has good Cards")
        void shouldNotAcceptRaiseWhenLostFirstHandButHasGoodCards() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
         assertEquals(arrebentaBot.getRaiseResponse(intel), 1);
        }

        @Test
        @DisplayName("Should not accept raise when in the last hand, have weak cards and opponent score over 8")
        void ShouldNotAcceptRaiseWhenInTheLastHandHaveWeakCardsAndOpponentScoreOverEight() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertEquals(arrebentaBot.getRaiseResponse(intel),0);
        }

        @Test
        @DisplayName("Should not accept raise when in the last hand, have weak cards and opponent score under 9")
        void ShouldNotAcceptRaiseWhenInTheLastHandHaveWeakCardsAndOpponentScoreUnderNine() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(3);

            assertEquals(arrebentaBot.getRaiseResponse(intel),0);
        }

        @Test
        @DisplayName("Should accept raise when in the last hand and opponent score under 9 and have manilha")
        void ShouldAcceptRaiseWhenInTheLastHandAndOpponentScoreUnderNineAndHaveManilha() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(3);

            assertEquals(arrebentaBot.getRaiseResponse(intel),1);
        }

        @Test
        @DisplayName("Should accept raise when in the last hand and opponent score under 9 and have three")
        void ShouldAcceptRaiseWhenInTheLastHandAndOpponentScoreUnderNineAndHaveThree() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(3);

            assertEquals(arrebentaBot.getRaiseResponse(intel),1);
        }

        @Test
        @DisplayName("Should  accept raise when in the last hand and opponent score under 9 and have two")
        void ShouldAcceptRaiseWhenInTheLastHandAndOpponentScoreUnderNineAndHaveTwo() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(3);

            assertEquals(arrebentaBot.getRaiseResponse(intel),1);
        }

        @Test
        @DisplayName("Should  accept raise when in the last hand and opponent score under 9 and have Ace")
        void ShouldAcceptRaiseWhenInTheLastHandAndOpponentScoreUnderNineAndHaveAce() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(3);

            assertEquals(arrebentaBot.getRaiseResponse(intel),1);
        }

        @Test
        @DisplayName("Should accept raise when in the last hand and opponent score over 8 but have manilha")
        void ShouldAcceptRaiseWhenInTheLastHandAndOpponentScoreOverEightButHaveManilha() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertEquals(arrebentaBot.getRaiseResponse(intel),1);
        }

        @Test
        @DisplayName("Should accept raise when in the last hand and opponent score over 8 but have three")
        void ShouldAcceptRaiseWhenInTheLastHandAndOpponentScoreOverEightButHaveThree() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertEquals(arrebentaBot.getRaiseResponse(intel),1);
        }

        @Test
        @DisplayName("Should accept raise when in the last hand and opponent score over 8 but have two")
        void ShouldAcceptRaiseWhenInTheLastHandAndOpponentScoreOverEightButHaveTwo() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertEquals(arrebentaBot.getRaiseResponse(intel),1);
        }
    }

    @Nested
    class DecideIfRaises {
        List<TrucoCard> openCards = Collections.singletonList(
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS));

        @Test
        @DisplayName("shouls not raise when handPoint is equal 3 or higher")
        void shouldNotRaiseWhenHandPointsIsThreeOrHigher() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 3)
                    .botInfo(cards, 9)
                    .opponentScore(9);
            assertFalse(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("shouls not raise when have 11 points")
        void shouldNotRaiseWhemhaveElevenPOints() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 11)
                    .opponentScore(9);
            assertFalse(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("shouls raise when have manilhas")
        void shouldAcceptRaiseWhemhaveManilhas() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES)
            );
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(9);
            assertTrue(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Shoul raise whem carde value is over or iqual to 21 and has manilhas")
        void shouldAcceptRaseWhenTheVCardsValueIsOverOrEqualToFifiteenAndHasManilhas() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
            );

            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(3);
            assertTrue(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Shoul not accept raise whem the value of the cards is minor then 18 and have no manilhas")
        void shouldNotAcceptRaseWhenTheValueOfTheCardsIsMinorThenEighteenAndHaveNoManilhas() {
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(11);
            assertFalse(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should not raise in the first hand")
        void shouldNotRaiseInTheFirstHand() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertFalse(arrebentaBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should raise when in first hand but has casal")
        void shouldRaiseWhenInFirstHandButHasCasal() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertTrue(arrebentaBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should raise when in first hand but has 2 or more THREES")
        void shouldRaiseWhenInFirstHandButHasAtLestTwoThrees() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertTrue(arrebentaBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should raise when in first hand but has 2 or more TWOS")
        void shouldRaiseWhenInFirstHandButHasAtLestTwoTwos() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS));

            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertTrue(arrebentaBot.decideIfRaises(intel));
        }
        @Test
        @DisplayName("Should not raise when opponent's score is close to winning")
        void shouldNotRaiseWhenOpponentScoreIsCloseToWinning() {
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 6)
                    .opponentScore(10);

            assertFalse(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not raise when opponent's cards are stronger")
        void shouldNotRaiseWhenOpponentCardsAreStronger() {
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(9);

            assertFalse(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should not raise when holding weak cards in initial rounds")
        void shouldNotRaiseWhenHoldingWeakCardsInInitialRounds() {
            List<TrucoCard> openCards = Arrays.asList(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );

            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 9)
                    .opponentScore(0);  // Initial round

            assertFalse(arrebentaBot.decideIfRaises(stepBuilder.build()));
        }
        @Test
        @DisplayName("Should not raise when have weak card in last hand")
        void ShouldNotRaiseWhenHaveWeakCardInLastHand() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertFalse(arrebentaBot.decideIfRaises(intel));

        }
        @Test
        @DisplayName("Should raise when have manilha in last hand")
        void ShouldRaiseWhenHaveManilhaInLastHand() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);
            when(intel.getOpponentScore()).thenReturn(9);

            assertTrue(arrebentaBot.decideIfRaises(intel));

        }
        @Test
        @DisplayName("Should raise when have Three in last hand")
        void ShouldRaiseWhenHaveThreeInLastHand() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertTrue(arrebentaBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should raise when have Two in last hand")
        void ShouldRaiseWhenHaveTwoInLastHand() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertTrue(arrebentaBot.decideIfRaises(intel));
        }

        @Test
        @DisplayName("Should raise when have Ace in last hand")
        void ShouldRaiseWhenHaveAceInLastHand() {
            GameIntel intel = mock(GameIntel.class);

            List<TrucoCard> cards = Collections.singletonList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES));
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            when(intel.getVira()).thenReturn(vira);
            when(intel.getCards()).thenReturn(cards);

            assertTrue(arrebentaBot.decideIfRaises(intel));
        }
    }
}