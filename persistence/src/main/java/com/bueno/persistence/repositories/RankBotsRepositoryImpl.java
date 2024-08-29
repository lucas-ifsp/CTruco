//package com.bueno.persistence.repositories;
//
//import com.bueno.domain.usecases.game.dtos.BotWinsOnRankDto;
//import com.bueno.domain.usecases.game.repos.RankBotsRepository;
//import com.bueno.persistence.ConnectionFactory;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class RankBotsRepositoryImpl implements RankBotsRepository {
//    @Override
//    public List<BotWinsOnRankDto> findAll() {
//        try (Statement statement = ConnectionFactory.createStatement()) {
//            List<BotWinsOnRankDto> bots = new ArrayList<>();
//            ResultSet res = statement.executeQuery("SELECT * FROM bot_rank;");
//            while (res.next()) bots.add(resultSetToBotRankInfoDto(res));
//            return bots;
//        } catch (SQLException e) {
//            System.err.println(e.getClass() + ": " + e.getMessage());
//            e.printStackTrace();
//            return List.of();
//        }
//    }
//
//    @Override
//    public void save(BotWinsOnRankDto botWinsOnRankDto) {
//        String sql = "INSERT INTO bot_rank values (?,?)";
//        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
//            statement.setLong(1, botWinsOnRankDto.botWins());
//            statement.setString(2, botWinsOnRankDto.botName());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            System.err.println(e.getClass() + ": " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void saveAll(List<BotWinsOnRankDto> botWinsOnRankDto) {
//        botWinsOnRankDto.forEach(this::save);
//    }
//
//    @Override
//    public void update(BotWinsOnRankDto botWinsOnRankDto) {
//        String sql = "UPDATE bot_rank SET bot_name = ? WHERE rank = ?;";
//        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
//            statement.setString(1, botWinsOnRankDto.botName());
//            statement.setLong(2, botWinsOnRankDto.botWins());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            System.err.println(e.getClass() + ": " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void updateAll(List<BotWinsOnRankDto> botWinsOnRankDto) {
//        botWinsOnRankDto.forEach(this::update);
//    }
//
//    private BotWinsOnRankDto resultSetToBotRankInfoDto(ResultSet res) throws SQLException {
//        return new BotWinsOnRankDto(
//                res.getString("bot_name"),
//                res.getLong("rank")
//        );
//    }
//}
