package com.francisco.pedrohenriquebot;

import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;

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