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

package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.dtos.GameDto;
import com.bueno.domain.usecases.game.dtos.GameResultDto;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.hand.dtos.HandDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;


@Service
public class RemoveGameUseCase {

    private final GameRepository gameRepo;
    private final GameResultRepository gameResultRepo;

    public RemoveGameUseCase(GameRepository gameRepo, GameResultRepository gameResultRepo) {
        this.gameRepo = gameRepo;
        this.gameResultRepo = gameResultRepo;
    }

    public List<UUID> byInactivityAfter(int minutes) {
        final boolean isFromInactivity = true;
        final List<UUID> gamesToRemove = gameRepo.findAllInactiveAfter(minutes)
                .stream()
                .map(this::inactivePlayerUuid)
                .toList();
        gamesToRemove.forEach(game -> byUserUuid(game, isFromInactivity));
        return gamesToRemove;
    }

    public UUID inactivePlayerUuid(GameDto game) {
        final HandDto currentHand = game.hands().get(game.hands().size() - 1);
        UUID uuid = currentHand.currentPlayer().uuid();
        return uuid;
    }

    public void byUserUuid(UUID userUuid, boolean isFromInactive) {
        final UUID uuid = Objects.requireNonNull(userUuid, "User UUID must not be null.");
        final GameDto game = gameRepo.findByPlayerUuid(Objects.requireNonNull(uuid))
                .orElseThrow(() -> new NoSuchElementException("The is no active game for user UUID: " + userUuid));
        gameRepo.delete(game.gameUuid());
        if (isFromInactive)
            gameResultRepo.save(createGameResultFromAbandon(game, userUuid));
        else gameResultRepo.save(createGameResultFromDto(game));
    }

    private GameResultDto createGameResultFromAbandon(GameDto game, UUID quitterUuid) {
        final UUID player1uuid = game.player1().uuid().equals(quitterUuid)
                ? game.player1().uuid() : game.player2().uuid();
        final UUID player2uuid = game.player1().uuid().equals(quitterUuid)
                ? game.player2().uuid() : game.player1().uuid();
        final UUID winnerUuid = player1uuid.equals(quitterUuid) ? player2uuid : player1uuid;
        final int player1Score = player1uuid.equals(winnerUuid) ? 12 : 0;
        final int player2Score = player2uuid.equals(winnerUuid) ? 12 : 0;

        return new GameResultDto(
                game.gameUuid(),
                game.timestamp(),
                LocalDateTime.now(),
                winnerUuid,
                player1uuid,
                player1Score,
                player2uuid,
                player2Score);
    }

    private GameResultDto createGameResultFromDto(GameDto game) {
        final UUID player1uuid = game.player1().uuid();
        final UUID player2uuid = game.player2().uuid();
        final UUID winnerUuid = game.player1().score() == 12 ? player1uuid : player2uuid;
        final int player1Score = game.player1().score();
        final int player2Score = game.player2().score();

        return new GameResultDto(
                game.gameUuid(),
                game.timestamp(),
                LocalDateTime.now(),
                winnerUuid,
                player1uuid,
                player1Score,
                player2uuid,
                player2Score);
    }
}
