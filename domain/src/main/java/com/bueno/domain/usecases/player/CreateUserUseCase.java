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

import com.bueno.domain.entities.player.util.User;
import com.bueno.domain.usecases.utils.EntityAlreadyExistsException;
import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

import java.util.UUID;

public class CreateUserUseCase {
    private final UserRepository repo;

    public CreateUserUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public UUID create(RequestModel model){
        final Validator<RequestModel> validator = new CreateUserValidator();
        final Notification notification = validator.validate(model);

        if(notification.hasErrors()) throw new IllegalArgumentException(notification.errorMessage());

        repo.findByUsername(model.username).ifPresent(unused -> {
            throw new EntityAlreadyExistsException("This username is already in use.");});

        final User user = new User(model.username, model.email);

        repo.save(user);
        return user.getUuid();
    }

    public record RequestModel(String username, String email){}
}
