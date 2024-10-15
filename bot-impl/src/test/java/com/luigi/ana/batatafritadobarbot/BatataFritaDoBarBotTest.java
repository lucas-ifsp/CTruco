package com.luigi.ana.batatafritadobarbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BatataFritaDoBarBotTest {
    private BatataFritaDoBarBot batataFritaDoBarBot;
    private GameIntel.StepBuilder stepBuilder;
    private GameIntel intel;


    //1
    @Test
    @DisplayName("Make sure the bot is the first to play")
    void makeSureTheBotIsTheFirstToPlay() {
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        assertThat(batataFritaDoBarBot.checkIfIsTheFirstToPlay(intel));
    }

    //2
    @Test
    @DisplayName("Should return true if zap exits")
    void returnsTrueIfZapExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasZap(stepBuilder.build()));
    }

    //3
    @Test
    @DisplayName("Should return false if zap not exits")
    void returnsFalseIfZapNotExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasZap(stepBuilder.build()));
    }

    //4
    @Test
    @DisplayName("Should return true if copas exits")
    void returnsTrueIfCopasExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasCopas(stepBuilder.build()));
    }

    //5
    @Test
    @DisplayName("Should return false if copas not exits")
    void returnsFalseIfCopasNotExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasCopas(stepBuilder.build()));
    }

    //6
    @Test
    @DisplayName("Should return true if espadilha exits")
    void returnsTrueIfEspadilhaExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasEspadilha(stepBuilder.build()));
    }

    //7
    @Test
    @DisplayName("Should return false if espadilha not exits")
    void returnsFalseIfEspadilhaNotExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasEspadilha(stepBuilder.build()));
    }

    //8
    @Test
    @DisplayName("Should return true if ouros exits")
    void returnsTrueIfOurosExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasOuros(stepBuilder.build()));
    }

    //9
    @Test
    @DisplayName("Should return false if ouros not exits")
    void returnsFalseIfOurosNotExists() {
        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.KING, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), myCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertTrue(batataFritaDoBarBot.hasOuros(stepBuilder.build()));
    }

    // 10
    @Test
    @DisplayName("Should return zero when there are no manilha")
    void shouldReturnZeroWhenNoManilha() {

        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);


        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
        );


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), listOfViras, vira, 1)
                .botInfo(playerCards, 0)
                .opponentScore(1);


        assertEquals(0, batataFritaDoBarBot.getNumberOfManilhas(intel));
    }

    //11
    @Test
    @DisplayName("2 manilhas on 2H, 3C, 2S")
    void shouldReturn2manilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertEquals(2, batataFritaDoBarBot.getNumberOfManilhas(stepBuilder.build()));
    }

    //12
    @Test
    @DisplayName("3 manilhas on 2H, 2C, 2S")
    void shouldReturn3manilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertEquals(3, batataFritaDoBarBot.getNumberOfManilhas(stepBuilder.build()));
    }

    // 13
    @Test
    @DisplayName("Should return the lowest card based on vira")
    void shouldReturnLowestCardBasedOnVira() {

        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);


        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        TrucoCard expectedLowestCard = TrucoCard.of(CardRank.ACE, CardSuit.HEARTS);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), listOfViras, vira, 1)
                .botInfo(playerCards, 1)
                .opponentScore(0);


        assertEquals(expectedLowestCard, batataFritaDoBarBot.getLowestCard(intel));
    }

    // 14
    @Test
    @DisplayName("Should return the lowest card based on vira when all manilhas")
    void shouldReturnLowestCardBasedOnViraWhenAllManilhas() {

        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);


        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
        );

        TrucoCard expectedLowestCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), listOfViras, vira, 1)
                .botInfo(playerCards, 1)
                .opponentScore(0);


        assertEquals(expectedLowestCard, batataFritaDoBarBot.getLowestCard(stepBuilder.build()));
    }

    //15
    @Test
    @DisplayName("Should return the lowest card based on vira when no has manilhas")
    void shouldReturnLowestCardBasedOnViraWhenNoHasManilhas() {

        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);


        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
        );

        TrucoCard expectedLowestCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), listOfViras, vira, 1)
                .botInfo(playerCards, 1)
                .opponentScore(0);


        assertEquals(expectedLowestCard, batataFritaDoBarBot.getLowestCard(stepBuilder.build()));
    }



    //16
    @Test
    @DisplayName("Should return the highest card based on vira")
    void shouldReturnHighestCardBasedOnVira() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        TrucoCard expectedHighestCard = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), listOfViras, vira, 1)
                .botInfo(playerCards, 1)
                .opponentScore(0);


        assertEquals(expectedHighestCard, batataFritaDoBarBot.getHighestCard(intel));

    }

    // 17
    @Test
    @DisplayName("Should return the highest card based on vira when all manilhas")
    void shouldReturnHighestCardBasedOnViraWhenAllManilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );

        TrucoCard expectedHighestCard = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), listOfViras, vira, 1)
                .botInfo(playerCards, 1)
                .opponentScore(0);


        assertEquals(expectedHighestCard, batataFritaDoBarBot.getHighestCard(intel));

    }

    // 18
    @Test
    @DisplayName("Should return the highest card based on vira when no has manilhas")
    void shouldReturnHighestCardBasedOnViraWhenNoHasManilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
        );

        TrucoCard expectedHighestCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), listOfViras, vira, 1)
                .botInfo(playerCards, 1)
                .opponentScore(0);


        assertEquals(expectedHighestCard, batataFritaDoBarBot.getHighestCard(intel));

    }

    // 19
    @Test
    @DisplayName("Should return the highest normal card excluding manilhas")
    void shouldReturnHighestNormalCard() {

        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> listOfViras = List.of(vira);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
        );

        TrucoCard expectedHighestCard = TrucoCard.of(CardRank.TWO, CardSuit.SPADES);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), listOfViras, vira, 1)
                .botInfo(playerCards, 1)
                .opponentScore(0);


        assertEquals(expectedHighestCard, batataFritaDoBarBot.getHighestNormalCard(intel));
    }

    //20
    @Test
    @DisplayName("make sure not play casal maior in first round")
    void returnsTrueIfCasalMaiorExist() {
        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(vira), vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        assertEquals(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS), batataFritaDoBarBot.getLowestCard(stepBuilder.build()));
    }

    //21
    @Test
    @DisplayName("make sure blefe is working")
    void makeSureBlefeIsWorking() {
        when(intel.getOpponentScore() == 9 && intel.getScore() == 2);
        int dif = intel.getOpponentScore() - intel.getScore();

        assertEquals(7, dif);

    }

    //22
    @Test
    @DisplayName("make sure throw the lowest card to win")
    void makeSureThrowTheLowestCardToWin() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
        );

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(vira), vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(0);

        when(opponentCard.equals(TrucoCard.of(CardRank.KING, CardSuit.CLUBS)));
        assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS), batataFritaDoBarBot.getLowestToWin(stepBuilder.build()));

    }

    //23
    @Test
    @DisplayName("Sure not to ask for truco if opponent is in hand of eleven")
    void sureNotToAskForTrucoIfOpponentIsInHandOfEleven() {
        when(intel.getOpponentScore()).thenReturn(11);
        assertFalse(batataFritaDoBarBot.decideIfRaises(intel));
    }

    //24
    @Test
    @DisplayName("0 manilhas on 4H, 5C, 7S")
    void shouldReturn0manilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
        );

        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertEquals(0, batataFritaDoBarBot.getNumberOfManilhas(stepBuilder.build()));
    }

    //25
    @Test
    @DisplayName("Should return the lowest manilha to win")
    void shouldReturnTheLowestManilhaToWin() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
        );

        List<TrucoCard> openCards = List.of(vira, opponentCard);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertEquals(Optional.ofNullable(TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)), batataFritaDoBarBot.getLowestToWin(stepBuilder.build()));
    }


    //26
    @Test
    @DisplayName("make sure zap is the highest manilha")
    void makeSureZapIsTheHighestManilha() {
        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), batataFritaDoBarBot.getHighestCard(stepBuilder.build()));
    }


    //27
    @Test
    @DisplayName("Should return WON the last round")
    void shouldReturnWonTheLastRound() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
        );

        List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertTrue(batataFritaDoBarBot.isLastRoundWinner(stepBuilder.build()));

    }

    //28
    @Test
    @DisplayName("Should return LOST the last round")
    void shouldReturnLostTheLastRound() {
        TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
        );

        List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS));

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertFalse(batataFritaDoBarBot.isLastRoundWinner(stepBuilder.build()));
    }

    //29
    @Test
    @DisplayName("Make sure the enemy's score doesn't go above or equal 12 if they ask for truco")
    void makeSureTheEnemySScoreDoesnTGoAboveOrEqual12IfTheyAskForTruco() {
        when(intel.getOpponentScore()).thenReturn(9);
        assertFalse(batataFritaDoBarBot.decideIfRaises(intel));
    }

    //30
    @Test
    @DisplayName("Make sure not ask truco when lost the first and avg less then 9")
    void makeSureNotAskTrucoWhenLostTheFirstAndAvgLessThen9() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertFalse(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));

    }

    //31
    @Test
    @DisplayName("Ask for truco if you have 3 manilhas")
    void askForTrucoIfYouHave3Manilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertTrue(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));
    }

    // 32
    @Test
    @DisplayName("Ask for truco when has 3 three")
    void askForTrucoWhenHas3Tree() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertTrue(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));
    }

    //33

    @Test
    @DisplayName("Ask truco when avg is higher then 7")
    void askTrucoWhenAvgIsHigherThan7() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertTrue(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));
    }

    //34
    @Test
    @DisplayName("Ask truco when avg is equal then 7")
    void askTrucoWhenAvgIsEqualThen7() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertTrue(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));
    }

    //35
    @Test
    @DisplayName("No Ask truco when avg is 5")
    void NoAskTrucoWhenAVGis4() {
        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertFalse(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));
    }

    //36
    @Test
    @DisplayName("Ask truco when the last card is a three")
    void AskTrucoWhenTheLastCardIsAThree() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertTrue(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));

    }

    // 37
    @Test
    @DisplayName("No ask truco when lost first round and card is less then 9")
    void NoAskTrucoWhenLostFirstRoundAndCardIsLessThen9() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

        List<TrucoCard> myCards = List.of(
                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
        );

        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                .botInfo(myCards, 1)
                .opponentScore(1);

        assertFalse(batataFritaDoBarBot.decideIfRaises(stepBuilder.build()));

    }

    // 38
    @Test
    @DisplayName("Should return true if Mao de ferro")
    void returnsTrueIfMaoDeFerro() {

        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 11)
                .opponentScore(11);


        assertTrue(batataFritaDoBarBot.isMaoDeFerro(stepBuilder.build()));
    }

    // 39
    @Test
    @DisplayName("Should return false if not Mao de ferro")
    void returnsFalseIfNotMaoDeFerro() {

        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 10)
                .opponentScore(11);


        assertFalse(batataFritaDoBarBot.isMaoDeFerro(stepBuilder.build()));
    }

    // 40
    @Test
    @DisplayName("Should return true if Mao de Onze")
    void returnsTrueIfMaoDeOnze() {

        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 11)
                .opponentScore(9);


        assertTrue(batataFritaDoBarBot.isMaoDeOnze(stepBuilder.build()));
    }

    // 41
    @Test
    @DisplayName("Should return false if not Mao de Onze")
    void returnsFalseIfNotMaoDeOnze() {

        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(9);


        assertFalse(batataFritaDoBarBot.isMaoDeOnze(stepBuilder.build()));
    }

    //42
    @Test
    @DisplayName("Should return true to ask for mao de onze when having Zap and manilha")
    void shouldReturnTrueAskMaoDeOnzeWhenHasZapAndManilha() {

        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(11);



        assertTrue(batataFritaDoBarBot.getMaoDeOnzeResponse(intel));
    }

    // 43
    @Test
    @DisplayName("Should return true to ask for mao de onze when having manilha and avg cards greater than seven")
    void shouldReturnTrueAskMaoDeOnzeWhenHasManilhaAndAVGGreaterThan7() {

        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES)
        );


        List<TrucoCard> openCards = List.of();

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(11);


        assertTrue(batataFritaDoBarBot.getMaoDeOnzeResponse(intel));
    }

    //44
    @Test
    @DisplayName("Should return true when opponent's score is greater than 7, has manilha, and avg cards greater than seven")
    void shouldReturnTrueWhenOpponentScoreGreaterThan7AndHasManilhaAndAVGGreaterThan7() {


        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);


        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(8);


        assertTrue(batataFritaDoBarBot.getMaoDeOnzeResponse(stepBuilder.build()));
    }

    // 45
    @Test
    @DisplayName("Should return false when has no manilha and avg cards less than seven")
    void shouldReturnFalseWhenNoManilhaAndAVGNotGreaterThan7() {


        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);


        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        List<TrucoCard> openCards = List.of(vira);


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(8);


        assertFalse(batataFritaDoBarBot.getMaoDeOnzeResponse(stepBuilder.build()));
    }

    //46
    @Test
    @DisplayName("Should return true to mao de onze response when mao de ferro")
    void shouldReturnTrueToMaoDeOnzeResponseWhenMaoDeFerro() {


        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);


        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
        );

        List<TrucoCard> openCards = List.of(vira);


        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 11)
                .opponentScore(11);


        assertFalse(batataFritaDoBarBot.getMaoDeOnzeResponse(stepBuilder.build()));
    }


    // 47
    @Test
    @DisplayName("Should return an average card value is 4")
    void shouldReturnAverageCardsIs4() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );

        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(8);



        assertEquals(4,batataFritaDoBarBot.getAverageCardValue(stepBuilder.build()) );
    }

    // 48
    @Test
    @DisplayName("Should return an average card value is 8")
    void shouldReturnAverageCardsIs8() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
        );


        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(8);

        assertEquals(8,batataFritaDoBarBot.getAverageCardValue(stepBuilder.build()) );
    }

    // 49
    @Test
    @DisplayName("Should return average when player only manilhas")
    void shouldReturnAverageWhenPlayerOnlyManilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
        );


        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(8);

        assertEquals(11, batataFritaDoBarBot.getAverageCardValue(stepBuilder.build()) );
    }

    // 50
    @Test
    @DisplayName("Should return average card value is 1")
    void shouldReturnAverageCardsIs1() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);

        List<TrucoCard> playerCards = List.of(
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
        );


        List<TrucoCard> openCards = List.of(vira);

        stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 0)
                .botInfo(playerCards, 9)
                .opponentScore(8);

        assertEquals(1, batataFritaDoBarBot.getAverageCardValue(stepBuilder.build()) );
    }



}