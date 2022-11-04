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

package com.bueno.persistence.dao;

import com.bueno.domain.usecases.game.dtos.PlayerWinsDto;
import com.bueno.persistence.dto.GameResultEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameResultDao extends JpaRepository<GameResultEntity, UUID> {

    @Query("""
            SELECT new com.bueno.domain.usecases.game.dtos.PlayerWinsDto(a.username, count(a.username))
            FROM UserEntity a
            RIGHT JOIN GameResultEntity b ON a.uuid = b.winnerUuid
            GROUP BY username
            ORDER BY username
            """
    )
    List<PlayerWinsDto> findTopWinners(Pageable pageable);
}
