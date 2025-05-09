package com.luciano.bonelli.zecatatubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ZecaTatuBotTest {
    private ZecaTatuBot zecaTatuBot;
    private GameIntel.StepBuilder stepBuilder;
    private GameIntel intel;

    @BeforeEach
    public void setup() {
        zecaTatuBot = new ZecaTatuBot();
        intel = mock(GameIntel.class);
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

    @Test
    @DisplayName("CountHowManyManilhasInHand")
    public void countManilhas() {
        TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
        when(intel.getVira()).thenReturn(vira);
        TrucoCard card1 = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);
        TrucoCard card2 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
        TrucoCard card3 = TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES);

        when(intel.getCards()).thenReturn(List.of(card1, card2, card3));
        long countManilha = zecaTatuBot.countManilha(intel);
        assertThat(countManilha).isEqualTo(2);
    }
}

