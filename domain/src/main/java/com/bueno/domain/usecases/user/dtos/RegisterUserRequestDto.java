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

package com.bueno.domain.usecases.user.dtos;

import java.util.Objects;

public record RegisterUserRequestDto(String username, String password, String email) {
    public RegisterUserRequestDto(String username, String password, String email) {
        this.username = Objects.requireNonNull(username, "Username must not be null.");
        if(username.isEmpty()) throw new IllegalArgumentException("Username must not be empty.");
        this.password = Objects.requireNonNull(password, "Password must not be null.");
        if(password.isEmpty()) throw new IllegalArgumentException("Password must not be empty.");
        this.email = Objects.requireNonNull(email, "E-mail must not be null.");
        if(email.isEmpty()) throw new IllegalArgumentException("E-mail must not be empty.");
    }
}
