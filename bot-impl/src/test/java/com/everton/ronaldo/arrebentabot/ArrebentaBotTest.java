package com.everton.ronaldo.arrebentabot;

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
    void beforeEach(){
        arrebentaBot = new ArrebentaBot();
    }

    @Nested
    class chooseCard {

        @Test
        @DisplayName("Shoul choose the higher card when tied")
        void shouldChooseTheHigherCardWhenTied(){
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
        void shouldChooseTheHigherCardWhenWonFirstRound(){
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
        void shouldChooseMiddleCardWhenOpponentCardIsEqualOrHigherTheSmallerCardAndEqualOrSmallerTheHigherCard(){
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
        void shouldChooseHigherCardWhenOpponentCardIsHigherOrEqualThenMiddleCard(){
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
        void shouldChooseSmallerCardWhenOpponentCardIsHigherThenAllBotCards(){
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
        void shouldChooseSmallerCardWhenOpponentCardIsSmallerThenAllBotCards(){
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        void shouldNotAcceptMaoDeOnzeWhemRelativeValueOfTheCardsAreMinorThenTenAndTheOpponentScoreUnderEleven(){

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 11)
                    .opponentScore(10);
            assertFalse(arrebentaBot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Shoul accept mao de onze when opponent score is 11")
        void shouldAcceptMaoDeOnzeWhemOpponentScoreIsEleven(){

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(cards, 11)
                    .opponentScore(11);
            assertTrue(arrebentaBot.getMaoDeOnzeResponse(stepBuilder.build()));
        }
    }
}