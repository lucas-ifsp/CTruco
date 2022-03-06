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

package com.bueno.domain.usecases.bot.helper;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.game.HandScore;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.game.PossibleAction;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.bot.spi.BotServiceManager;
import com.bueno.domain.usecases.bot.spi.GameIntel;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.usecases.PlayCardUseCase;
import com.bueno.domain.usecases.hand.usecases.ScoreProposalUseCase;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bueno.domain.entities.game.PossibleAction.*;
import static com.bueno.domain.usecases.bot.spi.GameIntel.*;

public class BotUseCase {
    private final GameRepository repo;

    public BotUseCase(GameRepository repo) {
        this.repo = repo;
    }

    public void playWhenNecessary(Game game){
        final Player currentPlayer = game.currentHand().getCurrentPlayer();
        final UUID uuid = currentPlayer.getUuid();
        final Intel intel = game.getIntel();

        if(!currentPlayer.isBot() || isNotCurrentPlayer(uuid, intel)) return;
        if (isNotCurrentPlayer(uuid, intel)) return;
        if (shouldDecideMaoDeOnze(intel)) decideMaoDeOnze(currentPlayer, intel, repo);
        else if (wantToRaiseRequest(currentPlayer, intel)) startRaiseRequest(uuid, repo);
        else if (shouldPlayCard(intel)) playCard(currentPlayer, intel, repo);
        else answerRaiseRequest(currentPlayer, intel, repo);
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
        final var botUuid = bot.getUuid();
        final var botService = BotServiceManager.load(bot.getUsername());
        final var useCase = new ScoreProposalUseCase(repo);
        final var hasAccepted = botService.getMaoDeOnzeResponse(toGameIntel(bot,intel));

        if(hasAccepted) useCase.accept(botUuid);
        else useCase.quit(botUuid);
    }

    private boolean wantToRaiseRequest(Player bot, Intel intel) {
        final var actions = intel.possibleActions().stream()
                .map(PossibleAction::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PossibleAction.class)));

        final var botService = BotServiceManager.load(bot.getUsername());
        final var canNotStartRequest = !actions.contains(RAISE) || actions.contains(QUIT);

        if(canNotStartRequest) return false;
        return botService.decideIfRaises(toGameIntel(bot,intel));
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
        final var botUuid = bot.getUuid();
        final var botService = BotServiceManager.load(bot.getUsername());
        final var chosenCard = botService.chooseCard(toGameIntel(bot,intel));
        final var useCase = new PlayCardUseCase(repo);

        if(chosenCard.isDiscard()) useCase.discard(new PlayCardUseCase.RequestModel(botUuid, chosenCard.content()));
        else useCase.playCard(new PlayCardUseCase.RequestModel(botUuid, chosenCard.content()));
    }

    private void answerRaiseRequest(Player bot, Intel intel, GameRepository repo) {
        final var botUuid = bot.getUuid();
        final var botService = BotServiceManager.load(bot.getUsername());
        final var useCase = new ScoreProposalUseCase(repo);
        final var actions = intel.possibleActions().stream()
                .map(PossibleAction::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PossibleAction.class)));

        switch (botService.getRaiseResponse(toGameIntel(bot,intel))){
            case -1 -> {if(actions.contains(QUIT)) useCase.quit(botUuid);}
            case 0 -> {if(actions.contains(ACCEPT)) useCase.accept(botUuid);}
            case 1 -> {if(actions.contains(RAISE)) useCase.raise(botUuid);}
        }
    }

    private GameIntel toGameIntel(Player player, Intel intel){
        final Function<String, RoundResult> toRoundResult = name -> name == null ? RoundResult.DREW
                : name.equals(player.getUsername()) ? RoundResult.WON : RoundResult.LOST;

        final List<RoundResult> roundResults = intel.roundWinners().stream()
                .map(winner -> winner.orElse(null))
                .map(toRoundResult).collect(Collectors.toList());

        return StepBuilder.with()
                .gameInfo(roundResults, intel.openCards(), intel.vira(), intel.handScore())
                .botInfo(player.getCards(), intel.currentPlayerScore())
                .opponentScore(intel.currentOpponentScore())
                .opponentCard(intel.cardToPlayAgainst().orElse(null))
                .build();
    }
}
