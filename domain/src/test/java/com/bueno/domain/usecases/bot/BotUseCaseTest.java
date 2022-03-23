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

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotUseCaseTest {

    @Mock private Game game;
    @Mock private Intel intel;
    @Mock private Hand hand;
    @Mock private Player player;
    @Mock private GameRepository repo;

    @Mock private MaoDeOnzeHandler maoDeOnzeHandler;
    @Mock private CardPlayingHandler cardPlayingHandler;
    @Mock private RaiseHandler raiseHandler;
    @Mock private RaiseRequestHandler raiseRequestHandler;

    @InjectMocks
    private BotUseCase sut;

    @BeforeEach
    void setUp() {
        UUID playerUUID = UUID.randomUUID();
        lenient().when(game.currentHand()).thenReturn(hand);
        lenient().when(game.getIntel()).thenReturn(intel);
        lenient().when(hand.getCurrentPlayer()).thenReturn(player);
        lenient().when(intel.isGameDone()).thenReturn(false);
        lenient().when(intel.currentPlayerUuid()).thenReturn(Optional.of(playerUUID));
        lenient().when(player.getUuid()).thenReturn(playerUUID);
        lenient().when(player.isBot()).thenReturn(true);
        lenient().when(player.getUsername()).thenReturn("DummyBot");
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should not accept null repo")
    void shouldNotAcceptNullRepo() {
        repo = null;
        assertThrows(NullPointerException.class, () -> new BotUseCase(repo));
    }

    @Test
    @DisplayName("Should do nothing if there is no current player")
    void shouldDoNothingIfThereIsNoCurrentPlayer() {
        when(intel.currentPlayerUuid()).thenReturn(Optional.empty());
        assertEquals(intel, sut.playWhenNecessary(game));
        verify(intel, times(1)).currentPlayerUuid();
    }

    @Test
    @DisplayName("Should do nothing if the game is done")
    void shouldDoNothingIfTheGameIsDone() {
        when(intel.isGameDone()).thenReturn(true);
        assertEquals(intel, sut.playWhenNecessary(game));
        verify(intel, times(1)).isGameDone();
    }

    @Test
    @DisplayName("Should do nothing with player is not a bot")
    void shouldDoNothingWithPlayerIsNotABot() {
        when(player.isBot()).thenReturn(false);
        assertEquals(intel, sut.playWhenNecessary(game));
        verify(player, times(1)).isBot();
    }

    @Test
    @DisplayName("Should not throw if preconditions are met")
    void shouldNotThrowIfPreconditionsAreMet() {
        assertEquals(game.getIntel(), sut.playWhenNecessary(game));
    }

    @Test
    @DisplayName("Should first handle mao de onze")
    void shouldFirstHandleMaoDeOnze() {
        when(maoDeOnzeHandler.handle(intel, player)).thenReturn(true);
        assertEquals(game.getIntel(), sut.playWhenNecessary(game));
        verify(maoDeOnzeHandler, times(1)).handle(intel, player);
        verify(raiseHandler, times(0)).handle(intel, player);
    }

    @Test
    @DisplayName("Should handle raise decision before choosing card")
    void shouldHandleRaiseDecisionBeforeChoosingCard() {
        when(maoDeOnzeHandler.handle(intel, player)).thenReturn(false);
        when(raiseHandler.handle(intel, player)).thenReturn(true);
        assertEquals(game.getIntel(), sut.playWhenNecessary(game));
        verify(raiseHandler, times(1)).handle(intel, player);
        verify(cardPlayingHandler, times(0)).handle(intel, player);
    }

    @Test
    @DisplayName("Should handle card playing if already decided to raise of not")
    void shouldHandleCardPlayingIfAlreadyDecidedToRaiseOfNot() {
        when(raiseHandler.handle(intel, player)).thenReturn(false);
        when(cardPlayingHandler.handle(intel, player)).thenReturn(true);
        assertEquals(game.getIntel(), sut.playWhenNecessary(game));
        verify(cardPlayingHandler, times(1)).handle(intel, player);
        verify(raiseRequestHandler, times(0)).handle(intel, player);
    }

    @Test
    @DisplayName("Should handle if is bot turn just because it must decide about raise request")
    void shouldHandleIfIsBotTurnJustBecauseItMustDecideAboutRaiseRequest() {
        assertEquals(game.getIntel(), sut.playWhenNecessary(game));
        verify(raiseRequestHandler, times(1)).handle(intel, player);
    }

    @Test
    @DisplayName("Should create default handlers if they are not injected in constructor")
    void shouldCreateDefaultHandlersIfTheyAreNotInjectedInConstructor() {
        sut = new BotUseCase(repo, null, null, null, null);
        assertDoesNotThrow(() -> sut.playWhenNecessary(game));
    }

    @Test
    @DisplayName("Should have at least one default bot implementation of bot spi")
    void shouldHaveAtLeastOneDefaultBotImplementationOfBotSpi() {
        assertFalse(BotUseCase.availableBots().isEmpty());
    }
}