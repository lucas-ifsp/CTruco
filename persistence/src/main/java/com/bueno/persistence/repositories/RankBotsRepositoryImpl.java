package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import com.bueno.domain.usecases.game.repos.RankBotsRepository;
import com.bueno.persistence.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "INSERT INTO bot_rank values (?,?)";
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setLong(1, botRankInfoDto.botRank());
            statement.setString(2, botRankInfoDto.botName());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(List<BotRankInfoDto> botRankInfoDto) {
        botRankInfoDto.forEach(this::save);
    }

    @Override
    public void update(BotRankInfoDto botRankInfoDto) {
        String sql = "UPDATE bot_rank SET bot_name = ? WHERE rank = ?;";
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setString(1, botRankInfoDto.botName());
            statement.setLong(2, botRankInfoDto.botRank());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateAll(List<BotRankInfoDto> botRankInfoDto) {
        botRankInfoDto.forEach(this::update);
    }

    private BotRankInfoDto resultSetToBotRankInfoDto(ResultSet res) throws SQLException {
        return new BotRankInfoDto(
                res.getString("bot_name"),
                res.getLong("rank")
        );
    }
}
