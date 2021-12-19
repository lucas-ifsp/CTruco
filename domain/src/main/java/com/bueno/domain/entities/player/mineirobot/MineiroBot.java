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

import com.bueno.domain.entities.game.HandScore;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.game.PossibleActions;
import com.bueno.domain.entities.player.util.Bot;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.PlayHandUseCase;

import java.util.EnumSet;
import java.util.UUID;

public class MineiroBot extends Player implements Bot {

    public final GameRepository repo;

    public MineiroBot(GameRepository repo) {
        super("MineiroBot");
        this.repo = repo;
    }

    public MineiroBot(GameRepository repo, UUID uuid) {
        super("MineiroBot", uuid);
        this.repo = repo;
    }

    public MineiroBot() {
        this(null);
    }

    @Override
    public CardToPlay chooseCardToPlay() {
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
            final EnumSet<PossibleActions> possibleActions = intel.possibleActions(getUuid());
            final PlayHandUseCase playHandUseCase = new PlayHandUseCase(repo);

            if(intel.isMaoDeOnze() && intel.getHandScore() == HandScore.ONE){
                if(getMaoDeOnzeResponse()) playHandUseCase.accept(getUuid());
                else playHandUseCase.quit(getUuid());
                return;
            }
            if(canStartRaiseRequest(intel, possibleActions) && requestTruco()){
                playHandUseCase.raiseBet(getUuid());
                return;
            }
            if(possibleActions.contains(PossibleActions.PLAY)){
                final CardToPlay chosenCard = chooseCardToPlay();
                if(chosenCard.isDiscard()) playHandUseCase.discard(getUuid(), chosenCard.content());
                else playHandUseCase.playCard(getUuid(), chosenCard.content());
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

    private boolean canStartRaiseRequest(Intel intel, EnumSet<PossibleActions> possibleActions) {
        return possibleActions.contains(PossibleActions.RAISE)
                && !possibleActions.contains(PossibleActions.QUIT)
                && !(intel.getScoreProposal() == HandScore.TWELVE)
                && intel.getHandScore().increase().get() <= intel.maximumHandScore();
    }

    private boolean shouldPlay(Intel intel) {
        return !intel.isGameDone() && intel.currentPlayer().equals(getUuid());
    }
}
