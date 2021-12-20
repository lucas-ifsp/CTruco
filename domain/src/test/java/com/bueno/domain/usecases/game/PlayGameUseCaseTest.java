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

package com.bueno.domain.usecases.game;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayGameUseCaseTest {

    @Mock private Player p1;
    @Mock private Player p2;
    private PlayGameUseCase sut;

    @BeforeAll
    static void init(){
        LogManager.getLogManager().reset();
    }

    @BeforeEach
    void setUp(){
        when(p1.chooseCardToPlay()).thenReturn(CardToPlay.of(Card.of(Rank.THREE, Suit.SPADES)));
        when(p2.chooseCardToPlay()).thenReturn(CardToPlay.of(Card.of(Rank.TWO, Suit.SPADES)));
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
        final UUID p1UUID = UUID.randomUUID();
        final UUID p2UUID = UUID.randomUUID();
        when(p1.getUuid()).thenReturn(p1UUID);
        when(p2.getUuid()).thenReturn(p2UUID);

        final Intel intel = sut.playNewHand();
        assertFalse(intel.isGameDone());
    }
}