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
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.utils.exceptions.GameNotFoundException;
import com.bueno.domain.usecases.utils.validation.Notification;
import com.bueno.domain.usecases.utils.validation.Validator;

import java.util.Objects;
import java.util.UUID;

public class ActionValidator extends Validator<UUID> {
    private final GameRepository gameRepository;
    private final PossibleAction action;

    public ActionValidator(GameRepository gameRepository, PossibleAction action) {
        this.gameRepository = gameRepository;
        this.action = action;
    }

    @Override
    public Notification validate(UUID uuid) {
        if(uuid == null) throw new NullPointerException("UUID is null.");
        final var game = gameRepository.findByPlayerUuid(uuid).map(GameConverter::fromDto)
                .orElseThrow(() -> new GameNotFoundException("User with UUID " + uuid + " is not in an active game."));
//        if(game.isDone()) throw new GameNotFoundException("Game is over. Start a new game.");//TODO Definir comportamento do mongodb quando um jogo ativo já acabou

        final var requester = getRequester(uuid, Objects.requireNonNull(game));
        final var currentHand = game.currentHand();
        final var possibleActions = currentHand.getPossibleActions();
        final var notification = new Notification();

        if(!currentHand.getCurrentPlayer().equals(requester))
            notification.addError("User with UUID " + uuid + " in not the current player.");

        if(!possibleActions.contains(action))
            notification.addError("Invalid action for hand state. Valid actions: " + possibleActions);

        return notification;
    }

    private Player getRequester(UUID uuid, Game game) {
        return game.getPlayer1().getUuid().equals(uuid) ? game.getPlayer1() : game.getPlayer2();
    }
}