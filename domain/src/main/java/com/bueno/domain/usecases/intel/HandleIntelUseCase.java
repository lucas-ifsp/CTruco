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

package com.bueno.domain.usecases.intel;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.utils.UnsupportedGameRequestException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class HandleIntelUseCase {

    private final GameRepository repo;

    public HandleIntelUseCase(GameRepository repo) {
        this.repo = Objects.requireNonNull(repo);
    }

    public List<Intel> findIntelSince(UUID uuid, Instant lastIntelTimestamp){
        final Game game = getGameOrThrow(uuid);
        return game.getIntelSince(lastIntelTimestamp);
    }

    public List<Card> getOwnedCards(UUID uuid){
        final Game game = getGameOrThrow(uuid);
        final Player player = game.getPlayer1().getUuid().equals(uuid) ? game.getPlayer1() : game.getPlayer2();
        return List.copyOf(player.getCards());
    }

    public Boolean isPlayerTurn(UUID uuid) {
        final Game game = getGameOrThrow(uuid);
        return uuid.equals(game.getIntel().currentPlayerUuid().orElse(null));
    }

    private Game getGameOrThrow(UUID uuid) {
        Objects.requireNonNull(uuid, "UUID must not be null.");
        return repo.findByUserUuid(uuid).orElseThrow(
                () -> new UnsupportedGameRequestException("User with UUID " + uuid + " is not in an active game."));
    }
}
