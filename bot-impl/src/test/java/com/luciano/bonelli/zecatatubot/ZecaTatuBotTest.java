package com.luciano.bonelli.zecatatubot;

import com.bueno.spi.model.GameIntel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZecaTatuBotTest {
    private ZecaTatuBot zecaTatuBot;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setup() {
        zecaTatuBot = new ZecaTatuBot();
    }

    @Nested
    @DisplayName("Test getMaoDeOnzeResponse method")
    class GetMaoDeOnzeResponseTest {

    }

    @Nested
    @DisplayName("Test decideIfRaises method")
    class DecideIfRaisesTest {

    }


    @Nested
    @DisplayName("Test chooseCard method")
    class ChooseCardTest {

    }

    @Nested
    @DisplayName("Test getRaiseResponse method")
    class GetRaiseResponseTest {
    }

}