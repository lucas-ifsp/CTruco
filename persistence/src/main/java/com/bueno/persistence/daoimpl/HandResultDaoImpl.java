package com.bueno.persistence.daoimpl;

import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.HandResultDao;
import com.bueno.persistence.dto.HandResultEntity;

import java.sql.SQLException;
import java.sql.Statement;

public class HandResultDaoImpl implements HandResultDao {

    @Override
    public void save(HandResultEntity hand) throws SQLException {
        try(Statement statement = ConnectionFactory.createStatement()){
            statement.executeQuery(createHandResultSaveQuery(hand));
        }
    }

    private static String createHandResultSaveQuery(HandResultEntity hand) {
        return "INSERT INTO hand_results(id,hand_type,game_uuid,hand_winner,points,points_proposal) " +
               "VALUES (" +
               hand.getId() +
               "," +
               hand.getHandType() +
               "," +
               hand.getGameUuid() +
               "," +
               hand.getHandWinner() +
               "," +
               hand.getPoints() +
               "," +
               hand.getPointsProposal() +
               ");";
    }
}
