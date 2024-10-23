/*
 *  Copyright (C) 2024 Gabriel Henrique Costa Cruzeiro, Kayky Bruno Rocha - IFSP/SCL
 *  Contact: gabriel <dot> cruzeiro <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: kayky <dot> rocha <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.gabriel.kayky.coisaruim;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;



public class CoisaRuim implements BotServiceProvider{

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return getRound(intel.getRoundResults().size()).getMaoDeOnzeResponse(intel);

    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return getRound(intel.getRoundResults().size()).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return getRound(intel.getRoundResults().size()).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return getRound(intel.getRoundResults().size()).getRaiseResponse(intel);
    }

    private GameStrategy getRound(int roundNumber) {
        return switch (roundNumber) {
            case 0 -> new FirstRound();
            case 1 -> new SecondRound();
            case 2 -> new ThirdRound();
            default -> throw new IllegalStateException("Unexpected value: " + roundNumber);
        };
    }

}
