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

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@ExtendWith(MockitoExtension.class)
class WaitingMaoDeOnzeTest {

    @Mock Hand hand;

    @Test
    @DisplayName("Should throw if plays first card in waiting mao de onze state")
    void shouldThrowIfPlaysFirstCardInWaitingMaoDeOnzeState() {
        WaitingMaoDeOnze sut = new WaitingMaoDeOnze(hand);
        assertThatIllegalStateException().isThrownBy(() -> sut.playFirstCard(null, null));
    }

    @Test
    @DisplayName("Should throw if plays second card in waiting mao de onze state")
    void shouldThrowIfPlaysSecondCardInWaitingMaoDeOnzeState() {
        WaitingMaoDeOnze sut = new WaitingMaoDeOnze(hand);
        assertThatIllegalStateException().isThrownBy(() -> sut.playSecondCard(null, null));
    }

    @Test
    @DisplayName("Should throw if raises in waiting mao de onze state")
    void shouldThrowIfRaisesInWaitingMaoDeOnzeState() {
        WaitingMaoDeOnze sut = new WaitingMaoDeOnze(hand);
        assertThatIllegalStateException().isThrownBy(() -> sut.raise(null));
    }
}