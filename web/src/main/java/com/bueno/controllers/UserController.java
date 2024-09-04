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

import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.game.usecase.ReportTopWinnersUseCase;
import com.bueno.domain.usecases.game.usecase.UserRecordUseCase;
import com.bueno.domain.usecases.game.dtos.UserRecordDto;
import com.bueno.domain.usecases.user.FindUserUseCase;
import com.bueno.domain.usecases.user.RegisterUserUseCase;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import com.bueno.domain.usecases.user.dtos.RegisterUserRequestDto;
import com.bueno.domain.usecases.user.dtos.RegisterUserResponseDto;
import com.bueno.responses.ResponseBuilder;
import com.bueno.responses.ResponseEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final UserRecordUseCase userRecordUseCase;
    private final PasswordEncoder encoder;
    private final GameResultRepository gameResultRepository;

    public UserController(RegisterUserUseCase registerUserUseCase, FindUserUseCase findUserUseCase, UserRecordUseCase userRecordUseCase, PasswordEncoder encoder, GameResultRepository gameResultRepository) {
        this.registerUserUseCase = registerUserUseCase;
        this.findUserUseCase = findUserUseCase;
        this.userRecordUseCase = userRecordUseCase;
        this.encoder = encoder;
        this.gameResultRepository = gameResultRepository;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<RegisterUserResponseDto> create(@RequestBody RegisterUserRequestDto request) {
        final String encodedPassword = encoder.encode(request.password());
        final RegisterUserRequestDto encodedPasswordRequest = new RegisterUserRequestDto(
                request.username(),
                encodedPassword,
                request.email());

        final var response = registerUserUseCase.create(encodedPasswordRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/users/top-winners")
    private ResponseEntity<?> topWinners() {
        try {
            ReportTopWinnersUseCase useCase = new ReportTopWinnersUseCase(gameResultRepository);
            var response = useCase.create(5);
            return new ResponseBuilder(HttpStatus.OK)
                    .addEntry(new ResponseEntry("topWinners", response))
                    .addTimestamp()
                    .build();
        } catch (Exception e) {
            return new ResponseBuilder(HttpStatus.NOT_FOUND)
                    .addEntry(new ResponseEntry("error", "the server couldn't found the top winners"))
                    .addTimestamp()
                    .build();
        }
    }

    @GetMapping(path = "/api/v1/users/{uuid}")
    public ResponseEntity<?> find(@PathVariable UUID uuid) {
        final var response = findUserUseCase.findByUUID(uuid);
        final ApplicationUserDto responseWithoutPassword = new ApplicationUserDto(
                response.uuid(),
                response.username(),
                "",
                response.email());

        return ResponseEntity.ok(responseWithoutPassword);
    }

    @GetMapping(path = "/api/v1/users/{uuid}/matches")
    public UserRecordDto removeGame(@PathVariable UUID uuid) {
        return userRecordUseCase.listByUuid(uuid);
    }
}
