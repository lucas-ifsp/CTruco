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
import com.bueno.domain.entities.player.User;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.UnsupportedGameRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateGameUseCaseTest {

    @Mock private User user;

    @Mock private GameRepository gameRepo;
    @Mock private UserRepository userRepo;

    @InjectMocks
    private CreateGameUseCase sut;

    final UUID userUUID = UUID.randomUUID();

    @Test
    @DisplayName("Should create game with valid user and bot")
    void shouldCreateGameWithValidUserAndBot() {
        when(user.getUsername()).thenReturn("User");
        when(user.getUuid()).thenReturn(userUUID);
        when(userRepo.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
        final var requestModel = new CreateForUserAndBotRequestModel(user.getUuid(), "DummyBot");
        assertNotNull(sut.createForUserAndBot(requestModel));
        verify(gameRepo, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw if user UUID is not registered in database")
    void shouldThrowIfUserUuidIsNotRegisteredInDatabase() {
        when(user.getUuid()).thenReturn(userUUID);
        when(userRepo.findByUuid(user.getUuid())).thenReturn(Optional.empty());
        final var requestModel = new CreateForUserAndBotRequestModel(user.getUuid(), "DummyBot");
        assertThrows(EntityNotFoundException.class, () -> sut.createForUserAndBot(requestModel));
    }

    @Test
    @DisplayName("Should throw if first user is already playing another game")
    void shouldThrowIfFirstUserIsAlreadyPlayingAnotherGame() {
        final Game game = new Game(Player.of(user), Player.ofBot("DummyBot"));
        when(user.getUsername()).thenReturn("User");
        when(user.getUuid()).thenReturn(userUUID);
        when(userRepo.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
        when(gameRepo.findByUserUuid(user.getUuid())).thenReturn(Optional.of(game));
        final var requestModel = new CreateForUserAndBotRequestModel(user.getUuid(), "DummyBot");
        assertThrows(UnsupportedGameRequestException.class, () -> sut.createForUserAndBot(requestModel));
        verify(gameRepo, times(1)).findByUserUuid(user.getUuid());
    }

    @Test
    @DisplayName("Should throw if bot name is not a valid bot service implementation name")
    void shouldThrowIfBotNameIsNotAValidBotServiceImplementationName() {
        when(user.getUuid()).thenReturn(userUUID);
        final var requestModel = new CreateForUserAndBotRequestModel(user.getUuid(), "NoBot");
        assertThrows(NoSuchElementException.class, () -> sut.createForUserAndBot(requestModel));
    }

    @Test
    @DisplayName("Should create game with valid bots")
    void shouldCreateGameWithValidBots() {
        final CreateForBotsRequestModel requestModel = new CreateForBotsRequestModel(
                UUID.randomUUID(), "DummyBot", UUID.randomUUID(), "DummyBot");
        assertNotNull(sut.createForBots(requestModel));
        verify(gameRepo, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw if any of bot names is an unavailable bot service implementation")
    void shouldThrowIfAnyOfBotNamesIsAnUnavailableBotServiceImplementation() {
        final UUID uuid = UUID.randomUUID();
        assertAll(
                () ->  assertThrows(NoSuchElementException.class,
                        () -> sut.createForBots(new CreateForBotsRequestModel(uuid, "DummyBot", uuid,"NoBot"))),
                () ->  assertThrows(NoSuchElementException.class,
                        () -> sut.createForBots(new CreateForBotsRequestModel(uuid, "NoBot", uuid,"DummyBot")))
        );
    }
}