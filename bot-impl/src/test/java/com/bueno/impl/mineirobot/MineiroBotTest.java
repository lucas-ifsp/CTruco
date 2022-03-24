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

package com.bueno.impl.mineirobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MineiroBotTest {

    @Mock private GameIntel intel;
    @InjectMocks private MineiroBot sut;

    @Test
    @DisplayName("Should throw if get raise response with null intel")
    void shouldThrowIfGetRaiseResponseWithNullIntel() {
        assertThrows(NullPointerException.class, () -> sut.getRaiseResponse(null));
    }

    @Test
    @DisplayName("Should does not throw if get raise response with proper intel")
    void shouldDoesNotThrowIfGetRaiseResponseWithProperIntel() {
        when(intel.getHandPoints()).thenReturn(3);
        when(intel.getCards()).thenReturn(List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)));
        when(intel.getVira()).thenReturn(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
        assertDoesNotThrow(() -> sut.getRaiseResponse(intel));
    }

    @Test
    @DisplayName("Should throw if get mao de onze response with null intel")
    void shouldThrowIfGetMaoDeOnzeResponseWithNullIntel() {
        assertThrows(NullPointerException.class, () -> sut.getMaoDeOnzeResponse(null));
    }

    @Test
    @DisplayName("Should throw if decides if raises with null intel")
    void shouldThrowIfDecidesIfRaisesWithNullIntel() {
        assertThrows(NullPointerException.class, () -> sut.decideIfRaises(null));
    }

    @Test
    @DisplayName("Should throw if choose card with null intel")
    void shouldThrowIfChooseCardWithNullIntel() {
        assertThrows(NullPointerException.class, () -> sut.chooseCard(null));
    }
}