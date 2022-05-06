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

import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.FindGameUseCase;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.domain.usecases.utils.exceptions.UnsupportedGameRequestException;
import com.bueno.domain.usecases.intel.converters.IntelConverter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointsProposalUseCaseTest {

    private PointsProposalUseCase sut;

    @Mock private Player player1;
    @Mock private Player player2;
    @Mock private GameRepository repo;

    private UUID p1Uuid;
    private UUID p2Uuid;
    private Game game;

    @BeforeAll
    static void init() {
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp() {
        p1Uuid = UUID.randomUUID();
        p2Uuid = UUID.randomUUID();

        lenient().when(player1.getUuid()).thenReturn(p1Uuid);
        lenient().when(player1.getUsername()).thenReturn(p1Uuid.toString());

        lenient().when(player2.getUuid()).thenReturn(p2Uuid);
        lenient().when(player2.getUsername()).thenReturn(p2Uuid.toString());

        game = new Game(player1, player2);
        lenient().when(repo.findByUserUuid(any())).thenReturn(Optional.of(game));
        sut = new PointsProposalUseCase(repo);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        game = null;
        p1Uuid = null;
        p2Uuid = null;
    }

    @Test
    @DisplayName("Should throw if accept method parameter is null")
    void shouldThrowIfAcceptMethodParameterIsNull() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.accept(null));
    }

    @Test
    @DisplayName("Should throw if quit method parameter is null")
    void shouldThrowIfQuitMethodParameterIsNull() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.quit(null));
    }

    @Test
    @DisplayName("Should throw if raiseBet method parameter is null")
    void shouldThrowIfRaiseBetMethodParameterIsNull() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.raise(null));
    }

    @Test
    @DisplayName("Should throw if there is no active game for player UUID")
    void shouldThrowIfThereIsNoActiveGameForPlayerUuid() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.raise(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw if opponent is playing in player turn")
    void shouldThrowIfOpponentIsPlayingInPlayerTurn() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.raise(p2Uuid));
    }

    @Test
    @DisplayName("Should throw if requests actions not allowed in hand state")
    void shouldThrowIfRequestsActionsNotAllowedInHandState() {
        assertAll(
                () -> assertThrows(UnsupportedGameRequestException.class, () -> sut.accept(p1Uuid)),
                () -> assertThrows(UnsupportedGameRequestException.class, () -> sut.quit(p1Uuid))
                );
    }

    @Test
    @DisplayName("Should throw if requests action and the game is done")
    void shouldThrowIfRequestsActionAndTheGameIsDone() {
        when(player1.getScore()).thenReturn(12);
        assertThrows(UnsupportedGameRequestException.class, () -> sut.raise(p1Uuid));
    }

    @Test
    @DisplayName("Should be able to raise bet if invariants are met")
    void shouldBeAbleToRaiseBetIfInvariantsAreMet() {
        final IntelDto intel = sut.raise(p1Uuid);
        assertEquals(3, intel.getHandPointsProposal());
    }

    @Test
    @DisplayName("Should be able to accept bet if invariants are met")
    void shouldBeAbleToAcceptBetIfInvariantsAreMet() {
        sut.raise(p1Uuid);
        final IntelDto intel = sut.accept(p2Uuid);
        assertEquals(3, intel.getHandPoints());
    }

    @Test
    @DisplayName("Should be able to quit bet if invariants are met")
    void shouldBeAbleToQuitBetIfInvariantsAreMet() {
        final IntelDto firstIntel = sut.raise(p1Uuid);
        sut.quit(p2Uuid);//last intel is already pointing to a new hand.
        final FindGameUseCase findGameUseCase = new FindGameUseCase(repo);
        final Game game = findGameUseCase.loadUserGame(p1Uuid).orElseThrow();

        final List<IntelDto> intelSince = game.getIntelSince(firstIntel.getTimestamp()).stream().map(IntelConverter::of).collect(Collectors.toList());
        final IntelDto quitIntel = intelSince.get(intelSince.size() - 2);
        assertAll(
                () -> assertEquals(1, quitIntel.getHandPoints()),
                () -> assertEquals(player1.getUsername(), quitIntel.getHandWinner())
        );
    }
}
