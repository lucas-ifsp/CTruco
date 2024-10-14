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

    //14
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

    // 15
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

    //16
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

    //17
    @Test
    @DisplayName("make sure blefe is working")
    void makeSureBlefeIsWorking() {
        when(intel.getOpponentScore() == 9 && intel.getScore() == 2);
        int dif = intel.getOpponentScore() - intel.getScore();

        assertEquals(7, dif);

    }

    //18
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

    //19
    @Test
    @DisplayName("Sure not to ask for truco if opponent is in hand of eleven")
    void sureNotToAskForTrucoIfOpponentIsInHandOfEleven() {
        when(intel.getOpponentScore()).thenReturn(11);
        assertFalse(batataFritaDoBarBot.decideIfRaises(intel));
    }

    //20
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

    //21
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


    //22
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


    //23
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

    //24
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

    //25
    @Test
    @DisplayName("Make sure the enemy's score doesn't go above or equal 12 if they ask for truco")
    void makeSureTheEnemySScoreDoesnTGoAboveOrEqual12IfTheyAskForTruco() {
        when(intel.getOpponentScore()).thenReturn(9);
        assertFalse(batataFritaDoBarBot.decideIfRaises(intel));
    }

    //26
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

    //27
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

    //28
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

    //29
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

    // 30
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

    // 31
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

    // 32
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



}