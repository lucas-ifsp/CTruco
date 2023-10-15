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

// Authors: Juan Rossi e Guilherme Lopes

package com.rossi.lopes.trucoguru;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class TrucoGuru implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        final Boolean isMaoDeOnze = intel.getScore() == 11;
        if (isMaoDeOnze) return false;

        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        GameIntel.RoundResult lastRound = roundResults.get(roundResults.size() - 1);
        Boolean hasWonLastRound = lastRound == GameIntel.RoundResult.WON;
        Boolean hasStrongCard = TrucoGuruUtils.hasStrongCard(intel.getCards(), intel.getVira());

        if (hasWonLastRound && hasStrongCard) return true;


        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(intel.getOpponentScore() >= 11) return -1;
        if(intel.getHandPoints() == 12) return -1;

        Boolean hasCasalMaior = TrucoGuruUtils.hasCasalMaior(intel.getCards(), intel.getVira());
        if(hasCasalMaior) return 1;
        Boolean hasCasalMenor = TrucoGuruUtils.hasCasalMenor(intel.getCards(), intel.getVira());
        if(hasCasalMenor) return 1;

        return 0;
    }
}
