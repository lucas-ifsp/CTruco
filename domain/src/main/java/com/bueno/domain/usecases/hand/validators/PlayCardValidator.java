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

import com.bueno.domain.entities.game.PossibleAction;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

public class PlayCardValidator extends Validator<PlayCardUseCase.RequestModel> {

    private final ActionValidator actionValidator;

    public PlayCardValidator(GameRepository repo, PossibleAction action) {
        this.actionValidator = new ActionValidator(repo, action);
    }

    @Override
    public Notification validate(PlayCardUseCase.RequestModel input) {
        if(input.card() == null) return new Notification("Card is null");
        return actionValidator.validate(input.requester());
    }
}