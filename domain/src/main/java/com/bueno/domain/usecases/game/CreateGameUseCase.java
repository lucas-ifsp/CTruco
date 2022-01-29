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
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.BotFactory;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.utils.EntityNotFoundException;
import com.bueno.domain.usecases.player.UserRepository;
import com.bueno.domain.usecases.utils.UnsupportedGameRequestException;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class CreateGameUseCase {

    private GameRepository gameRepo;
    private UserRepository userRepo;
    private final static Logger LOGGER = Logger.getLogger(CreateGameUseCase.class.getName());

    public CreateGameUseCase(GameRepository gameRepo, UserRepository userRepo) {
        this.gameRepo = Objects.requireNonNull(gameRepo);
        this.userRepo = userRepo;
    }

    public Intel create(UUID user1UUID, UUID user2UUID){
        Objects.requireNonNull(user1UUID);
        Objects.requireNonNull(user2UUID);

        final Player user1 = userRepo.findByUUID(user1UUID)
                .orElseThrow(() -> new EntityNotFoundException("User not found:" + user1UUID));
        final Player user2 = userRepo.findByUUID(user2UUID)
                .orElseThrow(() -> new EntityNotFoundException("User not found:" + user2UUID));

        return create(user1, user2);
    }

    public Intel create(UUID userUUID, String botName){
        Objects.requireNonNull(userUUID);
        Objects.requireNonNull(botName);

        final Player user = userRepo.findByUUID(userUUID)
                .orElseThrow(() -> new EntityNotFoundException("User not found:" + userUUID));

        final Player bot = BotFactory.create(botName, gameRepo);

        return create(user, bot);
    }

    Intel create(Player p1, Player p2) {
        gameRepo.findByPlayerUsername(p1.getUsername()).ifPresent(unused -> {
            throw new UnsupportedGameRequestException(p1.getUsername() + " is already playing a game.");});

        gameRepo.findByPlayerUsername(p2.getUsername()).ifPresent(unused -> {
            throw new UnsupportedGameRequestException(p2.getUsername() + " is already playing a game.");});

        Game game = new Game(p1, p2);
        gameRepo.save(game);

        LOGGER.info("Game has been created with UUID: " + game.getUuid());

        return game.getIntel();
    }
}
