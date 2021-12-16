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

package com.bueno.application.standalone;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.GameRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class InMemoryGameRepository implements GameRepository {

    private Map<UUID, Game> games = new HashMap();

    @Override
    public void save(Game game) {
        games.put(game.getUuid(), game);
    }

    @Override
    public Optional findByUuid(UUID key) {
        return Optional.ofNullable(games.get(key));
    }

    @Override
    public Optional<Game> findByUserUuid(UUID uuid) {
        Predicate<Game> hasPlayerWithUuid = game ->
                hasUuid(game.getPlayer1(), uuid) || hasUuid(game.getPlayer2(), uuid);
        return games.values().stream().filter(hasPlayerWithUuid).findAny();
    }

    private static boolean hasUuid(Player player, UUID uuid) {
        return player.getUuid().equals(uuid);
    }


    @Override
    public Optional<Game> findByPlayerUsername(String username) {
        Predicate<Game> hasPlayerWithUsername = game ->
                hasUsername(game.getPlayer1(), username) || hasUsername(game.getPlayer2(), username);
        return games.values().stream().filter(hasPlayerWithUsername).findAny();
    }

    private static boolean hasUsername(Player player, String username) {
        return player.getUsername().equals(username);
    }
}
