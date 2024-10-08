package com.brito.macena.boteco.utils;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class GameTest {

    private GameIntel intel;

    @BeforeAll
    static void setupAll() { System.out.println("Starting Game tests..."); }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finishing Game tests...");
    }

    @BeforeEach
    public void setUp() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
        TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
        intel = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(card1, card2, card3), vira, 2)
                .botInfo(List.of(card1, card2, card3), 0)
                .opponentScore(0)
                .build();
    }
}
