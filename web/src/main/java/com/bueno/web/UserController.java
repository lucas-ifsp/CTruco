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

package com.bueno.web;

import com.bueno.domain.usecases.user.CreateUserUseCase;
import com.bueno.domain.usecases.user.FindUserUseCase;
import com.bueno.domain.usecases.user.UserRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserUseCase findUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, FindUserUseCase findUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserUseCase = findUserUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserRequestModel userRequestModel){
        final var response = createUserUseCase.create(userRequestModel);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/by_uuid/{playerUuid}")
    public ResponseEntity<?> find(@PathVariable UUID playerUuid){
        final var response = findUserUseCase.findByUUID(playerUuid);
        return ResponseEntity.ok(response);
    }
}
