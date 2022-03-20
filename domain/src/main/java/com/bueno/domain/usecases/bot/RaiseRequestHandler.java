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
import com.bueno.domain.usecases.hand.usecases.ScoreProposalUseCase;
import com.bueno.spi.service.BotServiceProvider;

import java.util.EnumSet;
import java.util.stream.Collectors;

import static com.bueno.domain.entities.intel.PossibleAction.*;
import static com.bueno.domain.usecases.bot.SpiModelAdapter.toGameIntel;

class RaiseRequestHandler extends Handler{

    private final BotServiceProvider botService;
    private final ScoreProposalUseCase scoreUseCase;

    RaiseRequestHandler(ScoreProposalUseCase scoreUseCase, BotServiceProvider botService, Player bot, Intel intel) {
        this.scoreUseCase = scoreUseCase;
        this.botService = botService;
        this.bot = bot;
        this.intel = intel;
    }

    @Override
    boolean handle() {
        if(shouldHandle()) {
            final var botUuid = bot.getUuid();
            final var actions = intel.possibleActions().stream()
                    .map(PossibleAction::valueOf)
                    .collect(Collectors.toCollection(() -> EnumSet.noneOf(PossibleAction.class)));

            switch (botService.getRaiseResponse(toGameIntel(bot, intel))) {
                case -1 -> { if (actions.contains(QUIT)) scoreUseCase.quit(botUuid); }
                case 0 -> { if (actions.contains(ACCEPT)) scoreUseCase.accept(botUuid); }
                case 1 -> { if (actions.contains(RAISE)) scoreUseCase.raise(botUuid); }
            }
            return true;
        }
        return false;
    }

    boolean shouldHandle(){
        return !intel.isMaoDeOnze() && !intel.possibleActions().contains("PLAY");
    }
}
