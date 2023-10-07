/*
 *  Copyright (C) 2023 João Pedro da Silva and Renan Brufato
 *  Contact: jps <dot> spj <at> gmail <dot> com 
 *  Contact: brufato17 <dot> renan <at> gmail <dot> com
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

package com.silvabrufato.impl.silvabrufatobot;


import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;


public class SilvaBrufatoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(Utilities.countManilhas(intel) >= 2) return true;
        if(Utilities.countManilhas(intel) == 1 && Utilities.countCardsEqualOrHigherThanAce(intel) >= 1) return true;
        if(intel.getOpponentScore() >= 9) return false;
        return Utilities.countCardsEqualOrHigherThanAce(intel) >= 2;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decideIfRaises'");
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return switch(intel.getRoundResults().size()) {
            case 0 -> BotStrategyToCards.FIRST_HAND_STRATEGY.throwCard(intel);
            case 1 -> BotStrategyToCards.SECOND_HAND_STRATEGY.throwCard(intel);
            case 2 -> BotStrategyToCards.FIRST_HAND_STRATEGY.throwCard(intel);
            default -> throw new IllegalArgumentException("Unexpected value: " + intel.getRoundResults().size());
        };
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRaiseResponse'");
    }

}
