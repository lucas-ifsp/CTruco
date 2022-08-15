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

package com.bueno.auth.security;

import com.bueno.domain.usecases.user.FindUserUseCase;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final FindUserUseCase findUserUseCase;

    public ApplicationUserService(FindUserUseCase findUserUseCase) {
        this.findUserUseCase = findUserUseCase;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final ApplicationUserDto dto = findUserUseCase.findByUsername(username);
        return ApplicationUser.ofUserDTO(dto);
    }

    public UserDetails loadUserById(UUID uuid) throws UsernameNotFoundException {
        final ApplicationUserDto dto = findUserUseCase.findByUUID(uuid);
        return ApplicationUser.ofUserDTO(dto);
    }
}
