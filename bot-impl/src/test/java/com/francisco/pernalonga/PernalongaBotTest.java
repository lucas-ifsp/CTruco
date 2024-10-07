package com.francisco.pernalonga;

import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PernalongaBotTest {

    private PernalongaBot sut;
    private GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp() {
        sut = new PernalongaBot();
    }


    @Test
    @DisplayName("Should return correct bot name")
    void shouldReturnCorrectBotName() {
        assertEquals("PernalongaBot", sut.getName());
    }

    @Nested
    @DisplayName("Testing getMaoDeOnzeResponse")
    class GetMaoDeOnzeResponseTest {

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