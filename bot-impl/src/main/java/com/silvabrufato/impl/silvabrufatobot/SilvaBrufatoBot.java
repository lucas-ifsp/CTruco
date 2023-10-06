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

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class SilvaBrufatoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaoDeOnzeResponse'");
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decideIfRaises'");
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        BotStrategy botStrategy = null;
        if(intel.getRoundResults().isEmpty()) botStrategy = BotStrategy.FIRST_HAND_STRATEGY;
        if(intel.getRoundResults().size() == 1) botStrategy = BotStrategy.SECOND_HAND_STRATEGY;
        if(intel.getRoundResults().size() == 2) botStrategy = BotStrategy.THIRD_HAND_STRATEGY;
        return botStrategy.throwCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRaiseResponse'");
    }

}
