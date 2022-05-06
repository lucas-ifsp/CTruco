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

package com.bueno.domain.usecases.game.dtos;

import java.util.Objects;
import java.util.UUID;

public record CreateForBotsDto(UUID bot1Uuid, String bot1Name, UUID bot2Uuid, String bot2Name) {
    public CreateForBotsDto(UUID bot1Uuid, String bot1Name, UUID bot2Uuid, String bot2Name) {
        this.bot1Uuid = Objects.requireNonNull(bot1Uuid, "Bot1 UUID must not be null!");
        this.bot1Name = Objects.requireNonNull(bot1Name, "Bot1 name must not be null!");
        if (bot1Name.isEmpty()) throw new IllegalArgumentException("Bot1 name must not be empty!");
        this.bot2Uuid = Objects.requireNonNull(bot2Uuid, "Bot2 UUID must not be null!");
        this.bot2Name = Objects.requireNonNull(bot2Name, "Bot2 name must not be null!");
        if (bot2Name.isEmpty()) throw new IllegalArgumentException("Bot2 name must not be empty!");
    }
}
