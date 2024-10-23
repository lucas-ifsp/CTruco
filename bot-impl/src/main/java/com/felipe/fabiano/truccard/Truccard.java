/*
 *  Copyright (C) 2024 Felipe G. de Marchi and Fabiano R. Junior - IFSP/SCL
 *  Contact: felipe <dot> corsi <at> ifsp <dot> edu <dot> br or
 *  fabiano <dot> j <at> ifsp <dot> edu <dot> br
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
 *  along with CTruco. If not, see <https://www.gnu.org/licenses/>
 */

package com.felipe.fabiano.truccard;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import static com.felipe.fabiano.truccard.TrucoUtils.*;

public class Truccard implements BotServiceProvider {
    //DECISION-MAKING PROCESS
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int numberOfManilhas = manilhaCounter(intel);
        if (numberOfManilhas>=2) return true;
        int enemyScore = intel.getOpponentScore();
        if (enemyScore>=8 && strongCardsCounter(intel)==3) return true;
        if (isPlayingSecond(intel)) {
            if (numberOfManilhas==1 && (intel.getCards().stream().filter(card -> card.relativeValue(intel.getVira())>=8).count()>=2)) return true;
            if (enemyScore>8 && strongCardsCounter(intel)==2 && intel.getCards().stream().anyMatch(card -> card.relativeValue(intel.getVira()) >= 9))return true;
            if (enemyScore>9 && strongCardsCounter(intel)==2) return true;
            return enemyScore > 9 && strongCardsCounter(intel) == 1 && intel.getCards().stream().filter(card -> card.relativeValue(intel.getVira()) >= 6).count() >= 2;
        } else {
            if (numberOfManilhas==1 && (intel.getCards().stream().filter(card -> card.relativeValue(intel.getVira())>8).count()>=2)) return true;
            if (enemyScore>8 && intel.getCards().stream().filter(card -> card.relativeValue(intel.getVira()) >= 9).count()>=2)return true;
            if (enemyScore>9 && strongCardsCounter(intel)==2) return true;
            return enemyScore > 9 && strongCardsCounter(intel) == 1 && intel.getCards().stream().filter(card -> card.relativeValue(intel.getVira()) >= 6).count() >= 2;
        }
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return getRoundToPlay(intel.getRoundResults().size()).decideIfRaises(intel);
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        return getRoundToPlay(intel.getRoundResults().size()).getRaiseResponse(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return getRoundToPlay(intel.getRoundResults().size()).chooseCard(intel);
    }

    private GameRound getRoundToPlay(int roundNumber) {
        return switch (roundNumber) {
            case 0 -> new FirstRoundState();
            case 1 -> new SecondRoundState();
            case 2 -> new ThirdRoundState();
            default -> throw new IllegalStateException("Unexpected Round Number: " + roundNumber);
        };
    }

    @Override
    public String getName() {
        return "Truccard";
    }
}