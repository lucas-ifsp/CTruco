package com.petrilli.sandro.malasiabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class MalasiaBotTest {

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
    private MalasiaBot malasiaBot;
        @Test
        @DisplayName("Can kill opponent card")
        void canKillOpponentcard() {
            TrucoCard Vira = TrucoCard.of(CardRank.FIVE, CardSuit.SPADES);

            List<TrucoCard> Mao = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS));


            List<TrucoCard> botCards;

            stepBuilder  = GameIntel.StepBuilder.with()
                    .gameInfo(roundResultsFirstHand, openCardsLow, Vira, 1)
                    .botInfo(Mao, 0)
                    .opponentScore(0);
        }

}