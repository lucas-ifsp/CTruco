package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.CardSuit.HIDDEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeitasBotTest {
    private TeitasBot teitasBot;

    @BeforeEach
    void setUp() {
        teitasBot = new TeitasBot();
    }

    GameIntel.StepBuilder builder;
    GameIntel gameIntel;

    @Nested
    @DisplayName("if we need to raise")
    class ShouldRaiseForAll {

        @Test
        @DisplayName("Should raise if we have nuts or strong hand")
        void shouldRaiseWithNutsOrStrong() {

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

            List<TrucoCard> Tablecards = Collections.singletonList(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

            TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), Tablecards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertEquals(1, teitasBot.getRaiseResponse(builder.build()));
        }

        @Test
        @DisplayName("Should raise if we have good hand and empty round with hidden opponent")
        void shouldRaiseIfEmptyRoundAndGoodHand() {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));
            TrucoCard opponentCard = TrucoCard.of(CardRank.HIDDEN, HIDDEN);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1).botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);
            assertEquals(1, teitasBot.getRaiseResponse(builder.build()));
        }

        @Test
        @DisplayName("Should raise if we drew at past.")
        void shouldRaiseIfDrewAtPast() {
            //the hand isn't relevant
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS), TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS));

            TrucoCard opponentCard = TrucoCard.of(CardRank.HIDDEN, HIDDEN);
            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), List.of(), vira, 1).botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertEquals(1, teitasBot.getRaiseResponse(builder.build()));

        }}

    @Nested
    @DisplayName("if we need the strongest card and is first round")
    class  ShouldChooseCorrectCard{
        @Test
        @DisplayName("Should get strongest card if is second round and made the first")
        void shouldGetStrongestCardIfIsWewinthefirstRound() {
            //opponent card doesnt matter.
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
            );
            List<TrucoCard> tableCards = Collections.singletonList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
            );
            TrucoCard opponentCard = TrucoCard.of(CardRank.HIDDEN, HIDDEN);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), tableCards, vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard)
                    .build();

            assertEquals(TrucoCard.of(THREE, HEARTS), teitasBot.chooseCard(builder.build()).content());
        }
    }
    @Test
    @DisplayName("Should get the weakest card if is first round and opponent card is hidden.")
    void shouldGetTheWeakestCardIfIsFirstRoundAndOpponentCardIsHidden() {
        List<GameIntel.RoundResult> rounds = List.of(GameIntel.RoundResult.WON);
        TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
        List<TrucoCard> botCards = Arrays.asList(
                TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                TrucoCard.of(CardRank.FOUR, CLUBS));

        List<TrucoCard> tableCards = Collections.singletonList(
                TrucoCard.of(CardRank.HIDDEN, HIDDEN)
        );
        TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, DIAMONDS);
        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(rounds, List.of(), vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(TrucoCard.of(FOUR,CLUBS), teitasBot.chooseCard(stepBuilder.build()).content());
    }


}



