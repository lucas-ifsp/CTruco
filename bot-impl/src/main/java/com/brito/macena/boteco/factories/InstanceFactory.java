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

package com.brito.macena.boteco.factories;

import com.brito.macena.boteco.intel.analyze.Pattern;
import com.brito.macena.boteco.intel.analyze.Trucador;
import com.brito.macena.boteco.intel.profiles.Agressive;
import com.brito.macena.boteco.intel.profiles.Passive;
import com.brito.macena.boteco.intel.trucoCaller.PassiveTrucoCaller;
import com.brito.macena.boteco.intel.trucoCaller.SneakyTrucoCaller;
import com.brito.macena.boteco.intel.trucoResponder.PassiveTrucoResponder;
import com.brito.macena.boteco.intel.trucoResponder.SneakyTrucoResponder;
import com.brito.macena.boteco.interfaces.Analyzer;
import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.interfaces.TrucoCaller;
import com.brito.macena.boteco.interfaces.TrucoResponder;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;

public class InstanceFactory {
    public static Analyzer createAnaliseInstance(GameIntel intel) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDifference = myScore - oppScore;

        if (oppScore > myScore && scoreDifference < -6) {
            return new Trucador(intel);
        } else {
            return new Pattern(intel);
        }
    }

    public static ProfileBot createProfileBot(GameIntel intel, Status status) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDifference = myScore - oppScore;

        if (oppScore > myScore && scoreDifference < -6) {
            return new Passive(intel, status);
        } else {
            return new Agressive(intel, status);
        }
    }

    public static TrucoCaller createTrucoCallerInstance(GameIntel intel) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDistance = myScore - oppScore;

        if (scoreDistance < -4) {
            return new SneakyTrucoCaller();
        } else {
            return new PassiveTrucoCaller();
        }
    }

    public static TrucoResponder createTrucoResponder(GameIntel intel) {
        int myScore = intel.getScore();
        int oppScore = intel.getOpponentScore();
        int scoreDifference = myScore - oppScore;

        if (oppScore > myScore && scoreDifference <= -6) {
            return new SneakyTrucoResponder();
        }
//        if (oppScore > myScore && scoreDifference <= -4) {
//            return new AggressiveTrucoResponder();
//        }
        return new PassiveTrucoResponder();
    }
}
