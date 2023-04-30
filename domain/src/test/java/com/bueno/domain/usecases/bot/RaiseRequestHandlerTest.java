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
import com.bueno.domain.usecases.bot.handlers.RaiseRequestHandler;
import com.bueno.domain.usecases.hand.PointsProposalUseCase;
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

@ExtendWith({MockitoExtension.class})
class RaiseRequestHandlerTest {

    @Mock Player bot;
    @Mock Intel intel;
    @Mock BotServiceProvider botService;
    @Mock PointsProposalUseCase scoreUseCase;
    RaiseRequestHandler sut;

    @BeforeEach
    void setUp() {
        lenient().when(bot.getUuid()).thenReturn(UUID.randomUUID());
        sut = new RaiseRequestHandler(scoreUseCase, botService);
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should quit if bot service implementation is answering to quit and quit is allowed")
    void shouldQuitIfBotServiceImplementationIsAnsweringToQuitAndQuitIsAllowed() {
        when(intel.possibleActions()).thenReturn(Set.of("QUIT"));
        when(botService.getRaiseResponse(any())).thenReturn(-1);
        sut.handle(intel, bot);
        verify(scoreUseCase, times(1)).quit(bot.getUuid());
        verify(scoreUseCase, times(0)).accept(bot.getUuid());
        verify(scoreUseCase, times(0)).raise(bot.getUuid());
    }

    @Test
    @DisplayName("Should accept if bot service implementation is answering to accept and accept is allowed")
    void shouldAcceptIfBotServiceImplementationIsAnsweringToAcceptAndAcceptIsAllowed() {
        when(intel.possibleActions()).thenReturn(Set.of("ACCEPT"));
        when(botService.getRaiseResponse(any())).thenReturn(0);
        sut.handle(intel, bot);
        verify(scoreUseCase, times(0)).quit(bot.getUuid());
        verify(scoreUseCase, times(1)).accept(bot.getUuid());
        verify(scoreUseCase, times(0)).raise(bot.getUuid());
    }

    @Test
    @DisplayName("Should raise if bot service implementation is answering to raise and raise is allowed")
    void shouldRaiseIfBotServiceImplementationIsAnsweringToRaiseAndRaiseIsAllowed() {
        when(intel.possibleActions()).thenReturn(Set.of("RAISE"));
        when(botService.getRaiseResponse(any())).thenReturn(1);
        sut.handle(intel, bot);
        verify(scoreUseCase, times(0)).quit(bot.getUuid());
        verify(scoreUseCase, times(0)).accept(bot.getUuid());
        verify(scoreUseCase, times(1)).raise(bot.getUuid());
    }

    @Test
    @DisplayName("Should not raise if bot service implementation is answering to raise and raise is not allowed")
    void shouldNotRaiseIfBotServiceImplementationIsAnsweringToRaiseAndRaiseIsNotAllowed() {
        when(botService.getRaiseResponse(any())).thenReturn(1);
        sut.handle(intel, bot);
        verify(scoreUseCase, times(0)).raise(bot.getUuid());
    }

    @Test
    @DisplayName("Should not handle if should play")
    void shouldNotHandleIfShouldPlay() {
        when(intel.possibleActions()).thenReturn(Set.of("PLAY"));
        assertThat(sut.shouldHandle(intel)).isFalse();
    }

    @Test
    @DisplayName("Should not handle in mao de onze")
    void shouldNotHandleInMaoDeOnze() {
        when(intel.isMaoDeOnze()).thenReturn(true);
        assertThat(sut.shouldHandle(intel)).isFalse();
    }
}