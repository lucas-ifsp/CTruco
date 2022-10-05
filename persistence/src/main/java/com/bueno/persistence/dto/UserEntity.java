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

package com.bueno.persistence.dto;

import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "APP_USER")
public class UserEntity {
    @Id
    @Column(name = "ID")
    private UUID uuid;
    private String username;
    private String email;
    private String password;

    private UserEntity(UUID uuid, String username, String password, String email) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public static UserEntity from(ApplicationUserDto user) {
        return new UserEntity(user.uuid(), user.username(), user.password(), user.email());
    }

    public static ApplicationUserDto toApplicationUser(UserEntity dto){
        if(dto == null) return null;
        return new ApplicationUserDto(dto.uuid, dto.username, dto.password, dto.email);
    }
}
