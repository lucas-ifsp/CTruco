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

package com.bueno.domain.entities.hand.states;

import com.bueno.domain.entities.hand.Hand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OneCardTest {

    @Mock Hand hand;

    @Test
    @DisplayName("Should throw if plays first card in one card state")
    void shouldThrowIfPlaysFirstCardInOneCardState() {
        OneCard sut = new OneCard(hand);
        assertThatThrownBy(() -> sut.playFirstCard(null, null)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should throw if accepts request or mao de onze in one card state")
    void shouldThrowIfAcceptsRequestOrMaoDeOnzeInOneCardState() {
        OneCard sut = new OneCard(hand);
        assertThatThrownBy(() -> sut.accept(null)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should throw if quits request or mao de onze in one card state")
    void shouldThrowIfQuitsRequestOrMaoDeOnzeInOneCardState() {
        OneCard sut = new OneCard(hand);
        assertThatThrownBy(() -> sut.quit(null)).isInstanceOf(IllegalStateException.class);
    }
}