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
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class FindUserUseCase {

    private final UserRepository repo;

    public FindUserUseCase(UserRepository repo) {
        this.repo = Objects.requireNonNull(repo, "User repository must not be null.");
    }

    public ApplicationUserDto findByUUID(UUID uuid){
        return repo.findByUuid(Objects.requireNonNull(uuid))
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    public ApplicationUserDto findByUsername(String username){
        return repo.findByUsername(Objects.requireNonNull(username))
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }
}
