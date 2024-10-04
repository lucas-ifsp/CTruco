package com.bianca.joaopedro.lgtbot;
import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LgtbotTest {
    private Lgtbot lgtbot;
    private GameIntel.StepBuilder stepBuilder;
    private GameIntel intel;

    @BeforeEach
    public void config() {
        lgtbot = new Lgtbot();

    }

    @Nested
    @DisplayName("Test getMaoDeOnzeResponse method")
    class GetMaoDeOnzeResponseTest {
        @Test
        @DisplayName("Aceitar mão de onze quando oponente tem menos de 7 pontos e jogador possui boas cartas")
        public void testShouldAcceptMaoDeOnze_WhenOpponentScoreLessThan7_HasThreeGoodCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> goodCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(goodCards, 11)
                    .opponentScore(6);
            assertTrue(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Aceitar mão de onze quando oponente tem menos de 11 pontos e jogador possui cartas fortes")
        public void testShouldAcceptMaoDeOnze_WhenOpponentScoreLessThan11_HasThreeStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> strongCards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(strongCards, 11)
                    .opponentScore(10);
            assertTrue(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Aceitar mão de onze quando oponente tem exatamente 11 pontos")
        public void testShouldAcceptMaoDeOnze_WhenOpponentScoreEqualTo11() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 11)
                    .opponentScore(11);
            assertTrue(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }


        @Test
        @DisplayName("Não aceitar mão de onze quando não tem boas cartas e oponente tem menos de 7 pontos")
        public void testShouldNotAcceptMaoDeOnze_WhenNoGoodCardsAndOpponentScoreLessThan7() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> badCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(badCards, 11)
                    .opponentScore(6);
            assertFalse(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Test decideIfRaises method")
    class DecideIfRaisesTest {

        @Test
        @DisplayName("Deve pedir truco quando tem boas cartas e oponente tem menos de 6 pontos")
        public void testShouldRaise_WhenHasGoodCardsAndOpponentScoreLessThan6() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> goodCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(goodCards, 9)
                    .opponentScore(5);

            assertTrue(lgtbot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Deve pedir truco quando o oponente tem 11 pontos")
        public void testShouldRaise_WhenOpponentHas11Points() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(myCards, 9)
                    .opponentScore(11);

            assertTrue(lgtbot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Não deve pedir truco quando tem cartas fracas e oponente tem menos de 6 pontos")
        public void testShouldNotRaise_WhenHasWeakCardsAndOpponentScoreLessThan6() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> weakCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(weakCards, 9)
                    .opponentScore(5);

            assertFalse(lgtbot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Deve pedir truco quando tem mais 2 ou mais cartas fortes")
        public void testShouldRaise_WhenHasStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> weakCards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(weakCards, 9)
                    .opponentScore(5);

            assertTrue(lgtbot.decideIfRaises(stepBuilder.build()));
        }


        @Test
        @DisplayName("Não deve pedir truco quando tem cartas fracas e oponente tem menos de 6 pontos")
        public void testShouldNotRaise_WhenHasWeakCardsAndOpponentScoreLessThan() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES); // Carta vira
            List<TrucoCard> weakCards = List.of( // Cartas fracas do jogador
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1) // Configura as informações do jogo
                    .botInfo(weakCards, 9) // Configura as informações do bot
                    .opponentScore(5); // Configura a pontuação do oponente

            assertFalse(lgtbot.decideIfRaises(stepBuilder.build())); // Verifica se não deve pedir truco
        }


        @Test
        @DisplayName("Deve pedir truco na rodada 2 após ganhar a primeira rodada, tendo boas cartas")
        public void testShouldRaiseInRound2_AfterWinningFirstRoundWithGoodCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(vira), vira, 1)
                    .botInfo(myCards, 9)
                    .opponentScore(5);

            assertTrue(lgtbot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Se perdeu na primeira, deve pedir truco na rodada 2 se tiver 2 ou mais cartas fortes")
        public void testShouldRaiseInRound2_ifILostFistRounAndHaveTwoOrMoreStrongCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> strongCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(strongCards, 9)
                    .opponentScore(5);

            assertTrue(lgtbot.decideIfRaises(stepBuilder.build()));
        }



        @Test
        @DisplayName("Se perdeu na primeira, não deve pedir truco na rodada 2 se tiver apenas 1 carta forte")
        public void testShouldRaiseInRound2_ifILostInTheFirstRoundIDontAskForTrucoInRoundTwoIfIHaveOneStrongCard() {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
            List<TrucoCard> strongCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(strongCards, 3)
                    .opponentScore(3);

            assertFalse(lgtbot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Não deve pedir truco quando tem apenas uma carta e oponente tem 10 pontos")
        public void testShouldNotRaise_WhenHasOneCardAndOpponentScore10() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> oneCard = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
            );
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(vira), vira, 1)
                    .botInfo(oneCard, 9)
                    .opponentScore(10);

            assertFalse(lgtbot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Deve pedir truco quando tem 3 ou mais cartas boas após perder a primeira rodada")
        public void testShouldRaise_AfterLosingFirstRoundWithThreeGoodCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> goodCards = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(vira), vira, 1)
                    .botInfo(goodCards, 9)
                    .opponentScore(5);

            assertTrue(lgtbot.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("Bot recusa truco quando tem cartas fracas")
        public void testShouldNotRaisePoints_WhenBotHasWeakCards() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);
            List<TrucoCard> weakCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(weakCards, 10)
                    .opponentScore(9);
            assertFalse(lgtbot.decideIfRaises(stepBuilder.build()));
        }


        @Nested
        @DisplayName("Test chooseCard method")
        class ChooseCardTest {

            @Test
            @DisplayName("Bot escolhe a melhor carta para jogar")
            public void testBotChoosesBestCardToPlay() {
                TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.KING, CardSuit.CLUBS)
                );

                List<TrucoCard> openCards = List.of(vira);
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), openCards, vira, 1)
                        .botInfo(botCards, 11)
                        .opponentScore(6);

                TrucoCard expectedCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                CardToPlay cardToPlay = lgtbot.chooseCard(stepBuilder.build());

            }

            @Nested
            @DisplayName("First Round")
            class FirstRoundTest {

                @Nested
                @DisplayName("If play first")
                class PlayFirstTest {

                    @Test
                    @DisplayName("Na primeira rodada, se tiver duas cartas fortes, retorna a carta mais fraca")
                    void testChooseCard_getWeakerCardIfTwoStrongCardsFirstRound() {
                        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);


                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
                        );
                        List<TrucoCard> openCards = List.of(vira);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1);
                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));

                    }
                }
            }
        }



    }
}
