/*
 *  Copyright (C) 2023 Lucas Silva de Almeida - IFSP/SCL and Alexandre Rodrigues Strapasson - IFSP/SCL
 *  Contact: lucas <dot> almeida1 <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: a <dot> strapasson <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.almeida.strapasson.veiodobar;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class GameIntelMockBuilder {
    private final GameIntel intel;

    private GameIntelMockBuilder() {
        intel = mock(GameIntel.class);
    }

    static GameIntelMockBuilder make() { return new GameIntelMockBuilder(); }

    GameIntelMockBuilder cardsToBe(TrucoCard... cards) {
        when(intel.getCards()).thenReturn(Arrays.stream(cards).toList());
        return this;
    }

    GameIntelMockBuilder scoreToBe(int botScore){
        when(intel.getScore()).thenReturn(botScore);
        return this;
    }

    GameIntelMockBuilder scoreOponentToBe(int oponentScore){
        when(intel.getOpponentScore()).thenReturn(oponentScore);
        return this;
    }

    GameIntelMockBuilder cardsToBeAceTwoAndThreeOfSuit(CardSuit suit) {
        return cardsToBe(
                TrucoCard.of(CardRank.ACE, suit),
                TrucoCard.of(CardRank.TWO, suit),
                TrucoCard.of(CardRank.THREE, suit)
        );
    }

    GameIntelMockBuilder cardsToBeThreeOf(CardRank rank) {
        return cardsToBe(
                TrucoCard.of(rank, CardSuit.SPADES),
                TrucoCard.of(rank, CardSuit.HEARTS),
                TrucoCard.of(rank, CardSuit.CLUBS)
        );
    }

    GameIntelMockBuilder viraToBeDiamondsOfRank(CardRank viraRank) {
        when(intel.getVira()).thenReturn(TrucoCard.of(viraRank, CardSuit.DIAMONDS));
        return this;
    }

    GameIntelMockBuilder opponentCardToBe(TrucoCard card) {
        when(intel.getOpponentCard()).thenReturn(Optional.of(card));
        return this;
    }

    GameIntelMockBuilder botToBeFirstToPlay() {
        when(intel.getOpponentCard()).thenReturn(Optional.empty());
        return this;
    }

    GameIntelMockBuilder botToWinTheFirstRound() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.WON));
        return this;
    }

    GameIntelMockBuilder botToLoseTheFirstRound() {
        when(intel.getRoundResults()).thenReturn(List.of(GameIntel.RoundResult.LOST));
        return this;
    }

    GameIntelMockBuilder roundResultToBe(GameIntel.RoundResult... results) {
        when(intel.getRoundResults()).thenReturn(Arrays.stream(results).toList());
        return this;
    }

    GameIntel finish() { return intel; }
}
