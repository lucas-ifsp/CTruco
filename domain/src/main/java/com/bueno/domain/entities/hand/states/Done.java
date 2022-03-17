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

package com.bueno.domain.entities.hand.states;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;

import java.util.EnumSet;

public class Done implements HandState {

    public Done(Hand context){
        context.setCurrentPlayer(null);
        context.setPossibleActions(EnumSet.noneOf(PossibleAction.class));
    }

    @Override
    public void playFirstCard(Player player, Card card) {
        throw new IllegalStateException("Can not play card because hand is done.");
    }

    @Override
    public void playSecondCard(Player player, Card card) {
        throw new IllegalStateException("Can not play card because hand is done.");
    }

    @Override
    public void accept(Player responder) {
        throw new IllegalStateException("Can not accept bet because hand is done.");
    }

    @Override
    public void quit(Player responder) {
        throw new IllegalStateException("Can not quit hand because hand is done.");
    }

    @Override
    public void raise(Player requester) {
        throw new IllegalStateException("Can not bet because hand is done.");
    }
}
