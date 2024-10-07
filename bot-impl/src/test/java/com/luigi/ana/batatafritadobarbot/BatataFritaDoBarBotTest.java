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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    //10
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

    //11
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





}