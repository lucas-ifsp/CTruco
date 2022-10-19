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

package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.game.dtos.GameDto;
import com.bueno.domain.usecases.game.dtos.PlayerDto;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.persistence.dao.GameDao;
import com.bueno.persistence.dao.PlayerDao;
import com.bueno.persistence.dto.GameEntity;
import com.bueno.persistence.dto.PlayerEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final GameDao gameDao;
    private final PlayerDao playerDao;

    public GameRepositoryImpl(GameDao dao, PlayerDao playerDao) {
        this.gameDao = dao;
        this.playerDao = playerDao;
    }

    @Override
    public void save(GameDto dto) {
        gameDao.findById(dto.gameUuid())
                .ifPresent(game -> {throw new EntityNotFoundException("Game already exists: " + game.getId());});
        playerDao.save(PlayerEntity.from(dto.player1()));
        playerDao.save(PlayerEntity.from(dto.player2()));
        gameDao.save(GameEntity.from(dto));
    }

    @Override
    public void update(GameDto dto) {
        gameDao.findById(dto.gameUuid())
                .orElseThrow(() -> new EntityNotFoundException("Can not update non-existing game: " + dto.gameUuid()));
        playerDao.save(PlayerEntity.from(dto.player1()));
        playerDao.save(PlayerEntity.from(dto.player2()));
        gameDao.save(GameEntity.from(dto));
    }

    @Override
    public void delete(UUID uuid) {
        final GameEntity game = gameDao.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Can not update non-existing game: " + uuid));
        playerDao.deleteById(game.getPlayer1());
        playerDao.deleteById(game.getPlayer2());
        gameDao.delete(game);
    }

    @Override
    public Optional<GameDto> findByPlayerUuid(UUID playerUuid) {
        final UUID uuid = Objects.requireNonNull(playerUuid, "User UUID must not be null.");
        final Optional<GameEntity> possibleGame = gameDao.findByPlayer1OrPlayer2(uuid, uuid);
        return getGameDto(possibleGame);
    }

    private Optional<GameDto> getGameDto(Optional<GameEntity> possibleGame) {
        if(possibleGame.isEmpty()) return Optional.empty();
        final GameEntity game = possibleGame.get();
        final PlayerDto player1 = playerDao.findById(game.getPlayer1()).orElseThrow().toDto();
        final PlayerDto player2 = playerDao.findById(game.getPlayer2()).orElseThrow().toDto();
        return Optional.of(game.toDto(Map.of(player1.uuid(), player1, player2.uuid(), player2)));
    }
}
