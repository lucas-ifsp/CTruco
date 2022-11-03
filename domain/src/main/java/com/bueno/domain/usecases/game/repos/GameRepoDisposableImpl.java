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

package com.bueno.domain.usecases.game.repos;

import com.bueno.domain.usecases.game.dtos.GameDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class GameRepoDisposableImpl implements GameRepository {

    private GameDto game;

    @Override
    public void save(GameDto dto) {
        game = dto;
    }

    @Override
    public void update(GameDto dto) {
        game = dto;
    }

    @Override
    public void delete(UUID uuid) {
        game = null;
    }

    @Override
    public Optional<GameDto> findByPlayerUuid(UUID playerUuid) {
        return Optional.ofNullable(game);
    }

    @Override
    public Collection<GameDto> findAllInactiveAfter(int minutes) {
        return null;
    }
}
