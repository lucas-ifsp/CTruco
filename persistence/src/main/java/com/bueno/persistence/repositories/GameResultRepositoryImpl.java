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
import com.bueno.persistence.dto.PlayerWinsEntity;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.persistence.dao.GameResultDao;
import com.bueno.persistence.dto.GameResultEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GameResultRepositoryImpl implements GameResultRepository {

    private final GameResultDao repo;

    public GameResultRepositoryImpl(GameResultDao repo) {
        this.repo = repo;
    }

    @Override
    public void save(GameResultDto gameResult) {
        repo.save(GameResultEntity.from(gameResult));
    }

    @Override
    public Map<String, Integer> findTopWinners(Integer maxNumberOfUsers){
        final List<PlayerWinsEntity> topWinnersDto = repo.findTopWinners(Pageable.ofSize(maxNumberOfUsers));
        final Map<String, Integer> result = new LinkedHashMap<>();
        topWinnersDto.forEach(dto -> result.put(dto.username(), dto.wins().intValue()));
        return result;
    }
}
