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

package com.bueno.domain.entities.player;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.hand.HandPoints;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    private List<Card> cards;
    private Card c1;
    private Card c2;
    private Card c3;

    @BeforeEach
    void setUp() {
        c1 = Card.of(Rank.THREE, Suit.CLUBS);
        c2 = Card.of(Rank.TWO, Suit.CLUBS);
        c3 = Card.of(Rank.ACE, Suit.SPADES);
        cards = new ArrayList<>(List.of(c1, c2, c3));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should create a player from a user")
    void shouldCreateAPlayerFromAUser() {
        final User user = new User("Test", "test@test.com");
        final Player sut = Player.of(user);
        assertAll(
                () -> assertEquals(user.getUsername(), sut.getUsername()),
                () -> assertNotNull(sut.getUuid())
        );
    }

    @Test
    @DisplayName("Should create a player for a bot")
    void shouldCreateAPlayerForABot() {
        assertTrue(Player.ofBot("TestBot").isBot());
    }

    @Test
    @DisplayName("Should create a player with specific UUID for a bot")
    void shouldCreateAPlayerWithSpecificUuidForABot() {
        final UUID uuid = UUID.randomUUID();
        final Player sut = Player.ofBot(uuid, "TestBot");
        assertEquals(uuid, sut.getUuid());
    }

    @Test
    @DisplayName("Should play an owned card")
    void shouldPlayAnOwnedCard() {
        final Player sut = Player.ofBot("Bot test");
        sut.setCards(cards);
        final Card playedCard = sut.play(c1);
        assertAll(
                () -> assertFalse(sut.getCards().contains(playedCard)),
                () -> assertEquals(c1, playedCard)
        );
    }

    @Test
    @DisplayName("Should discard an owned card")
    void shouldDiscardAnOwnedCard() {
        final Player sut = Player.ofBot("Bot test");
        sut.setCards(cards);
        final Card discard = sut.discard(c1);
        assertAll(
                () -> assertFalse(sut.getCards().contains(discard)),
                () -> assertEquals(Card.closed(), discard)
        );
    }

    @Test
    @DisplayName("Should throw if tries to play a card not owned")
    void shouldThrowIfTriesToPlayACardNotOwned() {
        final Player sut = Player.ofBot("Bot test");
        final Card otherCard = Card.of(Rank.FOUR, Suit.SPADES);
        sut.setCards(cards);
        assertThrows(IllegalArgumentException.class, () -> sut.play(otherCard));
    }

    @Test
    @DisplayName("Should throw if tries to discard a card not owned")
    void shouldThrowIfTriesToDiscardACardNotOwned() {
        final Player sut = Player.ofBot("Bot test");
        final Card otherCard = Card.of(Rank.FOUR, Suit.SPADES);
        sut.setCards(cards);
        assertThrows(IllegalArgumentException.class, () -> sut.discard(otherCard));
    }

    @Test
    @DisplayName("Should correctly add score")
    void shouldCorrectlyAddScore() {
        final Player sut = Player.ofBot("Bot test");
        sut.addScore(HandPoints.THREE);
        sut.addScore(HandPoints.SIX);
        assertEquals(HandPoints.NINE.get(), sut.getScore());
    }

    @Test
    @DisplayName("Should limit player score to 12")
    void shouldLimitPlayerScoreTo12() {
        final Player sut = Player.ofBot("Bot test");
        sut.addScore(HandPoints.NINE);
        sut.addScore(HandPoints.SIX);
        assertEquals(HandPoints.TWELVE.get(), sut.getScore());
    }

    @Test
    @DisplayName("Should players with the same uuid be the same")
    void shouldPlayersWithTheSameUuidBeTheSame() {
        final UUID uuid = UUID.randomUUID();
        assertEquals(Player.ofBot(uuid, "Test1"), Player.ofBot(uuid, "Test2"));
    }

    @Test
    @DisplayName("Should players with the same UUID have the same hashcode")
    void shouldPlayersWithTheSameUuidHaveTheSameHashcode() {
        final UUID uuid = UUID.randomUUID();
        assertEquals(Player.ofBot(uuid, "Test1").hashCode(), Player.ofBot(uuid, "Test2").hashCode());
    }

    @Test
    @DisplayName("Should players with different uuid not be the same")
    void shouldPlayersWithDifferentUuidNotBeTheSame() {
        assertNotEquals(Player.ofBot("Test1"), Player.ofBot( "Test1"));
    }

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        final UUID uuid = UUID.randomUUID();
        final Player sut = Player.ofBot(uuid, "TestBot");
        String out = String.format("Player = %s (%s) has %d point(s)", sut.getUsername(), sut.getUuid(), sut.getScore());
        assertEquals(out, sut.toString());
    }


}