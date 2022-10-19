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

public class WaitingRaiseResponse implements HandState {

    private final Hand context;

    public WaitingRaiseResponse(Hand context) {
        this.context = context;
        final EnumSet<PossibleAction> actions = EnumSet.of(PossibleAction.QUIT, PossibleAction.ACCEPT);
        if(context.canRaiseBet()) actions.add(PossibleAction.RAISE);
        this.context.setPossibleActions(actions);
    }

    @Override
    public void playFirstCard(Player player, Card card) {
        throw new IllegalStateException("Can not play card until bet is responded.");
    }

    @Override
    public void playSecondCard(Player player, Card card) {
        throw new IllegalStateException("Can not play card until bet is responded.");
    }

    @Override
    public void accept(Player responder) {
        context.setPoints(context.getPointsProposal());
        context.removePointsProposal();
        context.setCurrentPlayer(defineCurrentPlayer());
        context.setState(defineNextState());
        context.updateHistory(Event.ACCEPT);
    }

    private Player defineCurrentPlayer() {
        return context.getCardToPlayAgainst().isEmpty() ? context.getFirstToPlay() : context.getLastToPlay();
    }

    private HandState defineNextState() {
        return context.getCardToPlayAgainst().isPresent() ? new OneCard(context) : new NoCard(context);
    }

    @Override
    public void quit(Player responder) {
        //context.setLastBetRaiser(null);
        //context.removePointsProposal();
        context.setResult(HandResult.of(context.getOpponentOf(responder), context.getPoints()));
        context.setState(new Done(context));
        context.updateHistory(Event.QUIT);
    }

    @Override
    public void raise(Player requester) {
        final HandPoints score = context.getPointsProposal() != null ? context.getPointsProposal() : context.getPoints();
        context.setPoints(score);
        context.addPointsProposal();
        context.setLastBetRaiser(requester);
        context.setCurrentPlayer(context.getOpponentOf(requester));
        context.setState(new WaitingRaiseResponse(context));
        context.updateHistory(Event.RAISE);
    }
}
