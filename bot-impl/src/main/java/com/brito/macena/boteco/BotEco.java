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

package com.brito.macena.boteco;

import com.brito.macena.boteco.factories.InstanceFactory;
import com.brito.macena.boteco.interfaces.Analyzer;
import com.brito.macena.boteco.interfaces.ProfileBot;
import com.brito.macena.boteco.interfaces.TrucoCaller;
import com.brito.macena.boteco.utils.MyHand;
import com.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class BotEco implements BotServiceProvider {
    private Status status;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        updateStatus(intel);
        MyHand myHand = new MyHand(intel.getCards(), intel.getVira());
        if (status == Status.EXCELLENT) return true;
        if (status == Status.GOOD) {
            int scoreDistance = intel.getScore() - intel.getOpponentScore();
            return scoreDistance == 0 || myHand.powerOfCard(intel, 0) >= 3;
        } return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        updateStatus(intel);
        TrucoCaller trucoCaller = InstanceFactory.createTrucoCallerInstance(intel);
        return trucoCaller.shouldCallTruco(intel, status);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        ProfileBot profileBot = InstanceFactory.createProfileBot(intel, Status.MEDIUM);
        return profileBot.choose(intel, Status.MEDIUM);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "BotEco :)";
    }

    private void updateStatus(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        if (!myCards.isEmpty()){
            Analyzer analise = InstanceFactory.createAnaliseInstance(intel);
            status = analise.myHand(intel);
        }
    }
}
