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

package com.bueno.controllers;

import com.bueno.domain.usecases.user.RegisterUserUseCase;
import com.bueno.domain.usecases.user.FindUserUseCase;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.domain.usecases.user.dtos.RegisterUserRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final PasswordEncoder encoder;


    public UserController(RegisterUserUseCase registerUserUseCase, FindUserUseCase findUserUseCase, PasswordEncoder encoder) {
        this.registerUserUseCase = registerUserUseCase;
        this.findUserUseCase = findUserUseCase;
        this.encoder = encoder;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> create(@RequestBody RegisterUserRequestDto request){
        final String encodedPassword = encoder.encode(request.password());
        final RegisterUserRequestDto encodedPasswordRequest = new RegisterUserRequestDto(
                request.username(),
                encodedPassword,
                request.email());

        final var response = registerUserUseCase.create(encodedPasswordRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/api/v1/user/by_uuid/{playerUuid}")
    public ResponseEntity<?> find(@PathVariable UUID playerUuid){
        final var response = findUserUseCase.findByUUID(playerUuid);
        final ApplicationUserDto responseWithoutPassword = new ApplicationUserDto(
                response.uuid(),
                response.username(),
                null,
                response.email());

        return ResponseEntity.ok(responseWithoutPassword);
    }
}
