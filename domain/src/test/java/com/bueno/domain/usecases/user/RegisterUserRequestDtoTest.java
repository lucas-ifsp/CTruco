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

import com.bueno.domain.usecases.user.dtos.RegisterUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RegisterUserRequestDtoTest {

    @Test
    @DisplayName("Should not throw if parameters are valid")
    void shouldNotThrowIfParametersAreValid() {
        assertThatNoException().isThrownBy(
                () -> new RegisterUserRequestDto("username", "password", "email@email.com"));
    }

    @Test
    @DisplayName("Should throw if username is null")
    void shouldThrowIfUsernameIsNull() {
        assertThatNullPointerException().isThrownBy(
                () -> new RegisterUserRequestDto(null, "password", "email@email.com"));
    }

    @Test
    @DisplayName("Should throw if username is empty")
    void shouldThrowIfUsernameIsEmpty() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new RegisterUserRequestDto("", "password", "email@email.com"));
    }

    @Test
    @DisplayName("Should throw if password is null")
    void shouldThrowIfPasswordIsNull() {
        assertThatNullPointerException().isThrownBy(
                () -> new RegisterUserRequestDto("username", null, "email@email.com"));
    }

    @Test
    @DisplayName("Should throw if password is empty")
    void shouldThrowIfPasswordIsEmpty() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new RegisterUserRequestDto("username", "", "email@email.com"));
    }

    @Test
    @DisplayName("Should throw if user email is null")
    void shouldThrowIfUserEmailIsNull() {
        assertThatNullPointerException().isThrownBy(
                () -> new RegisterUserRequestDto("username", "password", null));
    }

    @Test
    @DisplayName("Should throw if email is empty")
    void shouldThrowIfEmailIsEmpty() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new RegisterUserRequestDto("username", "password", ""));
    }
}