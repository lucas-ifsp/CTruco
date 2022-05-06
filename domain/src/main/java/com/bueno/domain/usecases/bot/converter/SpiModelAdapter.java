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

package com.bueno.domain.usecases.bot.converter;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.entities.intel.Intel;
import com.bueno.domain.entities.player.Player;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.RoundResult;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpiModelAdapter {

    public static GameIntel toGameIntel(Player player, Intel intel) {
        final Function<UUID, RoundResult> toRoundResult = uuid -> uuid == null ? RoundResult.DREW
                : uuid.equals(player.getUuid()) ? RoundResult.WON : RoundResult.LOST;

        final List<RoundResult> roundResults = intel.roundWinnersUuid().stream()
                .map(winner -> winner.orElse(null))
                .map(toRoundResult).collect(Collectors.toList());

        final Function<List<Card>, List<TrucoCard>> toTrucoCardList = cardList ->
                cardList.stream().map(SpiModelAdapter::toTrucoCard).collect(Collectors.toList());

        final List<TrucoCard> openCards = toTrucoCardList.apply(intel.openCards());
        final List<TrucoCard> botCards = toTrucoCardList.apply(player.getCards());

        return GameIntel.StepBuilder.with()
                .gameInfo(roundResults, openCards, toTrucoCard(intel.vira()), intel.handPoints())
                .botInfo(botCards, intel.currentPlayerScore())
                .opponentScore(intel.currentOpponentScore())
                .opponentCard(toTrucoCard(intel.cardToPlayAgainst().orElse(null)))
                .build();
    }

    public static Card toCard(TrucoCard card){
        if(card == null) return null;
        final String rankName = card.getRank().toString();
        final String suitName = card.getSuit().toString();
        return Card.of(Rank.ofSymbol(rankName), Suit.ofSymbol(suitName));
    }

    private static TrucoCard toTrucoCard(Card card){
        if(card == null) return null;
        final String rankName = card.getRank().toString();
        final String suitName = card.getSuit().toString();
        return TrucoCard.of(CardRank.ofSymbol(rankName), CardSuit.ofSymbol(suitName));
    }
}
