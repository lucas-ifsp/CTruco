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

package com.bueno.domain.usecases.player;

import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

import java.util.Objects;

public class CreateUserValidator extends Validator<CreateUserUseCase.RequestModel> {

    @Override
    public Notification validate(CreateUserUseCase.RequestModel model) {
        if(model == null) return new Notification("Request model is null");

        final Notification notification = new Notification();
        if(model.username() == null) notification.addError("Username is null.");
        if(model.email() == null) notification.addError("E-mail is null.");
        if(notification.hasErrors()) return notification;

        if(Objects.requireNonNull(model.username()).isEmpty()) notification.addError("Username is empty");
        if(Objects.requireNonNull(model.email()).isEmpty()) notification.addError("E-mail is empty");
        return notification;
    }
}
