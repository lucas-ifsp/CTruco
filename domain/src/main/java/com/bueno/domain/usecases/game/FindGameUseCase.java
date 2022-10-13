/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.usecases.game.repos.ActiveGameRepository;
import com.bueno.domain.usecases.game.repos.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FindGameUseCase {

    private final ActiveGameRepository repo;
    private final GameRepository newRepo;

    public FindGameUseCase(ActiveGameRepository repo, GameRepository newRepo) {
        this.repo = repo;
        this.newRepo = newRepo;
    }

    public Optional<Game> load(UUID uuid) {
        final UUID gameUuid = Objects.requireNonNull(uuid, "Game UUID must not be null.");
        newRepo.findByUuid(uuid).ifPresent(System.out::println);
        return repo.findByUuid(gameUuid);
    }

    public Optional<Game> findByUserUuid(UUID userUuid) {
        final UUID uuid = Objects.requireNonNull(userUuid, "User UUID must not be null.");
        newRepo.findByPlayerUuid(uuid).ifPresentOrElse(System.out::println, () -> System.out.println("Not found: " + uuid));
        return repo.findByUserUuid(Objects.requireNonNull(uuid));
    }
}
