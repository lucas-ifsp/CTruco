/*
 *  Copyright (C) 2024 Rafael Feltrim and Lucas M. de Barros
 *  Contact: rafael <dot> feltrim <at> ifsp <dot> edu <dot> br
 *  Contact: lucas <dot> mesquita <at> ifsp <dot> edu <dot> br
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


package com.rafael.lucas.mestrimbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class Mestrim implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return Strategy.of(intel).getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return Strategy.of(intel).decideIfRaises();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return Strategy.of(intel).chooseCard();
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return Strategy.of(intel).getRaiseResponse();
    }
}
