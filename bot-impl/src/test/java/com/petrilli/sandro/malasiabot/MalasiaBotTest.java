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


public class MalasiaBotTest {
        private MalasiaBot malasiaBot;

        List<TrucoCard> Mao = Arrays.asList(
                TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                TrucoCard.of(CardRank.KING, CardSuit.HEARTS));
        TrucoCard opponentCard = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

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

        List<TrucoCard> openCardsLow = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS));

        private GameIntel.StepBuilder stepBuilder;

        @BeforeEach
        public void setUp(){
            malasiaBot = new MalasiaBot();
        }

        /*@Test
        @DisplayName("Can kill opponent card")
        void canKillOpponentcard() {
            TrucoCard Vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

            List<TrucoCard> botCards;

            stepBuilder  = GameIntel.StepBuilder.with()
                    .gameInfo(roundResultsFirstHand, openCardsLow, Vira, 1)
                    .botInfo(Mao, 0)
                    .opponentScore(0);

        }*/

        @Nested
        @DisplayName("choose card tests")
        class ChooseCardTests {
            @Test
            @DisplayName("Should play the lowest winning card against opponent")
            void shouldPlayTheLowestWinningCardAgainstOpponentTest(){
                TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

                List<TrucoCard> openCards = Arrays.asList(
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS));

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                        .botInfo(Mao, 9)
                        .opponentScore(3)
                        .opponentCard(opponentCard);

                assertEquals(CardRank.KING, malasiaBot.chooseCard(stepBuilder.build()).content().getRank());
            }
        }
}