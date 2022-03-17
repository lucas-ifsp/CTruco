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
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.hand.HandPoints;
import com.bueno.domain.entities.intel.Event;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;

import java.util.EnumSet;

public class WaitingMaoDeOnze implements HandState {

    private final Hand context;

    public WaitingMaoDeOnze(Hand context) {
        this.context = context;
        this.context.setPossibleActions(EnumSet.of(PossibleAction.ACCEPT, PossibleAction.QUIT));
    }

    @Override
    public void playFirstCard(Player player, Card card) {
        throw new IllegalStateException("Can not play first card before deciding if plays mão de onze.");
    }

    @Override
    public void playSecondCard(Player player, Card card) {
        throw new IllegalStateException("Can not play second card before deciding if plays mão de onze.");
    }

    @Override
    public void accept(Player responder) {
        context.setPoints(HandPoints.THREE);
        context.setCurrentPlayer(context.getFirstToPlay());
        context.setPossibleActions(EnumSet.of(PossibleAction.PLAY));
        context.setState(new NoCard(context));
        context.updateHistory(Event.ACCEPT_HAND);
    }

    @Override
    public void quit(Player responder) {
        Player opponent = context.getOpponentOf(responder);
        context.setResult(HandResult.of(opponent, HandPoints.ONE));
        context.setState(new Done(context));
        context.updateHistory(Event.QUIT_HAND);
    }

    @Override
    public void raise(Player requester) {
        throw new IllegalStateException("Can not raise while deciding if plays mão de onze.");
    }
}
