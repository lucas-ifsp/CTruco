/*
 *  Copyright (C) 2023 Lucas Pereira dos Santos and Felipe Santos Lourenço
 *  Contact: lucas <dot> pereira3 <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: santos <dot> lourenco <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.lucas.felipe.newbot;


import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;


public class NewBot implements BotServiceProvider {
    public boolean getMaoDeOnzeResponse(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).getMaoDeOnzeResponse(intel);
    }

    public boolean decideIfRaises(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).decideIfRaises(intel);
    }

    public CardToPlay chooseCard(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).chooseCard(intel);
    }

    public int getRaiseResponse(GameIntel intel){
        return decideStrategyToPlay(getRound(intel)).getRaiseResponse(intel);
    }

    private BotServiceProvider decideStrategyToPlay(int round) {
        return switch (round) {
            case 0 -> new FirstRoundStrategy();
            case 1 -> new SecondRoundStrategy();
            case 2 -> new ThirdRoundStrategy();
            default -> throw new IllegalArgumentException("Invalid round number: " + round);
        };
    }

    private int getRound(GameIntel intel) {
        return intel.getRoundResults().size();
    }
}
