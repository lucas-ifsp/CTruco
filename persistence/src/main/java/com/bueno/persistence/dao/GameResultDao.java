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

import com.bueno.persistence.dto.GameResultEntity;
import com.bueno.persistence.dto.GameResultQR;
import com.bueno.persistence.dto.PlayerWinsQR;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameResultDao extends JpaRepository<GameResultEntity, UUID> {

    @Query("""
            SELECT a.username as username, count(a.username) as wins
            FROM UserEntity a
            RIGHT JOIN GameResultEntity b ON a.uuid = b.winnerUuid
            GROUP BY username
            ORDER BY username
            """
    )
    List<PlayerWinsQR> findTopWinners(Pageable pageable);

    @Query(
            value = """
            SELECT ending_time ending, temp1.p1 player1, temp2.p2 player2, temp3.win winner FROM
                (SELECT game_id, player1 p1_uuid, username p1, game_end_time ending_time FROM app_user app
                LEFT JOIN game_result game ON app.id = game.player1
                WHERE game.player1 = :uuid OR game.player2 = :uuid
                ) AS temp1
            INNER JOIN
                (SELECT game_id, player2 p2_uuid, username p2 FROM app_user app
                LEFT JOIN game_result game ON app.id = game.player2
                 WHERE game.player1 = :uuid OR game.player2 = :uuid
                ) AS temp2
            ON temp1.game_id = temp2.game_id
            INNER JOIN
                (SELECT game_id, winner win_uuid, username win  FROM app_user app
                LEFT JOIN game_result game ON app.id = game.winner
                 WHERE game.player1 = :uuid OR game.player2 = :uuid
                ) AS temp3
            ON temp1.game_id = temp3.game_id
            ORDER BY ending_time DESC
            """
            , nativeQuery = true
    )
    List<GameResultQR> findAllByPlayerUuid(@Param("uuid") UUID uuid);
}
