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
import com.bueno.domain.entities.player.util.Player;

import java.util.Objects;
import java.util.logging.Logger;

public class CreateGameUseCase {

    private GameRepository dao;
    private final static Logger LOGGER = Logger.getLogger(CreateGameUseCase.class.getName());

    public CreateGameUseCase(GameRepository dao) {
        this.dao = dao;
    }

    public Game create(Player p1, Player p2) {
        Objects.requireNonNull(p1);
        Objects.requireNonNull(p2);

        dao.findByPlayerUsername(p1.getUsername()).ifPresent(unused -> {
            throw new UnsupportedGameRequestException(p1.getUsername() + " is already playing a game.");});

        dao.findByPlayerUsername(p2.getUsername()).ifPresent(unused -> {
            throw new UnsupportedGameRequestException(p2.getUsername() + " is already playing a game.");});

        Game game = new Game(p1, p2);
        dao.save(game);

        LOGGER.info("Game has been created with UUID: " + game.getUuid());

        return game;
    }
}
