/*
 *  Copyright (C) 2024 Eduardo D. Derisso - IFSP/SCL and Vinicius S. G. Oliveira - IFSP/SCL
 *  Contact: duarte <dot> derisso <at> ifsp <dot> edu <dot> br
 *  Contact: vinicius <dot> goncalves1 <at> ifsp <dot> edu <dot> br
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

package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

public class CamaleaoTruqueiro implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return RoundStrategy.of(intel).getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return RoundStrategy.of(intel).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return RoundStrategy.of(intel).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return RoundStrategy.of(intel).getRaiseResponse(intel);
    }
}
