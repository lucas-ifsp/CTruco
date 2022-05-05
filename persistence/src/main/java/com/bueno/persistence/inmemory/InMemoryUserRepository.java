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

package com.bueno.persistence.inmemory;

import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.model.ApplicationUserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, ApplicationUserDTO> db = new HashMap<>();

    @Override
    public void save(ApplicationUserDTO user) {
        db.put(user.username(), user);
    }

    @Override
    public Optional<ApplicationUserDTO> findByUsername(String username) {
        return Optional.ofNullable(db.get(username));
    }

    @Override
    public Optional<ApplicationUserDTO> findByEmail(String email) {
        return db.values().stream().filter(user -> user.email().equals(email)).findAny();
    }

    @Override
    public Optional<ApplicationUserDTO> findByUuid(UUID uuid) {
        return db.values().stream().filter(user -> user.uuid().equals(uuid)).findAny();
    }
}
