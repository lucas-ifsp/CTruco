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

import com.bueno.domain.usecases.hand.PointsProposalUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/games/players/{uuid}")
public class PointsController {

    private final PointsProposalUseCase pointsUseCase;

    public PointsController(PointsProposalUseCase pointsUseCase) {
        this.pointsUseCase = pointsUseCase;
    }

    @PostMapping("/raised-points")
    private ResponseEntity<?> raise(@PathVariable UUID uuid){
        final var intel = pointsUseCase.raise(uuid);
        return ResponseEntity.ok(intel);
    }

    @PostMapping("/accepted-bet")
    private ResponseEntity<?> accept(@PathVariable UUID uuid){
        final var intel = pointsUseCase.accept(uuid);
        return ResponseEntity.ok(intel);
    }

    @PostMapping("/quit-hand")
    private ResponseEntity<?> quit(@PathVariable UUID uuid){
        final var intel = pointsUseCase.quit(uuid);
        return ResponseEntity.ok(intel);
    }
}
