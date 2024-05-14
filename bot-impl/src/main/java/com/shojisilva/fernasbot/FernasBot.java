/*
 *  Copyright (C) 2024 Henrique Shoji Kato - IFSP/SCL and Luiz Henrique do Nascimento Silva - IFSP/SCL
 *  Contact: henrique <dot> kato <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: nascimento <dot> henrique1 <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.shojisilva.fernasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class FernasBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return getInstanceByRound(intel.getRoundResults().size()).getRaiseResponse(intel);
    }

    private FernasStrategy getInstanceByRound(int roundNumber) {
        return switch (roundNumber) {
            case 0 -> new FernasFirstHand();
            case 1 -> new FernasSecondHand();
            case 2 -> new FernasThirdHand();
            default -> throw new IllegalStateException("Unexpected value: " + roundNumber);
        };
    }
}
