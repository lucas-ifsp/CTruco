/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.spi.model;

import com.bueno.spi.model.GameIntel.StepBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.HEARTS;
import static com.bueno.spi.model.CardSuit.SPADES;
import static org.junit.jupiter.api.Assertions.*;

class GameIntelTest {

    private StepBuilder stepBuilder;
    private List<GameIntel.RoundResult> results;
    private List<TrucoCard> openCards;
    private List<TrucoCard> botCards;
    private TrucoCard vira;

    @BeforeEach
    void setUp() {
        results = List.of(GameIntel.RoundResult.DREW);
        openCards = List.of(TrucoCard.of(TWO, HEARTS));
        botCards = List.of(TrucoCard.of(ACE, HEARTS));
        vira = TrucoCard.of(THREE, SPADES);
        stepBuilder = StepBuilder.with()
                .gameInfo(results, openCards, vira, 1)
                .botInfo(botCards, 9)
                .opponentScore(3);
    }

    @Test
    @DisplayName("Should correctly create GameIntel")
    void shouldCorrectlyCreateGameIntel() {
        final TrucoCard opponentCard = TrucoCard.of(THREE, SPADES);
        final GameIntel intel = stepBuilder.opponentCard(opponentCard).build();
        assertAll(
                () -> assertIterableEquals(results, intel.getRoundResults()),
                () -> assertIterableEquals(openCards, intel.getOpenCards()),
                () -> assertIterableEquals(botCards, intel.getCards()),
                () -> assertEquals(vira, intel.getVira()),
                () -> assertEquals(1, intel.getHandPoints()),
                () -> assertEquals(9, intel.getScore()),
                () -> assertEquals(3, intel.getOpponentScore()),
                () -> assertEquals(opponentCard, intel.getOpponentCard().orElse(null))
        );
    }

    @Test
    @DisplayName("Should correctly create GameIntel with absent opponent card")
    void shouldCorrectlyCreateGameIntelWithAbsentOpponentCard() {
        final GameIntel intel = stepBuilder.build();
        assertEquals(Optional.empty(), intel.getOpponentCard());
    }

    @Test
    @DisplayName("Should objects with same state be equal")
    void shouldObjectsWithSameStateBeEqual() {
        assertEquals(stepBuilder.build(), stepBuilder.build());
    }

    @Test
    @DisplayName("Should objects with different state be not equal")
    void shouldObjectsWithDifferentStateBeNotEqual() {
        final TrucoCard opponentCard = TrucoCard.of(THREE, SPADES);
        final GameIntel intel = stepBuilder.build();
        final GameIntel otherIntel = stepBuilder.opponentCard(opponentCard).build();
        assertNotEquals(intel, otherIntel);
    }

    @Test
    @DisplayName("Should objects with same state have the same hashcode")
    void shouldObjectsWithSameStateHaveTheSameHashcode() {
        assertEquals(stepBuilder.build().hashCode(), stepBuilder.build().hashCode());

    }
}