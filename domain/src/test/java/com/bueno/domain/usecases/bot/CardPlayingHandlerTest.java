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

package com.bueno.domain.usecases.bot;

import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.bot.handlers.CardPlayingHandler;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardPlayingHandlerTest {

    @Mock Player bot;
    @Mock Intel intel;
    @Mock BotServiceProvider botService;
    @Mock PlayCardUseCase cardUseCase;
    CardPlayingHandler sut;

    @BeforeEach
    void setUp() {
        lenient().when(bot.getUuid()).thenReturn(UUID.randomUUID());
        sut = new CardPlayingHandler(cardUseCase, botService);
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should not handle if can not play")
    void shouldNotHandleIfCanNotPlay() {
        when(intel.possibleActions()).thenReturn(Set.of("RAISE"));
        assertThat(sut.shouldHandle(intel)).isFalse();
    }

    @Test
    @DisplayName("Should handle playing card and return true")
    void shouldHandlePlayingCardAndReturnTrue() {
        when(botService.chooseCard(any())).thenReturn(CardToPlay.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
        sut.handle(intel, bot);
        verify(cardUseCase, times(1)).playCard(any());
        verify(cardUseCase, times(0)).discard(any());
    }

    @Test
    @DisplayName("Should handle discard")
    void shouldHandleDiscard() {
        when(botService.chooseCard(any())).thenReturn(CardToPlay.discard(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)));
        sut.handle(intel, bot);
        verify(cardUseCase, times(0)).playCard(any());
        verify(cardUseCase, times(1)).discard(any());
    }
}