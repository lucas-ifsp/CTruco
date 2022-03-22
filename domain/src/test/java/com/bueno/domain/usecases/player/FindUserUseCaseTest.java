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

package com.bueno.domain.usecases.player;

import com.bueno.domain.entities.player.User;
import com.bueno.domain.usecases.player.FindUserUseCase.ResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseTest {

    @Mock UserRepository repo;
    @InjectMocks FindUserUseCase sut;

    @Test
    @DisplayName("Should throw if injected repository is null")
    void shouldThrowIfInjectedRepositoryIsNull() {
        assertThrows(NullPointerException.class, () -> new FindUserUseCase(null));
    }

    @Test
    @DisplayName("Should retrieve user by uuid if available in the repository")
    void shouldRetrieveUserByUuidIfAvailableInTheRepository() {
        final UUID uuid = UUID.randomUUID();
        final User user = new User(uuid, "name", "email@email.com");
        when(repo.findByUuid(uuid)).thenReturn(Optional.of(user));
        final ResponseModel model = sut.findByUUID(uuid);
        assertAll(
                () -> assertEquals(user.getUuid(), model.uuid()),
                () -> assertEquals(user.getUsername(), model.username()),
                () -> assertEquals(user.getEmail(), model.email())
        );
    }

    @Test
    @DisplayName("Should retrieve user by username if available in the repository")
    void shouldRetrieveUserByUsernameIfAvailableInTheRepository() {
        final UUID uuid = UUID.randomUUID();
        final String email = "email@email.com";
        final User user = new User(uuid, "name", email);
        when(repo.findByUsername(email)).thenReturn(Optional.of(user));
        final ResponseModel model = sut.findByUsername(email);
        assertAll(
                () -> assertEquals(user.getUuid(), model.uuid()),
                () -> assertEquals(user.getUsername(), model.username()),
                () -> assertEquals(user.getEmail(), model.email())
        );
    }




}