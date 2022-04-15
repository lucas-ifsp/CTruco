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

package com.bueno.domain.usecases.hand;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.bot.BotUseCase;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.intel.IntelResponseModel;
import com.bueno.domain.usecases.utils.UnsupportedGameRequestException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PlayCardUseCase {

    private final GameRepository repo;
    private final BotUseCase botUseCase;

    public PlayCardUseCase(GameRepository repo) {
        this.repo = Objects.requireNonNull(repo);
        this.botUseCase = new BotUseCase(repo);
    }

    public IntelResponseModel playCard(PlayCardRequestModel playCardRequestModel) {
        return playCard(playCardRequestModel, false);
    }

    public IntelResponseModel discard(PlayCardRequestModel playCardRequestModel) {
        return playCard(playCardRequestModel, true);
    }

    private IntelResponseModel playCard(PlayCardRequestModel request, boolean discard){
        final var validator = new ActionValidator(repo, PossibleAction.PLAY);
        final var notification = validator.validate(request.getUuid());

        if(notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());

        final Game game = repo.findByUserUuid(request.getUuid()).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();
        final Card playingCard = discard ? player.discard(request.getCard()) : player.play(request.getCard());

        if(hand.getCardToPlayAgainst().isEmpty()) hand.playFirstCard(player, playingCard);
        else hand.playSecondCard(player, playingCard);
        hand.getResult().ifPresent(unused -> updateGameStatus(game));

        if(game.isDone()) return IntelResponseModel.of(game.getIntel());

        botUseCase.playWhenNecessary(game);

        return IntelResponseModel.of(game.getIntel());
    }

    private void updateGameStatus(Game game) {
        game.updateScores();
        if(!game.isDone()) game.prepareNewHand();
    }
}
