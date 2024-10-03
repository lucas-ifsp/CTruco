package com.bianca.joaopedro.lgtbot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LgtbotTest {
    private Lgtbot lgtbot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void config(){
        lgtbot = new Lgtbot();
    }

    @Nested
    @DisplayName("Test getMaoDeOnzeResponse method")
    class GetMaoDeOnzeResponseTest {
        @Test
        @DisplayName("Should return true when opponent has less than 7 points, 3 cards stronger than Jack, and has manilhas")
        void shouldReturnTrueWhenLessThan7PointsAndHasStrongCardsAndManilhas() {
            GameIntel intel = mock(GameIntel.class);
            List<TrucoCard> strongCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );
            List<TrucoCard> manilhas = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
            );

            when(intel.getOpponentScore()).thenReturn(6);
            when(lgtbot.getStrongerCards(intel, CardRank.JACK)).thenReturn(strongCards);
            when(lgtbot.getManilhas(intel)).thenReturn(manilhas);

            assertTrue(lgtbot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should return true when opponent has less than 11 points, 3 cards stronger than Ace, and has manilhas")
        void shouldReturnTrueWhenLessThan11PointsAndHasStrongerCardsThanAce() {
            GameIntel intel = mock(GameIntel.class);
            List<TrucoCard> strongCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );
            List<TrucoCard> manilhas = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES)
            );

            when(intel.getOpponentScore()).thenReturn(9);
            when(lgtbot.getStrongerCards(intel, CardRank.ACE)).thenReturn(strongCards);
            when(lgtbot.getManilhas(intel)).thenReturn(manilhas);

            assertTrue(lgtbot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should return true when opponent has exactly 11 points")
        void shouldReturnTrueWhenOpponentHasExactly11Points() {
            GameIntel intel = mock(GameIntel.class);

            when(intel.getOpponentScore()).thenReturn(11);

            assertTrue(lgtbot.getMaoDeOnzeResponse(intel));
        }

        @Test
        @DisplayName("Should return false when opponent has more than 7 points and no strong cards or manilhas")
        void shouldReturnFalseWhenMoreThan7PointsAndNoStrongCardsOrManilhas() {
            GameIntel intel = mock(GameIntel.class);
            List<TrucoCard> strongCards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );
            List<TrucoCard> manilhas = List.of();

            when(intel.getOpponentScore()).thenReturn(8);
            when(lgtbot.getStrongerCards(intel, CardRank.JACK)).thenReturn(strongCards);
            when(lgtbot.getManilhas(intel)).thenReturn(manilhas);

            assertFalse(lgtbot.getMaoDeOnzeResponse(intel));
        }
    }

}
