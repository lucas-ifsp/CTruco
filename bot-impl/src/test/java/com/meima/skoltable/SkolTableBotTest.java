package com.meima.skoltable;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SkolTableBotTest {

    private SkolTable skolTable;

    @BeforeEach
    public void setUp(){
        skolTable = new SkolTable();
    }


    @Test
    @DisplayName("Should play strongest card in first round if it's stronger than opponent's")
    void shouldPlayStrongestCardInFirstRoundIfStrongerThanOpponents() {

        List<TrucoCard> botCards = List.of(TrucoCard.of(ACE, SPADES), TrucoCard.of(THREE, CLUBS), TrucoCard.of(FOUR, HEARTS));
        TrucoCard vira = TrucoCard.of(THREE, HEARTS);
        TrucoCard opponentCard = TrucoCard.of(ACE, CLUBS);

        List<TrucoCard> openCards = List.of(vira);
        List<GameIntel.RoundResult> results = List.of();

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);


        assertEquals(TrucoCard.of(FOUR, HEARTS), skolTable.chooseCard(stepBuilder.build()).content());
    }

}