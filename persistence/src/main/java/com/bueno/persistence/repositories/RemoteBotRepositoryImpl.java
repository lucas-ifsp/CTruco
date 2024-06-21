package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.persistence.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
public class RemoteBotRepositoryImpl implements RemoteBotRepository {// TODO - remover Entities e DAOs (fora mongo), fazer todo o processo nos repositoriesImpl

    public RemoteBotRepositoryImpl() {
    }

    @Override
    public List<RemoteBotDto> findAll() {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<RemoteBotDto> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("SELECT * FROM remote_bot;");
            while (res.next()) bots.add(resultSetToRemoteBotDto(res));
            return bots;
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public Optional<RemoteBotDto> findByName(String name) {
        try {
            return getByAttribute("name", name);
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<RemoteBotDto> findById(UUID uuid) {
        try {
            return getByAttribute("uuid", uuid);
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<RemoteBotDto> findByUserId(UUID userId) {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<RemoteBotDto> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("""
                    SELECT * FROM remote_bot b JOIN app_user u on u.uuid = b.user_uuid;
                    """);
            while (res.next()) {
                bots.add(resultSetToRemoteBotDto(res));
            }
            return bots;
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void save(RemoteBotDto dto) {
        String sql = """
                INSERT INTO remote_bot(uuid,user_uuid,name,url,port)
                        VALUES (? , ? , ? , ? , ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, dto.uuid());
            preparedStatement.setObject(2, dto.user());
            preparedStatement.setString(3, dto.name());
            preparedStatement.setString(4, dto.url());
            preparedStatement.setString(5, dto.port());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't save the RemoteBot.");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(RemoteBotDto dto) {
        String sql = "DELETE FROM remote_bot WHERE uuid = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, dto.uuid());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't delete the RemoteBot: " + dto.name());
            e.printStackTrace();
        }
    }

    @Override
    public boolean existByName(String botName) {
        String sql = "SELECT 1 FROM remote_bot WHERE name = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, sql);
            ResultSet res = preparedStatement.executeQuery();
            return res.next();
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't save the RemoteBot.");
            e.printStackTrace();
            return false;
        }
    }

    private RemoteBotDto resultSetToRemoteBotDto(ResultSet res) throws SQLException {
        return new RemoteBotDto(
                res.getObject("uuid", UUID.class),
                res.getObject("user_uuid", UUID.class),
                res.getString("name"),
                res.getString("url"),
                res.getString("port"));
    }

    private <T> Optional<RemoteBotDto> getByAttribute(String name, T value) throws SQLException {
        String sql = "SELECT * FROM remote_bot WHERE " + name + " = ? ;";
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setObject(1, value);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return Optional.of(resultSetToRemoteBotDto(res));
            }
        }
        return Optional.empty();
    }
}
