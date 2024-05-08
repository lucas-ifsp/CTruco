package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeitasBotTest {
    private TeitasBot teitasBot;
    @BeforeEach
    void setUp() {
        teitasBot = new TeitasBot();
    }
    GameIntel.StepBuilder builder;

    @Nested
    @DisplayName("if we need to raise")
    class ShouldRaiseForAll{

    }
        @Test
        @DisplayName("Should raise if we have nuts or strong hand")
        void shouldRaiseWithNutsOrStrong(){

        TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

        List<TrucoCard> botCards = Arrays.asList(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS), TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));

        List<TrucoCard> Tablecards = Collections.singletonList(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS));

        TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(GameIntel.RoundResult.WON), Tablecards, vira, 1)
                .botInfo(botCards, 0)
                .opponentScore(0)
                .opponentCard(opponentCard);

        assertEquals(1,teitasBot.getRaiseResponse(builder.build()));

    }
    }


