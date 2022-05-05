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

package com.bueno.domain.entities.player;

import java.util.Objects;
import java.util.UUID;

public class User {
    private final UUID uuid;
    private final String username;
    private final String password;
    private final String email;

    public User(String username, String email, String password) {
        this(null, username, email, password);
    }

    public User(UUID uuid, String username, String email, String password) {
        this.uuid = uuid == null? UUID.randomUUID() : uuid;
        this.username = Objects.requireNonNull(username, "Username must not be null.");
        this.password = Objects.requireNonNull(password, "Password must not be null.");
        this.email = Objects.requireNonNull(email, "E-mail must not be null.");
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("User = %s (%s), %s", username, uuid, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, uuid);
    }
}
