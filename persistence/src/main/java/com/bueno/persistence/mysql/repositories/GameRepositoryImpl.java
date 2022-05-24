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

package com.bueno.persistence.mysql.repositories;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.persistence.mysql.dao.GameDao;
import com.bueno.persistence.mysql.dto.GameDto;

import java.util.Optional;
import java.util.UUID;

public class GameRepositoryImpl implements GameRepository {

    private final GameDao dao;

    public GameRepositoryImpl(GameDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(Game game) {
        dao.save(GameDto.of(game));
    }

    @Override
    public Optional<Game> findByUuid(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> findByUserUuid(UUID uuid) {
        return Optional.empty();
    }
}
