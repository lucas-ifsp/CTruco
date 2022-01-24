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
import com.bueno.domain.entities.player.util.Player;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadGameUseCaseTest {

    private LoadGameUseCase sut;
    private Game game;

    @Mock private Player player1;
    @Mock private Player player2;
    @Mock private GameRepository repo;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp(){
        game = new Game(player1, player2);
        sut = new LoadGameUseCase(repo);
    }

    @Test
    @DisplayName("Should load game by user UUID")
    void shouldLoadGameByUuid() {
        when(repo.findByUserUuid(any())).thenReturn(Optional.of(game));
        final UUID userUUID = UUID.randomUUID();
        assertEquals(game.getIntel(), sut.loadUserGame(userUUID).map(Game::getIntel).orElse(null));
    }

    @Test
    @DisplayName("Should throw if key is null")
    void shouldThrowIfKeyIsNull() {
        assertThrows(NullPointerException.class, () -> sut.load(null));
    }
}