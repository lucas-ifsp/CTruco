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
                id,
                r1_c1,
                r1_c2,
                r2_c1,
                r2_c2,
                r3_c1,
                r3_c2,
                hand_type,
                game_uuid,
                hand_winner,
                points,
                points_proposal,
                vira
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) { // TODO - Testar todos os endpoints SPRING
            preparedStatement.setLong(1, hand.getId());
            preparedStatement.setString(2, hand.getCard1Round1());
            preparedStatement.setString(3, hand.getCard2Round1());
            preparedStatement.setString(4, hand.getCard1Round2());
            preparedStatement.setString(5, hand.getCard2Round2());
            preparedStatement.setString(6, hand.getCard1Round3());
            preparedStatement.setString(7, hand.getCard2Round3());
            preparedStatement.setObject(8, hand.getGameUuid());
            preparedStatement.setString(9, hand.getHandType());
            preparedStatement.setObject(10, hand.getHandWinner());
            preparedStatement.setInt(11, hand.getPoints());
            preparedStatement.setInt(12, hand.getPointsProposal());
            preparedStatement.setObject(13, hand.getRound1Winner());
            preparedStatement.setObject(14, hand.getRound2Winner());
            preparedStatement.setObject(15, hand.getRound3Winner());
            preparedStatement.setString(16, hand.getVira());
            preparedStatement.executeUpdate();
        }
    }
}
