package com.murilos.aline.teconomarrecobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TecoNoMarrecoBotTest {

    TecoNoMarrecoBot tecoNoMarrecoBot;

    private List<TrucoCard> cards;

    private TrucoCard cardVira;

    private List<TrucoCard> hand;
    private List<GameIntel.RoundResult> roundResult;

    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    void setUp(){ tecoNoMarrecoBot = new TecoNoMarrecoBot();}

    @Test
    @DisplayName("Testa se aceita mão de onze com casal maior")
    void testsWhetherHeAcceptsAhandFromElevenWithABiggerCouple(){
        hand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, SPADES));
        cardVira = TrucoCard.of(THREE, SPADES);
        roundResult = List.of();
        cards = List.of();

        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(0);

        Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(acceptMaoDeOnze);
    }

    @Test
    @DisplayName("Testa carta a ser jogada")
    void testChooseCard(){
        hand = List.of(TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, SPADES));
        cardVira = TrucoCard.of(THREE, SPADES);
        roundResult = List.of();
        cards = List.of();

        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
        CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
        assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FOUR,CLUBS));

    }

    @Test
    @DisplayName("Testa se aceita mão de onze com mão completa de três")
    void shouldAcceptHandOfElevenWithThreeCardsThree(){
        hand = List.of(TrucoCard.of(THREE, CLUBS), TrucoCard.of(THREE, HEARTS), TrucoCard.of(THREE, SPADES));
        cardVira = TrucoCard.of(ACE, DIAMONDS);
        roundResult = List.of();
        cards = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(0);
        Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(acceptMaoDeOnze);
    }

    @Test
    @DisplayName("Testa se aceita mao de onze com pontuacao do oponente menor que 4")
    void shouldAcceptHandOfElevenWithOpponentScoreLessThanFour(){
        hand = List.of(TrucoCard.of(TWO, CLUBS), TrucoCard.of(ACE, HEARTS), TrucoCard.of(FOUR, SPADES));
        cardVira = TrucoCard.of(SIX, CLUBS);
        roundResult = List.of();
        cards = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(3);
        Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(acceptMaoDeOnze);
    }

    @Test
    @DisplayName("Testa se aceita mao de onze com casal menor")
    void shouldAcceptHandOfElevenWithTwoManilhas(){
        hand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, DIAMONDS), TrucoCard.of(FIVE, SPADES));
        cardVira = TrucoCard.of(FOUR, CLUBS);
        roundResult = List.of();
        cards = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(5);
        Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(acceptMaoDeOnze);
    }

    @Test
    @DisplayName("Testa se aceita mao de onze com manilha e tres")
    void shouldAcceptHandOfElevenWithManilhaAndTres(){
        hand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, DIAMONDS), TrucoCard.of(THREE, SPADES));
        cardVira = TrucoCard.of(FOUR, CLUBS);
        roundResult = List.of();
        cards = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(5);
        Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(acceptMaoDeOnze);
    }

    @Test
    @DisplayName("Testa se aceita mao de onze com manilha e duas cartas dois")
    void shouldAcceptHandOfElevenWithManilhaAndTwoCardsTwo(){
        hand = List.of(TrucoCard.of(TWO, HEARTS), TrucoCard.of(FIVE, DIAMONDS), TrucoCard.of(TWO, SPADES));
        cardVira = TrucoCard.of(FOUR, CLUBS);
        roundResult = List.of();
        cards = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(5);
        Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(acceptMaoDeOnze);
    }

    @Test
    @DisplayName("Testa se aceita mao de onze com duas cartas tres")
    void shouldAcceptHandOfElevenWithTwoCardsThree(){
        hand = List.of(TrucoCard.of(THREE, HEARTS), TrucoCard.of(SIX, DIAMONDS), TrucoCard.of(THREE, SPADES));
        cardVira = TrucoCard.of(FOUR, CLUBS);
        roundResult = List.of();
        cards = List.of();
        stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(7);
        Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
        assertTrue(acceptMaoDeOnze);
    }



}