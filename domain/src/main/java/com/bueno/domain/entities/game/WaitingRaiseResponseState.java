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

public class WaitingRaiseResponseState implements HandState {

    private Hand context;

    public WaitingRaiseResponseState(Hand context) {
        this.context = context;
        this.context.setPossibleActions(EnumSet.of(PossibleActions.QUIT, PossibleActions.ACCEPT, PossibleActions.RAISE));
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

        final Player currentPlayer = context.getCardToPlayAgainst().isEmpty() ? context.getFirstToPlay() : context.getLastToPlay();
        context.setCurrentPlayer(currentPlayer);

        final HandState nextState = context.getCardToPlayAgainst().isPresent() ? new OneCardState(context) : new NoCardState(context);
        context.setState(nextState);
    }

    @Override
    public void quit(Player responder) {
        Player opponent = context.getOpponentOf(responder);
        context.setLastBetRaiser(null);
        context.setResult(new HandResult(opponent, context.getScore()));
        context.setState(new DoneState(context));
    }

    @Override
    public void raiseBet(Player requester) {
        final HandScore currentScore = context.getScoreProposal() != null ?
                context.getScoreProposal() : context.getScore();

        context.setScore(currentScore);
        context.addScoreProposal();
        context.setLastBetRaiser(requester);

        final Player opponent = context.getOpponentOf(requester);
        context.setCurrentPlayer(opponent);

        context.setState(new WaitingRaiseResponseState(context));
    }
}
