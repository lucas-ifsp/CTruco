package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MinePowerBotIntelMockBuilder {
    private final GameIntel intel;

    private MinePowerBotIntelMockBuilder(){
        intel = mock(GameIntel.class);
    }

    static MinePowerBotIntelMockBuilder create() { return new MinePowerBotIntelMockBuilder(); }

    MinePowerBotIntelMockBuilder cards(TrucoCard... cards) {
        when(intel.getCards()).thenReturn(Arrays.stream(cards).toList());
        return this;
    }

    MinePowerBotIntelMockBuilder cardsToBeAceTwoAndThreeOfSuit(CardSuit suit) {
        return cards(
                TrucoCard.of(CardRank.ACE, suit),
                TrucoCard.of(CardRank.TWO, suit),
                TrucoCard.of(CardRank.THREE, suit)
        );
    }

    MinePowerBotIntelMockBuilder opponentCardToBe(TrucoCard card) {
        when(intel.getOpponentCard()).thenReturn(Optional.of(card));
        return this;
    }

    MinePowerBotIntelMockBuilder scoreMine(int botScore){
        when(intel.getScore()).thenReturn(botScore);
        return this;
    }

    MinePowerBotIntelMockBuilder scoreOponent(int oponentScore){
        when(intel.getOpponentScore()).thenReturn(oponentScore);
        return this;
    }

    MinePowerBotIntelMockBuilder viraToBeDiamondsOfRank(CardRank viraRank) {
        when(intel.getVira()).thenReturn(TrucoCard.of(viraRank, CardSuit.DIAMONDS));
        return this;
    }

    MinePowerBotIntelMockBuilder viraToBe(CardRank viraRank, CardSuit viraSuit){
        when(intel.getVira()).thenReturn(TrucoCard.of(viraRank, viraSuit));
        return this;
    }

    MinePowerBotIntelMockBuilder roundToBeSecond(RoundResult roundResult){
        when(intel.getRoundResults()).thenReturn(List.of(roundResult));
        return this;
    }

    GameIntel finish() { return intel; }
}
