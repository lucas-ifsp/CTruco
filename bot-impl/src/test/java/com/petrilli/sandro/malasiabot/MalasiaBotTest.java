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
    @DisplayName("choose card tests")
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
        @DisplayName("Should accept raise if have MaoGiga")
        void shouldAcceptRaiseIfHaveMaoGiga() {
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

    }
}