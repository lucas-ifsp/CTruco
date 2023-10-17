package com.correacarini.trucomachinebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.correacarini.impl.trucomachinebot.TrucoMachineBot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrucoMachineBotTest {
    @Test
    @DisplayName("Should choose a card")
    void ShouldChooseACard() {
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(TrucoCard.of(ACE, CLUBS)), 0);

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertNotNull(cardToPlay);
    }

    @Test
    @DisplayName("Should return greatest card in round 1 if is first to play")
    void ShouldReturnGreatestCardInRound1IfIsFirstToPlay() {
        List<TrucoCard> botCards = List.of(
                TrucoCard.of(SIX, CLUBS),
                TrucoCard.of(FIVE, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(botCards, 0);

        CardToPlay cardToPlay = new TrucoMachineBot().chooseCard(stepBuilder.build());
        assertEquals(CardToPlay.of(TrucoCard.of(THREE, CLUBS)), cardToPlay);
    }
}
