package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.*;

class PatriciaAparecidaTest {

    private PatriciaAparecida patricia;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    void setUp() {
        patricia = new PatriciaAparecida();
    }

    @Nested
    @DisplayName("Check the variables to calculate the probability")
    class checkVariables {

        @Nested
        @DisplayName("Number of remainder cards")
        class RemainderCards{

            @Test
            @DisplayName("Start Of the Game")
            public void startOfTheGame() {

                List<TrucoCard> botCards = List.of(
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay());
                TrucoCard vira = generateRandomCardToPlay();
                List<TrucoCard> openCards = List.of(vira);

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(Collections.EMPTY_LIST, openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfRemainderCards(stepBuilder.build()), 36);
            }

            @Test
            @DisplayName("Last Round")
            public void lastRound () {

                List<TrucoCard> botCards = List.of(generateRandomCardToPlay());
                TrucoCard vira = generateRandomCardToPlay();
                List<TrucoCard> openCards = List.of(
                        vira,
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay(),
                        generateRandomCardToPlay());
                List<GameIntel.RoundResult> roundResults = List.of(GameIntel.RoundResult.WON,
                                                                GameIntel.RoundResult.LOST);

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(roundResults, openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfRemainderCards(stepBuilder.build()), 33);
            }

        }

        @Nested
        @DisplayName("Number of best cards known")
        class BestCardsKnown{

            @Test
            @DisplayName("Have no better cards known")
            public void ShouldReturnZeroIfHaveNoBetterCardsKnown(){

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(ACE,CLUBS),
                        TrucoCard.of(ACE,HEARTS),
                        TrucoCard.of(ACE,SPADES));
                TrucoCard vira = TrucoCard.of(FOUR,SPADES);
                List<TrucoCard> openCards = List.of(
                        vira,
                        TrucoCard.of(ACE,DIAMONDS));

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(Collections.EMPTY_LIST, openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfBestCardsKnown(botCards.get(1), stepBuilder.build()),0);
            }

