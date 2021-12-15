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

package com.bueno.domain.entities.player.mineirobot;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.hand.Intel;
import com.bueno.domain.entities.hand.PossibleActions;
import com.bueno.domain.entities.player.util.Bot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.player.util.PlayingStrategy;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.PlayHandUseCase;

import java.util.EnumSet;

public class MineiroBot extends Player implements Bot {

    public final GameRepository repo;

    public MineiroBot(GameRepository repo) {
        super("MineiroBot");
        this.repo = repo;
    }

    public MineiroBot() {
        this(null);
    }

    @Override
    public Card playCard() {
        return PlayingStrategy.of(cards, this).playCard();
    }

    @Override
    public boolean requestTruco() {
        return PlayingStrategy.of(cards, this).requestTruco();
    }

    @Override
    public int getTrucoResponse(HandScore newHandScore) {
        return PlayingStrategy.of(cards, this).getTrucoResponse(newHandScore.get());
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        return PlayingStrategy.of(cards, this).getMaoDeOnzeResponse();
    }

    @Override
    public void playTurn(Intel intel) {
        if(shouldPlay(intel)){
            final EnumSet<PossibleActions> possibleActions = intel.possibleActions();
            final PlayHandUseCase playHandUseCase = new PlayHandUseCase(repo);
            if(possibleActions.contains(PossibleActions.RAISE) && requestTruco()){
                playHandUseCase.raiseBet(getUuid());
                return;
            }
            if(possibleActions.contains(PossibleActions.PLAY)){
                playHandUseCase.playCard(getUuid(), playCard());
                return;
            }
            final int response = getTrucoResponse(intel.getScoreProposal());
            switch (response){
                case -1 -> {if(possibleActions.contains(PossibleActions.QUIT)) playHandUseCase.quit(getUuid());}
                case 0 -> {if(possibleActions.contains(PossibleActions.ACCEPT)) playHandUseCase.accept(getUuid());}
                case 1 -> {if(possibleActions.contains(PossibleActions.RAISE)) playHandUseCase.raiseBet(getUuid());}
            }
        }
    }

    private boolean shouldPlay(Intel intel) {
        return !intel.isGameDone() && intel.currentPlayer().equals(getUuid());
    }
}
