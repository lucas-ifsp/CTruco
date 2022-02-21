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

package com.bueno.bots.mineirobot;

import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.bot.BotService;

public class MineiroBotService implements BotService {

    @Override
    public int getRaiseResponse(Player bot, Intel intel) {
        return PlayingStrategy.of(bot, intel).getRaiseResponse(intel.scoreProposal().orElseThrow());
    }

    @Override
    public boolean getMaoDeOnzeResponse(Player bot, Intel intel) {
        return PlayingStrategy.getMaoDeOnzeResponse(bot, intel);
    }

    @Override
    public boolean decideIfRaises(Player bot, Intel intel) {
        return PlayingStrategy.of(bot, intel).decideIfRaises();
    }

    @Override
    public CardToPlay chooseCard(Player bot, Intel intel) {
        return PlayingStrategy.of(bot, intel).chooseCard();
    }
}
