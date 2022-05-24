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

package com.bueno.domain.usecases.hand.validator;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.FindGameUseCase;
import com.bueno.domain.usecases.utils.validation.Notification;
import com.bueno.domain.usecases.utils.validation.Validator;

import java.util.Objects;
import java.util.UUID;

public class ActionValidator extends Validator<UUID> {

    private final FindGameUseCase findGameUseCase;
    private final PossibleAction action;

    public ActionValidator(FindGameUseCase findGameUseCase, PossibleAction action) {
        this.findGameUseCase = findGameUseCase;
        this.action = action;
    }

    @Override
    public Notification validate(UUID uuid) {
        if(uuid == null) return new Notification("UUID is null.");
        final var game = findGameUseCase.findByUserUuid(uuid).orElse(null);
        if(game == null) return new Notification("User with UUID " + uuid + " is not in an active game.");
        if(game.isDone()) return new Notification("Game is over. Start a new game.");

        final var requester = getRequester(uuid, Objects.requireNonNull(game));
        final var currentHand = game.currentHand();
        final var possibleActions = currentHand.getPossibleActions();

        final var notification = new Notification();

        if(!currentHand.getCurrentPlayer().equals(requester))
            notification.addError("User with UUID " + uuid + " is not in not the current player.");

        if(!possibleActions.contains(action))
            notification.addError("Invalid action for hand state. Valid actions: " + possibleActions);

        return notification;
    }

    private Player getRequester(UUID uuid, Game game) {
        return game.getPlayer1().getUuid().equals(uuid) ? game.getPlayer1() : game.getPlayer2();
    }
}