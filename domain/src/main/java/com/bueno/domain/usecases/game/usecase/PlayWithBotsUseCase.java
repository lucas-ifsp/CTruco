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

package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.service.SimulationService;

import java.util.List;
import java.util.UUID;

public class PlayWithBotsUseCase {

    private final UUID uuidBot1;
    private final String bot1Name;
    private final String bot2Name;

    public PlayWithBotsUseCase(UUID uuidBot1, String bot1Name, String bot2Name) {
        this.uuidBot1 = uuidBot1;
        this.bot1Name = bot1Name;
        this.bot2Name = bot2Name;
    }

    public List<PlayWithBotsDto> playWithBots(int times) {
        final var simulator = new SimulationService(uuidBot1, bot1Name, bot2Name);
        return simulator.runInParallel(times);
    }

}
