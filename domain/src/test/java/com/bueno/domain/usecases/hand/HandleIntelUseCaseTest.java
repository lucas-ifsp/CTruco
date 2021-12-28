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

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.game.Hand;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.game.InMemoryGameRepository;
import com.bueno.domain.usecases.game.UnsupportedGameRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandleIntelUseCaseTest {

    private HandleIntelUseCase sut;
    private CreateGameUseCase createGameUseCase;

    @Mock private Player player1;
    @Mock private Player player2;

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

        final InMemoryGameRepository repo = new InMemoryGameRepository();

        createGameUseCase = new CreateGameUseCase(repo);
        game = createGameUseCase.create(player1, player2);
        sut = new HandleIntelUseCase(repo);
    }

    @Test
    @DisplayName("Should throw if player requesting owned cards is not playing a game")
    void shouldThrowIfPlayerRequestingOwnedCardsIsNotPlayingAGame() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.getOwnedCards(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should throw if requests owned cards with null uuid")
    void shouldThrowIfRequestsOwnedCardsWithNullUuid() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.getOwnedCards(null));
    }

    @Test
    @DisplayName("Should throw if player requesting intel history is not playing a game")
    void shouldThrowIfPlayerRequestingIntelHistoryIsNotPlayingAGame() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.findIntelSince(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("Should throw if requests intel history with null uuid")
    void shouldThrowIfRequestsIntelHistoryWithNullUuid() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.findIntelSince(null, null));
    }

    @Test
    @DisplayName("Should not throw if requests intel history with null base intel")
    void shouldNotThrowIfRequestsIntelHistoryWithNullBaseIntel() {
        assertDoesNotThrow(() -> sut.findIntelSince(p1Uuid, null));
    }

    @Test
    @DisplayName("Should correctly get owned cards if invariants are met")
    void shouldCorrectlyGetOwnedCardsIfInvariantsAreMet() {
        final List<Card> cards = List.of(Card.of(Rank.THREE, Suit.CLUBS), Card.of(Rank.TWO, Suit.CLUBS), Card.of(Rank.ACE, Suit.CLUBS));
        when(player1.getCards()).thenReturn(cards);
        final List<Card> ownedCards = sut.getOwnedCards(p1Uuid);
        assertEquals(cards, ownedCards);
    }

    @Test
    @DisplayName("Should correctly get intel history if invariants are met")
    void shouldCorrectlyGetIntelHistoryIfInvariantsAreMet() {
        final Hand hand = game.currentHand();
        final Intel initialIntel = game.getIntel();
        hand.playFirstCard(player1, Card.closed());

        final List<Intel> intelSinceBeginning = sut.findIntelSince(p1Uuid, initialIntel);
        assertEquals(game.getIntelSince(initialIntel), intelSinceBeginning);
    }
}