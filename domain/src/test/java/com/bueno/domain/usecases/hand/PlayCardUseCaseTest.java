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
import com.bueno.domain.entities.deck.Deck;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.game.GameRepository;
import com.bueno.domain.usecases.intel.model.CardDto;
import com.bueno.domain.usecases.intel.model.IntelDto;
import com.bueno.domain.usecases.utils.exceptions.UnsupportedGameRequestException;
import com.bueno.domain.usecases.intel.converters.CardConverter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayCardUseCaseTest {

    private PlayCardUseCase sut;

    @Mock private Player player1;
    @Mock private Player player2;
    @Mock private GameRepository repo;

    private UUID p1Uuid;
    private UUID p2Uuid;

    @BeforeAll
    static void init() {
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp() {
        Deck deck = new Deck();
        when(player1.getCards()).thenReturn(deck.take(40));

        p1Uuid = UUID.randomUUID();
        p2Uuid = UUID.randomUUID();

        lenient().when(player1.getUuid()).thenReturn(p1Uuid);
        lenient().when(player1.getUsername()).thenReturn(p1Uuid.toString());

        lenient().when(player2.getUuid()).thenReturn(p2Uuid);
        lenient().when(player2.getUsername()).thenReturn(p2Uuid.toString());

        Game game = new Game(player1, player2);
        lenient().when(repo.findByUserUuid(any())).thenReturn(Optional.of(game));

        sut = new PlayCardUseCase(repo);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        p1Uuid = null;
        p2Uuid = null;
    }

    @Test
    @DisplayName("Should throw if there is no active game for player UUID")
    void shouldThrowIfThereIsNoActiveGameForPlayerUuid() {
        assertThrows(UnsupportedGameRequestException.class,
                () -> sut.playCard(new PlayCardRequest(UUID.randomUUID(), new CardDto("X", "X"))));
    }

    @Test
    @DisplayName("Should throw if opponent is playing in player turn")
    void shouldThrowIfOpponentIsPlayingInPlayerTurn() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.playCard(new PlayCardRequest(p2Uuid, new CardDto("X", "X"))));
    }

    @Test
    @DisplayName("Should throw if requests action when game is done")
    void shouldThrowIfRequestsActionWhenGameIsDone() {
        when(player1.getScore()).thenReturn(12);
        assertThrows(UnsupportedGameRequestException.class,
                () -> sut.playCard(new PlayCardRequest(p1Uuid, new CardDto("X", "X"))));
    }

    @Test
    @DisplayName("Should correctly play first card if invariants are met")
    void shouldCorrectlyPlayFirstCardIfInvariantsAreMet() {
        final Card card = Card.of(Rank.THREE, Suit.CLUBS);
        final CardDto cardDto = CardConverter.toEntity(card);
        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(card)));
        final IntelDto intel = sut.playCard(new PlayCardRequest(p1Uuid, cardDto));
        assertEquals(cardDto, intel.getCardToPlayAgainst());
    }

    @Test
    @DisplayName("Should correctly play hand if invariants are met")
    void shouldCorrectlyPlayHandIfInvariantsAreMet() {
        final CardDto card1P1 = new CardDto("3", "C");
        final CardDto card2P1 = new CardDto("2", "C");
        final CardDto card1P2 = new CardDto("A", "C");
        final CardDto card2P2 = new CardDto("K", "C");

        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(11);

        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card1P1), CardConverter.toDto(card2P1))));
        when(player2.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card1P2), CardConverter.toDto(card2P2))));

        sut.discard(new PlayCardRequest(p1Uuid, card1P1));
        sut.playCard(new PlayCardRequest(p2Uuid, card1P2));
        sut.playCard(new PlayCardRequest(p2Uuid, card2P2));

        assertDoesNotThrow(() -> sut.discard(new PlayCardRequest(p1Uuid, card2P1)));
    }

    @Test
    @DisplayName("Should correctly play second card if invariants are met")
    void shouldCorrectlyPlaySecondCardIfInvariantsAreMet() {
        final CardDto card1 = new CardDto("3", "C");
        final CardDto card2 = new CardDto("2", "C");

        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card1))));
        when(player2.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card2))));

        sut.discard(new PlayCardRequest(p1Uuid, card1));
        final IntelDto intel = sut.playCard(new PlayCardRequest(p2Uuid, card2));
        assertAll(
                () -> assertNull(intel.getCardToPlayAgainst()),
                () -> assertEquals(1, intel.getRoundsPlayed())
        );
    }

    @Test
    @DisplayName("Should not allow playing the same card twice in the same hand")
    void shouldNotAllowPlayingTheSameCardTwiceInTheSameHand() {
        final CardDto card1 = new CardDto("3", "C");
        final CardDto card2 = new CardDto("2", "C");

        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card1))));
        when(player2.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card2))));

        sut.playCard(new PlayCardRequest(p1Uuid, card1));
        sut.discard(new PlayCardRequest(p2Uuid, card2));
        assertThrows(IllegalArgumentException.class, () -> sut.playCard(new PlayCardRequest(p1Uuid, card1)));
    }

    @Test
    @DisplayName("Should not allow discard a card already played in the same hand")
    void shouldNotAllowDiscardingACardAlreadyPlayedInTheSameHand() {
        final CardDto card1 = new CardDto("3", "C");
        final CardDto card2 = new CardDto("2", "C");

        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card1))));
        when(player2.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.toDto(card2))));

        sut.playCard(new PlayCardRequest(p1Uuid, card1));
        sut.discard(new PlayCardRequest(p2Uuid, card2));
        assertThrows(IllegalArgumentException.class, () -> sut.discard(new PlayCardRequest(p1Uuid, card1)));
    }
}