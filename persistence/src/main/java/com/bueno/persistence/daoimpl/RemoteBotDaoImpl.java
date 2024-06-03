package com.bueno.persistence.daoimpl;

import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.RemoteBotDao;
import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RemoteBotDaoImpl implements RemoteBotDao {

    @Override
    public List<RemoteBotEntity> findAll() throws SQLException {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<RemoteBotEntity> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("SELECT * FROM remote_bot;");
            while (res.next()) bots.add(new RemoteBotEntity(
                    UUID.fromString(res.getString("uuid")),
                    UUID.fromString(res.getString("user_uuid")),
                    res.getString("name"),
                    res.getString("url"),
                    res.getString("port")
            ));
            return bots;
        }
    }

    @Override
    public RemoteBotEntity getByUuid(UUID uuid) throws SQLException {
        try (ResultSet res = SelectRemoteBotByAttribute(uuid.toString(), "uuid")) {
            if (res.next()) {
                return new RemoteBotEntity(
                        UUID.fromString(res.getString("uuid")),
                        UUID.fromString(res.getString("user_uuid")),
                        res.getString("name"),
                        res.getString("url"),
                        res.getString("port")
                );
            }
        }
        return null;
    }

    @Override
    public Optional<RemoteBotEntity> getByName(String name) throws SQLException {
        try (ResultSet res = SelectRemoteBotByAttribute(name, "name")) {
            if (res.next()) {
                return Optional.of(new RemoteBotEntity(
                        UUID.fromString(res.getString("uuid")),
                        UUID.fromString(res.getString("user_uuid")),
                        res.getString("name"),
                        res.getString("url"),
                        res.getString("port")
                ));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<RemoteBotEntity> getRemoteBotEntitiesByUser(UserEntity userEntity) throws SQLException {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<RemoteBotEntity> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("""
                    SELECT * FROM remote_bot b WHERE(SELECT * FROM app_user u WHERE u.uuid = b.user_uuid);
                    """);
            while (res.next()) {
                bots.add(new RemoteBotEntity(
                        UUID.fromString(res.getString("uuid")),
                        UUID.fromString(res.getString("user_uuid")),
                        res.getString("name"),
                        res.getString("url"),
                        res.getString("port")
                ));
            }
            return bots;
        }
    }

    @Override
    public boolean existsByName(String name) throws SQLException {
        try (ResultSet res = SelectRemoteBotByAttribute(name, "name")) {
            return res.next();
        }
    }

    @Override
    public void save(RemoteBotEntity bot) throws SQLException {
        String sql = """
                INSERT INTO remote_bot(uuid,user_uuid,name,url,port)
                        VALUES ( ? , ? , ? , ? , ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, bot.getUuid().toString());
            preparedStatement.setString(2, bot.getUserUuid().toString());
            preparedStatement.setString(3, bot.getName());
            preparedStatement.setString(4, bot.getUrl());
            preparedStatement.setString(5, bot.getPort());
            ResultSet res = preparedStatement.executeQuery(sql);

        }
    }

    @Override
    public void delete(RemoteBotEntity bot) throws SQLException {
        String sql = "DELETE FROM remote_bot WHERE uuid = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, bot.getUuid().toString());
            preparedStatement.executeQuery();
        }
    }

    private static ResultSet SelectRemoteBotByAttribute(String value, String type) throws SQLException {
        String sql = "SELECT * FROM remote_bot WHERE ? = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, value);
            return preparedStatement.executeQuery();
        }
    }
}
