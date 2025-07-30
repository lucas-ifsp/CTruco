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

package com.local.brito.macena.boteco.intel.trucoResponder;

import com.local.brito.macena.boteco.interfaces.TrucoResponder;
import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.brito.macena.boteco.factories.InstanceFactory;
import com.local.brito.macena.boteco.interfaces.Analyzer;
import com.local.brito.macena.boteco.utils.Game;

import java.util.List;

public class PassiveTrucoResponder implements TrucoResponder {

    @Override
    public int getRaiseResponse(GameIntel intel, Status status) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDistance = myScore - oppScore;
        List<TrucoCard> myCards = intel.getCards();

        if (!myCards.isEmpty()){
            Analyzer analise = InstanceFactory.createAnaliseInstance(intel);
            status = analise.myHand(intel);
        }

        if (status == Status.EXCELLENT) return 1;

        if (myCards.size() == 3){
            if (status == Status.BAD) return -1;
            else return 0;
        }

        if (myCards.size() == 2){
            if (Game.wonFirstRound(intel)) return 0;

            if (Game.lostFirstRound(intel)){
                if (status == Status.GOOD) return 0;
                return -1;
            }
            if (status == Status.GOOD && scoreDistance <= -6) {
                return 1;
            }
            if (status == Status.GOOD) return 0;
            return -1;
        }

        if (status == Status.GOOD && scoreDistance <= -6) return 1;
        if (status == Status.BAD || (status == Status.MEDIUM && scoreDistance <= -4)) return -1;
        return 0;
    }
}
