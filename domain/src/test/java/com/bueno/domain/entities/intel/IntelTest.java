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

package com.bueno.domain.entities.intel;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.game.Game;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandPoints;
import com.bueno.domain.entities.hand.Round;
import com.bueno.domain.entities.intel.Intel.PlayerIntel;
import com.bueno.domain.entities.player.Player;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntelTest {

    @Mock private Game game;
    @Mock private Hand hand;
    @Mock private Round round;
    @Mock Player p1;
    @Mock Player p2;

    @BeforeEach
    void setUp() {
        lenient().when(hand.getPossibleActions()).thenReturn(EnumSet.of(PossibleAction.ACCEPT));
        lenient().when(hand.getPoints()).thenReturn(HandPoints.ONE);
        lenient().when(hand.getFirstToPlay()).thenReturn(p1);
        lenient().when(hand.getLastToPlay()).thenReturn(p2);
        lenient().when(hand.getVira()).thenReturn(Card.of(Rank.THREE, Suit.CLUBS));
    }

    @Test
    @DisplayName("Should not allow null game in static constructor")
    void shouldNotAllowNullGameInStaticConstructor() {
        assertThatNullPointerException().isThrownBy(() -> Intel.ofGame(null));
    }

    @Test
    @DisplayName("Should not allow null hand in static constructor")
    void shouldNotAllowNullHandInStaticConstructor() {
        assertThatNullPointerException().isThrownBy(() -> Intel.ofHand(null, Event.HAND_START));
    }

    @Test
    @DisplayName("Should return that game is done if game is done")
    void shouldReturnThatGameIsDoneIfGameIsDone() {
        when(game.isDone()).thenReturn(true);
        when(game.currentHand()).thenReturn(hand);
        final Intel intel = Intel.ofGame(game);
        assertThat(intel.isGameDone()).as("It is true that game is done.").isTrue();
    }

    @Test
    @DisplayName("Should have no game winner from hand static constructor")
    void shouldHaveNoGameWinnerFromHandStaticConstructor() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertThat(intel.gameWinner().isEmpty()).as("It is true that game has no winner.").isTrue();
    }

    @Test
    @DisplayName("Should game not be done from hand static constructor")
    void shouldGameNotBeDoneFromHandStaticConstructor() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertThat(intel.gameWinner().isEmpty()).as("It is false that game is done.").isTrue();
    }

    @Test
    @DisplayName("Should intel have non null timestamp")
    void shouldIntelHaveNonNullTimestamp() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertThat(intel.timestamp()).as("Any intel must have a timestamp").isNotNull();
    }

    @Test
    @DisplayName("Should two intel objects be different if created in different moments")
    void shouldTwoIntelObjectsBeDifferentIfCreatedInDifferentMoments() {
        assertThat(Intel.ofHand(hand, Event.HAND_START)).isNotEqualTo(Intel.ofHand(hand, Event.HAND_START));
    }

    @Test
    @DisplayName("Should intel created with same data in different moments have different hashcode")
    void shouldIntelCreatedWithSameDataInDifferentMomentsHaveDifferentHashcode() {
        assertThat(Intel.ofHand(hand, Event.HAND_START).hashCode()).isNotEqualTo(Intel.ofHand(hand, Event.HAND_START).hashCode());
    }

    @Test
    @DisplayName("Should intel created with same data differ only by the timestamp")
    void shouldIntelCreatedWithSameDataDifferOnlyByTheTimestamp() {
        final Intel intel1 = Intel.ofHand(hand, Event.HAND_START);
        final Intel intel2 = Intel.ofHand(hand, Event.HAND_START);

        final String[] intelString1 = intel1.toString().split("] ");
        final String[] intelString2 = intel2.toString().split("] ");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(intelString1[0]).isNotEqualTo(intelString2[0]);
        softly.assertThat(intelString1[1]).isEqualTo(intelString2[1]);
        softly.assertAll();
    }

    @Test
    @DisplayName("Should intel return current hand vira")
    void shouldIntelReturnCurrentHandVira() {
        assertThat(Intel.ofHand(hand, Event.HAND_START).vira()).isEqualTo(hand.getVira());
    }

    @Test
    @DisplayName("Should correctly return event and event player")
    void shouldCorrectlyReturnEventAndEventPlayer() {
        when(p1.getUuid()).thenReturn(UUID.randomUUID());
        when(p2.getUuid()).thenReturn(UUID.randomUUID());
        when(p1.getUsername()).thenReturn("name1");
        when(p2.getUsername()).thenReturn("name2");
        when(p1.getCards()).thenReturn(new ArrayList<>(List.of(Card.of(Rank.TWO, Suit.CLUBS))));

        final Hand hand = new Hand(p1, p2, Card.of(Rank.THREE, Suit.CLUBS));

        hand.playFirstCard(p1, Card.of(Rank.TWO, Suit.CLUBS));
        final Intel sut = hand.getLastIntel();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(sut.openCards()).hasSameElementsAs(hand.getOpenCards());
        softly.assertThat(sut.openCards()).isEqualTo(hand.getOpenCards());
        softly.assertThat(sut.eventPlayerUuid().orElseThrow()).isEqualTo(p1.getUuid());
        softly.assertThat(sut.event().orElseThrow()).isEqualTo("PLAY");
        softly.assertThat(sut.players()).isEqualTo(List.of(new PlayerIntel (p1), new PlayerIntel(p2)));
        softly.assertThat(sut.possibleActions()).hasSameElementsAs(List.of("PLAY", "RAISE"));
        softly.assertAll();
    }


    @Test
    @DisplayName("Should get default values of player and opponent if current player is null")
    void shouldGetDefaultValuesOfPlayerAndOpponentIfCurrentPlayerIsNull() {
        final Intel intel = Intel.ofHand(hand, Event.HAND_START);
        assertAll(
                () -> assertEquals(Optional.empty(), intel.currentPlayerUuid()),
                () -> assertNull(intel.currentPlayerUsername()),
                () -> assertNull(intel.currentOpponentUsername()),
                () -> assertEquals(0, intel.currentPlayerScore()),
                () -> assertEquals(0, intel.currentOpponentScore())
        );
    }

    @Test
    @DisplayName("Should correctly obtain the name of the round winners")
    void shouldCorrectlyObtainTheNameOfTheRoundWinners() {
        when(p1.getUsername()).thenReturn("name1");
        when(p2.getUsername()).thenReturn("name2");
        when(round.getWinner()).thenReturn(Optional.of(p1)).thenReturn(Optional.empty()).thenReturn(Optional.of(p2));
        when(hand.getRoundsPlayed()).thenReturn(List.of(round, round, round));

        final List<Optional<String>> expected = List.of(Optional.of("name1"), Optional.empty(), Optional.of("name2"));
        final Intel intel = Intel.ofHand(hand, Event.PLAY);

        assertThat(intel.roundWinnersUsernames()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should correctly create player intel")
    void shouldCorrectlyCreatePlayerIntel() {
        final UUID uuid = UUID.randomUUID();
        final Player player = Player.of(uuid, "name");
        final List<Card> cards = List.of(Card.of(Rank.THREE, Suit.CLUBS));
        player.setCards(cards);
        final PlayerIntel playerIntel = new PlayerIntel(player);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(playerIntel.getUsername()).isEqualTo(player.getUsername());
        softly.assertThat(playerIntel.getUuid()).isEqualTo(player.getUuid());
        softly.assertThat(playerIntel.getScore()).isEqualTo(player.getScore());
        softly.assertThat(playerIntel.getCards()).hasSameElementsAs(player.getCards());
        softly.assertThat(playerIntel.getCards()).isNotSameAs(player.getCards());
        softly.assertAll();
    }
}