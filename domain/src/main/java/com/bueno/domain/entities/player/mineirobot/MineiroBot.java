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
import com.bueno.domain.entities.game.PossibleAction;
import com.bueno.domain.entities.player.util.Bot;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.ScoreProposalUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.PlayCardUseCase.RequestModel;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
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
        setIntel(intel);
        if(shouldPlay(getIntel())){
            final EnumSet<PossibleAction> possibleActions = getIntel().possibleActions();
            final PlayCardUseCase playCardUseCase = new PlayCardUseCase(repo);
            final ScoreProposalUseCase proposalUseCase = new ScoreProposalUseCase(repo);

            if(getIntel().isMaoDeOnze() && getIntel().handScore() == HandScore.ONE){
                if(getMaoDeOnzeResponse()) proposalUseCase.accept(getUuid());
                else proposalUseCase.quit(getUuid());
                return;
            }
            if(canStartRaiseRequest(possibleActions) && requestTruco()){
                proposalUseCase.raise(getUuid());
                return;
            }
            if(possibleActions.contains(PossibleAction.PLAY)){
                final CardToPlay chosenCard = chooseCardToPlay();
                if(chosenCard.isDiscard()) playCardUseCase.discard(new RequestModel(getUuid(), chosenCard.content()));
                else playCardUseCase.playCard(new RequestModel(getUuid(), chosenCard.content()));
                return;
            }
            final int response = getTrucoResponse(Objects.requireNonNull(getIntel().scoreProposal().orElse(null)));
            switch (response){
                case -1 -> {if(possibleActions.contains(PossibleAction.QUIT)) proposalUseCase.quit(getUuid());}
                case 0 -> {if(possibleActions.contains(PossibleAction.ACCEPT)) proposalUseCase.accept(getUuid());}
                case 1 -> {if(possibleActions.contains(PossibleAction.RAISE)) proposalUseCase.raise(getUuid());}
            }
        }
    }

    private boolean canStartRaiseRequest(EnumSet<PossibleAction> possibleActions) {
        return possibleActions.contains(PossibleAction.RAISE) && !possibleActions.contains(PossibleAction.QUIT);

    }

    private boolean shouldPlay(Intel intel) {
        final Optional<UUID> possibleUuid = intel.currentPlayerUuid();
        if(possibleUuid.isEmpty()) return false;
        return !intel.isGameDone() && possibleUuid.get().equals(getUuid());
    }
}
