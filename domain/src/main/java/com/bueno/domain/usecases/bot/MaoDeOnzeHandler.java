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

import com.bueno.domain.entities.hand.HandPoints;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.hand.usecases.ScoreProposalUseCase;
import com.bueno.spi.service.BotServiceManager;

class MaoDeOnzeHandler extends Handler {

    MaoDeOnzeHandler(Player bot, Intel intel, GameRepository repo) {
        this.bot = bot;
        this.intel = intel;
        this.repo = repo;
    }

    boolean handle() {
        if(shouldHandle()) {
            final var botUuid = bot.getUuid();
            final var botService = BotServiceManager.load(bot.getUsername());
            final var useCase = new ScoreProposalUseCase(repo);
            final var hasAccepted = botService.getMaoDeOnzeResponse(SpiModelAdapter.toGameIntel(bot, intel));

            if (hasAccepted) useCase.accept(botUuid);
            else useCase.quit(botUuid);
            return true;
        }
        return false;
    }

    private boolean shouldHandle() {
        final var hasNotDecided = HandPoints.fromIntValue(intel.handPoints()) == HandPoints.ONE;
        return intel.isMaoDeOnze() && hasNotDecided;
    }
}
