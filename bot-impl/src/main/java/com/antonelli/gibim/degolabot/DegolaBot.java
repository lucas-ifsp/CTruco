/*
 *  Copyright (C) 2025 Daniel da Silva Gibim and Mauricio Antonelli de Oliveira
 *  Contact: daniel <dot> gibim <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: a <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class DegolaBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int round = getRoundNumber(intel);
        return getStrategyForRound(round).getRaiseResponse(intel);
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int soma = BotUtils.handStrength(intel);
        return soma > 21;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int round = getRoundNumber(intel);
        return getStrategyForRound(round).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int round = getRoundNumber(intel);
        return getStrategyForRound(round).chooseCard(intel);
    }

    public Strategy getStrategyForRound(int round) {
        return switch (round) {
            case 1 -> new FirstRound();
            case 2 -> new SecondRound();
            case 3 -> new ThirdRound();
            default -> throw new IllegalStateException("Unexpected value: " + round);
        };
    }

    public int getRoundNumber(GameIntel intel) {
        int playedRounds = intel.getRoundResults().size();
        return playedRounds + 1;
    }
}
