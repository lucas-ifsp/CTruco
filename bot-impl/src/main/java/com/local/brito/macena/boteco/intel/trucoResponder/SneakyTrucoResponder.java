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

import java.util.List;
import java.util.Random;

public class SneakyTrucoResponder implements TrucoResponder {

    private final Random random = new Random();

    @Override
    public int getRaiseResponse(GameIntel intel, Status status) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDifference = myScore - oppScore;
        List<TrucoCard> myCards = intel.getCards();

        if (!myCards.isEmpty()) {
            Analyzer analyzer = InstanceFactory.createAnaliseInstance(intel);
            status = analyzer.myHand(intel);
        }

        if (status == Status.EXCELLENT) {
            return 1;
        }

        if (status == Status.GOOD) {
            return random.nextInt(100) < 80 ? 1 : 0;
        }

        if (status == Status.MEDIUM) {
            return random.nextInt(100) < 50 ? 1 : 0;
        }

        if (status == Status.BAD) {
            if (random.nextInt(100) < 25) {
                return 1;
            } else {
                return -1;
            }
        }

        if (scoreDifference <= -6 && random.nextInt(100) < 50) {
            return 1;
        }

        return 0;
    }
}