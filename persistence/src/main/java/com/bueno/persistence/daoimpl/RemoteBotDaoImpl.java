package com.bueno.persistence.daoimpl;

import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.RemoteBotDao;
import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    public RemoteBotEntity getByUuid(UUID uuid) throws SQLException {
        try (ResultSet res = SelectRemoteBotByUUID(uuid.toString())) {
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
    public RemoteBotEntity getByName(String name) throws SQLException {
        try (ResultSet res = SelectRemoteBotByName(name)) {
            res.next();
            return new RemoteBotEntity(
                    UUID.fromString(res.getString("uuid")),
                    UUID.fromString(res.getString("user_uuid")),
                    res.getString("name"),
                    res.getString("url"),
                    res.getString("port")
            );
        }
    }

    @Override
    public List<RemoteBotEntity> getRemoteBotEntitiesByUser(UserEntity userEntity) throws SQLException {
        try (Statement statement = ConnectionFactory.createStatement()) {
            List<RemoteBotEntity> bots = new ArrayList<>();
            ResultSet res = statement.executeQuery("""
                    SELECT * FROM remote_bot b WHERE b.user_uuid = (SELECT uuid FROM app_user u WHERE u.uuid = b.user_uuid);
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
        try (ResultSet res = SelectRemoteBotByName(name)) {
            return !res.isClosed();
        }
    }

    @Override
    public void save(RemoteBotEntity bot) throws SQLException {
        String sql = """
                INSERT INTO remote_bot(uuid,user_uuid,name,url,port)
                        VALUES (? , ? , ? , ? , ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, bot.getUuid().toString());
            preparedStatement.setString(2, bot.getUserUuid().toString());
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
            preparedStatement.setString(1, bot.getUuid().toString());
            preparedStatement.executeUpdate();
        }
    }

    private static ResultSet SelectRemoteBotByName(String value) throws SQLException {
        String sql = "SELECT * FROM remote_bot WHERE name = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, value);
            return preparedStatement.executeQuery();
        }
    }

    private static ResultSet SelectRemoteBotByUUID(String value) throws SQLException {
        String sql = "SELECT * FROM remote_bot WHERE uuid = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, value);
            return preparedStatement.executeQuery();
        }
    }

    private RemoteBotEntity resultSetToRemoteBotEntity(ResultSet res) throws SQLException {
        return new RemoteBotEntity(UUID.fromString(res.getString("uuid")),
                UUID.fromString(res.getString("user_uuid")),
                res.getString("name"),
                res.getString("url"),
                res.getString("port"));
    }
}
