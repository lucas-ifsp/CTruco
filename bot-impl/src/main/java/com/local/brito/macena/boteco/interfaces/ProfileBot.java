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

package com.local.brito.macena.boteco.interfaces;

import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.brito.macena.boteco.factories.InstanceFactory;

import java.util.List;

public abstract class ProfileBot {
    public CardToPlay choose(GameIntel intel, Status status) {
        ProfileBot profileBot = InstanceFactory.createProfileBot(intel, status);
        List<TrucoCard> myCards = intel.getCards();
        if (myCards.size() == 3) return profileBot.firstRoundChoose();
        if (myCards.size() == 2) return profileBot.secondRoundChoose();
        else return profileBot.thirdRoundChoose();
    }

    public abstract CardToPlay firstRoundChoose();
    public abstract CardToPlay secondRoundChoose();
    public abstract CardToPlay thirdRoundChoose();
}