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
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.game.InMemoryGameRepository;
import com.bueno.domain.usecases.game.UnsupportedGameRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayHandUseCaseTest {

    private Hand hand;
    private PlayHandUseCase sut;
    private CreateGameUseCase createGameUseCase;
    private Game game;

    @Mock private Player p1;
    @Mock private Player p2;

    private UUID p1Uuid;
    private UUID p2Uuid;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp() {
        p1Uuid = UUID.randomUUID();
        p2Uuid = UUID.randomUUID();

        lenient().when(p1.getUuid()).thenReturn(p1Uuid);
        lenient().when(p1.getUsername()).thenReturn(p1Uuid.toString());

        lenient().when(p2.getUuid()).thenReturn(p2Uuid);
        lenient().when(p2.getUsername()).thenReturn(p2Uuid.toString());

        final InMemoryGameRepository repo = new InMemoryGameRepository();
        createGameUseCase = new CreateGameUseCase(repo);
        game = createGameUseCase.create(p1, p2);

        sut  = new PlayHandUseCase(repo);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        hand = null;
    }

    @Test
    @DisplayName("Should throw if playCard method parameters are null")
    void shouldThrowIfPlayCardMethodParametersAreNull() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> sut.playCard(null, Card.closed())),
                () -> assertThrows(NullPointerException.class, () -> sut.playCard(p1.getUuid(), null))
        );
    }

    @Test
    @DisplayName("Should throw if accept method parameter is null")
    void shouldThrowIfAcceptMethodParameterIsNull() {
        assertThrows(NullPointerException.class, () -> sut.accept(null));
    }

    @Test
    @DisplayName("Should throw if quit method parameter is null")
    void shouldThrowIfQuitMethodParameterIsNull() {
        assertThrows(NullPointerException.class, () -> sut.quit(null));
    }

    @Test
    @DisplayName("Should throw if raiseBet method parameter is null")
    void shouldThrowIfRaiseBetMethodParameterIsNull() {
        assertThrows(NullPointerException.class, () -> sut.raiseBet(null));
    }

    @Test
    @DisplayName("Should throw if there is no active game for player UUID")
    void shouldThrowIfThereIsNoActiveGameForPlayerUuid() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.playCard(UUID.randomUUID(), Card.closed()));
    }

    @Test
    @DisplayName("Should throw if opponent is playing in player turn")
    void shouldThrowIfOpponentIsPlayingInPlayerTurn() {
        assertThrows(UnsupportedGameRequestException.class, () -> sut.playCard(p2Uuid, Card.closed()));
    }

    @Test
    @DisplayName("Should throw if requests actions not allowed in hand state")
    void shouldThrowIfRequestsActionsNotAllowedInHandState() {
        assertAll(
                () -> assertThrows(UnsupportedGameRequestException.class, () -> sut.accept(p1Uuid)),
                () -> assertThrows(UnsupportedGameRequestException.class, () -> sut.quit(p1Uuid)),
                () -> {
                    sut.raiseBet(p1Uuid);
                    assertThrows(UnsupportedGameRequestException.class, () -> sut.playCard(p2Uuid, Card.closed()));
                }
        );
    }

    @Test
    @DisplayName("Should throw if requests action and the game is done")
    void shouldThrowIfRequestsActionAndTheGameIsDone() {
        when(p1.getScore()).thenReturn(12);
        assertThrows(UnsupportedGameRequestException.class, () -> sut.playCard(p1Uuid, Card.closed()));
    }


/*
    @Test
    @DisplayName("Should throw if game is done")
    void shouldThrowIfGameIsDone() {

        assertThrows(UnsupportedGameRequestException.class, () -> sut.playCard(p1.getUuid(), Card.closed()));
    }*/


   /* @Test
    void shouldHaveHandWinnerAfterTwoRounds(){
       when(p1.playCard()).thenReturn(Card.of(Rank.THREE,Suit.SPADES)).thenReturn(Card.of(Rank.THREE,Suit.HEARTS));
       when(p2.playCard()).thenReturn(Card.of(Rank.FOUR,Suit.SPADES)).thenReturn(Card.of(Rank.FOUR,Suit.HEARTS));
       sut.play();
       assertEquals(p1, getWinner(hand));
   }

    @Test
    void shouldHaveHandWinnerAfterThreeRounds(){
        when(p1.playCard()).thenReturn(Card.of(Rank.THREE,Suit.SPADES)).thenReturn(Card.of(Rank.FOUR,Suit.HEARTS)).thenReturn(Card.of(Rank.THREE, Suit.DIAMONDS));
        when(p2.playCard()).thenReturn(Card.of(Rank.FOUR,Suit.SPADES)).thenReturn(Card.of(Rank.THREE,Suit.HEARTS)).thenReturn(Card.of(Rank.FOUR, Suit.DIAMONDS));
        sut.play();
        assertEquals(p1, getWinner(hand));
    }

    @Test
    @DisplayName("ShouldWinnerReceiveOnePointInMaoDeOnzeRun")
    void shouldWinnerReceiveOnePointInMaoDeOnzeRun() {
        when(game.getPlayer1()).thenReturn(p1);
        when(game.getPlayer2()).thenReturn(p2);
        when(game.isMaoDeOnze()).thenReturn(true);
        when(p1.getScore()).thenReturn(11);
        when(p1.getMaoDeOnzeResponse()).thenReturn(false);
        sut.play();
        assertAll(
                () -> assertEquals(p2, getWinner(hand)),
                () -> assertEquals(HandScore.ONE, hand.getScore())
        );
    }

    @Test
    @DisplayName("ShouldWinnerReceiveThreePointsForPlayedMaoDeOnze")
    void shouldWinnerReceiveThreePointsForPlayedMaoDeOnze() {
        when(game.getPlayer1()).thenReturn(p1);
        when(p1.playCard()).thenReturn(Card.of(Rank.THREE,Suit.SPADES));
        when(p2.playCard()).thenReturn(Card.of(Rank.FOUR,Suit.SPADES));
        when(game.isMaoDeOnze()).thenReturn(true);
        when(p1.getScore()).thenReturn(11);
        when(p1.getMaoDeOnzeResponse()).thenReturn(true);
        sut.play();
        assertEquals(HandScore.THREE, hand.getScore());
    }

*/
    private Player getWinner(Hand hand) {
        Optional<HandResult> handResult = hand.getResult();
        return handResult.flatMap(HandResult::getWinner).orElse(null);
    }

}