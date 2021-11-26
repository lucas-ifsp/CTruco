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

package com.bueno.domain.entities.deck;

import java.util.Objects;

public final class Card {

    private static final Card[] cache = new Card[41];

    private final Suit suit;
    private final Rank rank;

    private Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit  = suit;
    }

    public static Card of(Rank rank, Suit suit){
        Objects.requireNonNull(rank);
        Objects.requireNonNull(suit);
        if(rank == Rank.HIDDEN ^ suit == Suit.HIDDEN)
            throw new IllegalArgumentException("Both rank and suit must be HIDDEN or none: " + rank + suit);

        return fromCache(rank, suit);
    }

    public static Card closed(){
        return fromCache(Rank.HIDDEN, Suit.HIDDEN);
    }

    private static Card fromCache(Rank rank, Suit suit){
        int rankValue = rank.value();
        int suitValue = suit.value();
        int cachePosition = rankValue == 0 || suitValue == 0 ? 0 : (rankValue - 1) * 4 + suitValue;

        if(cache[cachePosition] == null) cache[cachePosition] = new Card(rank, suit);
        return cache[cachePosition];
    }

    public int compareValueTo(Card card, Card vira){
        return computeCardValue(this, vira) - computeCardValue(card, vira);
    }

    private int computeCardValue(Card card, Card vira) {
        if (!card.isManilha(vira))
            return card.getRank().value();
        else
            return switch (card.getSuit()) {
                case DIAMONDS -> 11;
                case SPADES -> 12;
                case HEARTS -> 13;
                case CLUBS -> 14;
                case HIDDEN -> throw new IllegalStateException("Hidden card can not be manilha!");
            };
    }

    public boolean isManilha(Card vira){
        return getRank() == vira.getRank().next();
    }

    public boolean isZap(Card vira){
        return isManilha(vira) && suit == Suit.CLUBS;
    }

    public boolean isCopas(Card vira){
        return isManilha(vira) && suit == Suit.HEARTS;
    }

    public boolean isEspadilha(Card vira){
        return isManilha(vira) && suit == Suit.SPADES;
    }

    public boolean isOuros(Card vira){
        return isManilha(vira) && suit == Suit.DIAMONDS;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public String toString() {
        return "["+ rank + suit +"]";
    }
}
