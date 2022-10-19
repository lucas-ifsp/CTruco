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

package com.bueno.domain.usecases.bot.handlers;

import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.dtos.PlayCardDto;
import com.bueno.domain.usecases.intel.converters.CardConverter;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.spi.service.BotServiceProvider;

import static com.bueno.domain.entities.intel.PossibleAction.PLAY;
import static com.bueno.domain.usecases.bot.converter.SpiModelAdapter.toCard;
import static com.bueno.domain.usecases.bot.converter.SpiModelAdapter.toGameIntel;

public class CardPlayingHandler implements Handler{

    private final BotServiceProvider botService;
    private final PlayCardUseCase cardUseCase;

    public CardPlayingHandler(PlayCardUseCase cardUseCase, BotServiceProvider botService) {
        this.botService = botService;
        this.cardUseCase = cardUseCase;
    }

    @Override
    public IntelDto handle(Intel intel, Player bot) {
        final var botUuid = bot.getUuid();
        final var chosenCard = botService.chooseCard(toGameIntel(bot, intel));
        final var card = toCard(chosenCard.content());
        final var requestModel = new PlayCardDto(botUuid, CardConverter.toDto(card));

        if (chosenCard.isDiscard()) return cardUseCase.discard(requestModel);
        return cardUseCase.playCard(requestModel);
    }

    @Override
    public boolean shouldHandle(Intel intel) {
        return intel.possibleActions().stream()
                .map(PossibleAction::valueOf)
                .anyMatch(action -> action.equals(PLAY));
    }
}
