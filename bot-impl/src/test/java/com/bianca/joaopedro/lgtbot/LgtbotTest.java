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
        @DisplayName("Rejeitar mão de onze quando jogador tem menos de três cartas boas")
        public void testShouldRejectMaoDeOnze_WhenHasLessThanThreeGoodCards() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> fewGoodCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)  // Terceira carta não tão boa
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(fewGoodCards, 11)
                    .opponentScore(8);
            assertFalse(lgtbot.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Aceitar mão de onze com cartas medianas quando oponente tem menos de 5 pontos")
        public void testShouldAcceptMaoDeOnze_WithAverageCards_WhenOpponentScoreLessThan5() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
            List<TrucoCard> averageCards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            List<TrucoCard> openCards = List.of(vira);
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(averageCards, 11)
                    .opponentScore(4);
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
            // Define a "vira" (carta virada na mesa)
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS);

            // Define as cartas fracas do bot
            List<TrucoCard> weakCards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),  // Dama de Ouros
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),    // Três de Copas
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)      // Quatro de Espadas
            );

            // Cartas abertas no jogo
            List<TrucoCard> openCards = List.of(vira);

            // Configura o estado do jogo
            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)  // Informações do jogo
                    .botInfo(weakCards, 10)  // Cartas e pontuação do bot
                    .opponentScore(9);       // Pontuação do oponente

            // Verifica se o bot NÃO decide aumentar os pontos (esperado: false)
            assertFalse(lgtbot.decideIfRaises(stepBuilder.build()), "O bot deveria recusar aumentar os pontos com cartas fracas.");
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

                // Verifica se a carta escolhida pelo bot é a esperada
                CardToPlay cardToPlay = lgtbot.chooseCard(stepBuilder.build());

            }




            @Nested
            @DisplayName("First Round")
            class FirstRoundTest {

                @Nested
                @DisplayName("If play first - Bait Mode")
                class PlayFirstTest {

                    @Test
                    @DisplayName("Na primeira rodada, se tiver três cartas boas, retorna a carta mais forte")
                    void testChooseCard_getTheBestCardIfThreeStrongCardsFirstRound() {
                        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),   // boa
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),   // boa
                                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)   // boa
                        );

                        List<TrucoCard> openCards = List.of(vira);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("Na primeira rodada, se tiver duas cartas boas, retorna a carta mais fraca")
                    void testChooseCard_getWeakerCardIfTwoGoodCardsFirstRound() {
                        TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),   // boa
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),   // boa
                                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)   // Fraca
                        );

                        List<TrucoCard> openCards = List.of(vira);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("Na primeira rodada, se tiver três cartas ruins, retorna a carta mais fraca")
                    void testChooseCard_getTheBestCardIfThreeBadCardsFirstRound() {
                        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS),   // ruim
                                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),   // ruim
                                TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)   // ruim
                        );

                        List<TrucoCard> openCards = List.of(vira);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));
                    }
                }

                @Nested
                @DisplayName("If opponent plays first")
                class OpponentPlayFirstTest {

                    @Test
                    @DisplayName("Should play the best card when opponent plays first")
                    void testOpponentPlaysFirstWithBestCard() {
                        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);
                        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                        );
                        List<TrucoCard> openCards = List.of(vira, opponentCard);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1)
                                .opponentCard(opponentCard);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("Should play the best card when opponent plays first considering manilha")
                    void testOpponentPlaysFirstWithBestCardConsideringManilha() {
                        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);
                        TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
                        );
                        List<TrucoCard> openCards = List.of(vira, opponentCard);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1)
                                .opponentCard(opponentCard);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)), lgtbot.chooseCard(stepBuilder.build()));
                    }


                    @Test
                    @DisplayName("Deve jogar a carta mais fraca se não tiver para ganhar do oponente")
                    void testOpponentPlaysFirstWithWeakCard() {
                        TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);
                        TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.HEARTS);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                                TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
                        );
                        List<TrucoCard> openCards = List.of(vira, opponentCard);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1)
                                .opponentCard(opponentCard);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)), lgtbot.chooseCard(stepBuilder.build()));

                    }
                }
            }

            @Nested
            @DisplayName("Second Round")
            class SecondRoundTest {

                @Nested
                @DisplayName("If play first")
                class PlayFirstSecondRoundTest {

                    @Test
                    @DisplayName("Na segunda rodada, se tiver ganhado a primeira, joga a carta mais fraca")
                    void testChooseCardSecondRound_getTheWeakCardIfWinFirst() {
                        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.TWO, CardSuit.HEARTS)
                        );

                        List<TrucoCard> openCards = List.of(vira);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("Na segunda rodada, se tiver perdido a primeira, joga a carta mais forte")
                    void testChooseCardSecondRound_getTheBestCardIfLooseFirst() {
                        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                        );

                        List<TrucoCard> openCards = List.of(vira);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));
                    }

                    @Test
                    @DisplayName("Na segunda rodada, se tiver empatado a primeira, joga a carta mais forte")
                    void testChooseCardSecondRound_getTheBestCardIfDrewFirst() {
                        TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.SPADES);

                        List<TrucoCard> myCards = List.of(
                                TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                                TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
                        );

                        List<TrucoCard> openCards = List.of(vira);

                        stepBuilder = GameIntel.StepBuilder.with()
                                .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                                .botInfo(myCards, 1)
                                .opponentScore(1);

                        assertEquals(CardToPlay.of(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)), lgtbot.chooseCard(stepBuilder.build()));
                    }
                }
            }

            @Nested
            @DisplayName("Last round")
            class LastRoundTest {

                @Test
                @DisplayName("Deve escolher a última carta")
                void lastRound() {
                    TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
                    TrucoCard opponentCard = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
                    );

                    List<TrucoCard> openCards = List.of(vira, opponentCard);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 1)
                            .opponentScore(1)
                            .opponentCard(opponentCard);
                    assertEquals(CardToPlay.of(TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)), lgtbot.chooseCard(stepBuilder.build()));
                }
            }
        }

        @Nested
        @DisplayName("Test getRaiseResponse method")
        class GetRaiseResponseTest {

            @Nested
            @DisplayName("First Round")
            class FirstRoundGetResponseTest {

                @Test
                @DisplayName("Deve dobrar se tiver pelo menos 2 manilhas e 1 carta boa")
                void shouldReturnOneIfHasTwoManilhasAndGoodCard() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
                    );
                    List<TrucoCard> openCards = List.of(vira);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 5)
                            .opponentScore(7);
                    assertEquals(1, lgtbot.getRaiseResponse(stepBuilder.build()));
                }

                @Test
                @DisplayName("Deve aceitar se tiver duas cartas fortes")
                void shoulReturnZeroIfHasTwoStrongCards() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
                    );
                    List<TrucoCard> openCards = List.of(vira);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 5)
                            .opponentScore(7);
                    assertEquals(0, lgtbot.getRaiseResponse(stepBuilder.build()));

                }

                @Test
                @DisplayName("Deve fugir se tiver 2 cartas ruins")
                void shouldRunIfHasTwoBadCards() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                    );
                    List<TrucoCard> openCards = List.of(vira);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 5)
                            .opponentScore(7);
                    assertEquals(-1, lgtbot.getRaiseResponse(stepBuilder.build()));

                }

                @Test
                @DisplayName("Deve aceitar se tiver 1 manilha e 1 carta forte")
                void shouldReturnZeroIfHasOneManilhaAndOneStrongCard() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
                    );
                    List<TrucoCard> openCards = List.of(vira);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 5)
                            .opponentScore(7);
                    assertEquals(0, lgtbot.getRaiseResponse(stepBuilder.build()));
                }

                @Test
                @DisplayName("Deve dobrar se tiver 3 manilhas no primeiro round")
                void shouldReturnOneIfHasThreeManilhasInFirstRound() {
                    TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                            TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                    );
                    List<TrucoCard> openCards = List.of(vira);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(), openCards, vira, 1)
                            .botInfo(myCards, 5)
                            .opponentScore(7);
                    assertEquals(1, lgtbot.getRaiseResponse(stepBuilder.build()));
                }

            }

            @Nested
            @DisplayName("Second Round")
            class SecondRoundGetResponseTest {
                @Test
                @DisplayName("Mesmo se tiver perdido a primeira rodada, deve aceitar se no segundo round ainda tiver cartas fortes")
                void shouldReturnZeroIfHasTwoStrongCards() {
                    TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS)
                    );
                    List<TrucoCard> openCards = List.of(vira);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                            .botInfo(myCards, 5)
                            .opponentScore(7);
                    assertEquals(0, lgtbot.getRaiseResponse(stepBuilder.build()));
                }

                @Test
                @DisplayName("Deve fugir no segundo round se perdeu a primeira rodada e tem apenas cartas fracas")
                void shouldReturnMinusOneIfLostFirstRoundAndHasOnlyBadCards() {
                    TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);

                    List<TrucoCard> myCards = List.of(
                            TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                            TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS)
                    );
                    List<TrucoCard> openCards = List.of(vira);
                    stepBuilder = GameIntel.StepBuilder.with()
                            .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 2)
                            .botInfo(myCards, 5)
                            .opponentScore(7);
                    assertEquals(-1, lgtbot.getRaiseResponse(stepBuilder.build()));
                }
            }
        }
    }
}

