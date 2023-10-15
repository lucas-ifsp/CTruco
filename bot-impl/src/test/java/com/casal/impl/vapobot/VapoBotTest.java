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

    }
}