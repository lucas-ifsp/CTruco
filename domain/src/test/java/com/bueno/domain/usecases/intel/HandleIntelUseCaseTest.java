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

package com.bueno.domain.usecases.intel;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.repos.GameRepositoryInMemoryImpl;
import com.bueno.domain.usecases.intel.converters.CardConverter;
import com.bueno.domain.usecases.intel.converters.IntelConverter;
import com.bueno.domain.usecases.utils.exceptions.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandleIntelUseCaseTest {

    private HandleIntelUseCase sut;
    private GameRepository repo;

    @Mock private Player player1;
    @Mock private Player player2;

    private UUID p1Uuid;
    private Game game;

    @BeforeEach
    void setUp() {
        p1Uuid = UUID.randomUUID();
        UUID p2Uuid = UUID.randomUUID();

        lenient().when(player1.getUuid()).thenReturn(p1Uuid);
        lenient().when(player1.getUsername()).thenReturn(p1Uuid.toString());

        lenient().when(player2.getUuid()).thenReturn(p2Uuid);
        lenient().when(player2.getUsername()).thenReturn(p2Uuid.toString());

        repo = new GameRepositoryInMemoryImpl();
        game = new Game(player1, player2);
        sut = new HandleIntelUseCase(repo);
    }

    @Test
    @DisplayName("Should throw if player requesting owned cards is not playing a game")
    void shouldThrowIfPlayerRequestingOwnedCardsIsNotPlayingAGame() {
        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> sut.ownedCards(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw if requests owned cards with null uuid")
    void shouldThrowIfRequestsOwnedCardsWithNullUuid() {
        assertThatNullPointerException().isThrownBy(() -> sut.ownedCards(null));
    }

    @Test
    @DisplayName("Should throw if player requesting intel history is not playing a game")
    void shouldThrowIfPlayerRequestingIntelHistoryIsNotPlayingAGame() {
        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> sut.findIntelSince(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("Should throw if requests intel history with null uuid")
    void shouldThrowIfRequestsIntelHistoryWithNullUuid() {
        assertThatNullPointerException().isThrownBy(() -> sut.findIntelSince(null, null));
    }

    @Test
    @DisplayName("Should not throw if requests intel history with null base intel")
    void shouldNotThrowIfRequestsIntelHistoryWithNullBaseIntel() {
        repo.save(GameConverter.toDto(game));
        assertThatNoException().isThrownBy(() -> sut.findIntelSince(p1Uuid, null));
    }

    @Test
    @DisplayName("Should correctly get owned cards if invariants are met")
    void shouldCorrectlyGetOwnedCardsIfInvariantsAreMet() {
        final List<Card> cards = List.of(Card.of(Rank.THREE, Suit.CLUBS), Card.of(Rank.TWO, Suit.CLUBS), Card.of(Rank.ACE, Suit.CLUBS));
        when(player1.getCards()).thenReturn(cards);
        repo.save(GameConverter.toDto(game));
        final List<Card> ownedCards = sut.ownedCards(p1Uuid)
                .cards()
                .stream()
                .map(CardConverter::fromDto)
                .collect(Collectors.toList());
        assertThat(ownedCards).isEqualTo(cards);
    }

    @Test
    @DisplayName("Should correctly get intel history if invariants are met")
    void shouldCorrectlyGetIntelHistoryIfInvariantsAreMet() {
        repo.save(GameConverter.toDto(game));
        final Instant initialTimestamp = IntelConverter.toDto(game.getIntel()).timestamp();
        repo.update(GameConverter.toDto(game));

        final var obtained = sut.findIntelSince(p1Uuid, initialTimestamp);
        final var expected = game.getIntelSince(initialTimestamp).stream()
                .map(IntelConverter::toDto)
                .collect(Collectors.toList());

        assertThat(obtained.intelSinceBaseTimestamp()).isEqualTo(expected);
    }
}