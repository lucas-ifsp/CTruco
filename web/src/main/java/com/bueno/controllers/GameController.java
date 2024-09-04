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
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.*;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.game.usecase.CreateGameUseCase;
import com.bueno.domain.usecases.game.usecase.PlayWithBotsUseCase;
import com.bueno.domain.usecases.game.usecase.RemoveGameUseCase;
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.responses.ResponseBuilder;
import com.bueno.responses.ResponseEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/games")
public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final RemoveGameUseCase removeGameUseCase;
    private final GameRepository gameRepository;
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi remoteBotApi;
    private final BotManagerService botManagerService;
    private final GameResultRepository gameResultRepository;


    public GameController(CreateGameUseCase createGameUseCase,
                          RemoveGameUseCase removeGameUseCase,
                          GameRepository gameRepository, RemoteBotRepository remoteBotRepository, RemoteBotApi remoteBotApi, BotManagerService botManagerService, GameResultRepository gameResultRepository) {
        this.createGameUseCase = createGameUseCase;
        this.removeGameUseCase = removeGameUseCase;
        this.gameRepository = gameRepository;
        this.remoteBotRepository = remoteBotRepository;
        this.remoteBotApi = remoteBotApi;
        this.botManagerService = botManagerService;
        this.gameResultRepository = gameResultRepository;
    }

    @PostMapping(path = "/user-bot")
    public ResponseEntity<IntelDto> createForUserAndBot(@RequestBody CreateForUserAndBotDto request) {
        final var intel = createGameUseCase.createForUserAndBot(request);
        return new ResponseEntity<>(intel, HttpStatus.CREATED);
    }

    @PostMapping(path = "/bot-bot")
    public ResponseEntity<PlayWithBotsResponseDto> createForBots(@RequestBody SimulationRequestDto request) {
        Objects.requireNonNull(request);

        int times = request.times();
        if (times <= 0) times = 1;

        final PlayWithBotsUseCase simulateBotsUseCase = new PlayWithBotsUseCase(remoteBotRepository, remoteBotApi, botManagerService);

        final UUID bot1UUID = UUID.randomUUID();
        final UUID bot2UUID = UUID.randomUUID();

        final PlayWithBotsResultsDto results = simulateBotsUseCase.playWithBots(bot1UUID, request.bot1Name(), bot2UUID, request.bot2Name(), times);
        final PlayWithBotsResponseDto response = getPlayWithBotsResponseDto(results, request);

        final var responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

    private static PlayWithBotsResponseDto getPlayWithBotsResponseDto(PlayWithBotsResultsDto results, SimulationRequestDto request) {

        /* nessa stream é introduzido um List<PlayWithBotsDto> com os nomes de quem ganhou cada partida e é mapeado para um Map<String,Long>
         em que a String é o nome do bot e o Long é a quantidade de vezes que ele ganhou.
         TODO Tente achar um jeito melhor de fazer isso*/
        final Map<String, Long> filteredResults = results.info().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> Map.entry(entry.getKey().name(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long bot1Wins = filteredResults.getOrDefault(request.bot1Name(), 0L);
        long bot2Wins = filteredResults.getOrDefault(request.bot2Name(), 0L);

        return new PlayWithBotsResponseDto(request.bot1Name(), bot1Wins, request.bot2Name(), bot2Wins, results.times(), results.timeToExecute());
    }


    @GetMapping(path = "/players/{uuid}")
    private ResponseEntity<?> getGame(@PathVariable UUID uuid) {
        final Optional<Game> possibleGame = gameRepository.findByPlayerUuid(uuid).map(GameConverter::fromDto);

        if (possibleGame.isPresent()) {
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

    @GetMapping(path = "players/{uuid}/match-history")
    private ResponseEntity<?> listGameResultsByPlayerUuid(@PathVariable UUID uuid) {
        final var results = gameResultRepository.findAllByUserUuid(uuid);
        if (results.isEmpty()) {
            return new ResponseBuilder(HttpStatus.NOT_FOUND)
                    .addEntry(new ResponseEntry("error", "User with UUID has no match history"))
                    .addTimestamp()
                    .build();
        }
        return new ResponseBuilder(HttpStatus.OK)
                .addEntry(new ResponseEntry("games", results))
                .addTimestamp()
                .build();
    }

    @DeleteMapping(path = "/players/{uuid}")
    public ResponseEntity<IntelDto> removeGame(@PathVariable UUID uuid) {
        final boolean isFromInactivity = false;
        removeGameUseCase.byUserUuid(uuid,false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
