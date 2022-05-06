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
import com.bueno.domain.usecases.game.model.CreateDetachedRequest;
import com.bueno.domain.usecases.game.model.CreateForBotsRequest;
import com.bueno.domain.usecases.game.model.CreateForUserAndBotRequest;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.model.ApplicationUserDTO;
import com.bueno.domain.usecases.utils.converters.IntelConverter;
import com.bueno.domain.usecases.utils.dtos.IntelDto;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.UnsupportedGameRequestException;
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

    public IntelDto createForUserAndBot(CreateForUserAndBotRequest request){
        Objects.requireNonNull(request, "Request not be null!");

        if(hasNoBotServiceWith(request.botName()))
            throw new NoSuchElementException("Service implementation not available: " + request.botName());

        final ApplicationUserDTO user = userRepo.findByUuid(request.userUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found:" + request.userUuid()));

        final Player userPlayer = Player.of(user.uuid(), user.username());
        final Player botPlayer = Player.ofBot(request.botName());

        gameRepo.findByUserUuid(userPlayer.getUuid()).ifPresent(unused -> {
            throw new UnsupportedGameRequestException(userPlayer.getUuid() + " is already playing a game.");});

        return create(userPlayer, botPlayer);
    }

    private boolean hasNoBotServiceWith(String botName) {
        return !BotServiceManager.providersNames().contains(botName);
    }

    public IntelDto createDetached(CreateDetachedRequest request){
        Objects.requireNonNull(request, "Request model not be null!");

        if(hasNoBotServiceWith(request.botName()))
            throw new NoSuchElementException("Service implementation not available: " + request.botName());

        final Player userPlayer = Player.of(request.userUuid(), request.username());
        final Player botPlayer = Player.ofBot(request.botName());

        return create(userPlayer, botPlayer);
    }

    IntelDto createForBots(CreateForBotsRequest request){
        Objects.requireNonNull(request);

        if(hasNoBotServiceWith(request.bot1Name()))
            throw new NoSuchElementException("Service implementation not available: " + request.bot1Name());

        if(hasNoBotServiceWith(request.bot2Name()))
            throw new NoSuchElementException("Service implementation not available: " + request.bot2Name());

        final var bot1 = Player.ofBot(request.bot1Uuid(), request.bot1Name());
        final var bot2 = Player.ofBot(request.bot2Uuid(), request.bot2Name());

        return create(bot1, bot2);
    }

    private IntelDto create(Player p1, Player p2) {
        final Game game = new Game(p1, p2);
        gameRepo.save(game);
        return IntelConverter.of(game.getIntel());
    }
}
