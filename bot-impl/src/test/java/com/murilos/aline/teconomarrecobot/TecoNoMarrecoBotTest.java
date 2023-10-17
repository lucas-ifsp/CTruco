package com.murilos.aline.teconomarrecobot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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


    @Nested
    @DisplayName("getMaoDeOnzeResponse")
    class GetMaoDeOnzeResponse{
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

        @Test
        @DisplayName("Testa se rejeita mao de onze com tres cartas As")
        void shouldRejectHandOfElevenWithThreeCardsAce(){
            hand = List.of(TrucoCard.of(ACE, HEARTS), TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(ACE, CLUBS));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(7);
            Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(acceptMaoDeOnze);
        }
    }

    @Nested
    @DisplayName("decideIfRaises")
    class DecideIfRaises{
        @Test
        @DisplayName("Testa se na segunda rodada possuir mão com valor maior que 15 pede truco")
        void shouldRequestTrucoIfInTheSecondRoundHaveAHandWithAValueGreaterThan15(){
            hand = List.of(TrucoCard.of(THREE, HEARTS), TrucoCard.of(SIX, DIAMONDS), TrucoCard.of(THREE, SPADES));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 6).opponentScore(4);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertTrue(requestTruco);
        }

        @Test
        @DisplayName("Testa se na ultima rodada possuir mão com valor maior que 10 pede truco")
        void shouldRequestTrucoIfInTheLastRoundHaveAHandWithAValueGreaterThan10(){
            hand = List.of(TrucoCard.of(THREE, HEARTS));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 6).opponentScore(4);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertTrue(requestTruco);
        }

        @Test
        @DisplayName("Testa se ganhou a primeira rodada e possui carta com valor maior que 10")
        void shouldRequestTrucoIfYouWonTheFirstRoundAndHaveACardWithValueGreaterThan10(){
            hand = List.of(TrucoCard.of(FIVE, HEARTS), TrucoCard.of(FOUR, HEARTS));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.WON);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 2).opponentScore(3);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertTrue(requestTruco);
        }

        @Test
        @DisplayName("Testa se empatou a primeira rodada e possui carta com valor maior que 11")
        void shouldRequestTrucoIfYDrewTheFirstRoundAndHaveACardWithValueGreaterThan11(){
            hand = List.of(TrucoCard.of(FIVE, HEARTS), TrucoCard.of(FOUR, HEARTS));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.DREW);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 2).opponentScore(3);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertTrue(requestTruco);
        }
        @Test
        @DisplayName("Testa se empatou a primeira rodada e nao possui carta com valor maior que 11")
        void shouldRequestTrucoIfYDrewTheFirstRoundAndNotHaveACardWithValueGreaterThan11(){
            hand = List.of(TrucoCard.of(KING, HEARTS), TrucoCard.of(ACE, CLUBS));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.DREW);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 4).opponentScore(5);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

    }

    @Nested
    @DisplayName("chooseCard")
    class ChooseCardTests{
        @Test
        @DisplayName("Testa jogar a carta que mata a do oponente quando não é manilha")
        void playTheKillCard() {
            hand = List.of(TrucoCard.of(TWO,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(SEVEN, SPADES);
            roundResult = List.of();
            cards = List.of();
            TrucoCard opponentCard = TrucoCard.of(JACK, DIAMONDS);
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0).opponentCard(opponentCard);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertEquals(TrucoCard.of(KING, CLUBS), cardToPlay.value());
        }
        @Test
        @DisplayName("Testa jogar a carta mais fraca quando não tem manilha")
        void playTheWeakestCard(){
            hand = List.of(TrucoCard.of(KING, DIAMONDS), TrucoCard.of(FOUR, CLUBS), TrucoCard.of(ACE, CLUBS));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of();
            cards = List.of();
            TrucoCard opponentCard = TrucoCard.of(TWO, DIAMONDS);
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0).opponentCard(opponentCard);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(FOUR,CLUBS));

        }
        @Test
        @DisplayName("Testa jogar matar manilha do oponente")
        void playKillManillha(){
            hand = List.of(TrucoCard.of(KING, DIAMONDS), TrucoCard.of(TWO, HEARTS), TrucoCard.of(TWO,SPADES));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of();
            cards = List.of();
            TrucoCard opponentCard = TrucoCard.of(TWO, DIAMONDS);
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0).opponentCard(opponentCard);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(TWO, SPADES));

        }
        @Test
        @DisplayName("Testa jogar a carta mais forte quando não tem manilha")
        void tryPlayingTheStrongestCardWhenYouDontHavesManilha(){
            hand = List.of(TrucoCard.of(KING, DIAMONDS), TrucoCard.of(THREE, CLUBS), TrucoCard.of(KING, CLUBS));
            cardVira = TrucoCard.of(KING, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE,CLUBS));

        }
        @Test
        @DisplayName("Testa jogar descartar manilha ouro")
        void playTheGoldIfYouHaveTheHand(){
            hand = List.of(TrucoCard.of(KING, DIAMONDS), TrucoCard.of(KING, CLUBS), TrucoCard.of(KING, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(KING,DIAMONDS));

        }
        @Test
        @DisplayName("Testa amarrar se tiver zap seco")
        void tryToTieItIfYouHaveADryZap() {
            hand = List.of(TrucoCard.of(THREE,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            TrucoCard opponentCard = TrucoCard.of(THREE, DIAMONDS);
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0).opponentCard(opponentCard);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertEquals(opponentCard.getRank(), cardToPlay.content().getRank());
        }
        @Test
        @DisplayName("Testa se tiver amarrado jogar maior manilha")
        void ifYouTiePlayBiggerManilha() {
            hand = List.of( TrucoCard.of(KING, SPADES), TrucoCard.of(KING, HEARTS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of(GameIntel.RoundResult.DREW);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(KING,HEARTS));
        }
        @Test
        @DisplayName("Testa jogar carta menor se tiver casal maior")
        void playingSmallerCardIfYouHaveBiggerCouple() {
            hand = List.of( TrucoCard.of(THREE, HEARTS),TrucoCard.of(KING, CLUBS), TrucoCard.of(KING, HEARTS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE,HEARTS));
        }
    }

    @Nested
    @DisplayName("getRaiseResponse")
    class GetRaiseResponse{
        @Test
        @DisplayName("Testa aceita truco se tiver uma manilha ou mais")
        void acceptRaiseIfYouHaveManilhaOrMore() {
            hand = List.of(TrucoCard.of(QUEEN,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(SEVEN, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            assertThat(tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build())).isZero();
        }

        @Test
        @DisplayName("Testa pede aumento se tiver casal maior")
        void askForRaiseIfHaveBiggerCouplee() {
            hand = List.of(TrucoCard.of(KING,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 1).opponentScore(0);

            assertThat(tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build())).isOne();
        }

    }
}