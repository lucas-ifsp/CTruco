package com.petrilli.sandro.malasiabot;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


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
    @DisplayName("Choose card tests")
    class ChooseCardTests {
        @Test
        @DisplayName("Should play the lowest winning card against opponent")
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
        @DisplayName("Should play the lowest manilha card against opponent")
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
        @DisplayName("Should play the lowest card if have MaoGiga")
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
        @DisplayName("Should play the highest card if dont met any requirements")
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


    }
    @Nested
    @DisplayName("Raise Response Tests")
    class raiseResponseTests {
        @Test
        @DisplayName("Should refuse raise if have MaoLixo")
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

            assertEquals(-1,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should refuse raise if have MaoMediaSemBoasCartas")
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

            assertEquals(-1,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should reraise if have MaoGiga")
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

            assertEquals(1,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should play lowest card if have MaoGiga")
        void shouldPlayLowestCardIfHaveMaoGiga() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);

            List<TrucoCard> Mao = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS));

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(roundResultsFirstHand, openCardsEmpty, vira, 1)
                    .botInfo(Mao, 0)
                    .opponentScore(0);

            assertEquals(CardRank.FIVE, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
        }

        @Test
        @DisplayName("Should accept raise if have MaoEspadasOuOurosEfiguras")
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

            assertEquals(0,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should reraise if have MaoZapOuCopasEasAtres")
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

            assertEquals(1,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should accept raise if have MaoEspadasOuOurosEasAtres")
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

            assertEquals(0,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should refuse raise if have Manilha and Mao Ruim")
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

            assertEquals(-1,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }


        @Test
        @DisplayName("Should accept raise if have mao media com uma boa carta")
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

            assertEquals(0,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should accept raise if duas boas sem manilha")
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

            assertEquals(0,malasiaBot.getRaiseResponse(stepBuilder.build()));
        }


    }
}