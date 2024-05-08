package com.antonelli.rufino.bardoalexbot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BarDoAlexBotTest {

    @Nested
    @DisplayName("Test of the bot logic to decide if raises")
    class DecideIfRaisesTests {
        @Test
        @DisplayName("Should return true if there are 3 manilhas in cards")
        void shouldReturnTrueIfThreeManilhas() {
            TrucoCard vira = TrucoCard.of(SEVEN, HEARTS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(SEVEN, SPADES),
                    TrucoCard.of(SEVEN, CLUBS),
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(FOUR, DIAMONDS)
            );

            BarDoAlexBot bot = new BarDoAlexBot();
            GameIntel intel = new GameIntel(cards, null, null, vira, null, 0, 0);

            assertThat(bot.decideIfRaises(intel)).isTrue();
        }

        @Test
        @DisplayName("Should return false if there are less than 3 manilhas in cards")
        void shouldReturnFalseIfLessThanThreeManilhas() {
            TrucoCard vira = TrucoCard.of(TWO, CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(TWO, SPADES),
                    TrucoCard.of(THREE, HEARTS),
                    TrucoCard.of(FIVE, DIAMONDS),
                    TrucoCard.of(EIGHT, SPADES)
            );

            BarDoAlexBot bot = new BarDoAlexBot();
            GameIntel intel = new GameIntel(cards, null, null, vira, null, 0, 0);

            assertThat(bot.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("Should return true if all cards are manilhas")
        void shouldReturnTrueIfAllCardsAreManilhas() {
            TrucoCard vira = TrucoCard.of(JACK, CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(JACK, SPADES),
                    TrucoCard.of(JACK, HEARTS),
                    TrucoCard.of(JACK, DIAMONDS),
                    TrucoCard.of(JACK, CLUBS)
            );

            BarDoAlexBot bot = new BarDoAlexBot();
            GameIntel intel = new GameIntel(cards, null, null, vira, null, 0, 0);

            assertThat(bot.decideIfRaises(intel)).isTrue();
        }

        @Test
        @DisplayName("Should return false if cards are null")
        void shouldReturnFalseIfCardsAreNull() {
            TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);

            BarDoAlexBot bot = new BarDoAlexBot();
            GameIntel intel = new GameIntel(null, null, null, vira, null, 0, 0);

            assertThat(bot.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("Should return false if cards list is empty")
        void shouldReturnFalseIfCardsListIsEmpty() {
            TrucoCard vira = TrucoCard.of(KING, HEARTS);

            BarDoAlexBot bot = new BarDoAlexBot();
            GameIntel intel = new GameIntel(List.of(), null, null, vira, null, 0, 0);

            assertThat(bot.decideIfRaises(intel)).isFalse();
        }

        @Test
        @DisplayName("Should return false if vira card is null")
        void shouldReturnFalseIfViraIsNull() {
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(SIX, SPADES),
                    TrucoCard.of(FOUR, CLUBS),
                    TrucoCard.of(THREE, HEARTS),
                    TrucoCard.of(ACE, DIAMONDS)
            );

            BarDoAlexBot bot = new BarDoAlexBot();
            GameIntel intel = new GameIntel(cards, null, null, null, null, 0, 0);

            assertThat(bot.decideIfRaises(intel)).isFalse();
        }
    }


}
