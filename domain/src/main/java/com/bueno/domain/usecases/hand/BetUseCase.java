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

package com.bueno.domain.usecases.hand;

import com.bueno.domain.entities.game.*;
import com.bueno.domain.entities.player.util.Bot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.game.UnsupportedGameRequestException;
import com.bueno.domain.usecases.hand.validators.ActionValidator;
import com.bueno.domain.usecases.utils.Notification;
import com.bueno.domain.usecases.utils.Validator;

import java.util.Objects;
import java.util.UUID;

public class BetUseCase {

    private final GameRepository repo;

    public BetUseCase(GameRepository repo) {
        this.repo = Objects.requireNonNull(repo);
    }

    public Intel raiseBet(UUID usedUuid){
        validateInput(usedUuid, PossibleActions.RAISE);

        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();

        hand.raiseBet(player);

        if(game.currentHand().getCurrentPlayer() instanceof Bot bot) bot.playTurn(game.getIntel());
        return game.getIntel();
    }

    public Intel accept(UUID usedUuid){
        validateInput(usedUuid, PossibleActions.ACCEPT);

        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();

        hand.accept(player);

        if(game.currentHand().getCurrentPlayer() instanceof Bot bot) bot.playTurn(game.getIntel());
        return game.getIntel();
    }

    public Intel quit(UUID usedUuid){
        validateInput(usedUuid, PossibleActions.QUIT);

        final Game game = repo.findByUserUuid(usedUuid).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();

        hand.quit(player);
        hand.getResult().ifPresent(unused -> updateGameStatus(game));

        if(game.isDone()) return game.getIntel();
        if(game.currentHand().getCurrentPlayer() instanceof Bot bot) bot.playTurn(game.getIntel());

        return game.getIntel();
    }

    private void validateInput(UUID usedUuid, PossibleActions raise) {
        final Validator<UUID> validator = new ActionValidator(repo, raise);
        final Notification notification = validator.validate(usedUuid);
        if (notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());
    }

    private void updateGameStatus(Game game) {
        game.updateScores();
        if(!game.isDone()) game.prepareNewHand();
    }
}
