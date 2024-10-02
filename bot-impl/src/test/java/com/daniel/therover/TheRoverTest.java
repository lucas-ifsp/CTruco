package com.daniel.therover;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class TheRoverTest {
    private TheRover theRover;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setup() {
        theRover = new TheRover();
    }

    @Nested
    @DisplayName("isPlayingFirst Tests")
    class isPlayingFirstTest {

        @Test
        @DisplayName("Should return false if player is playing second")
        void ShouldReturnFalseIfPlayerIsPlayingSecond() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertFalse(theRover.isPlayingFirst(stepBuilder.build()));
        }



    }

}
