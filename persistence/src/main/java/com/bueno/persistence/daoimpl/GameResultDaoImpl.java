package com.bueno.persistence.daoimpl;

import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.GameResultDao;
import com.bueno.persistence.dto.GameResultEntity;
import com.bueno.persistence.dto.GameResultQR;
import com.bueno.persistence.dto.PlayerWinsQR;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class GameResultDaoImpl implements GameResultDao {

    @Override
    public List<PlayerWinsQR> findTopWinners(Pageable pageable) throws SQLException {
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
            return players;
        }
    }

    @Override
    public List<GameResultQR> findAllByPlayerUuid(UUID uuid) throws SQLException {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<GameResultQR> players = new ArrayList<>();
            ResultSet res = statement.executeQuery("""
                    SELECT ending_time ending, temp1.p1 player1, temp2.p2 player2, temp3.win winner FROM
                        (SELECT game_uuid, player1_uuid p1_uuid, username p1, game_end ending_time FROM app_user app
                        LEFT JOIN game_result game ON app.uuid = game.player1_uuid
                        WHERE game.player1_uuid = :uuid OR game.player2_uuid = :uuid
                        ) AS temp1
                    INNER JOIN
                        (SELECT game_uuid, player2_uuid p2_uuid, username p2 FROM app_user app
                        LEFT JOIN game_result game ON app.uuid = game.player2_uuid
                         WHERE game.player1_uuid = :uuid OR game.player2_uuid = :uuid
                        ) AS temp2
                    ON temp1.game_uuid = temp2.game_uuid
                    INNER JOIN
                        (SELECT game_uuid, winner_uuid win_uuid, username win  FROM app_user app
                        LEFT JOIN game_result game ON app.uuid = game.winner_uuid
                         WHERE game.player1_uuid = :uuid OR game.player2_uuid = :uuid
                        ) AS temp3
                    ON temp1.game_uuid = temp3.game_uuid
                    ORDER BY ending_time DESC
                    """);
            while (res.next()) players.add(new GameResultQR(
                    LocalDateTime.parse(res.getString("ending")),
                    res.getString("player1"),
                    res.getString("player2"),
                    res.getString("winner")
            ));
            return players;
        }
    }

    @Override
    public void save(GameResultEntity game) throws SQLException {
        String sql = """
                INSERT INTO game_result(game_uuid,game_start,game_end,winner_uuid,player1_uuid,player1_score,player2_uuid,player2_score)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, game.getGameUuid());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(game.getGameStart()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(game.getGameEnd()));
            preparedStatement.setObject(4, game.getWinnerUuid());
            preparedStatement.setObject(5, game.getPlayer1Uuid());
            preparedStatement.setInt(6, game.getPlayer1Score());
            preparedStatement.setObject(7, game.getPlayer2Uuid());
            preparedStatement.setLong(8, game.getPlayer2Score());
            preparedStatement.executeUpdate();
        }
    }
}
