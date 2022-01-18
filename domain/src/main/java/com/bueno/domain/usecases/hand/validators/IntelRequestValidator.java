/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.usecases.hand.validators;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

import java.util.UUID;

public class IntelRequestValidator extends Validator<UUID> {

    private final GameRepository repo;

    public IntelRequestValidator(GameRepository repo) {
        this.repo = repo;
    }

    @Override
    public Notification validate(UUID uuid) {
        if(uuid == null) return new Notification("UUID is null.");
        final Game game = repo.findByUserUuid(uuid).orElse(null);
        if(game == null) return new Notification("User with UUID " + uuid + " is not in an active game.");
        return new Notification();
    }
}
