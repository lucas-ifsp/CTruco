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

import com.bueno.domain.usecases.hand.HandResultRepository;
import com.bueno.domain.usecases.hand.dtos.HandResultDto;
import com.bueno.persistence.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HandResultRepositoryImpl implements HandResultRepository {


    public HandResultRepositoryImpl() {
    }

    @Override
    public void save(HandResultDto handResultDto) {
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
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, getIfAvailable(handResultDto.openCards(),1));
            preparedStatement.setString(2, getIfAvailable(handResultDto.openCards(),2));
            preparedStatement.setString(3, getIfAvailable(handResultDto.openCards(),3));
            preparedStatement.setString(4, getIfAvailable(handResultDto.openCards(),4));
            preparedStatement.setString(5, getIfAvailable(handResultDto.openCards(),5));
            preparedStatement.setString(6, getIfAvailable(handResultDto.openCards(),6));
            preparedStatement.setObject(7, handResultDto.gameUuid());
            preparedStatement.setString(8, handResultDto.handType());
            preparedStatement.setObject(9, handResultDto.handWinner());
            preparedStatement.setInt(10, handResultDto.points());
            preparedStatement.setInt(11, handResultDto.pointsProposal());
            preparedStatement.setObject(12, getIfAvailable(handResultDto.roundWinners(),0));
            preparedStatement.setObject(13, getIfAvailable(handResultDto.roundWinners(),1));
            preparedStatement.setObject(14, getIfAvailable(handResultDto.roundWinners(),2));
            preparedStatement.setString(15, getIfAvailable(handResultDto.openCards(),0));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "| HandResult couldn't be saved");
            e.printStackTrace();
        }
    }

    private static <T> T getIfAvailable(List<T> list, int index) {
        if (index < list.size()) return list.get(index);
        return null;
    }
}
