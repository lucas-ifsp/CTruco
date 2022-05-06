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

package com.bueno.persistence.mysql.repositories;

import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.persistence.mysql.dao.UserDao;
import com.bueno.persistence.mysql.dto.UserDto;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserDao dao;

    public UserRepositoryImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public void save(ApplicationUserDto user) {
        dao.save(UserDto.from(user));
    }

    @Override
    public Optional<ApplicationUserDto> findByUsername(String username) {
        final UserDto dto = dao.getByUsername(username);
        return Optional.ofNullable(UserDto.toApplicationUser(dto));
    }

    @Override
    public Optional<ApplicationUserDto> findByEmail(String email) {
        final UserDto dto = dao.getByEmail(email);
        return Optional.ofNullable(UserDto.toApplicationUser(dto));
    }

    @Override
    public Optional<ApplicationUserDto> findByUuid(UUID uuid) {
        final UserDto dto = dao.getByUuid(uuid);
        return Optional.ofNullable(UserDto.toApplicationUser(dto));
    }
}
