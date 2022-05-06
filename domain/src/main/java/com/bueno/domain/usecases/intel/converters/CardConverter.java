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

package com.bueno.domain.usecases.intel.converters;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.deck.Rank;
import com.bueno.domain.entities.deck.Suit;
import com.bueno.domain.usecases.intel.dtos.CardDto;

public class CardConverter {
    public static Card toDto(CardDto cardDto) {
        final Rank rank = Rank.ofSymbol(cardDto.getRank());
        final Suit suit = Suit.ofSymbol(cardDto.getSuit());
        return Card.of(rank, suit);
    }

    public static CardDto toEntity(Card card) {
        final String rank = card.getRank().toString();
        final String suit = card.getSuit().toString();
        return new CardDto(rank, suit);
    }
}
