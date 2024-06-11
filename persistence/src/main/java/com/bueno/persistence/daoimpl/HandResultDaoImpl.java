package com.bueno.persistence.daoimpl;

import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.HandResultDao;
import com.bueno.persistence.dto.HandResultEntity;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class HandResultDaoImpl implements HandResultDao {

    @Override
    public void save(HandResultEntity hand) throws SQLException {
        String sql = """
                INSERT INTO hand_result(
                r1_c1,
                r1_c2,
                r2_c1,
                r2_c2,
                r3_c1,
                r3_c2,
                game_uuid,
                hand_type,
                hand_winner,
                points,
                points_proposal,
                r1_winner,
                r2_winner,
                r3_winner,
                vira
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) { // TODO - Testar todos os endpoints SPRING
            preparedStatement.setString(1, hand.getCard1Round1());
            preparedStatement.setString(2, hand.getCard2Round1());
            preparedStatement.setString(3, hand.getCard1Round2());
            preparedStatement.setString(4, hand.getCard2Round2());
            preparedStatement.setString(5, hand.getCard1Round3());
            preparedStatement.setString(6, hand.getCard2Round3());
            preparedStatement.setObject(7, hand.getGameUuid());
            preparedStatement.setString(8, hand.getHandType());
            preparedStatement.setObject(9, hand.getHandWinner());
            preparedStatement.setInt(10, hand.getPoints());
            preparedStatement.setInt(11, hand.getPointsProposal());
            preparedStatement.setObject(12, hand.getRound1Winner());
            preparedStatement.setObject(13, hand.getRound2Winner());
            preparedStatement.setObject(14, hand.getRound3Winner());
            preparedStatement.setString(15, hand.getVira());
            preparedStatement.executeUpdate();
        }
    }
}
