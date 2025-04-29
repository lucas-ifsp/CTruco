package com.bueno.persistence.daoimpl;

import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.RemoteBotDao;
import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RemoteBotDaoImpl implements RemoteBotDao {

    @Override
    public List<RemoteBotEntity> findAll() throws SQLException {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<RemoteBotEntity> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("SELECT * FROM remote_bot;");
            while (res.next()) bots.add(resultSetToRemoteBotEntity(res));
            return bots;
        }
    }


    @Override
    public Optional<RemoteBotEntity> getByUuid(UUID uuid) throws SQLException {
        return getByAttribute("uuid", uuid);
    }

    @Override
    public Optional<RemoteBotEntity> getByName(String name) throws SQLException {
        return getByAttribute("name", name);
    }

    private <T> Optional<RemoteBotEntity> getByAttribute(String name, T value) throws SQLException {
        String sql = "SELECT * FROM remote_bot WHERE " + name + " = ? ;";
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setObject(1, value);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return Optional.of(resultSetToRemoteBotEntity(res));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<RemoteBotEntity> getRemoteBotEntitiesByUser(UserEntity userEntity) throws SQLException {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<RemoteBotEntity> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("""
                    SELECT * FROM remote_bot b WHERE b.user_uuid = (SELECT uuid FROM app_user u WHERE u.uuid = b.user_uuid);
                    """);
            while (res.next()) {
                bots.add(resultSetToRemoteBotEntity(res));
            }
            return bots;
        }
    }

    @Override
    public boolean existsByName(String name) throws SQLException {
        String sql = "SELECT 1 FROM remote_bot WHERE name = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet res = preparedStatement.executeQuery();
            return res.next();
        }
    }

    @Override
    public void save(RemoteBotEntity bot) throws SQLException {
        String sql = """
                INSERT INTO remote_bot(uuid,user_uuid,name,url,port)
                        VALUES (? , ? , ? , ? , ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, bot.getUuid());
            preparedStatement.setObject(2, bot.getUserUuid());
            preparedStatement.setString(3, bot.getName());
            preparedStatement.setString(4, bot.getUrl());
            preparedStatement.setString(5, bot.getPort());
            preparedStatement.executeUpdate();

        }
    }

    @Override
    public void delete(RemoteBotEntity bot) throws SQLException {
        String sql = "DELETE FROM remote_bot WHERE uuid = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, bot.getUuid());
            preparedStatement.executeUpdate();
        }
    }

    private RemoteBotEntity resultSetToRemoteBotEntity(ResultSet res) throws SQLException {
        return new RemoteBotEntity(
                res.getObject("uuid",UUID.class),
                res.getObject("user_uuid",UUID.class),
                res.getString("name"),
                res.getString("url"),
                res.getString("port"));
    }
}
