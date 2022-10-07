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

package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.game.dtos.GameDto;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.persistence.dao.GameDao;
import com.bueno.persistence.dto.GameEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final GameDao dao;

    public GameRepositoryImpl(GameDao dao) {
        this.dao = dao;
    }

    @Override
    public void save(GameDto dto) {
        dao.save(GameEntity.from(dto));
    }

    @Override
    public Optional<GameDto> findByUuid(UUID gameUuid) {
        return dao.findById(gameUuid);
    }
}
