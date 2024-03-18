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

package com.bueno.controllers;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.usecases.game.usecase.CreateGameUseCase;
import com.bueno.domain.usecases.game.usecase.RemoveGameUseCase;
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.dtos.CreateForUserAndBotDto;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.responses.ResponseBuilder;
import com.bueno.responses.ResponseEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/games")
public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final RemoveGameUseCase removeGameUseCase;
    private final GameRepository gameRepository;

    public GameController(CreateGameUseCase createGameUseCase,
                          RemoveGameUseCase removeGameUseCase,
                          GameRepository gameRepository) {
        this.createGameUseCase = createGameUseCase;
        this.removeGameUseCase = removeGameUseCase;
        this.gameRepository = gameRepository;
    }

    @PostMapping(path = "/user-bot")
    public ResponseEntity<IntelDto> createForUserAndBot(@RequestBody CreateForUserAndBotDto request){
        final var intel = createGameUseCase.createForUserAndBot(request);
        return new ResponseEntity<>(intel, HttpStatus.CREATED);
    }

    @GetMapping(path = "/players/{uuid}")
    private ResponseEntity<?> getGame(@PathVariable UUID uuid){
        final Optional<Game> possibleGame = gameRepository.findByPlayerUuid(uuid).map(GameConverter::fromDto);

        if(possibleGame.isPresent()){
            final Game game = possibleGame.get();
            final String opponentName = game.getPlayer1().getUuid().equals(uuid)
                    ? game.getPlayer2().getUsername()
                    : game.getPlayer1().getUsername();

            return new ResponseBuilder(HttpStatus.OK)
                    .addEntry(new ResponseEntry("gameUuid", game.getUuid()))
                    .addEntry(new ResponseEntry("opponentName", opponentName))
                    .build();
        }
        return new ResponseBuilder(HttpStatus.NOT_FOUND)
                .addEntry(new ResponseEntry("error", "User with UUID " + uuid + " is not in an active game."))
                .addTimestamp()
                .build();
    }

    @DeleteMapping(path = "/players/{uuid}")
    public ResponseEntity<IntelDto> removeGame(@PathVariable UUID uuid){
        removeGameUseCase.byUserUuid(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
