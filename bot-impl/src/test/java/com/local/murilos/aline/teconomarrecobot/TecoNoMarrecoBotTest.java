package com.local.murilos.aline.teconomarrecobot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.List;

import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.CardRank.*;
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
        @Test
        @DisplayName("Testa se rejeita mao de onze com mão fraca")
        void shouldRejectHandOfElevenWithWeakHand(){
            hand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(SIX, CLUBS));
            cardVira = TrucoCard.of(TWO, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(2);
            Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(acceptMaoDeOnze);
        }

        @Test
        @DisplayName("Testa se rejeita mao de onze com apenas um tres")
        void shouldRejectHandOfElevenWithOneThree(){
            hand = List.of(TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(THREE, CLUBS));
            cardVira = TrucoCard.of(KING, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(2);
            Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(acceptMaoDeOnze);
        }

        @Test
        @DisplayName("Testa se aceita mao de onze contra mao de onze")
        void shouldAcceptHandOfElevenVersusHandOfEleven(){
            hand = List.of(TrucoCard.of(FIVE, HEARTS), TrucoCard.of(FIVE, CLUBS), TrucoCard.of(THREE, CLUBS));
            cardVira = TrucoCard.of(JACK, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(11);
            Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertTrue(acceptMaoDeOnze);
        }

        @Test
        @DisplayName("Testa se rejeita mao de onze com uma manilha e duas cartas fracas")
        void shouldRejectHandOfElevenWithOneManilhaAndTwoCardsWeak(){
            hand = List.of(TrucoCard.of(ACE, CLUBS), TrucoCard.of(FOUR, CLUBS), TrucoCard.of(FOUR, HEARTS));
            cardVira = TrucoCard.of(KING, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(2);
            Boolean acceptMaoDeOnze = tecoNoMarrecoBot.getMaoDeOnzeResponse(stepBuilder.build());
            assertFalse(acceptMaoDeOnze);
        }

        @Test
        @DisplayName("Testa se rejeita mao de onze com tres cartas dois")
        void shouldRejectHandOfElevenWithThreeCardsTwo(){
            hand = List.of(TrucoCard.of(TWO, CLUBS), TrucoCard.of(TWO, HEARTS), TrucoCard.of(TWO, SPADES));
            cardVira = TrucoCard.of(KING, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(5);
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
        void shouldNotRequestTrucoIfYDrewTheFirstRoundAndNotHaveACardWithValueGreaterThan11(){
            hand = List.of(TrucoCard.of(KING, HEARTS), TrucoCard.of(ACE, CLUBS));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.DREW);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 4).opponentScore(5);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

        @Test
        @DisplayName("Testa se na ultima rodada possuir mão com valor menor que 10 nao pede truco")
        void shouldNotRequestTrucoIfInTheLastRoundHaveAHandWithAValueLowerThan10(){
            hand = List.of(TrucoCard.of(FOUR, SPADES));
            cardVira = TrucoCard.of(FOUR, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 2).opponentScore(3);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

        @Test
        @DisplayName("Testa se na segunda rodada possuir mão com valor menor que 15  nao pede truco")
        void shouldNotRequestTrucoIfInTheSecondRoundHaveAHandWithAValueLowerThan15(){
            hand = List.of(TrucoCard.of(SIX, DIAMONDS), TrucoCard.of(FIVE, DIAMONDS));
            cardVira = TrucoCard.of(TWO, SPADES);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(1);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

        @Test
        @DisplayName("Testa se nao pede truco com mao fraca")
        void shouldNotRequestTrucoIfWeakHand(){
            hand = List.of(TrucoCard.of(FOUR, DIAMONDS), TrucoCard.of(FIVE, SPADES), TrucoCard.of(FIVE, HEARTS));
            cardVira = TrucoCard.of(KING, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 2).opponentScore(8);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

        @Test
        @DisplayName("Testa se nao pede truco com mao de onze")
        void shouldNotRequestTrucoIfMaoDeOnze(){
            hand = List.of(TrucoCard.of(THREE, DIAMONDS), TrucoCard.of(THREE, SPADES), TrucoCard.of(THREE, HEARTS));
            cardVira = TrucoCard.of(KING, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 11).opponentScore(10);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

        @Test
        @DisplayName("Testa se nao pede truco com apenas uma manilha na mao")
        void shouldNotRequestTrucoIfOneManilha(){
            hand = List.of(TrucoCard.of(ACE, DIAMONDS), TrucoCard.of(FOUR, HEARTS), TrucoCard.of(FOUR, SPADES));
            cardVira = TrucoCard.of(KING, HEARTS);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 2).opponentScore(2);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

        @Test
        @DisplayName("Testa se nao pede truco com carta media na ultima rodada")
        void shouldNotRequestTrucoIfMiddleCardInThirdRound(){
            hand = List.of(TrucoCard.of(ACE, SPADES));
            cardVira = TrucoCard.of(SEVEN, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 10).opponentScore(10);
            Boolean requestTruco = tecoNoMarrecoBot.decideIfRaises(stepBuilder.build());
            assertFalse(requestTruco);
        }

        @Test
        @DisplayName("Testa se nao pede truco com mao media na segunda rodada")
        void shouldNotRequestTrucoIfMiddleHandInSecondRound(){
            hand = List.of(TrucoCard.of(QUEEN, HEARTS), TrucoCard.of(ACE, CLUBS));
            cardVira = TrucoCard.of(QUEEN, CLUBS);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 9).opponentScore(6);
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
        @DisplayName("Testa jogar carta menor na primeira rodada se tiver casal maior")
        void playingSmallerCardIfYouHaveBiggerCouple() {
            hand = List.of( TrucoCard.of(THREE, HEARTS),TrucoCard.of(KING, CLUBS), TrucoCard.of(KING, HEARTS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE,HEARTS));
        }
        @Test
        @DisplayName("Testa jogar carta mais forte se tiver casal maior")
        void playStrongerCardsIfHaveBiggerCouple() {
            hand = List.of(TrucoCard.of(KING, CLUBS), TrucoCard.of(KING, HEARTS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(KING,CLUBS));
        }
        @Test
        @DisplayName("Testa jogar a manilha se oponente jogou manilha")
        void testPlayManilhaIfOpponentPlayedManilha() {
            hand = List.of(TrucoCard.of(KING, SPADES), TrucoCard.of(KING, HEARTS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            TrucoCard opponentCard = TrucoCard.of(KING, DIAMONDS);
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0).opponentCard(opponentCard);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertEquals(TrucoCard.of(KING, SPADES), cardToPlay.value());
        }
        @Test
        @DisplayName("Testa jogar menor manilha quando não tem descarte")
        void playsSmallerShackleWhenThereNotDisposal() {
            hand = List.of(TrucoCard.of(KING, DIAMONDS),TrucoCard.of(KING, SPADES), TrucoCard.of(KING, HEARTS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            TrucoCard opponentCard = TrucoCard.of(KING, CLUBS);
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0).opponentCard(opponentCard);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertEquals(TrucoCard.of(KING, DIAMONDS), cardToPlay.value());
        }
        @Test
        @DisplayName("Testa se tiver amarrado jogar maior carta quando não tiver manilha")
        void ifHaveItTiedPlayBiggerCard() {
            hand = List.of( TrucoCard.of(TWO, SPADES), TrucoCard.of(THREE, HEARTS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of(GameIntel.RoundResult.DREW);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isEqualTo(TrucoCard.of(THREE,HEARTS));
        }
        @Test
        @DisplayName("Testa amarrar se tiver não tiver manilha")
        void tieIfDontHaveManilha() {
            hand = List.of(TrucoCard.of(THREE,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(FOUR, CLUBS));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of();
            cards = List.of();
            TrucoCard opponentCard = TrucoCard.of(THREE, DIAMONDS);
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 1).botInfo(hand, 3).opponentScore(0).opponentCard(opponentCard);
            CardToPlay cardToPlay = tecoNoMarrecoBot.chooseCard(stepBuilder.build());
            assertThat(cardToPlay.value()).isNotEqualTo(TrucoCard.of(THREE,HEARTS));
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
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isZero();
        }
        @Test
        @DisplayName("Testa pede aumento se tiver casal maior")
        void askForRaiseIfHaveBiggerCouplee() {
            hand = List.of(TrucoCard.of(KING,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 1).opponentScore(0);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isOne();
        }
        @Test
        @DisplayName("Testa pede aumento se ganhou a primeira e ainda tem zap")
        void askForRaiseIfYouWonTheFirstRoundAndStillHaveZap() {
            hand = List.of(TrucoCard.of(ACE,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of(GameIntel.RoundResult.WON);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 1).opponentScore(0);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isOne();
        }
        @Test
        @DisplayName("Testa pede aumento se teve empate  tem zap")
        void askForRaiseIfThereWasDrawAndHaveZap() {
            hand = List.of(TrucoCard.of(ACE,HEARTS), TrucoCard.of(KING, CLUBS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of(GameIntel.RoundResult.DREW);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 1).opponentScore(0);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isOne();
        }
        @Test
        @DisplayName("Testa pede aumento se tem manilha forte")
        void askForRaiseIfHaveManilhaStrong() {
            hand = List.of(TrucoCard.of(ACE,HEARTS), TrucoCard.of(KING, SPADES), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of(GameIntel.RoundResult.DREW);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 1).opponentScore(0);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isZero();
        }
        @Test
        @DisplayName("Testa aceita se tem uma ou mais manilha e tiver um três")
        void acceptIfHaveManilhaEndStrongCard() {
            hand = List.of(TrucoCard.of(THREE,HEARTS), TrucoCard.of(KING, SPADES), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(JACK, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 1).opponentScore(0);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isZero();
        }
        @Test
        @DisplayName("Testa recusar se não tiver carta boas")
        void refuseIfYouDontHaveGoodCards() {
            hand = List.of(TrucoCard.of(FOUR,HEARTS), TrucoCard.of(KING, SPADES), TrucoCard.of(THREE, CLUBS));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of();
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 9).opponentScore(0);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isNegative();
        }
        @Test
        @DisplayName("Testa recusar se perdeu a primeira e não tem carta forte")
        void refuseIfYouLostTheFirstRoundAndHaveWeakCards() {
            hand = List.of(TrucoCard.of(FOUR,HEARTS), TrucoCard.of(ACE, SPADES));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 3).opponentScore(1);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isNegative();
        }
        @Test
        @DisplayName("Testa aceita se ganhou a primeira e tem um tres")
        void acceptIfWonTheFirstRoundOneAndHaveIsOneThree() {
            hand = List.of(TrucoCard.of(THREE,HEARTS), TrucoCard.of(KING, SPADES));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of(GameIntel.RoundResult.WON);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 3).opponentScore(1);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertThat(result).isZero();
        }
        @Test
        @DisplayName("Testa se corre com uma mailha")
        void refuseIfItHasOneManilha() {
            hand = List.of(TrucoCard.of(THREE,HEARTS), TrucoCard.of(TWO, SPADES));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of(GameIntel.RoundResult.WON);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 3).opponentScore(1);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertNotEquals(result,-1);
        }
        @Test
        @DisplayName("Testa se corre quando perdeu a primeira e tem duas manilhas")
        void refusesWhenHeLostTheFirstOneAndHasTwoManilhas() {
            hand = List.of(TrucoCard.of(TWO,HEARTS), TrucoCard.of(TWO, SPADES));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 3).opponentScore(1);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertNotEquals(result,-1);
        }
        @Test
        @DisplayName("Testa se corre com casal maior")
        void refusalWithBiggerCouple() {
            hand = List.of(TrucoCard.of(TWO,HEARTS), TrucoCard.of(TWO, CLUBS));
            cardVira = TrucoCard.of(ACE, SPADES);
            roundResult = List.of(GameIntel.RoundResult.LOST);
            cards = List.of();
            stepBuilder = GameIntel.StepBuilder.with().gameInfo(roundResult, cards, cardVira, 3).botInfo(hand, 3).opponentScore(1);
            int result = tecoNoMarrecoBot.getRaiseResponse(stepBuilder.build());
            assertNotEquals(result,-1);
        }

    }
}