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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
class PlayerTest {

    private List<Card> cards;
    private Card c1;

    @BeforeEach
    void setUp() {
        c1 = Card.of(Rank.THREE, Suit.CLUBS);
        Card c2 = Card.of(Rank.TWO, Suit.CLUBS);
        Card c3 = Card.of(Rank.ACE, Suit.SPADES);
        cards = new ArrayList<>(List.of(c1, c2, c3));
    }

    @AfterEach
    void tearDown() {
        c1 = null;
        cards = null;
    }

    @Test
    @DisplayName("Should create a player from a user")
    void shouldCreateAPlayerFromAUser() {
        final UUID uuid = UUID.randomUUID();
        final String username = "Test";
        final Player sut = Player.of(uuid, username);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.getUsername()).isEqualTo(username);
        softly.assertThat(sut.getUuid()).isEqualTo(uuid);
        softly.assertThat(sut.isBot()).as("It is false that user is a bot.").isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("Should create a player for a bot")
    void shouldCreateAPlayerForABot() {
        assertThat(Player.ofBot("TestBot").isBot()).as("It is true that user is a bot.").isTrue();
    }

    @Test
    @DisplayName("Should create a player with specific UUID for a bot")
    void shouldCreateAPlayerWithSpecificUuidForABot() {
        final UUID uuid = UUID.randomUUID();
        final Player sut = Player.ofBot(uuid, "TestBot");
        assertThat(sut.getUuid()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Should play an owned card")
    void shouldPlayAnOwnedCard() {
        final Player sut = Player.ofBot("Bot test");
        sut.setCards(cards);
        final Card playedCard = sut.play(c1);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(playedCard).isEqualTo(c1);
        softly.assertThat(sut.getCards().contains(playedCard))
                .as("It's false that user owns the card")
                .isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("Should discard an owned card")
    void shouldDiscardAnOwnedCard() {
        final Player sut = Player.ofBot("Bot test");
        sut.setCards(cards);
        final Card discard = sut.discard(c1);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(discard).isEqualTo(Card.closed());
        softly.assertThat(sut.getCards().contains(discard))
                .as("It's false that user owns the card")
                .isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("Should throw if tries to play a card not owned")
    void shouldThrowIfTriesToPlayACardNotOwned() {
        final Player sut = Player.ofBot("Bot test");
        final Card otherCard = Card.of(Rank.FOUR, Suit.SPADES);
        sut.setCards(cards);
        assertThatIllegalArgumentException().isThrownBy(() -> sut.play(otherCard));
    }

    @Test
    @DisplayName("Should throw if tries to discard a card not owned")
    void shouldThrowIfTriesToDiscardACardNotOwned() {
        final Player sut = Player.ofBot("Bot test");
        final Card otherCard = Card.of(Rank.FOUR, Suit.SPADES);
        sut.setCards(cards);
        assertThatIllegalArgumentException().isThrownBy(() -> sut.discard(otherCard));
    }

    @Test
    @DisplayName("Should correctly add score")
    void shouldCorrectlyAddScore() {
        final Player sut = Player.ofBot("Bot test");
        sut.addScore(HandPoints.THREE);
        sut.addScore(HandPoints.SIX);
        assertThat(sut.getScore()).isEqualTo(HandPoints.NINE.get());
    }

    @Test
    @DisplayName("Should limit player score to 12")
    void shouldLimitPlayerScoreTo12() {
        final Player sut = Player.ofBot("Bot test");
        sut.addScore(HandPoints.NINE);
        sut.addScore(HandPoints.SIX);
        assertThat(sut.getScore()).isEqualTo(HandPoints.TWELVE.get());
    }

    @Test
    @DisplayName("Should players with the same uuid be the same")
    void shouldPlayersWithTheSameUuidBeTheSame() {
        final UUID uuid = UUID.randomUUID();
        assertThat(Player.ofBot(uuid, "Test1")).isEqualTo(Player.ofBot(uuid, "Test2"));
    }

    @Test
    @DisplayName("Should players with the same UUID have the same hashcode")
    void shouldPlayersWithTheSameUuidHaveTheSameHashcode() {
        final UUID uuid = UUID.randomUUID();
        assertThat(Player.ofBot(uuid, "Test1").hashCode())
                .isEqualTo(Player.ofBot(uuid, "Test2").hashCode());
    }

    @Test
    @DisplayName("Should players with different uuid not be the same")
    void shouldPlayersWithDifferentUuidNotBeTheSame() {
        assertThat(Player.ofBot("Test1")).isNotEqualTo(Player.ofBot("Test1"));
    }

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        final UUID uuid = UUID.randomUUID();
        final Player sut = Player.ofBot(uuid, "TestBot");
        String out = String.format("Player = %s (%s) has %d point(s)", sut.getUsername(), sut.getUuid(), sut.getScore());
        assertThat(sut.toString()).isEqualTo(out);
    }
}