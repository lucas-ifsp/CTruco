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
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.dtos.CreateForBotsDto;
import com.bueno.domain.usecases.game.dtos.CreateForUserAndBotDto;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.usecase.CreateGameUseCase;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.IllegalGameEnrolmentException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateGameUseCaseTest {

    @Mock
    private GameRepository gameRepo;
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private CreateGameUseCase sut;

    final UUID userUUID = UUID.randomUUID();

    @Test
    @DisplayName("Should create game with valid user and bot")
    void shouldCreateGameWithValidUserAndBot() {
        final var user = new ApplicationUserDto(userUUID, "User", null, "email@email.com");
        when(userRepo.findByUuid(user.uuid())).thenReturn(Optional.of(user));
        final var requestModel = new CreateForUserAndBotDto(user.uuid(), "DummyBot");
        assertThat(sut.createForUserAndBot(requestModel)).isNotNull();
        verify(gameRepo, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw if user UUID is not registered in database")
    void shouldThrowIfUserUuidIsNotRegisteredInDatabase() {
        final var user = new ApplicationUserDto(userUUID, "User", null, "email@email.com");
        when(userRepo.findByUuid(user.uuid())).thenReturn(Optional.empty());
        final var requestModel = new CreateForUserAndBotDto(user.uuid(), "DummyBot");
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> sut.createForUserAndBot(requestModel));
    }

    @Test
    @DisplayName("Should throw if first user is already playing another game")
    void shouldThrowIfFirstUserIsAlreadyPlayingAnotherGame() {
        final var user = new ApplicationUserDto(userUUID, "User", null, "email@email.com");
        final Game game = new Game(Player.of(user.uuid(), user.username()), Player.ofBot("DummyBot"));
        when(userRepo.findByUuid(user.uuid())).thenReturn(Optional.of(user));
        when(gameRepo.findByPlayerUuid(user.uuid())).thenReturn(Optional.of(GameConverter.toDto(game)));
        final var requestModel = new CreateForUserAndBotDto(user.uuid(), "DummyBot");
        assertThatExceptionOfType(IllegalGameEnrolmentException.class)
                .isThrownBy(() -> sut.createForUserAndBot(requestModel));
        verify(gameRepo, times(1)).findByPlayerUuid(user.uuid());
    }

    @Test
    @DisplayName("Should throw if bot name is not a valid bot service implementation name")
    void shouldThrowIfBotNameIsNotAValidBotServiceImplementationName() {
        final var requestModel = new CreateForUserAndBotDto(userUUID, "NoBot");
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> sut.createForUserAndBot(requestModel));
    }
    @Disabled
    @Test
    @DisplayName("Should create game with valid bots")
    void shouldCreateGameWithValidBots() {
        final CreateForBotsDto requestModel = new CreateForBotsDto(
                UUID.randomUUID(),
                "DummyBot",
                UUID.randomUUID(),
                "DummyBot");
//        assertThat(sut.createForBots(requestModel)).isNotNull();
        verify(gameRepo, times(1)).save(any());
    }
    @Disabled
    @Test
    @DisplayName("Should throw if any of bot names is an unavailable bot service implementation")
    void shouldThrowIfAnyOfBotNamesIsAnUnavailableBotServiceImplementation() {
        final UUID uuid = UUID.randomUUID();
        SoftAssertions softly = new SoftAssertions();
//        softly.assertThatThrownBy(() ->
//                sut.createForBots(new CreateForBotsDto(uuid, "DummyBot", uuid,"NoBot")))
//                .isInstanceOf(NoSuchElementException.class);
//        softly.assertThatThrownBy(() ->
//                sut.createForBots(new CreateForBotsDto(uuid, "NoBot", uuid,"DummyBot")))
//                .isInstanceOf(NoSuchElementException.class);
//        softly.assertAll();
    }
}