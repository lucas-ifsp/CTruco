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
import com.bueno.domain.usecases.game.dtos.CreateForBotsDto;
import com.bueno.domain.usecases.game.repos.ActiveGameRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PlayWithBotsUseCaseTest {

    @Mock CreateGameUseCase createGameUseCase;
    @Mock FindGameUseCase findGameUseCase;
    @InjectMocks PlayWithBotsUseCase sut;

    @Test
    @DisplayName("Should throw if any injected parameter is null")
    void shouldThrowIfAnyInjectedParameterIsNull() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> new PlayWithBotsUseCase(null, findGameUseCase))
                .isInstanceOf(NullPointerException.class);
        softly.assertThatThrownBy(() -> new PlayWithBotsUseCase(createGameUseCase, null))
                .isInstanceOf(NullPointerException.class);
        softly.assertAll();
    }

    @Test
    @DisplayName("Should throw if play with bots receive any null parameters")
    void shouldThrowIfPlayWithBotsReceiveAnyNullParameters() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> sut.playWithBots(new CreateForBotsDto(null, "BotA", UUID.randomUUID(), "BotB")))
                .isInstanceOf(NullPointerException.class);

        softly.assertThatThrownBy(() -> sut.playWithBots(new CreateForBotsDto(UUID.randomUUID(), null, UUID.randomUUID(), "BotB")))
                .isInstanceOf(NullPointerException.class);

        softly.assertThatThrownBy(() -> sut.playWithBots(new CreateForBotsDto(UUID.randomUUID(), "BotA", null, "BotB")))
                .isInstanceOf(NullPointerException.class);

        softly.assertThatThrownBy(() -> sut.playWithBots(new CreateForBotsDto(UUID.randomUUID(), "BotA", UUID.randomUUID(), null)))
                .isInstanceOf(NullPointerException.class);

        softly.assertAll();
    }

    @Test
    @DisplayName("Should play with bots if preconditions are met")
    void shouldPlayWithBotsIfPreconditionsAreMet() {
        final var uuidA = UUID.randomUUID();
        final var uuidB = UUID.randomUUID();
        final var repo = new MockRepo();
        final var createGameUseCase = new CreateGameUseCase(repo, null, null);
        final var findGameUseCase = new FindGameUseCase(repo, null);
        final var requestModel = new CreateForBotsDto(uuidA, "DummyBot", uuidB, "DummyBot");
        createGameUseCase.createForBots(requestModel);

        final var sut = new PlayWithBotsUseCase(createGameUseCase, findGameUseCase);
        final var response = sut.playWithBots(requestModel);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.uuid()).isNotNull();
        softly.assertThat(response.name()).isNotNull();
        softly.assertAll();
    }

    static class MockRepo implements ActiveGameRepository {
        private Game game;
        @Override public void create(Game game) {this.game = game;}
        @Override
        public void delete(UUID uuid) {
        }
        @Override public Optional<Game> findByUuid(UUID uuid) {return Optional.ofNullable(game);}
        @Override public Optional<Game> findByUserUuid(UUID uuid) {return Optional.ofNullable(game);}
    }
}