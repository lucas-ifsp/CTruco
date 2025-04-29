package com.local.casal.impl.vapobot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VapoBotTest {
    private VapoBot vapoBot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void config() {
        vapoBot = new VapoBot();

    }

    @Nested
    @DisplayName("Testing higher card function")
    class HigherCardTest {

        @Test
        @DisplayName("3 of Hearts is higher than 3 of Spades and 2 of Clubs")
        void makeSureToReturnHighest3 () {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = Arrays.asList();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), vapoBot.getHighestCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("4 of Diamonds is higher than 7 of Spades and 3 of Clubs")
        void makeSureToReturnHighestIs4Diamonds () {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = Arrays.asList();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), vapoBot.getHighestCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("Manilha of Clubs is Higher than Manilha of Spades and Manilha of Diamonds")
        void makeSureThatHighestIsJackOfClubs () {
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS), vapoBot.getHighestCard(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Testing lowest card function")
    class LowestCardTest {

        @Test
        @DisplayName("A of Clubs is lower than 2 of Hearts and Manilha")
        void makeSureThatLowestIsAceOfClubs () {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(3);

            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), vapoBot.getLowestCard(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("In method getAverageCardValue()")
    class getIntAverageCardValueTest {

        @Test
        @DisplayName("2D, 3D and 7C average should be 7")
        void shouldCalculateTheRightAverage1(){
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            double average = vapoBot.getAverageCardValue(stepBuilder.build());
            double result = (double) 21/3;

            assertEquals(average, result);
        }

        @Test
        @DisplayName("average of 2D, 3C and 7C with vira being 2C should be 8.666...")
        void shouldCalculateTheRightAverage2(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            double average = vapoBot.getAverageCardValue(stepBuilder.build());
            double result = (double) 26/3;

            assertEquals(average, result);
        }

        @Test
        @DisplayName("average of 3D, 3C and JC with vira being 2C should be 9.66...")
        void shouldCalculateTheRightAverage3(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            double average = vapoBot.getAverageCardValue(stepBuilder.build());
            double result = (double) 29/3;

            assertEquals(average, result);
        }

        @Test
        @DisplayName("average of 3D, 3C and JC with vira being 2C should be 8...")
        void shouldCalculateTheRightAverage4(){
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            double average = vapoBot.getAverageCardValue(stepBuilder.build());
            double result = (double) 16/2;

            assertEquals(average, result);
        }
    }

    @Nested
    @DisplayName("Test if has Zap")
    class HasZapTest {
        @Test
        @DisplayName("Should return true if has zap")
        void shouldReturnTrueIfHasZap() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(3);

            assertTrue(vapoBot.hasZap(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return false if does not have zap")
        void shouldReturnFalseIfDoesNotHaveZap() {
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(3);

            assertFalse(vapoBot.hasZap(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Get last round result test")
    class LastRoundResultTest {
        @Test
        @DisplayName("Should return WON")
        void shouldReturnWon() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(3);

            assertEquals(GameIntel.RoundResult.WON, vapoBot.getLastRoundResult(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return LOST")
        void shouldReturnLost() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 3)
                    .botInfo(myCards, 6)
                    .opponentScore(3);

            assertEquals(GameIntel.RoundResult.LOST, vapoBot.getLastRoundResult(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return WON")
        void shouldReturnWonFirst() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 3)
                    .botInfo(myCards, 6)
                    .opponentScore(3);

            assertEquals(GameIntel.RoundResult.WON, vapoBot.getLastRoundResult(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should throw error if there is no last round played")
        void shouldThrowOutOfBoundsException() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(myCards, 6)
                    .opponentScore(3);

            assertThrows(ArrayIndexOutOfBoundsException.class, () -> vapoBot.getLastRoundResult(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Get round number test")
    class RoundNumberTest {
        @Test
        @DisplayName("Should return 1")
        void shouldReturnRoundOne() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 3)
                    .botInfo(myCards, 6)
                    .opponentScore(3);

            assertEquals(1, vapoBot.getRoundNumber(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return 2")
        void shouldReturnRoundTwo() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(3);

            assertEquals(2, vapoBot.getRoundNumber(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Should have")
    class GetManilhasAmountTest {
        @Test
        @DisplayName("2 manilhas on 6H, AC, 6C")
        void shouldReturn2() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(2, vapoBot.getAmountOfManilhas(stepBuilder.build()));
        }

        @Test
        @DisplayName("1 manilhas on AS, 4H, QC")
        void shouldReturn1() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(1, vapoBot.getAmountOfManilhas(stepBuilder.build()));
        }

        @Test
        @DisplayName("no manilhas on AS, 4H, QC")
        void shouldReturn0() {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(0, vapoBot.getAmountOfManilhas(stepBuilder.build()));
        }

        @Test
        @DisplayName("3 manilhas on AS, AH, AC")
        void shouldReturn3() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(3, vapoBot.getAmountOfManilhas(stepBuilder.build()));
        }
    }
    @Nested
    @DisplayName("Get lowest card that wins against opponent card")
    class LowestCardToWinTest {

        @Test
        @DisplayName("Should return JS when other card is 2S")
        void shouldReturnJackOfSpadesWhenOtherCardIsTwoOfSpades() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            TrucoCard opponentCard = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
            );

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(opponentCard);

            assertEquals(Optional.ofNullable(TrucoCard.of(CardRank.JACK, CardSuit.SPADES)), vapoBot.getLowestCardToWin(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return the lowest card when opponent card is hidden")
        void shouldReturnLowestCardWhenOpponentCardIsHidden() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            TrucoCard opponentCard = TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
            );

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(opponentCard);

            assertEquals(Optional.ofNullable(TrucoCard.of(CardRank.JACK, CardSuit.SPADES)), vapoBot.getLowestCardToWin(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return AS when other card is AC")
        void shouldReturnAceOfSpadesWhenOtherIsAceOfClubs() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(opponentCard);

            assertEquals(Optional.ofNullable(TrucoCard.of(CardRank.ACE, CardSuit.SPADES)), vapoBot.getLowestCardToWin(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return Optional.Empty() when there are no cards that can win")
        void shouldReturnEmptyOptional() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(opponentCard);

            assertEquals(Optional.empty(), vapoBot.getLowestCardToWin(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return AH as the lowest card to win")
        void ShouldReturnAH() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);


            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );
            List<TrucoCard> openCards = List.of(vira, opponentCard);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(opponentCard);

            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), vapoBot.getLowestCardToWin(stepBuilder.build()).get());
        }


    }


    @Nested
    @DisplayName("Check if opponent card")
    class checkIfOpponentCardIsBadTest {

        @Test
        @DisplayName("3S is not a bad card")
        void ShouldCheckThatKSIsNotABadCard() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

            List<TrucoCard> myCards = List.of();

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
            assertFalse(vapoBot.checkIfOpponentCardIsBad(stepBuilder.build()));
        }

        @Test
        @DisplayName("KS is not a bad card")
        void ShouldCheckThat3SIsNotABadCard() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

            List<TrucoCard> myCards = List.of();

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(TrucoCard.of(CardRank.KING, CardSuit.SPADES));

            assertEquals(false, vapoBot.checkIfOpponentCardIsBad(stepBuilder.build()));
        }

        @Test
        @DisplayName("QS is a bad card")
        void ShouldCheckThatQSIsABadCard() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

            List<TrucoCard> myCards = List.of();

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES));

            assertEquals(true, vapoBot.checkIfOpponentCardIsBad(stepBuilder.build()));
        }

        @Test
        @DisplayName("JH is a bad card")
        void ShouldCheckThatJHIsABadCard() {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

            List<TrucoCard> myCards = List.of();

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

            assertEquals(true, vapoBot.checkIfOpponentCardIsBad(stepBuilder.build()));
        }

        @Test
        @DisplayName("6C is a bad card when vira is 5S")
        void ShouldCheckThat6CIsNotABadCard() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

            List<TrucoCard> myCards = List.of();

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS));

            assertEquals(false, vapoBot.checkIfOpponentCardIsBad(stepBuilder.build()));
        }

    }

    @Test
    @DisplayName("Should throw NoSuchElementException when opponent dont play any card yet")
    void ShouldThrowsNoSuchElementException() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

        List<TrucoCard> myCards = List.of();

        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);
        assertThrows(NoSuchElementException.class, (() -> vapoBot.checkIfOpponentCardIsBad(stepBuilder.build())));
    }

    @Nested
    @DisplayName("Check if bot")
    class checkIfWillBeTheFirstToPlayTest {

        @Test
        @DisplayName("will be the first to play")
        void ShouldBeTheFirstToPlay(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

            List<TrucoCard> myCards = List.of();

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(true, vapoBot.checkIfWillBeTheFirstToPlay(stepBuilder.build()));
        }

        @Test
        @DisplayName("wont be the first to play")
        void ShouldNotBeTheFirstToPlay(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

            List<TrucoCard> myCards = List.of();

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1)
                    .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

            assertEquals(false, vapoBot.checkIfWillBeTheFirstToPlay(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Check if bot has advantage")
    class HasAdvantageTest {
        @Test
        @DisplayName("Bot with score 3 and opponent with score 0 should return true")
        void shouldDifferenceOfThreePointsToBotReturnTrue () {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 3)
                    .opponentScore(0)
                    .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

            assertTrue(vapoBot.hasAdvantage(stepBuilder.build()));
        }

        @Test
        @DisplayName("Bot with score 3 and opponent with score 8 should return false")
        void shouldDifferenceOfThreePointsToOpponentReturnFalse () {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 3)
                    .opponentScore(8)
                    .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

            assertFalse(vapoBot.hasAdvantage(stepBuilder.build()));
        }

        @Test
        @DisplayName("Opponent with any score higher than bot score should return false")
        void shouldHigherOpponentScoreReturnFalse () {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 3)
                    .opponentScore(5)
                    .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

            assertFalse(vapoBot.hasAdvantage(stepBuilder.build()));
        }

        @Test
        @DisplayName("Bot with only one score point higher than opponent should return false")
        void shouldHigherBotScoreByOnePointReturnFalse () {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 3)
                    .opponentScore(2)
                    .opponentCard(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

            assertFalse(vapoBot.hasAdvantage(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Counts how many cards have a relative value greater than")
    class NumberOfCardsThatHasARelativeValueGreaterThanTest {

        @Test
        @DisplayName("5 with cards 4D, 7C, AC")
        void shouldReturn2cards() {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(2, vapoBot.numberOfCardsThatHasARelativeValueGreaterThan(stepBuilder.build(), 5));

        }

        @Test
        @DisplayName("7 with cards 4D, 7C, AC")
        void shouldReturn1Card() {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(1, vapoBot.numberOfCardsThatHasARelativeValueGreaterThan(stepBuilder.build(), 7));

        }
    }

    @Nested
    @DisplayName("Get the highest card that is not a manilha")
    class GetTheHighestCardThatIsNotAManilha {

        @Test
        @DisplayName("choose 2H on KC, 2H and 7D with 6H as vira")
        void get2HAsHighestNotManilhaCard() {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS), vapoBot.getHighestCardThatIsNotAManilha(stepBuilder.build()).get());
        }
        @Test
        @DisplayName("choose AC on JC, JH and AC with QD as vira")
        void getACAsHighestNotManilhaCard() {
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);

            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), vapoBot.getHighestCardThatIsNotAManilha(stepBuilder.build()).get());
        }
        @Test
        @DisplayName("return Optional.empty() when all the cards are manilha")
        void shouldReturnOptionalEmpty() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(1);
            assertEquals(Optional.empty(), vapoBot.getHighestCardThatIsNotAManilha(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Choose card test")
    class ChooseCardTest {

        @Nested
        @DisplayName("in first round")
        class FirstRoundTest {

            @Nested
            @DisplayName("if play first")
            class PlayFirstTest {

                @Test
                @DisplayName("and has 2 manilhas")
                void Has2Manilhas() {
                    TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
                    );

                    List<TrucoCard> openCards = List.of(vira);

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1);
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)), vapoBot.chooseCard(stepBuilder.build()));
                }

                @Test
                @DisplayName("and has 3 manilhas")
                void Has3Manilhas() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
                    );

                    List<TrucoCard> openCards = List.of(vira);

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1);
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)), vapoBot.chooseCard(stepBuilder.build()));
                }

                @Test
                @DisplayName("and has one manilha and a strong card")
                void HasOneManilhaWithStrongCard() {
                    TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
                    );

                    List<TrucoCard> openCards = List.of(vira);

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1);
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)), vapoBot.chooseCard(stepBuilder.build()));
                }

                @Test
                @DisplayName("and has one manilha and weak cards")
                void HasOneManilhaWithWeakCards() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = List.of(vira);

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1);
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)), vapoBot.chooseCard(stepBuilder.build()));
                }

                @Test
                @DisplayName("and has no manilhas and strong cards")
                void HasStrongCardsAndNoManilhas() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = List.of(vira);

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1);
//                    System.out.println(vapoBot.chooseCard(stepBuilder.build()));
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)), vapoBot.chooseCard(stepBuilder.build()));
                }
            }

            @Nested
            @DisplayName("if opponent play first")
            class OpponentPlayFirstTest {

                @Nested
                @DisplayName("and can win the round")
                class CanWin {

                    @Test
                    @DisplayName("and has 2 manilhas and should win")
                    void Has2Manilhas() {
                        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
                        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);


                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
                        );
                        List<TrucoCard> openCards = List.of(vira, opponentCard);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1)
                                .opponentCard(opponentCard);
                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)), vapoBot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("and has 3 manilhas and should win")
                    void Has3Manilhas() {
                        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
                        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);


                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
                        );
                        List<TrucoCard> openCards = List.of(vira, opponentCard);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1)
                                .opponentCard(opponentCard);
                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.SPADES)), vapoBot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("and has one manilha should win")
                    void HasOneManilhas() {
                        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
                        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);


                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
                        );
                        List<TrucoCard> openCards = List.of(vira, opponentCard);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1)
                                .opponentCard(opponentCard);
                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)), vapoBot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("and has no manilha should win")
                    void HasNoManilhas() {
                        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
                        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);


                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                                TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        );
                        List<TrucoCard> openCards = List.of(vira, opponentCard);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1)
                                .opponentCard(opponentCard);
                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)), vapoBot.chooseCard(stepBuilder.build()));
                    }

                    @Nested
                    @DisplayName("and cannot win the round")
                    class CannotWin {
                        @Test
                        @DisplayName("and has 2 manilhas and should win")
                        void Has2Manilhas() {
                            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
                            TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);


                            List<TrucoCard> myCards = List.of(
                                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
                            );
                            List<TrucoCard> openCards = List.of(vira, opponentCard);

                            stepBuilder = GameIntel.StepBuilder.with()
                                    .gameInfo(List.of(), openCards, vira, 1)
                                    .botInfo(myCards, 1)
                                    .opponentScore(1)
                                    .opponentCard(opponentCard);
                            assertEquals(CardToPlay.of(TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)), vapoBot.chooseCard(stepBuilder.build()));
                        }

                        @Test
                        @DisplayName("and has 3 manilhas and should win")
                        void Has3Manilhas() {
                            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
                            TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);


                            List<TrucoCard> myCards = List.of(
                                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
                            );
                            List<TrucoCard> openCards = List.of(vira, opponentCard);

                            stepBuilder = GameIntel.StepBuilder.with()
                                    .gameInfo(List.of(), openCards, vira, 1)
                                    .botInfo(myCards, 1)
                                    .opponentScore(1)
                                    .opponentCard(opponentCard);
                            assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)), vapoBot.chooseCard(stepBuilder.build()));
                        }
                    }
                }
            }
        }

        @Nested
        @DisplayName("in second round")
        class SecondRoundTest {

            @Nested
            @DisplayName("if play first")
            class PlayFirstTest {

                @Test
                @DisplayName("and won the first round")
                void wonFirstRound() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1);
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)), vapoBot.chooseCard(stepBuilder.build()));
                }

                @Test
                @DisplayName("and drew the first round shold choose AD")
                void DrewFirstRound() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1);
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)), vapoBot.chooseCard(stepBuilder.build()));
                }
            }

            @Nested
            @DisplayName("if opponent play first")
            class OpponentPlayFirstTest {
                @Test
                @DisplayName("and lost the first round should choose 2D")
                void lostFirstRound() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = List.of(vira,
                            TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
                    );

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1)
                            .opponentCard(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)), vapoBot.chooseCard(stepBuilder.build()));
                }

                @Test
                @DisplayName("and drew the first round should choose 2D")
                void drewFirstRound() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = List.of(vira,
                            TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
                    );

                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1)
                            .opponentCard(TrucoCard.of(CardRank.THREE, CardSuit.SPADES));
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.TWO, CardSuit.SPADES)), vapoBot.chooseCard(stepBuilder.build()));
                }

            }

        }

        @Nested
        @DisplayName("in last round")
        class LastRoundTest {

            @Test
            @DisplayName("should choose the left card")
            void lastRound() {
                TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);
                TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
                );
                List<TrucoCard> openCards = List.of(vira, opponentCard);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 1)
                        .opponentScore(1)
                        .opponentCard(opponentCard);
                assertEquals(CardToPlay.of(TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)), vapoBot.chooseCard(stepBuilder.build()));

            }
        }

        @Nested
        @DisplayName("Test mão de onze method")
        class MaoDeOnzeResponseTest {
            @Test
            @DisplayName("Should return true when has any manilhas")
            void shouldReturnTrueIfHasManilhas() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 11)
                        .opponentScore(7);
                assertTrue(vapoBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("Should return false when opponent score is higher than 7 and bot cards are low in average")
            void shouldReturnFalseDependingOnCardsAverageAndOpponentScore() {
                TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 11)
                        .opponentScore(8);
                assertFalse(vapoBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("Should return true even if has bad card average and no manilhas," +
                    " but opponent score is lower than 7")
            void shouldReturnTrueIfOpponentHasLowScoreAndHasLowAverage() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 11)
                        .opponentScore(3);
                assertTrue(vapoBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }
        }

        @Nested
        @DisplayName("Test decideIfRaises method")
        class DecideIfRaisesTest {
            @Test
            @DisplayName("Should return true when has any manilhas")
            void shouldReturnTrueIfHasManilhas() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 11)
                        .opponentScore(7);
                assertTrue(vapoBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("Should return false when opponent score is higher than 7 and bot cards are low in average")
            void shouldReturnFalseDependingOnCardsAverageAndOpponentScore() {
                TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 11)
                        .opponentScore(8);
                assertFalse(vapoBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("Should return true even if has bad card average and no manilhas," +
                    " but opponent score is lower than 7")
            void shouldReturnTrueIfOpponentHasLowScoreAndHasLowAverage() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 11)
                        .opponentScore(3);
                assertTrue(vapoBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }
        }

        @Nested
        @DisplayName("Test getRaiseResponse method")
        class GetRaiseResponseTest {
            @Test
            @DisplayName("Should raise when has any manilhas")
            void shouldReturnTrueIfHasManilhas() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 5)
                        .opponentScore(7);
                assertEquals(1, vapoBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("Should raise when has high average")
            void shouldRaiseIfHasHighAverage() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 5)
                        .opponentScore(7);
                assertEquals(1, vapoBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("Should accept when has OK average - above 6")
            void shouldAcceptIfHasOKAverage() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                List<TrucoCard> myCards = List.of(
                        TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
                );
                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(myCards, 5)
                        .opponentScore(7);
                assertEquals(0, vapoBot.getRaiseResponse(stepBuilder.build()));
            }
        }
    }
}