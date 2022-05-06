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

package com.bueno.domain.usecases.game;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.usecases.game.model.CreateForBotsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlayWithBotsUseCaseTest {

    @Mock CreateGameUseCase createGameUseCase;
    @Mock FindGameUseCase findGameUseCase;
    @Mock GameRepository gameRepo;
    @Mock Intel intel;
    @Mock Game game;
    @InjectMocks PlayWithBotsUseCase sut;

    @Test
    @DisplayName("Should throw if any injected parameter is null")
    void shouldThrowIfAnyInjectedParameterIsNull() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> new PlayWithBotsUseCase(null, findGameUseCase, gameRepo)),
                () -> assertThrows(NullPointerException.class,
                        () -> new PlayWithBotsUseCase(createGameUseCase, null, gameRepo)),
                () -> assertThrows(NullPointerException.class,
                        () -> new PlayWithBotsUseCase(createGameUseCase, findGameUseCase, null))
        );
    }

    @Test
    @DisplayName("Should throw if play with bots receive any null parameters")
    void shouldThrowIfPlayWithBotsReceiveAnyNullParameters() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> sut.playWithBots(new CreateForBotsRequest(null, "BotA", UUID.randomUUID(), "BotB"))),
                () -> assertThrows(NullPointerException.class,
                        () -> sut.playWithBots(new CreateForBotsRequest(UUID.randomUUID(), null, UUID.randomUUID(), "BotB"))),
                () -> assertThrows(NullPointerException.class,
                        () -> sut.playWithBots(new CreateForBotsRequest(UUID.randomUUID(), "BotA", null, "BotB"))),
                () -> assertThrows(NullPointerException.class,
                        () -> sut.playWithBots(new CreateForBotsRequest(UUID.randomUUID(), "BotA", UUID.randomUUID(), null)))
        );
    }

    @Test
    @DisplayName("Should play with bots if preconditions are met")
    void shouldPlayWithBotsIfPreconditionsAreMet() {
        final var uuidA = UUID.randomUUID();
        final var uuidB = UUID.randomUUID();
        final var repo = new MockRepo();
        final var createGameUseCase = new CreateGameUseCase(repo, null);
        final var findGameUseCase = new FindGameUseCase(repo);
        final var requestModel = new CreateForBotsRequest(uuidA, "DummyBot", uuidB, "DummyBot");
        createGameUseCase.createForBots(requestModel);

        final var sut = new PlayWithBotsUseCase(createGameUseCase, findGameUseCase, repo);
        final var response = sut.playWithBots(requestModel);
        assertAll(
                () -> assertNotNull(response.getUuid()),
                () -> assertNotNull(response.getName())
        );
    }

    static class MockRepo implements GameRepository{
        private Game game;
        @Override public void save(Game game) {this.game = game;}
        @Override public Optional<Game> findByUuid(UUID uuid) {return Optional.ofNullable(game);}
        @Override public Optional<Game> findByUserUuid(UUID uuid) {return Optional.ofNullable(game);}
        @Override public Optional<Game> findByUserUsername(String userName) {return Optional.ofNullable(game);}
    }
}