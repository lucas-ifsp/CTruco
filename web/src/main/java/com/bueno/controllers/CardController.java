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

import com.bueno.domain.usecases.hand.PlayCardRequest;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/game/player/{playerUuid}/card")
public class CardController {

    private final PlayCardUseCase playCardUseCase;

    public CardController(PlayCardUseCase playCardUseCase) {
        this.playCardUseCase = playCardUseCase;
    }

    @PostMapping("/play")
    private ResponseEntity<?> play(@PathVariable UUID playerUuid, @RequestBody CardDto card){
        final var requestModel = new PlayCardRequest(playerUuid, card);
        final var intel = playCardUseCase.playCard(requestModel);
        return ResponseEntity.ok(intel);
    }

    @PostMapping("/discard")
    private ResponseEntity<?>  discard(@PathVariable UUID playerUuid, @RequestBody CardDto card){
        final var requestModel = new PlayCardRequest(playerUuid, card);
        final var intel = playCardUseCase.discard(requestModel);
        return ResponseEntity.ok(intel);
    }
}
