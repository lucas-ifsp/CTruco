package com.francisco.pedrohenriquebot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedroHenriqueBotTest {

    private PedroHenriqueBot sut;
    private GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp() {
        sut = new PedroHenriqueBot();
    }


    @Test
    @DisplayName("Should return correct bot name")
    void shouldReturnCorrectBotName() {
        assertEquals("PedroHenriqueBot", sut.getName());
    }

    @Nested
    @DisplayName("Testing getMaoDeOnzeResponse")
    class GetMaoDeOnzeResponseTest {
        @Test
        @DisplayName("Should accept mao de onze with 2 manilhas")
        void shouldAcceptMaoDeOnzeWith2Manilhas() {
            TrucoCard vira = TrucoCard.of(ACE, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(THREE, CLUBS),
                    TrucoCard.of(ACE, DIAMONDS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,0)
                    .botInfo(botCards,11)
                    .opponentScore(8);

            assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
        }

    }

    @Test
    void getMaoDeOnzeResponse() {
    }

    @Test
    void decideIfRaises() {
    }

    @Test
    void chooseCard() {
    }

    @Test
    void getRaiseResponse() {
    }
}