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

package com.bueno.domain.usecases.game;

import com.bueno.domain.usecases.game.repos.ActiveGameRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class RemoveGameUseCase {
    private final ActiveGameRepository repo;

    public RemoveGameUseCase(ActiveGameRepository repo) {
        this.repo = repo;
    }

    public void byUserUuid(UUID userUuid) {
        final UUID uuid = Objects.requireNonNull(userUuid, "User UUID must not be null.");
        repo.findByUserUuid(Objects.requireNonNull(uuid)).ifPresentOrElse(
                        game -> repo.delete(game.getUuid()),
                        () -> {throw new NoSuchElementException("The is no active game for user UUID: " + userUuid);});
    }
}
