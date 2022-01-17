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

class WaitingRaiseResponseState implements HandState {

    private final Hand context;

    WaitingRaiseResponseState(Hand context) {
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
        context.setScore(context.getScoreProposal());
        context.removeScoreProposal();
        context.setCurrentPlayer(defineCurrentPlayer());
        context.setState(defineNextState());
        context.updateHistory(Event.ACCEPT);
    }

    private Player defineCurrentPlayer() {
        return context.getCardToPlayAgainst().isEmpty() ? context.getFirstToPlay() : context.getLastToPlay();
    }

    private HandState defineNextState() {
        return context.getCardToPlayAgainst().isPresent() ? new OneCardState(context) : new NoCardState(context);
    }

    @Override
    public void quit(Player responder) {
        context.setLastBetRaiser(null);
        context.removeScoreProposal();
        context.setResult(new HandResult(context.getOpponentOf(responder), context.getScore()));
        context.setState(new DoneState(context));
        context.updateHistory(Event.QUIT);
    }

    @Override
    public void raise(Player requester) {
        final HandScore score = context.getScoreProposal() != null ? context.getScoreProposal() : context.getScore();
        context.setScore(score);
        context.addScoreProposal();
        context.setLastBetRaiser(requester);
        context.setCurrentPlayer(context.getOpponentOf(requester));
        context.setState(new WaitingRaiseResponseState(context));
        context.updateHistory(Event.RAISE);
    }
}
