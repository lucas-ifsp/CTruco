package com.brito.macena.boteco.utils;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

@DisplayName("Game Tests")
public class GameTest {

    private GameIntel intel;

    @BeforeAll
    static void setupAll() { System.out.println("Starting Game tests..."); }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finishing Game tests...");
    }
}
