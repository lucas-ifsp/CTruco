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

package com.bueno.domain.usecases.game;

import java.util.Objects;
import java.util.UUID;

public record RequestModelOfUserAndBot(UUID userUuid, String botName) {
    public RequestModelOfUserAndBot(UUID userUuid, String botName) {
        this.userUuid = Objects.requireNonNull(userUuid, "User UUID must not be null!");
        this.botName = Objects.requireNonNull(botName, "Bot name must not be null!");
        if(botName.isEmpty()) throw new IllegalArgumentException("Bot name must not be empty!");
    }
}