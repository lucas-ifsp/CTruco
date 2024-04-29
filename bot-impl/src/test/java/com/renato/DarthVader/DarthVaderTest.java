package com.renato.DarthVader;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DarthVaderTest {

    private DarthVader darthVader;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setUp() {
        darthVader = new DarthVader();
    }

    @Nested
    @DisplayName("Tests for the decideIfRaises method")
    class DecideIfRaisesMethod {
        @DisplayName("Should not accept if you don't have any manilha and don't have any high cards")
        @Test
        public void ShouldNotAcceptIfYouDontHaveAnyManilhaAndDontHaveAnyHighCards()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertFalse(darthVader.getMaoDeOnzeResponse(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Tests to choose a certain card")
    class ChooseCardMethod {

        @DisplayName("Should return the lowest card")
        @Test
        public void shouldReturnTheLowestCard()
        {
            List<TrucoCard> trucoCards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS));

            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            List<TrucoCard> openCards = List.of(vira);

            stepBuilder = GameIntel.StepBuilder.with().
                    gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1).
                    botInfo(trucoCards, 11).
                    opponentScore(5);

            assertEquals(TrucoCard.of(CardRank.FIVE,CardSuit.SPADES),darthVader.getSmallerCard(stepBuilder.build()));
        }

    }


}
