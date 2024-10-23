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
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.repos.GameRepositoryInMemoryImpl;
import com.bueno.domain.usecases.hand.dtos.PlayCardDto;
import com.bueno.domain.usecases.intel.converters.CardConverter;
import com.bueno.domain.usecases.intel.dtos.CardDto;
import com.bueno.domain.usecases.intel.dtos.IntelDto;
import com.bueno.domain.usecases.utils.exceptions.GameNotFoundException;
import com.bueno.domain.usecases.utils.exceptions.UnsupportedGameRequestException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayCardUseCaseTest {

    private PlayCardUseCase sut;

    @Mock private Player player1;
    @Mock private Player player2;
    @Mock private Deck deck;

    @Mock private RemoteBotRepository remoteRepository;
    @Mock private RemoteBotApi remoteBotApi;
    private Game game;
    private GameRepository repo;

    private UUID p1Uuid;
    private UUID p2Uuid;

    @BeforeEach
    void setUp() {
        when(deck.takeOne()).thenReturn(Card.of(Rank.FOUR, Suit.DIAMONDS));
        when(player1.getCards()).thenReturn(new Deck().take(40));
        when(player2.getCards()).thenReturn(new Deck().take(40));

        p1Uuid = UUID.randomUUID();
        p2Uuid = UUID.randomUUID();

        lenient().when(player1.getUuid()).thenReturn(p1Uuid);
        lenient().when(player1.getUsername()).thenReturn(p1Uuid.toString());

        lenient().when(player2.getUuid()).thenReturn(p2Uuid);
        lenient().when(player2.getUsername()).thenReturn(p2Uuid.toString());

        lenient().when(remoteRepository.findAll()).thenReturn(List.of());

        repo = new GameRepositoryInMemoryImpl();
        game = new Game(player1, player2, deck);
        sut = new PlayCardUseCase(repo, remoteRepository, remoteBotApi,new BotManagerService(remoteRepository,remoteBotApi));
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
        repo.save(GameConverter.toDto(game));
        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> sut.playCard(new PlayCardDto(UUID.randomUUID(), new CardDto("X", "X"))));
    }

    @Test
    @DisplayName("Should throw if opponent is playing in player turn")
    void shouldThrowIfOpponentIsPlayingInPlayerTurn() {
        repo.save(GameConverter.toDto(game));
        assertThatExceptionOfType(UnsupportedGameRequestException.class)
                .isThrownBy(() -> sut.playCard(new PlayCardDto(p2Uuid, new CardDto("X", "X"))));
    }

    @Test
    @DisplayName("Should throw if requests action when game is done")
    void shouldThrowIfRequestsActionWhenGameIsDone() {
        when(player1.getScore()).thenReturn(12);
        repo.save(GameConverter.toDto(game));
        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> sut.playCard(new PlayCardDto(p1Uuid, new CardDto("X", "X"))));
    }

    @Test
    @DisplayName("Should correctly play first card if invariants are met")
    void shouldCorrectlyPlayFirstCardIfInvariantsAreMet() {
        final Card card = Card.of(Rank.THREE, Suit.CLUBS);
        final CardDto cardDto = CardConverter.toDto(card);
        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(card)));
        repo.save(GameConverter.toDto(game));
        final IntelDto intel = sut.playCard(new PlayCardDto(p1Uuid, cardDto));
        assertThat(intel.cardToPlayAgainst()).isEqualTo(cardDto);
    }

    @Test
    @DisplayName("Should correctly play hand if invariants are met")
    void shouldCorrectlyPlayHandIfInvariantsAreMet() {
        final CardDto card1P1 = new CardDto("4", "C");
        final CardDto card2P1 = new CardDto("2", "C");
        final CardDto card1P2 = new CardDto("A", "C");
        final CardDto card2P2 = new CardDto("K", "C");

        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(11);

        repo.save(GameConverter.toDto(game));

        sut.playCard(new PlayCardDto(p1Uuid, card1P1));
        sut.playCard(new PlayCardDto(p2Uuid, card1P2));
        sut.playCard(new PlayCardDto(p2Uuid, card2P2));

        assertThatNoException().isThrownBy(() -> sut.discard(new PlayCardDto(p1Uuid, card2P1)));
    }

    @Test
    @DisplayName("Should correctly play second card if invariants are met")
    void shouldCorrectlyPlaySecondCardIfInvariantsAreMet() {
        final CardDto card1 = new CardDto("3", "C");
        final CardDto card2 = new CardDto("2", "C");

        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.fromDto(card1))));
        when(player2.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.fromDto(card2))));
        repo.save(GameConverter.toDto(game));

        sut.playCard(new PlayCardDto(p1Uuid, card1));
        final IntelDto intel = sut.playCard(new PlayCardDto(p2Uuid, card2));

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(intel.cardToPlayAgainst()).isNull();
        softly.assertThat(intel.roundsPlayed()).isOne();
        softly.assertAll();
    }

    @Test
    @DisplayName("Should not allow playing the same card twice in the same hand")
    void shouldNotAllowPlayingTheSameCardTwiceInTheSameHand() {
        final CardDto card1 = new CardDto("3", "C");
        final CardDto card2 = new CardDto("2", "C");

        repo.save(GameConverter.toDto(game));

        sut.playCard(new PlayCardDto(p1Uuid, card1));
        sut.playCard(new PlayCardDto(p2Uuid, card2));

        assertThatIllegalArgumentException().isThrownBy(() -> sut.playCard(new PlayCardDto(p1Uuid, card1)));
    }

    @Test
    @DisplayName("Should not allow discard a card already played in the same hand")
    void shouldNotAllowDiscardingACardAlreadyPlayedInTheSameHand() {
        final CardDto card1 = new CardDto("3", "C");
        final CardDto card2 = new CardDto("2", "C");

        when(player1.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.fromDto(card1))));
        when(player2.getCards()).thenReturn(new ArrayList<>(List.of(CardConverter.fromDto(card2))));
        repo.save(GameConverter.toDto(game));

        sut.playCard(new PlayCardDto(p1Uuid, card1));
        sut.playCard(new PlayCardDto(p2Uuid, card2));

        assertThatIllegalArgumentException().isThrownBy(() -> sut.discard(new PlayCardDto(p1Uuid, card1)));
    }
}