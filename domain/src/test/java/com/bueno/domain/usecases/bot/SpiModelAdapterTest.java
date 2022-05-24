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

package com.bueno.domain.usecases.bot;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.domain.usecases.bot.converter.SpiModelAdapter;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.RoundResult;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bueno.spi.model.GameIntel.RoundResult.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpiModelAdapterTest {

    @Mock private Player player;
    @Mock private Intel intel;

    @Test
    @DisplayName("Should create GameIntel from Player and Info")
    void shouldCreateGameIntelFromPlayerAndInfo() {
        final UUID p1Uuid = UUID.randomUUID();
        final List<Optional<UUID>> results = List.of(Optional.of(p1Uuid), Optional.of(UUID.randomUUID()), Optional.empty());
        final List<Card> openCards = List.of(Card.of(Rank.THREE, Suit.CLUBS), Card.closed(), Card.of(Rank.ACE, Suit.CLUBS));
        final List<Card> botCards = List.of(Card.of(Rank.TWO, Suit.CLUBS), Card.of(Rank.ACE, Suit.SPADES));

        when(player.getUuid()).thenReturn(p1Uuid);
        when(intel.roundWinnersUuid()).thenReturn(results);
        when(intel.openCards()).thenReturn(openCards);
        when(intel.vira()).thenReturn(Card.of(Rank.THREE, Suit.CLUBS));
        when(player.getCards()).thenReturn(botCards);
        when(intel.handPoints()).thenReturn(9);
        when(intel.currentPlayerScore()).thenReturn(1);
        when(intel.currentOpponentScore()).thenReturn(4);
        when(intel.cardToPlayAgainst()).thenReturn(Optional.of(Card.of(Rank.ACE, Suit.CLUBS)));

        final List<RoundResult> expectedResults = List.of(WON, LOST, DREW);
        final List<TrucoCard> expectedOpenCards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.closed(),
                TrucoCard.of(CardRank.ACE, CardSuit.CLUBS));
        final List<TrucoCard> expectedBotCards = List.of(TrucoCard.of(CardRank.TWO, CardSuit.CLUBS),
                TrucoCard.of(CardRank.ACE, CardSuit.SPADES));

        GameIntel expected = GameIntel.StepBuilder.with()
                .gameInfo(expectedResults, expectedOpenCards, TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), 9)
                .botInfo(expectedBotCards, 1)
                .opponentScore(4)
                .opponentCard(TrucoCard.of(CardRank.ACE, CardSuit.CLUBS))
                .build();

        assertThat(SpiModelAdapter.toGameIntel(player, intel)).isEqualTo(expected);
    }

    @Test
    @DisplayName("ShouldConvertTrucoCardToCard")
    void shouldConvertTrucoCardToCard() {
        TrucoCard trucoCard = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
        assertThat(SpiModelAdapter.toCard(trucoCard)).isEqualTo(Card.of(Rank.ACE, Suit.CLUBS));
    }
}