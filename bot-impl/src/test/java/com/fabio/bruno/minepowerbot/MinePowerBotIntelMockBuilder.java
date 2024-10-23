/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

// Authors: Bruno Cobuci & Fábio Seyiji

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
