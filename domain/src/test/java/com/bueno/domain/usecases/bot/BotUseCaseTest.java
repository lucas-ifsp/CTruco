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

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.bot.handlers.CardPlayingHandler;
import com.bueno.domain.usecases.bot.handlers.MaoDeOnzeHandler;
import com.bueno.domain.usecases.bot.handlers.RaiseHandler;
import com.bueno.domain.usecases.bot.handlers.RaiseRequestHandler;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.bot.usecase.BotUseCase;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.hand.HandResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotUseCaseTest {

    @Mock Game game;
    @Mock Intel intel;
    @Mock Hand hand;
    @Mock Player player;
    @Mock GameRepository gameRepository;
    @Mock GameResultRepository gameResultRepository;
    @Mock RemoteBotRepository remoteBotRepository;
    @Mock RemoteBotApi remoteBotApi;
    @Mock HandResultRepository handResultRepository;
    @Mock MaoDeOnzeHandler maoDeOnzeHandler;
    @Mock CardPlayingHandler cardPlayingHandler;
    @Mock RaiseHandler raiseHandler;
    @Mock RaiseRequestHandler raiseRequestHandler;

    @InjectMocks
    BotUseCase sut;

    @BeforeEach
    void setUp() {
        UUID playerUUID = UUID.randomUUID();
        lenient().when(game.currentHand()).thenReturn(hand);
        lenient().when(remoteBotRepository.findAll()).thenReturn(List.of());
        lenient().when(game.getIntel()).thenReturn(intel);
        lenient().when(hand.getCurrentPlayer()).thenReturn(player);
        lenient().when(intel.isGameDone()).thenReturn(false);
        lenient().when(intel.currentPlayerUuid()).thenReturn(Optional.of(playerUUID));
        lenient().when(player.getUuid()).thenReturn(playerUUID);
        lenient().when(player.isBot()).thenReturn(true);
        lenient().when(player.getUsername()).thenReturn("DummyBot");
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    @DisplayName("Should not accept null repo")
    void shouldNotAcceptNullRepo() {
        assertThatNullPointerException().isThrownBy(() -> new BotUseCase(
                null, remoteBotRepository, remoteBotApi, gameResultRepository, handResultRepository,new BotManagerService(remoteBotRepository,remoteBotApi)));
    }

    @Test
    @DisplayName("Should do nothing if there is no current player")
    void shouldDoNothingIfThereIsNoCurrentPlayer() {
        when(intel.currentPlayerUuid()).thenReturn(Optional.empty());
        assertThat(sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi))).isEqualTo(intel);
        verify(intel, times(1)).currentPlayerUuid();
    }

    @Test
    @DisplayName("Should do nothing if the game is done")
    void shouldDoNothingIfTheGameIsDone() {
        when(intel.isGameDone()).thenReturn(true);
        assertThat(sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi))).isEqualTo(intel);
        verify(intel, times(1)).isGameDone();
    }

    @Test
    @DisplayName("Should do nothing with player is not a bot")
    void shouldDoNothingWithPlayerIsNotABot() {
        when(player.isBot()).thenReturn(false);
        assertThat(sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi))).isEqualTo(intel);
        verify(player, times(1)).isBot();
    }

    @Test
    @DisplayName("Should not throw if preconditions are met")
    void shouldNotThrowIfPreconditionsAreMet() {
        assertThatNoException().isThrownBy(() -> sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi)));
    }

    @Test
    @DisplayName("Should first handle mao de onze")
    void shouldFirstHandleMaoDeOnze() {
        when(maoDeOnzeHandler.shouldHandle(intel)).thenReturn(true);
        sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi));
        verify(maoDeOnzeHandler, times(1)).handle(intel, player);
        verify(raiseHandler, times(0)).handle(intel, player);
    }

    @Test
    @DisplayName("Should handle raise decision before choosing card")
    void shouldHandleRaiseDecisionBeforeChoosingCard() {
        when(maoDeOnzeHandler.shouldHandle(intel)).thenReturn(false);
        when(raiseHandler.shouldHandle(intel)).thenReturn(true);
        sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi));
        verify(raiseHandler, times(1)).handle(intel, player);
        verify(cardPlayingHandler, times(0)).handle(intel, player);
    }

    @Test
    @DisplayName("Should handle card playing if already decided to raise or not")
    void shouldHandleCardPlayingIfAlreadyDecidedToRaiseOrNot() {
        when(raiseHandler.shouldHandle(intel)).thenReturn(false);
        when(cardPlayingHandler.shouldHandle(intel)).thenReturn(true);
        sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi));
        verify(cardPlayingHandler, times(1)).handle(intel, player);
        verify(raiseRequestHandler, times(0)).handle(intel, player);
    }

    @Test
    @DisplayName("Should handle if it is bot turn just because it must decide about raise request")
    void shouldHandleIfItIsBotTurnJustBecauseItMustDecideAboutRaiseRequest() {
        when(raiseRequestHandler.shouldHandle(any())).thenReturn(true);
        sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi));
        verify(raiseRequestHandler, times(1)).handle(intel, player);
    }

    @Test
    @DisplayName("Should create default handlers if they are not injected in constructor")
    void shouldCreateDefaultHandlersIfTheyAreNotInjectedInConstructor() {
        sut = new BotUseCase(
                gameRepository,
                remoteBotRepository,
                remoteBotApi,
                gameResultRepository,
                handResultRepository,new BotManagerService(remoteBotRepository,remoteBotApi) , null, null, null, null
        );
        when(game.getIntel().currentPlayerUuid()).thenReturn(Optional.empty());
        assertThatNoException().isThrownBy(() -> sut.playWhenNecessary(game,new BotManagerService(remoteBotRepository,remoteBotApi)));
    }

    @Test
    @DisplayName("Should have at least one default bot implementation of bot spi")
    void shouldHaveAtLeastOneDefaultBotImplementationOfBotSpi() {
        BotManagerService botManagerService = new BotManagerService(remoteBotRepository, remoteBotApi);
        assertThat(botManagerService.providersNames().isEmpty())
                .as("It's false that no implementation is available")
                .isFalse();
    }
}