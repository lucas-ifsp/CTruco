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
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.round.Round;
import com.bueno.domain.entities.truco.Truco;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayHandUseCaseTest {

    private Hand hand;
    private PlayHandUseCase sut;

    @Mock
    private Game game;
    @Mock
    private Player p1;
    @Mock
    private Player p2;
    @Mock
    private Deck deck;

    @BeforeAll
    static void init(){
        Logger.getLogger(Hand.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Round.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Truco.class.getName()).setLevel(Level.OFF);
    }

    @BeforeEach
    void setUp() {
        when(deck.takeOne()).thenReturn(Card.of(Rank.SEVEN, Suit.CLUBS));
        hand = new Hand(p1, p2, deck);
        when(game.prepareNewHand()).thenReturn(hand);
        sut  = new PlayHandUseCase(game);
    }

    @AfterEach
    void tearDown() {
        sut = null;
        hand = null;
    }

   @Test
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


    private Player getWinner(Hand hand) {
        Optional<HandResult> handResult = hand.getResult();
        return handResult.flatMap(HandResult::getWinner).orElse(null);
    }
}