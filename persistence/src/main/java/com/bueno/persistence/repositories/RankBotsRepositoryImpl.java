package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import com.bueno.domain.usecases.game.repos.RankBotsRepository;
import com.bueno.persistence.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RankBotsRepositoryImpl implements RankBotsRepository {
    @Override
    public List<BotRankInfoDto> findAll() {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<BotRankInfoDto> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("SELECT * FROM bot_rank;");
            while (res.next()) bots.add(resultSetToBotRankInfoDto(res));
            return bots;
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void save(BotRankInfoDto botRankInfoDto) {
        String sql = "INSERT INTO bot_rank values (?,?,?)";
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setLong(1, botRankInfoDto.botRank());
            statement.setString(2, botRankInfoDto.botName());
            statement.setLong(3, botRankInfoDto.botWins());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(List<BotRankInfoDto> botWinsOnRankDto) {
        botWinsOnRankDto.forEach(this::save);
    }

    @Override
    public void update(BotRankInfoDto botRankInfoDto) {
        String sql = "UPDATE bot_rank SET bot_name = ? WHERE rank = ?;";
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setString(1, botRankInfoDto.botName());
            statement.setLong(2, botRankInfoDto.botWins());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    //    @Override
//    public void updateAll(List<BotRankInfoDto> botRankInfoDtos) throws Exception {
//        if (botRankInfoDtos.size() != findAll().size()) throw new Exception("A quantidade de bots na lista é diferente " +
//                                                                            "da quantidade de tuplas no banco");
//        botRankInfoDtos.forEach(this::update);
//    }

    @Override
    public boolean refreshTable(List<BotRankInfoDto> botRankInfoDtos) {
        try {
            if (botRankInfoDtos.isEmpty()) throw new Exception("BotRankInfoDto is empty");

            if (botRankInfoDtos.size() < findAll().size())
                throw new Exception("BotRankInfoDto size is smaller than before, this is just a security feature");

            deleteAll();
            saveAll(botRankInfoDtos);

            return true;
        } catch (Exception e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteByRank(BotRankInfoDto botInfo) {
        String sql = "DELETE * FROM bot_rank where rank = ?;";
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setLong(1, botInfo.botRank());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    private boolean deleteAll() {
        try (Statement statement = ConnectionFactory.createStatement()) {
            statement.executeUpdate("DELETE FROM bot_rank");
            return true;
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private BotRankInfoDto resultSetToBotRankInfoDto(ResultSet res) throws SQLException {
        return new BotRankInfoDto(
                res.getString("bot_name"),
                res.getLong("wins"),
                res.getLong("rank")
        );
    }
}
