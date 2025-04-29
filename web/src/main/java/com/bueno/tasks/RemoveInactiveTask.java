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

package com.bueno.tasks;

import com.bueno.domain.usecases.game.usecase.RemoveGameUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

//TODO - fazer um criar rank utilizando @Scheduled 5hrs
@Component
public class RemoveInactiveTask {

    private static final Logger log = LoggerFactory.getLogger(RemoveInactiveTask.class);

    private final RemoveGameUseCase removeGameUseCase;

    public RemoveInactiveTask(RemoveGameUseCase removeGameUseCase) {
        this.removeGameUseCase = removeGameUseCase;
    }

    //@Scheduled(fixedRate = 30_000)
    public void reportCurrentTime() {
        final List<UUID> removedGames = removeGameUseCase.byInactivityAfter(5);
        removedGames.forEach(gameUuid -> log.info("Removed game {} due to inactivity.", gameUuid));
    }
}