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

import com.bueno.domain.entities.player.util.User;
import com.bueno.domain.usecases.utils.EntityNotFoundException;

import java.util.Objects;
import java.util.UUID;

public class FindUserUseCase {

    private final UserRepository repo;

    public FindUserUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public ResponseModel findByUUID(UUID uuid){
        final User user = repo.findByUUID(Objects.requireNonNull(uuid))
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        return new ResponseModel(user.getUuid(), user.getUsername(), user.getEmail());
    }

    public record ResponseModel(UUID uuid, String username, String email){}
}
