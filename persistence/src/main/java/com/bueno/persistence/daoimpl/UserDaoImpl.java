package com.bueno.persistence.daoimpl;

import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.persistence.ConnectionFactory;
import com.bueno.persistence.dao.UserDao;
import com.bueno.persistence.dto.UserEntity;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public Optional<UserEntity> getByUuid(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM app_user WHERE uuid = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            return resultSetToUserEntity(res);
        }
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM app_user WHERE email = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet res = preparedStatement.executeQuery();
            return resultSetToUserEntity(res);
        }
    }

    @Override
    public Optional<UserEntity> getByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM app_user WHERE username = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();
            return resultSetToUserEntity(res);
        }
    }

    @Override
    public void save(UserEntity user) throws SQLException {
        String sql = """
                INSERT INTO app_user(uuid,username,email,password)
                        VALUES ( ? , ? , ? , ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
        }
    }

    private Optional<UserEntity> resultSetToUserEntity(ResultSet res) throws SQLException {
        if (!res.isClosed()) {
            if (res.next()) {
                return Optional.of(UserEntity.from(
                        new ApplicationUserDto(
                                UUID.fromString(res.getString("uuid")),
                                res.getString("username"),
                                res.getString("password"),
                                res.getString("email")
                        )));
            }
        }
        return Optional.empty();
    }
}
