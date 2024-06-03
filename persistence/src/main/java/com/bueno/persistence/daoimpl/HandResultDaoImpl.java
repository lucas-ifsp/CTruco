package com.bueno.persistence.daoimpl;

import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.HandResultDao;
import com.bueno.persistence.dto.HandResultEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HandResultDaoImpl implements HandResultDao {

    @Override
    public void save(HandResultEntity hand) throws SQLException {
        String sql = """
                INSERT INTO hand_results(id,hand_type,game_uuid,hand_winner,points,points_proposal)
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {// TODO - estudar Try/Catch with resources;
            preparedStatement.setLong(1, hand.getId());
            preparedStatement.setString(2, hand.getHandType());
            preparedStatement.setString(3, hand.getGameUuid().toString());
            preparedStatement.setString(4, hand.getHandWinner().toString());
            preparedStatement.setInt(5, hand.getPoints());
            preparedStatement.setInt(6, hand.getPointsProposal());
            preparedStatement.executeQuery();
        }
    }
}
