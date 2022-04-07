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
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.entities.player.User;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.utils.EntityNotFoundException;
import com.bueno.domain.usecases.utils.UnsupportedGameRequestException;
import com.bueno.spi.service.BotServiceManager;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

public class CreateGameUseCase {

    private final GameRepository gameRepo;
    private final UserRepository userRepo;

    public CreateGameUseCase(GameRepository gameRepo, UserRepository userRepo) {
        this.gameRepo = Objects.requireNonNull(gameRepo);
        this.userRepo = userRepo;
    }


    public Intel createWithUserAndBot(UserAndBotRequestModel requestModel){
        System.out.println("Hello!!!");
       Objects.requireNonNull(requestModel);
       return createWithUserAndBot(requestModel.userUUID, requestModel.botName);
    }

    //TODO Remove all calls to this method by the request method one.
    public Intel createWithUserAndBot(UUID userUUID, String botName){
        Objects.requireNonNull(userUUID, "User UUID must not be null!");
        Objects.requireNonNull(botName, "Bot name must not be null!");

        if(hasNoBotServiceWith(botName))
            throw new NoSuchElementException("Service implementation not available: " + botName);

        final User user = userRepo.findByUuid(userUUID)
                .orElseThrow(() -> new EntityNotFoundException("User not found:" + userUUID));

        final Player userPlayer = Player.of(user);
        final Player botPlayer = Player.ofBot(botName);

        gameRepo.findByUserUuid(userPlayer.getUuid()).ifPresent(unused -> {
            throw new UnsupportedGameRequestException(userPlayer.getUuid() + " is already playing a game.");});

        return create(userPlayer, botPlayer);
    }

    private boolean hasNoBotServiceWith(String botName) {
        return !BotServiceManager.providersNames().contains(botName);
    }

    public Intel createWithBots(UUID bot1Uuid, String bot1Name, UUID bot2Uuid, String bot2Name){
        Objects.requireNonNull(bot1Uuid, "Bot UUID must not be null!");
        Objects.requireNonNull(bot1Name, "Bot name must not be null!");
        Objects.requireNonNull(bot2Uuid, "Bot UUID must not be null!");
        Objects.requireNonNull(bot2Name, "Bot name must not be null!");

        if(hasNoBotServiceWith(bot1Name))
            throw new NoSuchElementException("Service implementation not available: " + bot1Name);

        if(hasNoBotServiceWith(bot2Name))
            throw new NoSuchElementException("Service implementation not available: " + bot2Name);

        final var bot1 = Player.ofBot(bot1Uuid, bot1Name);
        final var bot2 = Player.ofBot(bot2Uuid, bot2Name);

        return create(bot1, bot2);
    }

    private Intel create(Player p1, Player p2) {
        Game game = new Game(p1, p2);
        gameRepo.save(game);
        return game.getIntel();
    }

    public record UserAndBotRequestModel(UUID userUUID, String botName) {
    }
}
