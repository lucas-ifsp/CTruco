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

package com.bueno.domain.usecases.bot.usecase;

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.bot.handlers.CardPlayingHandler;
import com.bueno.domain.usecases.bot.handlers.MaoDeOnzeHandler;
import com.bueno.domain.usecases.bot.handlers.RaiseHandler;
import com.bueno.domain.usecases.bot.handlers.RaiseRequestHandler;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.hand.HandResultRepository;
import com.bueno.domain.usecases.hand.PlayCardUseCase;
import com.bueno.domain.usecases.hand.PointsProposalUseCase;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Objects;

import static com.bueno.domain.usecases.intel.converters.IntelConverter.fromDto;

public class BotUseCase {
    private final GameRepository gameRepo;
    private final GameResultRepository gameResultRepo;
    private final HandResultRepository handResultRepo;
    private final RemoteBotRepository remoteBotRepo;
    private final RemoteBotApi remoteBotApi;
    private final BotManagerService botManagerService;
    private MaoDeOnzeHandler maoDeOnzeHandler;
    private RaiseHandler raiseHandler;
    private CardPlayingHandler cardHandler;
    private RaiseRequestHandler requestHandler;
    private BotServiceProvider bot1;
    private BotServiceProvider bot2;


    public BotUseCase(GameRepository gameRepo, RemoteBotRepository remoteBotRepo, RemoteBotApi remoteBotApi, BotManagerService botManagerService, String bot1Name, String bot2Name) {
        this(gameRepo, remoteBotRepo, remoteBotApi, null, null, botManagerService, null, null, null, null);
        bot1 = botManagerService.load(bot1Name);
        bot2 = botManagerService.load(bot2Name);
    }

    public BotUseCase(GameRepository gameRepo, RemoteBotRepository remoteBotRepo, RemoteBotApi remoteBotApi,
                      GameResultRepository gameResultRepo, HandResultRepository handResultRepo, BotManagerService botManagerService) {
        this(gameRepo, remoteBotRepo, remoteBotApi, gameResultRepo, handResultRepo, botManagerService, null, null, null, null);
    }

    public BotUseCase(GameRepository gameRepo, RemoteBotRepository remoteBotRepo, RemoteBotApi remoteBotApi,
                      GameResultRepository gameResultRepo, HandResultRepository handResultRepo, BotManagerService botManagerService,
                      MaoDeOnzeHandler maoDeOnze, RaiseHandler raise, CardPlayingHandler card,
                      RaiseRequestHandler request) {

        this.gameRepo = Objects.requireNonNull(gameRepo);
        this.gameResultRepo = gameResultRepo;
        this.remoteBotRepo = Objects.requireNonNull(remoteBotRepo);
        this.remoteBotApi = remoteBotApi;
        this.handResultRepo = handResultRepo;
        this.botManagerService = botManagerService;
        this.maoDeOnzeHandler = maoDeOnze;
        this.raiseHandler = raise;
        this.cardHandler = card;
        this.requestHandler = request;
    }

    public Intel playWhenNecessary(Game game, BotManagerService botManagerService) {
        final Player currentPlayer = game.currentHand().getCurrentPlayer();
        final Intel intel = game.getIntel();

        if (!isBotTurn(currentPlayer, intel)) return intel;

        if (isBotVsBot()) {
            if (shouldBot1Play(currentPlayer)) {
                initializeNullHandlers(bot1);
            } else
                initializeNullHandlers(bot2);
        } else
            initializeNullHandlers(botManagerService.load(currentPlayer.getUsername()));

        if (maoDeOnzeHandler.shouldHandle(intel))
            return fromDto(maoDeOnzeHandler.handle(intel, currentPlayer));

        if (raiseHandler.shouldHandle(intel)) {
            final IntelDto dto = raiseHandler.handle(intel, currentPlayer);
            if (dto != null) return fromDto(dto);
        }

        if (cardHandler.shouldHandle(intel))
            return fromDto(cardHandler.handle(intel, currentPlayer));

        if (requestHandler.shouldHandle(intel))
            return fromDto(requestHandler.handle(intel, currentPlayer));

        return null;
    }

    private boolean shouldBot1Play(Player currentPlayer) {
        return bot1.getName().equals(currentPlayer.getUsername());
    }

    private boolean isBotVsBot() {
        return bot1 != null && bot2 != null;
    }

    private boolean isBotTurn(Player handPlayer, Intel intel) {
        final var currentPlayerUUID = intel.currentPlayerUuid();
        if (currentPlayerUUID.isEmpty() || intel.isGameDone() || !handPlayer.isBot()) return false;
        return handPlayer.getUuid().equals(currentPlayerUUID.get());
    }

    private void initializeNullHandlers(BotServiceProvider botService) {
        if (maoDeOnzeHandler == null)
            maoDeOnzeHandler = new MaoDeOnzeHandler(
                    new PointsProposalUseCase(gameRepo, remoteBotRepo, remoteBotApi, gameResultRepo, handResultRepo, botManagerService),
                    botService);
        if (raiseHandler == null)
            raiseHandler = new RaiseHandler(
                    new PointsProposalUseCase(gameRepo, remoteBotRepo, remoteBotApi, gameResultRepo, handResultRepo, botManagerService),
                    botService);
        if (cardHandler == null)
            cardHandler = new CardPlayingHandler(
                    new PlayCardUseCase(gameRepo, remoteBotRepo, remoteBotApi, gameResultRepo, handResultRepo, botManagerService),
                    botService);
        if (requestHandler == null)
            requestHandler = new RaiseRequestHandler(
                    new PointsProposalUseCase(gameRepo, remoteBotRepo, remoteBotApi, gameResultRepo, handResultRepo, botManagerService),
                    botService);
    }
}
