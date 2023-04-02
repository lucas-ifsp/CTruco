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

package com.bueno.domain.entities.game;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Deck;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {

    private Game sut;
    @Mock private Player player1;
    @Mock private Player player2;
    @Mock private Deck deck;

    @BeforeEach
    void setUp(){
        when(deck.takeOne()).thenReturn(Card.of(Rank.FOUR, Suit.DIAMONDS));
        when(player1.getCards()).thenReturn(List.of(Card.of(Rank.KING, Suit.CLUBS), Card.of(Rank.KING, Suit.SPADES)));
        when(player2.getCards()).thenReturn(List.of(Card.of(Rank.JACK, Suit.CLUBS), Card.of(Rank.JACK, Suit.SPADES)));
        sut = new Game(player1, player2, deck);
    }

    @Test
    @DisplayName("Should correctly create game")
    void shouldCorrectlyCreateGame() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.getHands().size()).as("Number of hands").isOne();
        softly.assertThat(sut.getUuid()).as("Game uuid").isNotNull();
        softly.assertThat(sut.getPlayer1()).as("Player 1").isEqualTo(player1);
        softly.assertThat(sut.getPlayer2()).as("Player 2").isEqualTo(player2);
        softly.assertAll();
    }

    @Test
    @DisplayName("Should correctly prepare hand")
    void shouldCorrectlyPrepareHand() {
        sut = new Game(player1, player2);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.getFirstToPlay()).as("First to play").isEqualTo(player1);
        softly.assertThat(sut.getLastToPlay()).as("Last to play").isEqualTo(player2);
        softly.assertThat(sut.handsPlayed()).as("Number of hands").isOne();
        softly.assertAll();
    }

    @Test
    @DisplayName("Should have winner when game ends")
    void shouldGetWinnerWhenGameEnds() {
        when(player1.getScore()).thenReturn(12);
        assertThat(sut.getWinner().orElse(null)).isEqualTo(player1);
    }

    @Test
    @DisplayName("Should have no winner before game ends")
    void shouldGetNoWinnerBeforeGameEnds() {
        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(11);
        assertThat(sut.getWinner().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should be mao de onze if only one player has 11 points")
    void shouldBeMaoDeOnzeIfOnlyOnePlayerHas11Points() {
        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(3);
        assertThat(sut.isMaoDeOnze()).isTrue();
    }

    @Test
    @DisplayName("Should not be mao de onze if both players have 11 points")
    void shouldNotBeMaoDeOnzeIfBothPlayersHave11Points() {
        when(player1.getScore()).thenReturn(11);
        when(player2.getScore()).thenReturn(11);
        assertThat(sut.isMaoDeOnze()).isFalse();
    }

    @Test
    @DisplayName("Should not be mao de onze if no player has 11 points")
    void shouldNotBeMaoDeOnzeIfNoPlayerHas11Points() {
        when(player1.getScore()).thenReturn(10);
        when(player2.getScore()).thenReturn(8);
        assertThat(sut.isMaoDeOnze()).isFalse();
    }

    @Test
    @DisplayName("Should get intel from current hand")
    void shouldGetIntelFromCurrentHand() {
        assertThat(sut.getIntel()).isNotNull();
    }

    @Test
    @DisplayName("Should get all history when base intel is null")
    void shouldGetAllHistoryWhenBaseIntelIsNull() {
        sut.prepareNewHand();
        sut.prepareNewHand();
        assertThat(sut.getIntelSince(null).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should correctly get intel in the same hand")
    void shouldCorrectlyGetIntelInTheSameHand() {
        final Intel firstHandIntel = sut.getIntel();
        final Hand hand = sut.currentHand();

        hand.playFirstCard(player1, Card.of(Rank.KING, Suit.CLUBS));
        hand.playSecondCard(player2, Card.of(Rank.JACK, Suit.CLUBS));
        hand.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));

        assertThat(sut.getIntelSince(firstHandIntel.timestamp()).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should correctly get intel between hands")
    void shouldCorrectlyGetIntelBetweenHands() {
        final Intel firstHandIntel = sut.getIntel();
        final Hand hand = sut.currentHand();

        hand.playFirstCard(player1, Card.of(Rank.KING, Suit.CLUBS));
        hand.playSecondCard(player2, Card.of(Rank.JACK, Suit.CLUBS));
        hand.playFirstCard(player1, Card.of(Rank.KING, Suit.SPADES));
        hand.playSecondCard(player2, Card.closed());
        sut.prepareNewHand();
        final Hand newHand = sut.currentHand();
        newHand.playFirstCard(player2, Card.of(Rank.KING, Suit.CLUBS));

        assertThat(sut.getIntelSince(firstHandIntel.timestamp()).size()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should games with same uuid be equal")
    void shouldGamesWithSameUuidBeEqual() {
        final UUID uuid = UUID.randomUUID();
        assertThat(new Game(player1, player2, uuid, new Deck())).isEqualTo(new Game(player1, player2, uuid, new Deck()));
    }

    @Test
    @DisplayName("Should games of same uuid have the same hashcode")
    void shouldGamesOfSameUuidHaveTheSameHashcode() {
        final UUID uuid = UUID.randomUUID();
        assertThat(new Game(player1, player2, uuid, new Deck()).hashCode()).isEqualTo(new Game(player1, player2, uuid, new Deck()).hashCode());
    }

    @Test
    @DisplayName("Should correctly toString")
    void shouldCorrectlyToString() {
        final UUID uuid = UUID.randomUUID();
        when(player1.getUsername()).thenReturn("Player1");
        when(player2.getUsername()).thenReturn("Player2");
        when(player1.getScore()).thenReturn(2);
        when(player2.getScore()).thenReturn(8);

        final String expected = String.format("Game = %s, %s %d x %d %s",
                uuid, player1.getUsername(), player1.getScore(), player2.getScore(), player2.getUsername());

        assertThat(new Game(player1, player2, uuid, new Deck()).toString()).isEqualTo(expected);
    }
}