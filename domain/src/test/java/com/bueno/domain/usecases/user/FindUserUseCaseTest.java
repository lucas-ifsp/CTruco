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

package com.bueno.domain.usecases.user;

import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseTest {

    @Mock UserRepository repo;
    @InjectMocks FindUserUseCase sut;

    @Test
    @DisplayName("Should throw if injected repository is null")
    void shouldThrowIfInjectedRepositoryIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new FindUserUseCase(null));
    }

    @Test
    @DisplayName("Should retrieve user by uuid if available in the repository")
    void shouldRetrieveUserByUuidIfAvailableInTheRepository() {
        final UUID uuid = UUID.randomUUID();
        final var user = new ApplicationUserDto(uuid, "name", "email@email.com", "password");
        when(repo.findByUuid(uuid)).thenReturn(Optional.of(user));
        final ApplicationUserDto model = sut.findByUUID(uuid);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(model.uuid()).isEqualTo(user.uuid());
        softly.assertThat(model.username()).isEqualTo(user.username());
        softly.assertThat(model.email()).isEqualTo(user.email());
        softly.assertAll();
    }

    @Test
    @DisplayName("Should retrieve user by username if available in the repository")
    void shouldRetrieveUserByUsernameIfAvailableInTheRepository() {
        final var uuid = UUID.randomUUID();
        final var email = "email@email.com";
        final var user = new ApplicationUserDto(uuid, "name", "password", email);
        when(repo.findByUsername(email)).thenReturn(Optional.of(user));
        final ApplicationUserDto model = sut.findByUsername(email);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(model.uuid()).isEqualTo(user.uuid());
        softly.assertThat(model.username()).isEqualTo(user.username());
        softly.assertThat(model.email()).isEqualTo(user.email());
        softly.assertAll();
    }
}