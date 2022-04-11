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

import com.bueno.domain.usecases.intel.HandleIntelUseCase;
import com.bueno.model.GameIntelResponse;
import com.bueno.model.PlayerCardsResponse;
import com.bueno.model.PlayerTurnResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/game/player/{playerId}")
public class IntelController {

    private final HandleIntelUseCase intelUseCase;

    public IntelController(HandleIntelUseCase intelUseCase) {
        this.intelUseCase = intelUseCase;
    }

    @GetMapping(path = "/cards")
    private ResponseEntity<?> getCards(@PathVariable UUID playerId){
        final var cards = intelUseCase.getOwnedCards(playerId);
        return ResponseEntity.ok(new PlayerCardsResponse(cards));
    }

    @GetMapping(path = "/in_turn")
    private ResponseEntity<?> isPlayerTurn(@PathVariable UUID playerId){
        final var isPlayerTurn = intelUseCase.isPlayerTurn(playerId);
        return ResponseEntity.ok(new PlayerTurnResponse(isPlayerTurn));
    }

    @GetMapping(path = "/intel_since/{lastIntelTimestamp}")
    private ResponseEntity<?> getIntelSince(@PathVariable UUID playerId, @PathVariable Instant lastIntelTimestamp){
        final var intelSince = intelUseCase.findIntelSince(playerId, lastIntelTimestamp);
        final var response = intelSince.stream()
                .map(GameIntelResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
