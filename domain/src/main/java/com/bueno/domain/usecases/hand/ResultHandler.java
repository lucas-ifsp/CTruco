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
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.usecases.game.SaveGameResultUseCase;
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.intel.converters.IntelConverter;
import com.bueno.domain.usecases.intel.dtos.IntelDto;

class ResultHandler {

    private final SaveGameResultUseCase saveGameResultUseCase;

    ResultHandler(SaveGameResultUseCase saveGameResultUseCase) {
        this.saveGameResultUseCase = saveGameResultUseCase;
    }

    IntelDto handle(Game game){
        final Hand currentHand = game.currentHand();
        currentHand.getResult().ifPresent(unused -> updateGameStatus(game));

        if (game.isDone()){
            if(saveGameResultUseCase != null) saveGameResultUseCase.save(GameConverter.of(game));
            return IntelConverter.of(game.getIntel());
        }
        return null;
    }

    private void updateGameStatus(Game game) {
        game.updateScores();
        if(!game.isDone()) game.prepareNewHand();
    }

}
