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

package com.bueno.domain.usecases.bot.impl;

import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.usecases.bot.spi.BotServiceProvider;
import com.bueno.domain.usecases.bot.spi.GameIntel;

public class MineiroBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int handPointsProposal = intel.getHandPoints() == 1 ? 3 : intel.getHandPoints() + 3;
        return PlayingStrategy.of(intel).getRaiseResponse(handPointsProposal);
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return PlayingStrategy.getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return PlayingStrategy.of(intel).decideIfRaises();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return PlayingStrategy.of(intel).chooseCard();
    }
}
