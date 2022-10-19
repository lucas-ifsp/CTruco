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
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.hand.dtos.PlayCardDto;
import com.bueno.domain.usecases.hand.validator.ActionValidator;
import com.bueno.domain.usecases.intel.converters.CardConverter;
import com.bueno.domain.usecases.intel.converters.IntelConverter;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.domain.usecases.utils.exceptions.UnsupportedGameRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayCardUseCase {
    private final GameRepository gameRepository;
    private final GameResultRepository gameResultRepository;
    private final HandResultRepository handResultRepository;
    private final BotUseCase botUseCase;

    public PlayCardUseCase(GameRepository gameRepository) {
        this(gameRepository, null, null);
    }

    @Autowired
    public PlayCardUseCase(GameRepository gameRepository,
                           GameResultRepository gameResultRepository,
                           HandResultRepository handResultRepository) {

        this.gameRepository = gameRepository;
        this.gameResultRepository = gameResultRepository;
        this.handResultRepository = handResultRepository;
        this.botUseCase = new BotUseCase(gameRepository, gameResultRepository, handResultRepository);
    }

    public IntelDto playCard(PlayCardDto request) {
        return playCard(request, false);
    }

    public IntelDto discard(PlayCardDto request) {
        return playCard(request, true);
    }

    private IntelDto playCard(PlayCardDto request, boolean discard) {
        final var validator = new ActionValidator(gameRepository, PossibleAction.PLAY);
        final var notification = validator.validate(request.uuid());

        if (notification.hasErrors()) throw new UnsupportedGameRequestException(notification.errorMessage());

        Game game = gameRepository.findByPlayerUuid(request.uuid()).map(GameConverter::fromDto).orElseThrow();
        final Hand hand = game.currentHand();
        final Player player = hand.getCurrentPlayer();
        final Card cardToPlay = CardConverter.fromDto(request.card());
        final Card playedCard = discard ? player.discard(cardToPlay) : player.play(cardToPlay);

        if (hand.getCardToPlayAgainst().isEmpty()) hand.playFirstCard(player, playedCard);
        else hand.playSecondCard(player, playedCard);

        final ResultHandler resultHandler = new ResultHandler(gameResultRepository, handResultRepository);
        final IntelDto gameResult = resultHandler.handle(game);

        gameRepository.update(GameConverter.toDto(game));
        if(gameResult != null) return gameResult;

        botUseCase.playWhenNecessary(game);

        game = gameRepository.findByPlayerUuid(request.uuid()).map(GameConverter::fromDto).orElseThrow();
        return IntelConverter.toDto(game.getIntel());
    }
}
