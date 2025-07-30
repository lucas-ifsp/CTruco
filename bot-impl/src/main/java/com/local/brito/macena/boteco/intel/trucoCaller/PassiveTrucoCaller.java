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

package com.local.brito.macena.boteco.intel.trucoCaller;

import com.local.brito.macena.boteco.interfaces.TrucoCaller;
import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.brito.macena.boteco.utils.MyHand;

import java.util.List;

public class PassiveTrucoCaller implements TrucoCaller {

    @Override
    public boolean shouldCallTruco(GameIntel intel, Status status) {
        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());
        int scoreDistance = intel.getScore() - intel.getOpponentScore();
        List<TrucoCard> myCards = intel.getCards();

        if (scoreDistance <= -9 && (status == Status.MEDIUM || status == Status.BAD) && myCards.size() == 3) {
            return true;
        }
        if (status == Status.EXCELLENT && myCards.size() <= 2) {
            return true;
        }
        if (status == Status.GOOD && myCards.size() == 2 && myHand.powerOfCard(intel, 1) >= 8) {
            return true;
        }

        return intel.getOpponentCard().isPresent() && myCards.size() == 1;
    }
}