            @Test
            @DisplayName("Have better cards known")
            public void ShouldReturnAPositiveNuberIfHaveBetterCardsKnown(){

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(ACE,CLUBS),
                        TrucoCard.of(ACE,HEARTS),
                        TrucoCard.of(ACE,SPADES));
                TrucoCard vira = TrucoCard.of(THREE,SPADES);
                List<TrucoCard> openCards = List.of(
                        vira,
                        TrucoCard.of(ACE,DIAMONDS));

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(Collections.EMPTY_LIST, openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfBestCardsKnown(botCards.get(1), stepBuilder.build()),1);
            }
        }

        @Nested
        @DisplayName("Number of best cards unknown")
        class BestCardsUnkown {
            @Test
            @DisplayName("Case is Manilha")
            public void ShouldReturnTheNumberOfBetterCardsUnknownInCaseOfCardIsManilha() {

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(SIX, SPADES),
                        TrucoCard.of(SIX, DIAMONDS));
                TrucoCard vira = TrucoCard.of(FIVE, SPADES);
                List<TrucoCard> openCards = List.of(
                        vira,
                        TrucoCard.of(KING, DIAMONDS),
                        TrucoCard.of(SIX, HEARTS),
                        TrucoCard.of(SIX, CLUBS));

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(LOST), openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfBestCardsUnknown(TrucoCard.of(SIX,DIAMONDS), stepBuilder.build()), 0);
            }

            @Test
            @DisplayName("Case isnt Manilha")
            public void ShouldReturnTheNumberOfBetterCardsUnknownInCaseOfCardIsntManilha() {

                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(KING, DIAMONDS),
                        TrucoCard.of(SIX, DIAMONDS));
                TrucoCard vira = TrucoCard.of(FIVE, SPADES);
                List<TrucoCard> openCards = List.of(
                        vira,
                        TrucoCard.of(SIX, SPADES),
                        TrucoCard.of(SIX, HEARTS),
                        TrucoCard.of(SIX, CLUBS));

                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(LOST), openCards, vira, 0).
                        botInfo(botCards, 0).
                        opponentScore(1);

                assertEquals(patricia.getNumberOfBestCardsUnknown(TrucoCard.of(KING, DIAMONDS), stepBuilder.build()), 12);
            }
        }

        @Nested
        @DisplayName("Should return the number of opponent's cards ")
        class CardsOfOpponent {

            @Test
            @DisplayName("Case win the last round")
            public void ShouldReturnTheNumberOfTheOpponentCardsCaseWinLastRound() {
                List<TrucoCard> botCards = List.of(TrucoCard.of(SIX, SPADES),
                        TrucoCard.of(FOUR,SPADES),
                        TrucoCard.of(TWO,HEARTS));

                TrucoCard vira = TrucoCard.of(FIVE,CLUBS);
                List<TrucoCard> openCards = List.of(vira);


                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(LOST,WON),
                                openCards,vira,1).
                        botInfo(botCards,0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfOpponentsCards(stepBuilder.build()),1);
            }

            @Test
            @DisplayName("Case lost the last round")
            public void ShouldReturnTheNumberOfTheOpponentCardsCaseLostLastRound(){
                List<TrucoCard> botCards = List.of(TrucoCard.of(SIX, SPADES),
                        TrucoCard.of(FOUR,SPADES),
                        TrucoCard.of(TWO,HEARTS));

                TrucoCard vira = TrucoCard.of(FIVE,CLUBS);
                List<TrucoCard> openCards = List.of(vira);


                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(WON,LOST),
                                openCards,vira,1).
                        botInfo(botCards,0).
                        opponentScore(0);

                assertEquals(patricia.getNumberOfOpponentsCards(stepBuilder.build()),0);
            }
        }
    }


    @Nested
    @DisplayName("Exception Thrown by Methods Tests")
    class ExceptionThrownByMethodsTests{
        private List<TrucoCard> botCards;
        private TrucoCard vira;
        private List<TrucoCard> openCards ;


        @BeforeEach
        void setCards(){
                botCards = List.of(TrucoCard.of(SIX, SPADES),
                    TrucoCard.of(FOUR,SPADES),
                    TrucoCard.of(TWO,HEARTS));
                vira = TrucoCard.of(FIVE,CLUBS);
                openCards = List.of(vira);
        }
        @Nested
        @DisplayName("Mão de Onze Exception Tests")
        class MaoDeOnzeExceptions{

            @Test
            @DisplayName("Should Throw exception if bot accepts mão de onze without 11 points")
            public void shouldThrowExceptionInMaoDeOnzeWhenBotDoesntHave11Points(){
                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(WON,LOST), openCards, vira, 1).
                        botInfo(botCards, 10).
                        opponentScore(10);

                assertThrows(IllegalArgumentException.class , () -> patricia.getMaoDeOnzeResponse(stepBuilder.build())) ;   }
        }
        @Nested
        @DisplayName("Decide if Raises Tests")
        class DecideIfRaisesExceptions{
            @Test
            @DisplayName("Should Throw exception if bot raises beyond 12 points")
            public void shouldThrowExceptionInDecideIfRaisesIfBotRaiseAfter12Points() {
                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(WON, LOST), openCards, vira, 2).
                        botInfo(botCards, 0).
                        opponentScore(0);

                assertThrows(IllegalArgumentException.class, () -> patricia.decideIfRaises(stepBuilder.build()));
            }
        }
        @Nested
        @DisplayName("ChoiceCardExceptions")
        class chooseCardExceptions{
            @Test
            @DisplayName("Should Throw exception if bot chooses null as card")
            public void shouldThrowExceptionIfDecideToChooseNullCard() {
                stepBuilder = GameIntel.StepBuilder.with().
                        gameInfo(List.of(WON, LOST), openCards, vira, 13).
                        botInfo(Collections.EMPTY_LIST, 0).
                        opponentScore(0);

                assertThrows(IllegalStateException.class, () -> patricia.chooseCard(stepBuilder.build()));
            }
        }
    }




    public GameIntel.RoundResult generateRandomRoundResult() {
        GameIntel.RoundResult[] values = GameIntel.RoundResult.values();

        Random random = new Random();
        int index = random.nextInt(values.length);

        return values[index];
    }

    public CardRank generateRandomCardRank() {
        CardRank[] values = CardRank.values();
        Random random = new Random();
        int index = random.nextInt(values.length);
        return values[index];
    }

    public CardSuit generateRandomCardSuit() {
        CardSuit[] values = CardSuit.values();
        Random random = new Random();
        int index = random.nextInt(values.length);

        while (values[index] == CardSuit.HIDDEN) {
            index = random.nextInt(values.length);
        }

        return values[index];
    }

    public TrucoCard generateRandomCard(){ //assegura que CardRank.HIDDEN só receba CardSuit.HIDDEN
        CardRank cardRank = generateRandomCardRank();
        if(cardRank == CardRank.HIDDEN){
            return TrucoCard.of(cardRank,CardSuit.HIDDEN);
        }
        return TrucoCard.of(cardRank,generateRandomCardSuit());
    }

    public TrucoCard generateRandomCardToPlay(){ //sem cartas viradas (hidden)
        TrucoCard cardToPlay = generateRandomCard();
        while (cardToPlay.getSuit() == CardSuit.HIDDEN) {
            cardToPlay = generateRandomCard();
        }
        return cardToPlay;
    }

}