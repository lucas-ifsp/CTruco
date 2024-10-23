/*
 *  Copyright (C) 2024 Alan Andrade Vasconi de Souza - IFSP/SCL and Ian de Oliveira Fernandes - IFSP/SCL
 *  Contact: alan<dot>vasconi<at>aluno<dot>ifsp<dot>edu<dot>br
 *  Contact: ian<dot>f<at>aluno<dot>ifsp<dot>edu<dot>br
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

package com.alanIan.casinhadecabloco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class CasinhaDeCabloco implements BotServiceProvider {

    private int numberOfRounds(GameIntel intel){
        return intel.getRoundResults().size();
    }

    public GameState gameState(GameIntel intel) {
        return switch (numberOfRounds(intel)) {
            case 0 -> new FirstRound(intel);
            case 1 -> new SecondRound(intel);
            default -> new ThirdRound(intel);
        };
    }
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return gameState(intel).getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return gameState(intel).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return gameState(intel).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return gameState(intel).getRaiseResponse(intel);
    }
}