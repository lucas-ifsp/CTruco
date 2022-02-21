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

package com.bueno.domain.usecases.bot;
import com.bueno.domain.entities.game.HandScore;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.game.PossibleAction;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.ScoreProposalUseCase;

import java.util.EnumSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bueno.domain.entities.game.PossibleAction.*;

public interface BotService {

    int getRaiseResponse(Player bot, Intel intel);
    boolean getMaoDeOnzeResponse(Player bot, Intel intel);
    boolean decideIfRaises(Player bot, Intel intel);
    CardToPlay chooseCard(Player bot, Intel intel);

    default void handle(Player bot, Intel intel, GameRepository repo) {
        final UUID botUuid = bot.getUuid();
        if (isNotCurrentPlayer(botUuid, intel)) return;
        if (shouldDecideMaoDeOnze(intel)) decideMaoDeOnze(bot, intel, repo);
        else if (wantToRaiseRequest(bot, intel)) startRaiseRequest(botUuid, repo);
        else if (shouldPlayCard(intel)) playCard(bot, intel, repo);
        else answerRaiseRequest(bot, intel, repo);
    }

    private boolean isNotCurrentPlayer(UUID botUUID, Intel intel) {
        final var currentPlayerUUID = intel.currentPlayerUuid();
        if (currentPlayerUUID.isEmpty() || intel.isGameDone()) return true;
        return !currentPlayerUUID.get().equals(botUUID);
    }

    private boolean shouldDecideMaoDeOnze(Intel intel) {
        final var hasNotDecided = HandScore.fromIntValue(intel.handScore()).orElse(null) == HandScore.ONE;
        return intel.isMaoDeOnze() && hasNotDecided;
    }

    private void decideMaoDeOnze(Player bot, Intel intel, GameRepository repo) {
        final UUID botUuid = bot.getUuid();
        final var useCase = new ScoreProposalUseCase(repo);
        final var hasAccepted = getMaoDeOnzeResponse(bot, intel);
        if(hasAccepted) useCase.accept(botUuid);
        else useCase.quit(botUuid);
    }

    private boolean wantToRaiseRequest(Player bot, Intel intel) {
        final var actions = intel.possibleActions().stream()
                .map(PossibleAction::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PossibleAction.class)));

        final boolean canNotStartRequest = !actions.contains(RAISE) || actions.contains(QUIT);
        if(canNotStartRequest) return false;
        return decideIfRaises(bot, intel);
    }

    private void startRaiseRequest(UUID botUuid, GameRepository repo) {
        final var useCase = new ScoreProposalUseCase(repo);
        useCase.raise(botUuid);
    }

    private boolean shouldPlayCard(Intel intel) {
        return  intel.possibleActions().stream()
                .map(PossibleAction::valueOf)
                .anyMatch(action -> action.equals(PLAY));
    }

    private void playCard(Player bot, Intel intel, GameRepository repo) {
        final UUID botUuid = bot.getUuid();
        final CardToPlay chosenCard = chooseCard(bot, intel);
        final var useCase = new PlayCardUseCase(repo);
        if(chosenCard.isDiscard()) useCase.discard(new PlayCardUseCase.RequestModel(botUuid, chosenCard.content()));
        else useCase.playCard(new PlayCardUseCase.RequestModel(botUuid, chosenCard.content()));
    }

    private void answerRaiseRequest(Player bot, Intel intel, GameRepository repo) {
        final UUID botUuid = bot.getUuid();
        final var actions = intel.possibleActions().stream()
                .map(PossibleAction::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PossibleAction.class)));

        final var useCase = new ScoreProposalUseCase(repo);
        switch (getRaiseResponse(bot, intel)){
            case -1 -> {if(actions.contains(QUIT)) useCase.quit(botUuid);}
            case 0 -> {if(actions.contains(ACCEPT)) useCase.accept(botUuid);}
            case 1 -> {if(actions.contains(RAISE)) useCase.raise(botUuid);}
        }
    }
}
