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
    class ShouldRaise {
        @Test
        @DisplayName("Should raise when has 3 manilhas")
        void shouldRaiseWhenHas3Manilhas() {
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(ACE, CLUBS)
            );
            TrucoCard vira = TrucoCard.of(KING, DIAMONDS);

            GameIntel intel = new GameIntel(null, botCards, null, vira, null, 0, 0, null);
            boolean decideIfRaises = new BarDoAlexBot().decideIfRaises(intel);
            assertThat(decideIfRaises).isEqualTo(true);
        }


    }

    @Nested
    @DisplayName("Test of the bot logic to choose card")
    class ChooseCard {
        @Test
        @DisplayName("Should choose last card")
        void shouldChooseLastCard() {
            List<TrucoCard> botCards = List.of(
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(ACE, SPADES),
                    TrucoCard.of(KING, CLUBS)
            );
            TrucoCard vira = TrucoCard.of(KING, DIAMONDS);

            GameIntel intel = new GameIntel(null, botCards, null, vira, null, 0, 0, null);
            CardToPlay card = new BarDoAlexBot().chooseCard(intel);
            assertThat(card.getCard()).isEqualTo(TrucoCard.of(KING, CLUBS));
        }


    }

    @Nested
    @DisplayName("Test of the bot logic to raise response")
    class RaiseResponse {
        @Test
        @DisplayName("Should return 1 when opponent score is 11 and bot score is not 0")
        void shouldReturn1WhenOpponentScoreIs11AndBotScoreIsNot0() {

        }


    }

    @Test
    @DisplayName("Should reject when score is 11")
    void shouldRejectWhenScoreIs11() {
        TrucoCard vira = TrucoCard.of(QUEEN, DIAMONDS);
        GameIntel intel = new GameIntel(null, null, null, vira, null, 11, 0, null);
        int raiseResponse = new BarDoAlexBot().getRaiseResponse(intel);
        assertThat(raiseResponse).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should get MaoDeOnze response if opponent has score 11")
    void shouldGetMaoDeOnzeIfOpponentScoreIs11() {
        TrucoCard vira = TrucoCard.of(QUEEN, DIAMONDS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(JACK, SPADES),
                TrucoCard.of(SIX, SPADES),
                TrucoCard.of(SEVEN, SPADES)
        );

        GameIntel intel = new GameIntel(null, botCards, null, vira, null, 0, 11, null);
        boolean maoDeOnze = new BarDoAlexBot().getMaoDeOnzeResponse(intel);
        assertThat(maoDeOnze).isTrue();
    }

    @Test
    @DisplayName("Should get MaoDeOnze response if cards aren't strong")
    void shouldGetMaoDeOnzeIfCardsArentStrong() {
        TrucoCard vira = TrucoCard.of(QUEEN, DIAMONDS);
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(JACK, SPADES),
                TrucoCard.of(SIX, SPADES),
                TrucoCard.of(SEVEN, SPADES)
        );

        GameIntel intel = new GameIntel(null, botCards, null, vira, null, 0, 2, null);
        boolean maoDeOnze = new BarDoAlexBot().getMaoDeOnzeResponse(intel);
        assertThat(maoDeOnze).isTrue();
    }



}
