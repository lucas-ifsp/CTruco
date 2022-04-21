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
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.entities.player.User;
import com.bueno.domain.usecases.utils.dtos.IntelDto;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.UnsupportedGameRequestException;
import com.bueno.domain.usecases.utils.converters.IntelConverter;
import com.bueno.spi.service.BotServiceManager;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class CreateGameUseCase {

    private final GameRepository gameRepo;
    private final UserRepository userRepo;

    public CreateGameUseCase(GameRepository gameRepo, UserRepository userRepo) {
        this.gameRepo = Objects.requireNonNull(gameRepo);
        this.userRepo = userRepo;
    }

    public IntelDto createForUserAndBot(CreateForUserAndBotRequestModel requestModel){
        Objects.requireNonNull(requestModel, "Request model not be null!");

        if(hasNoBotServiceWith(requestModel.getBotName()))
            throw new NoSuchElementException("Service implementation not available: " + requestModel.getBotName());

        final User user = userRepo.findByUuid(requestModel.getUserUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found:" + requestModel.getUserUuid()));

        final Player userPlayer = Player.of(user);
        final Player botPlayer = Player.ofBot(requestModel.getBotName());

        gameRepo.findByUserUuid(userPlayer.getUuid()).ifPresent(unused -> {
            throw new UnsupportedGameRequestException(userPlayer.getUuid() + " is already playing a game.");});

        return create(userPlayer, botPlayer);
    }

    private boolean hasNoBotServiceWith(String botName) {
        return !BotServiceManager.providersNames().contains(botName);
    }

    IntelDto createForBots(CreateForBotsRequestModel requestModel){
        Objects.requireNonNull(requestModel);

        if(hasNoBotServiceWith(requestModel.getBot1Name()))
            throw new NoSuchElementException("Service implementation not available: " + requestModel.getBot1Name());

        if(hasNoBotServiceWith(requestModel.getBot2Name()))
            throw new NoSuchElementException("Service implementation not available: " + requestModel.getBot2Name());

        final var bot1 = Player.ofBot(requestModel.getBot1Uuid(), requestModel.getBot1Name());
        final var bot2 = Player.ofBot(requestModel.getBot2Uuid(), requestModel.getBot2Name());

        return create(bot1, bot2);
    }

    private IntelDto create(Player p1, Player p2) {
        final Game game = new Game(p1, p2);
        gameRepo.save(game);
        return IntelConverter.of(game.getIntel());
    }
}
