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

package com.bueno.domain.usecases.game;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.utils.UnsupportedGameRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateGameUseCaseTest {

    @Mock private Player p1;
    @Mock private Player p2;

    @Mock private GameRepository repo;

    private CreateGameUseCase sut;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp(){
        sut = new CreateGameUseCase(repo, null);
    }

    @Test
    @DisplayName("Should create game with valid parameters")
    void shouldCreateGameWithValidParameters() {
        when(p1.getUsername()).thenReturn("p1");
        when(p2.getUsername()).thenReturn("p2");
        Intel intel = sut.create(p1, p2);
        assertAll(
                () -> assertEquals(p1.getUsername(), intel.currentPlayerUsername()),
                () -> assertEquals(p2.getUsername(), intel.currentOpponentUsername()),
                () -> assertNotNull(intel.timestamp())
        );
    }

    @Test
    @DisplayName("Should throw if any player parameter is null")
    @SuppressWarnings("ConstantConditions")
    void shouldThrowIfAnyPlayerParameterIsNull() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> sut.create(null, p2)),
                () -> assertThrows(NullPointerException.class, () -> sut.create(p1, null))
        );
    }

    @Test
    @DisplayName("Should not create new game if one of the players is enrolled in another game")
    void shouldNotCreateNewGameIfOneOfThePlayersEnrolledInAnotherGame() {
        final Player bot1 = Player.ofBot(UUID.randomUUID(), "MineiroBot");
        final Player bot2 = Player.ofBot(UUID.randomUUID(), "MineiroBot");
        when(repo.findByPlayerUsername(any())).thenReturn(Optional.of(new Game(bot1, bot2)));
        assertThrows(UnsupportedGameRequestException.class, () -> sut.create(p1, p2));
    }
}