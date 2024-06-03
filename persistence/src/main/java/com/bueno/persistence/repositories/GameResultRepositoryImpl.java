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

import com.bueno.domain.usecases.game.dtos.GameResultDto;
import com.bueno.domain.usecases.game.dtos.GameResultUsernamesDto;
import com.bueno.domain.usecases.game.dtos.PlayerWinsDto;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.persistence.dao.GameResultDao;
import com.bueno.persistence.dto.GameResultEntity;
import com.bueno.persistence.dto.GameResultQR;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class GameResultRepositoryImpl implements GameResultRepository {

    private final GameResultDao repo;

    public GameResultRepositoryImpl(GameResultDao repo) {
        this.repo = repo;
    }

    @Override
    public void save(GameResultDto gameResult) {
        try {
            repo.save(GameResultEntity.from(gameResult));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "| GameResult couldn't be saved");
            e.printStackTrace();
        }
    }

    @Override
    public List<PlayerWinsDto> findTopWinners(Integer maxNumberOfUsers) {
        try {
            return repo.findTopWinners(Pageable.ofSize(maxNumberOfUsers)).stream()
                    .map(playerWins -> new PlayerWinsDto(playerWins.userName(), playerWins.wins().intValue()))
                    .toList();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<GameResultUsernamesDto> findAllByUserUuid(UUID uuid) {
        final List<GameResultQR> result;
        try {
            result = repo.findAllByPlayerUuid(uuid);
            return result.stream()
                    .map(r -> new GameResultUsernamesDto(r.ending(), r.player1(), r.player2(), r.winner()))
                    .toList();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return List.of();
    }
}
