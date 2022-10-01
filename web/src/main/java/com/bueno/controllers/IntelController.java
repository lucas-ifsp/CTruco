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

import com.bueno.domain.usecases.intel.HandleIntelUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/games/players/{uuid}")
public class IntelController {

    private final HandleIntelUseCase intelUseCase;

    public IntelController(HandleIntelUseCase intelUseCase) {
        this.intelUseCase = intelUseCase;
    }

    @GetMapping(path = "/in-turn")
    private ResponseEntity<?> isPlayerTurn(@PathVariable UUID uuid){
        final var responseModel = intelUseCase.isPlayerTurn(uuid);
        return ResponseEntity.ok(responseModel);
    }

    @GetMapping(path = "/last-intel")
    private ResponseEntity<?> getLastIntel(@PathVariable UUID uuid){
        final var responseModel = intelUseCase.findLastIntel(uuid);
        return ResponseEntity.ok(responseModel);
    }

    @GetMapping(path = "/intel-since/{timestamp}")
    private ResponseEntity<?> getIntelSince(@PathVariable UUID uuid, @PathVariable Instant timestamp){
        final var intelSince = intelUseCase.findIntelSince(uuid, timestamp);
        return ResponseEntity.ok(intelSince);
    }
}
