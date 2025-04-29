package com.local.petrilli.sandro.malasiabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MalasiaBotTest {
    private MalasiaBot malasiaBot;

    TrucoCard ThreeOfSpades = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

    TrucoCard FourOfDiamonds = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

    TrucoCard QueenOfDiamonds = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);


    List<GameIntel.RoundResult> roundResultsFirstHand = List.of(
    );
    List<GameIntel.RoundResult> roundResults1Win = List.of(
            GameIntel.RoundResult.WON
    );

    List<GameIntel.RoundResult> roundResults1Lose = List.of(
            GameIntel.RoundResult.LOST
    );

    List<GameIntel.RoundResult> roundResultsTie = List.of(
            GameIntel.RoundResult.DREW
    );

    List<GameIntel.RoundResult> roundResultsWinLose = List.of(
            GameIntel.RoundResult.WON,
            GameIntel.RoundResult.LOST
    );
    List<GameIntel.RoundResult> roundResultsLoseWin = List.of(
            GameIntel.RoundResult.LOST,
            GameIntel.RoundResult.WON
    );

    List<TrucoCard> openCardsLow = Arrays.asList(
            TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
            TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

    List<TrucoCard> openCardsEmpty = List.of();

    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setUp() {
        malasiaBot = new MalasiaBot();
    }

    @Nested
    @DisplayName("Accept Mao de onze tests")
    class MaoDeOnzeTests {
        @Nested
        @DisplayName("Should accept mao de onze with")
        class MaoDeOnze {
            @Test
            @DisplayName("MaoGiga")
            void shouldAcceptMaoDeOnzeWithMaoGiga() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 11)
                        .opponentScore(5);

                assertTrue(malasiaBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("zap or copas and figuras")
            void shouldAcceptMaoDeOnzeWithMaoZapOuCopasEFiguras() {
                TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 11)
                        .opponentScore(0);

                assertTrue(malasiaBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("espadas or ouros and figuras")
            void shouldAcceptMaoDeOnzeWithMaoEspadasOuOurosEFiguras() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 11)
                        .opponentScore(0);

                assertTrue(malasiaBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("zap or copas and ace to three")
            void shouldAcceptMaoDeOnzeWithMaoZapOuCopasEAsAtres() {
                TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 11)
                        .opponentScore(0);

                assertTrue(malasiaBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("espada or ouro and ace to three")
            void shouldAcceptMaoDeOnzeWithMaoEspadaOuOuroEAsATresHand() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 11)
                        .opponentScore(0);

                assertTrue(malasiaBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("two good cards and no manilha")
            void shouldAcceptMaoDeOnzeWithMaoComDuasBoasSemManilhaHand() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.KING, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 11)
                        .opponentScore(0);

                assertTrue(malasiaBot.getMaoDeOnzeResponse(stepBuilder.build()));
            }
        }
    }

    @Nested
    @DisplayName("Choose card tests")
    class ChooseCardTests {
        @Nested
        @DisplayName("Should play the")
        class ChooseCard {
            @Test
            @DisplayName("Lowest winning card against opponent")
            void shouldPlayTheLowestWinningCardAgainstOpponentTest() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0)
                        .opponentCard(FourOfDiamonds);

                assertEquals(CardRank.THREE, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }


            @Test
            @DisplayName("Lowest manilha card against opponent")
            void shouldPlayTheLowestManilhaCardAgainstOpponentTest() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0)
                        .opponentCard(ThreeOfSpades);

                assertEquals(CardRank.TWO, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
                assertEquals(CardSuit.SPADES, malasiaBot.chooseCard(stepBuilder.build()).content().getSuit());
            }

            @Test
            @DisplayName("Lowest card if have MaoGiga")
            void shouldPlayTheLowestCardIfHaveMaoGiga() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(CardRank.FIVE, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }


            @Test
            @DisplayName("Highest card if dont met any requirements")
            void shouldPlayTheHighestCardIfDontMetAnyRequirements() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(CardRank.SIX, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }

            @Test
            @DisplayName("Medium card if have Zap ou Copas and Aces to three")
            void shouldPlayTheMediumCardIfhaveZapOuCopasAndAcesToThree() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(CardRank.THREE, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }

            @Test
            @DisplayName("Strongest card if have Espada ou Ouros and Aces to three")
            void shouldPlayTheStrongestCardIfHaveEspadaOuOurosAndAcesToThree() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.TWO, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(CardRank.TWO, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }

            @Test
            @DisplayName("Strongest card if have Zap ou Copas and Pictures")
            void shouldPlayTheStrongestCardIfHaveZapOuCopasAndPictures() {
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(CardRank.TWO, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }

            @Test
            @DisplayName("Weakest card if cant beat opponent card")
            void shouldPlayTheWeakestCardIfCantBeatOpponentCard() {
                TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0).opponentCard(QueenOfDiamonds);

                assertEquals(CardRank.FIVE, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }
        }
    }

    @Nested
    @DisplayName("Raise Response Tests")
    class raiseResponseTests {
        @Nested
        @DisplayName("Should accept raise if has")
        class raiseResponseAccept {
            @Test
            @DisplayName("MaoGiga")
            void shouldReraiseIfHaveMaoGiga() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(1, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("espadas or ouros and pictures")
            void shouldAcceptRaiseIfHaveMaoEspadasOuOurosEfiguras() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(0, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("zap or copas and ace to three")
            void shouldReRaiseIfHaveMaoZapOuCopasEasAtres() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(1, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("espadas ou ouros and ace to three")
            void shouldAcceptRaiseIfHaveMaoEspadasOuOurosEasAtres() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(0, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("mao media com uma boa carta")
            void shouldRefuseRaiseIfHaveMaoMediaComUmaBoaCarta() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(0, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("duas boas sem manilha")
            void shouldAcceptRaiseIfDuasBoasSemManilha() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(0, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }
        }

        @Nested
        @DisplayName("Should reject raise if has")
        class RaiseResponseReject {
            @Test
            @DisplayName("manilha e mao ruim")
            void shouldRefuseRaiseIfHaveManilhaAndMaoRuim() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(-1, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("mao media sem boas cartas")
            void shouldRefuseRaiseIfHaveMaoMediaSemBoasCartas() {
                TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(-1, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }

            @Test
            @DisplayName("MaoLixo")
            void shouldRefuseRaiseIfHaveMaoLixo() {
                TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertEquals(-1, malasiaBot.getRaiseResponse(stepBuilder.build()));
            }
        }
    }

    @Nested
    @DisplayName("Decide if Raise Tests")
    class decideIfRaiseTests {
        @Nested
        @DisplayName("Should raise if have")
        class raise {
            @Test
            @DisplayName("MaoGiga and lost first round")
            void shouldRaiseIfMaoGigaAndLostFirstRound() {
                TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResults1Lose, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertTrue(malasiaBot.decideIfRaises(stepBuilder.build()));
            }

            @Test
            @DisplayName("better card than opponent in last hand")
            void shouldRaiseIfHaveBetterCardThanOpponentInLastHand() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsWinLose, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0).opponentCard(QueenOfDiamonds);

                assertTrue(malasiaBot.decideIfRaises(stepBuilder.build()));
            }

            @Test
            @DisplayName("mao lixo")
            void shouldRaiseIfHaveMaoLixo() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsWinLose, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0).opponentCard(QueenOfDiamonds);

                assertTrue(malasiaBot.decideIfRaises(stepBuilder.build()));
            }

            @Test
            @DisplayName("mao zap ou copa e figuras")
            void shouldRaiseIfHaveMaoZapOuCopaEFiguras() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0).opponentCard(QueenOfDiamonds);

                assertTrue(malasiaBot.decideIfRaises(stepBuilder.build()));
            }

        }


        @Nested
        @DisplayName("Shouldnt raise if have")
        class dontRaise {
            @Test
            @DisplayName("Mao media")
            void shouldNotRaiseIfHaveMaoMedia() {
                TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertFalse(malasiaBot.decideIfRaises(stepBuilder.build()));
            }
            @Test
            @DisplayName("Mao lixo com manilha")
            void shouldNotRaiseIfMaoManilhaAndLixo() {
                TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

                List<TrucoCard> Mao = Arrays.asList(
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                        .botInfo(Mao, 0)
                        .opponentScore(0);

                assertFalse(malasiaBot.decideIfRaises(stepBuilder.build()));
            }
        }
    }
}
