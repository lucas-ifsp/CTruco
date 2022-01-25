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

import com.bueno.domain.entities.player.util.Player;

import java.util.Objects;
import java.util.UUID;

public class CreatePlayerUseCase {
    private final PlayerRepository repository;

    public CreatePlayerUseCase(PlayerRepository repository) {
        this.repository = repository;
    }

    public UUID create(String username){
        Objects.requireNonNull(username);

        if(username.isEmpty()) throw new IllegalArgumentException("Username must no be null");

        repository.findByUsername(username).ifPresent(unused -> {
            throw new EntityAlreadyExistsException("This username is already in use.");});

        Player player = new Player(username);

        repository.save(player);
        return player.getUuid();
    }
}
