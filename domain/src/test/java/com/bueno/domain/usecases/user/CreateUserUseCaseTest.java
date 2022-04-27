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

import com.bueno.domain.usecases.user.model.User;
import com.bueno.domain.usecases.user.model.UserRequestModel;
import com.bueno.domain.usecases.utils.exceptions.EntityAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock UserRepository repo;
    @InjectMocks CreateUserUseCase sut;

    @Test
    @DisplayName("Should throw if injected repository is null")
    void shouldThrowIfInjectedRepositoryIsNull() {
        assertThrows(NullPointerException.class, () -> new CreateUserUseCase(null));
    }

    @Test
    @DisplayName("Should throw if request model is null")
    void shouldThrowIfRequestModelIsNull() {
        assertThrows(NullPointerException.class, () -> sut.create(null));
    }

    @Test
    @DisplayName("Should throw if a user with same username is already registered in the database")
    void shouldThrowIfAUserWithSameUsernameIsAlreadyRegisteredInTheDatabase() {
        when(repo.findByUsername(any())).thenReturn(Optional.of(new User("name", "email")));
        var requestModel = new UserRequestModel("name", "email");
        assertThrows(EntityAlreadyExistsException.class, () -> sut.create(requestModel));
    }

    @Test
    @DisplayName("Should throw if a user with same email is already registered in the database")
    void shouldThrowIfAUserWithSameEmailIsAlreadyRegisteredInTheDatabase() {
        when(repo.findByEmail(any())).thenReturn(Optional.of(new User("name", "email")));
        var requestModel = new UserRequestModel("name", "email");
        assertThrows(EntityAlreadyExistsException.class, () -> sut.create(requestModel));
    }

    @Test
    @DisplayName("Should create user in database if preconditions are met")
    void shouldCreateUserInDatabaseIfPreconditionsAreMet() {
        var requestModel = new UserRequestModel("name", "email");
        assertNotNull(sut.create(requestModel));
        verify(repo, times(1)).save(any());
    }
}