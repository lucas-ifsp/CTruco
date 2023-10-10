package com.tatayrapha.leonardabot;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeonardaBotTest {

    @Mock
    GameIntel gameIntel;

    LeonardaBot leonardaBot;

    @BeforeEach
    void setUp() {
        leonardaBot = new LeonardaBot();
    }

    @Test
    @DisplayName("Should throw random card.")
    void shouldThrowRandomCard() {
        final List<TrucoCard> trucoCardList = List.of(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS), TrucoCard.of(CardRank.TWO, CardSuit.CLUBS), TrucoCard.of(CardRank.THREE, CardSuit.CLUBS));
        when(gameIntel.getCards()).thenReturn(trucoCardList);

        CardToPlay chosenCard = leonardaBot.chooseCard(gameIntel);
        assertThat(trucoCardList).contains(chosenCard.value());
    }
}
