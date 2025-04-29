/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.local.brito.macena.boteco.utils;

import com.bueno.spi.model.GameIntel;

public class Game {
    public static boolean wonFirstRound(GameIntel intel) {
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
        return false;
    }

    public static boolean lostFirstRound(GameIntel intel) {
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST;
        return false;
    }

    public static boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(card -> card.isManilha(intel.getVira()));
    }

    public static boolean isCriticalSituation(GameIntel intel) {
        int scoreDifference = intel.getOpponentScore() - intel.getScore();
        int handPoints = intel.getHandPoints();

        return scoreDifference >= 6 || handPoints >= 6;
    }
}
