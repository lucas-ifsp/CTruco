package com.joao.alexandre.jormungandrbot;

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

class JormungandrBotTest {
    private JormungandrBot jormungandrBot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void config() {
        jormungandrBot = new JormungandrBot();

    }

    @Nested
    @DisplayName("Testing getLowestCardInHand() function")
    class GetLowestCardInHandTest {

        @Test
        @DisplayName("When player has one card in hand, should return that one card as lowest card")
        void shouldReturnCardInHandIfHandOnlyHasOneCard() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> currentCards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(currentCards, 0)
                    .opponentScore(0);
            
            assertEquals(
                    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                    jormungandrBot.getLowestCardInHand(stepBuilder.build())
            );
        }
    }
}