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

import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.persistence.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    public UserRepositoryImpl() {
    }

    @Override
    public void save(ApplicationUserDto dto) {
        String sql = """
                INSERT INTO app_user(uuid,username,email,password)
                        VALUES ( ? , ? , ? , ?);
                """;
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, dto.uuid());
            preparedStatement.setString(2, dto.username());
            preparedStatement.setString(3, dto.email());
            preparedStatement.setString(4, dto.password());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't save the user: " + dto.username());
            e.printStackTrace();
        }
    }

    @Override
    public Optional<ApplicationUserDto> findByUsername(String username) {
        try {
            return getByAttribute("username", username);
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<ApplicationUserDto> findByEmail(String email) {
        try {
            return getByAttribute("email", email);
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<ApplicationUserDto> findByUuid(UUID uuid) {
        try {
            return getByAttribute("uuid", uuid);
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private <T> Optional<ApplicationUserDto> getByAttribute(String name, T value) throws SQLException {
        String sql = "SELECT * FROM app_user WHERE " + name + " = ? ;";
        try (PreparedStatement preparedStatement = ConnectionFactory.createPreparedStatement(sql)) {
            preparedStatement.setObject(1, value);
            ResultSet res = preparedStatement.executeQuery();
            return resultSetToApplicationUserDto(res);
        }
    }

    private Optional<ApplicationUserDto> resultSetToApplicationUserDto(ResultSet res) throws SQLException {
        if (!res.isClosed()) {
            if (res.next()) {
                return Optional.of(
                        new ApplicationUserDto(
                                res.getObject("uuid", UUID.class),
                                res.getString("username"),
                                res.getString("password"),
                                res.getString("email")
                        ));
            }
        }
        return Optional.empty();
    }
}
