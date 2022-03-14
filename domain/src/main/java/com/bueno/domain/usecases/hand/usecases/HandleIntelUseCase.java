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

package com.bueno.domain.usecases.hand.usecases;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.validators.IntelRequestValidator;
import com.bueno.domain.usecases.utils.UnsupportedGameRequestException;
import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HandleIntelUseCase {

    private final GameRepository repo;

    public HandleIntelUseCase(GameRepository repo) {
        this.repo = Objects.requireNonNull(repo);
    }

    public List<Intel> findIntelSince(UUID usedUuid, Intel lastIntel){
        validateInput(usedUuid);
        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        return game.getIntelSince(lastIntel);
    }

    public List<Card> getOwnedCards(UUID usedUuid){
        validateInput(usedUuid);
        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Player player = game.getPlayer1().getUuid().equals(usedUuid) ? game.getPlayer1() : game.getPlayer2();
        return List.copyOf(player.getCards());
    }

    private void validateInput(UUID usedUuid) {
        final Validator<UUID> validator = new IntelRequestValidator(repo);
        final Notification notification = validator.validate(usedUuid);
        if (notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());
    }
}
