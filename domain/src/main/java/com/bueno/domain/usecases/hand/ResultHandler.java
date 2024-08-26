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

package com.bueno.domain.usecases.hand;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.usecases.game.converter.GameResultConverter;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.game.usecase.SaveGameResultUseCase;
import com.bueno.domain.usecases.hand.converter.HandResultConverter;
import com.bueno.domain.usecases.intel.converters.IntelConverter;
import com.bueno.domain.usecases.intel.dtos.IntelDto;

class ResultHandler {

    private final GameRepository gameRepository;
    private final GameResultRepository gameResultRepository;
    private final HandResultRepository handResultRepository;

    ResultHandler(GameRepository gameRepository, GameResultRepository gameResultRepository, HandResultRepository handResultRepository) {
        this.gameRepository = gameRepository;
        this.gameResultRepository = gameResultRepository;
        this.handResultRepository = handResultRepository;
    }

    IntelDto handle(Game game) {
        game.currentHand().getResult().ifPresent(unused -> {
            if (handResultRepository != null) handResultRepository.save(HandResultConverter.of(game));
            updateGameStatus(game);
        });

        if (game.isDone()) {
//            SaveGameResultUseCase usecase = new SaveGameResultUseCase(gameRepository, gameResultRepository);
//            usecase.save(GameResultConverter.toDto(game));
            return IntelConverter.toDto(game.getIntel());
        }
        return null;
    }

    private void updateGameStatus(Game game) {
        game.updateScores();
        if (!game.isDone()) game.prepareNewHand();
    }
}
