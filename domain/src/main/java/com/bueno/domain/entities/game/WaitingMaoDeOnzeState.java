/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.entities.game;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.player.util.Player;

import java.util.EnumSet;

class WaitingMaoDeOnzeState implements HandState {

    private final Hand context;

    WaitingMaoDeOnzeState(Hand context) {
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
        context.setScore(HandScore.THREE);
        context.setCurrentPlayer(context.getFirstToPlay());
        context.setPossibleActions(EnumSet.of(PossibleAction.PLAY));
        context.setState(new NoCardState(context));
        context.updateHistory(Event.ACCEPT_HAND);
    }

    @Override
    public void quit(Player responder) {
        Player opponent = context.getOpponentOf(responder);
        context.setResult(new HandResult(opponent, HandScore.ONE));
        context.setState(new DoneState(context));
        context.updateHistory(Event.QUIT_HAND);
    }

    @Override
    public void raise(Player requester) {
        throw new IllegalStateException("Can not raise while deciding if plays mão de onze.");
    }
}
