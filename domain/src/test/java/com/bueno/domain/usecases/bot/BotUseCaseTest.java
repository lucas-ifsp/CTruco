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
import com.bueno.domain.entities.hand.HandPoints;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private UUID playerUUID;
    private BotUseCase sut;

    @BeforeEach
    void setUp() {
        playerUUID = UUID.randomUUID();
        lenient().when(game.currentHand()).thenReturn(hand);
        lenient().when(game.getIntel()).thenReturn(intel);
        lenient().when(hand.getCurrentPlayer()).thenReturn(player);
        lenient().when(player.getUuid()).thenReturn(playerUUID);
        sut = new BotUseCase(repo);

    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should not accept null repo")
    void shouldNotAcceptNullRepo() {
        assertThrows(NullPointerException.class, () -> new BotUseCase(null));
    }

    @Test
    @DisplayName("Should do nothing with player is not a bot")
    void shouldDoNothingWithPlayerIsNotABot() {
        when(player.isBot()).thenReturn(false);
        assertEquals(intel, sut.playWhenNecessary(game));
        verify(player, times(1)).isBot();
    }

    @Test
    @DisplayName("Should do nothing if it is no player turn")
    void shouldDoNothingIfItIsNoPlayerTurn() {
        when(player.isBot()).thenReturn(true);
        when(intel.currentPlayerUuid()).thenReturn(Optional.empty());
        assertEquals(intel, sut.playWhenNecessary(game));
        verify(intel, times(1)).currentPlayerUuid();
    }

    @Test
    @DisplayName("Do nothing if the game is done")
    void doNothingIfTheGameIsDone() {
        when(player.isBot()).thenReturn(true);
        when(intel.currentPlayerUuid()).thenReturn(Optional.of(playerUUID));
        when(intel.isGameDone()).thenReturn(true);
        assertEquals(intel, sut.playWhenNecessary(game));
        verify(intel, times(1)).isGameDone();
    }

    @Test
    @DisplayName("Should decide mao de onze if is mao de onze and hand points is ONE")
    void shouldDecideMaoDeOnzeIfIsMaoDeOnzeAndHandPointsIsOne() {
        when(player.isBot()).thenReturn(true);
        when(intel.currentPlayerUuid()).thenReturn(Optional.of(playerUUID));
        when(intel.isGameDone()).thenReturn(false);
        when(intel.isMaoDeOnze()).thenReturn(true);
        when(intel.handPoints()).thenReturn(HandPoints.ONE.get());
        assertEquals(intel, sut.playWhenNecessary(game));
        verify(intel, times(1)).isMaoDeOnze();
        verify(intel, times(1)).handPoints();
    }



}