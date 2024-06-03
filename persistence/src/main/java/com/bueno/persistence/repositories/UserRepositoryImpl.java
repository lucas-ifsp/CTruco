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
import com.bueno.persistence.dao.UserDao;
import com.bueno.persistence.dto.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    private final UserDao dao;

    public UserRepositoryImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public void save(ApplicationUserDto dto) {
        try {
            dao.save(UserEntity.from(dto));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() + "System couldn't save the user: " + dto.username() +
                               "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public Optional<ApplicationUserDto> findByUsername(String username) {
        final UserEntity dto;
        try {
            dto = dao.getByUsername(username);
            return Optional.ofNullable(UserEntity.toApplicationUser(dto));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() +
                               "\n" + Arrays.toString(e.getStackTrace()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ApplicationUserDto> findByEmail(String email) {
        final UserEntity dto;
        try {
            dto = dao.getByEmail(email);
            return Optional.ofNullable(UserEntity.toApplicationUser(dto));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() +
                               "\n" + Arrays.toString(e.getStackTrace()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ApplicationUserDto> findByUuid(UUID uuid) {
        final UserEntity dto;
        try {
            dto = dao.getByUuid(uuid);
            return Optional.ofNullable(UserEntity.toApplicationUser(dto));
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage() +
                               "\n" + Arrays.toString(e.getStackTrace()));
        }
        return Optional.empty();
    }
}
