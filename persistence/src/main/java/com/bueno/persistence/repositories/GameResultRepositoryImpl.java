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
import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dto.GameResultQR;
import com.bueno.persistence.dto.PlayerWinsQR;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class GameResultRepositoryImpl implements GameResultRepository {

    public GameResultRepositoryImpl() {
    }

    @Override
    public void save(GameResultDto gameResult) {
        String sql = """
                INSERT INTO game_result(game_uuid,game_start,game_end,winner_uuid,player1_uuid,player1_score,player2_uuid,player2_score)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, gameResult.gameUuid());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(gameResult.gameStart()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(gameResult.gameEnd()));
            preparedStatement.setObject(4, gameResult.winnerUuid());
            preparedStatement.setObject(5, gameResult.player1Uuid());
            preparedStatement.setInt(6, gameResult.player1Score());
            preparedStatement.setObject(7, gameResult.player2Uuid());
            preparedStatement.setLong(8, gameResult.player2Score());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "| GameResult couldn't be saved");
            e.printStackTrace();
        }
    }

    @Override
    public List<PlayerWinsDto> findTopWinners(Integer maxNumberOfUsers) {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<PlayerWinsQR> players = new ArrayList<>();
            ResultSet res = statement.executeQuery("""
                    SELECT a.username as username, count(a.username) as wins
                    FROM app_user a
                    RIGHT JOIN game_result b ON a.uuid = b.winner_uuid
                    GROUP BY username
                    ORDER BY username;
                    """);
            while (res.next()) players.add(new PlayerWinsQR(
                    res.getString("username"),
                    res.getLong("wins")
            ));
            return players.stream()
                    .map(playerWinsQR -> new PlayerWinsDto(
                            playerWinsQR.userName(),
                            playerWinsQR.wins().intValue()))
                    .toList();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<GameResultUsernamesDto> findAllByUserUuid(UUID uuid) {
        String sql = """
                SELECT ending_time ending, temp1.p1 player1, temp2.p2 player2, temp3.win winner FROM
                    (SELECT game_uuid, player1_uuid p1_uuid, username p1, game_end ending_time FROM app_user app
                    LEFT JOIN game_result game ON app.uuid = game.player1_uuid
                    WHERE game.player1_uuid = ? OR game.player2_uuid = ?
                    ) AS temp1
                INNER JOIN
                    (SELECT game_uuid, player2_uuid p2_uuid, username p2 FROM app_user app
                    LEFT JOIN game_result game ON app.uuid = game.player2_uuid
                     WHERE game.player1_uuid = ? OR game.player2_uuid = ?
                    ) AS temp2
                ON temp1.game_uuid = temp2.game_uuid
                INNER JOIN
                    (SELECT game_uuid, winner_uuid win_uuid, username win  FROM app_user app
                    LEFT JOIN game_result game ON app.uuid = game.winner_uuid
                     WHERE game.player1_uuid = ? OR game.player2_uuid = ?
                    ) AS temp3
                ON temp1.game_uuid = temp3.game_uuid
                ORDER BY ending_time DESC
                """;
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            List<GameResultQR> players = new ArrayList<>();
            statement.setObject(1, uuid);
            statement.setObject(2, uuid);
            statement.setObject(3, uuid);
            statement.setObject(4, uuid);
            statement.setObject(5, uuid);
            statement.setObject(6, uuid);
            ResultSet res = statement.executeQuery();
            while (res.next()) players.add(new GameResultQR(
                    LocalDateTime.parse(res.getString("ending")),
                    res.getString("player1"),
                    res.getString("player2"),
                    res.getString("winner")
            ));
            return players.stream().map(r -> new GameResultUsernamesDto(
                            r.ending(),
                            r.player1(),
                            r.player2(),
                            r.winner()
                    ))
                    .toList();

        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return List.of();

    }
}
