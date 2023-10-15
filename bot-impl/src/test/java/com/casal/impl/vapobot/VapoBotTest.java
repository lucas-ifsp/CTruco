package com.casal.impl.vapobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VapoBotTest {
    private VapoBot vapoBot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void config() {
        vapoBot = new VapoBot();

    }

    @Nested
    @DisplayName("Testing higher card function")
    class HigherCardTest {

        @Test
        @DisplayName("3 of Hearts is higher than 3 of Spades and 2 of Clubs")
        void makeSureToReturnHighest3 () {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = Arrays.asList();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS), vapoBot.getHighestCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("4 of Diamonds is higher than 7 of Spades and 3 of Clubs")
        void makeSureToReturnHighestIs4Diamonds () {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = Arrays.asList();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), vapoBot.getHighestCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("Manilha of Clubs is Higher than Manilha of Spades and Manilha of Diamonds")
        void makeSureThatHighestIsJackOfClubs () {
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.JACK, CardSuit.CLUBS), vapoBot.getHighestCard(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Testing lowest card function")
    class LowestCardTest {

        @Test
        @DisplayName("A of Clubs is lower than 2 of Hearts and Manilha")
        void makeSureThatLowestIsAceOfClubs () {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            );

            List<TrucoCard> openCards = List.of();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 1)
                    .opponentScore(3);

            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), vapoBot.getLowestCard(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("In method getAverageCardValue()")
    class getIntAverageCardValueTest {

        @Test
        @DisplayName("2D,3D and 7C average should be 7.66...")
        void shouldHaveAnAverageOf7(){
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);

            List<TrucoCard> myCards = Arrays.asList(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS)
            );

            List<TrucoCard> openCards = Arrays.asList();

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            double average = vapoBot.getAverageCardValue(stepBuilder.build());
            double result = 23/3;

            assertEquals(average, result);
        }
    }
}