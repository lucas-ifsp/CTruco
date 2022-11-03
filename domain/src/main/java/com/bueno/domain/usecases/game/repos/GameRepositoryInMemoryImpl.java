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
import com.bueno.domain.usecases.game.dtos.PlayerDto;

import java.util.*;
import java.util.function.Predicate;

public class GameRepositoryInMemoryImpl implements GameRepository {

    private static final Map<UUID, GameDto> games = new HashMap<>();

    @Override
    public void save(GameDto game) {
        games.put(game.gameUuid(), game);
    }

    @Override
    public void update(GameDto gameDto) {
        games.replace(gameDto.gameUuid(), gameDto);
    }

    @Override
    public void delete(UUID uuid) {
        games.remove(uuid);
    }

    @Override
    public Optional<GameDto> findByPlayerUuid(UUID uuid) {
        Predicate<GameDto> hasPlayer = game -> hasUuid(game.player1(), uuid) || hasUuid(game.player2(), uuid);
        return games.values().stream().filter(hasPlayer).findAny();
    }

    @Override
    public Collection<GameDto> findAllInactiveAfter(int minutes) {
        return null;
    }

    private static boolean hasUuid(PlayerDto player, UUID uuid) {
        return player.uuid().equals(uuid);
    }
}
