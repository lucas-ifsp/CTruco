/*
 * Copyright (C) 2021 Lucas B. R. de Oliveira
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
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.deck.Suit;
import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.hand.Hand;
import com.bueno.truco.domain.entities.hand.Intel;
import com.bueno.truco.domain.entities.player.util.Player;
import com.bueno.truco.domain.entities.round.Round;
import com.bueno.truco.domain.entities.truco.Truco;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayGameUseCaseTest {

    @Mock
    private Player p1;
    @Mock
    private Player p2;
    private PlayGameUseCase sut;

    @BeforeAll
    static void init(){
        Logger.getLogger(Game.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Hand.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Round.class.getName()).setLevel(Level.OFF);
        Logger.getLogger(Truco.class.getName()).setLevel(Level.OFF);
    }

    @BeforeEach
    void setUp(){
        when(p1.playCard()).thenReturn(new Card(3, Suit.SPADES));
        when(p2.playCard()).thenReturn(new Card(2, Suit.SPADES));
        sut = new PlayGameUseCase(p1, p2);
    }

    @AfterEach
    void tearDown(){
        sut = null;
    }

    @Test
    @DisplayName("Should have no winner after single simple hand")
    void shouldHaveNoWinnerAfterSingleSimpleHand(){
        when(p1.requestTruco()).thenReturn(false);
        when(p2.requestTruco()).thenReturn(false);
        final Intel intel = sut.playNewHand();
        Assertions.assertAll(
                ()-> assertNotEquals(12, intel.getOpponentScore(p2)),
                ()-> assertNotEquals(12, intel.getOpponentScore(p1))
        );
    }
}