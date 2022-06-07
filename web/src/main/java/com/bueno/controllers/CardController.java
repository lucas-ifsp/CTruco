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

import com.bueno.domain.usecases.hand.dtos.PlayCardDto;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.intel.HandleIntelUseCase;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/games/players/{uuid}/cards")
public class CardController {

    private final PlayCardUseCase playCardUseCase;
    private final HandleIntelUseCase intelUseCase;

    public CardController(PlayCardUseCase playCardUseCase, HandleIntelUseCase intelUseCase) {
        this.playCardUseCase = playCardUseCase;
        this.intelUseCase = intelUseCase;
    }

    @PostMapping("/played")
    private ResponseEntity<?> play(@PathVariable UUID uuid, @RequestBody CardDto card){
        final var requestModel = new PlayCardDto(uuid, card);
        final var intel = playCardUseCase.playCard(requestModel);
        return ResponseEntity.ok(intel);
    }

    @PostMapping("/discarded")
    private ResponseEntity<?>  discard(@PathVariable UUID uuid, @RequestBody CardDto card){
        final var requestModel = new PlayCardDto(uuid, card);
        final var intel = playCardUseCase.discard(requestModel);
        return ResponseEntity.ok(intel);
    }

    @GetMapping
    private ResponseEntity<?> getCards(@PathVariable UUID uuid){
        final var responseModel = intelUseCase.ownedCards(uuid);
        return ResponseEntity.ok(responseModel);
    }
}
