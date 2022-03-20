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

import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.intel.PossibleAction;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.hand.usecases.PlayCardUseCase;
import com.bueno.spi.service.BotServiceProvider;

import static com.bueno.domain.entities.intel.PossibleAction.PLAY;
import static com.bueno.domain.usecases.bot.SpiModelAdapter.toCard;
import static com.bueno.domain.usecases.bot.SpiModelAdapter.toGameIntel;

class CardPlayingHandler extends Handler {

    private final BotServiceProvider botService;
    private final PlayCardUseCase cardUseCase;

    public CardPlayingHandler(PlayCardUseCase cardUseCase, BotServiceProvider botService, Player bot, Intel intel) {
        this.bot = bot;
        this.intel = intel;
        this.botService = botService;
        this.cardUseCase = cardUseCase;
    }

    @Override
    boolean handle() {
        if(shouldHandle()) {
            final var botUuid = bot.getUuid();
            final var chosenCard = botService.chooseCard(toGameIntel(bot, intel));
            final var card = toCard(chosenCard.content());

            if (chosenCard.isDiscard()) cardUseCase.discard(new PlayCardUseCase.RequestModel(botUuid, card));
            else cardUseCase.playCard(new PlayCardUseCase.RequestModel(botUuid, card));
            return true;
        }
        return false;
    }

    @Override
    boolean shouldHandle() {
        return intel.possibleActions().stream()
                .map(PossibleAction::valueOf)
                .anyMatch(action -> action.equals(PLAY));
    }
}
