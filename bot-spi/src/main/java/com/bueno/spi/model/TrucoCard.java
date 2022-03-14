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

package com.bueno.spi.model;

import java.util.Objects;

public final class TrucoCard {

    private static final TrucoCard[] cache = new TrucoCard[41];
    private final CardSuit suit;
    private final CardRank rank;

    private TrucoCard(CardRank rank, CardSuit suit) {
        this.rank = rank;
        this.suit  = suit;
    }

    public static TrucoCard of(CardRank rank, CardSuit suit){
        Objects.requireNonNull(rank);
        Objects.requireNonNull(suit);
        if(rank == CardRank.HIDDEN ^ suit == CardSuit.HIDDEN)
            throw new IllegalArgumentException("Both rank and suit must be HIDDEN or none: " + rank + suit);

        return fromCache(rank, suit);
    }

    public static TrucoCard closed(){
        return fromCache(CardRank.HIDDEN, CardSuit.HIDDEN);
    }

    private static TrucoCard fromCache(CardRank rank, CardSuit suit){
        int rankValue = rank.value();
        int suitValue = suit.value();
        int cachePosition = rankValue == 0 || suitValue == 0 ? 0 : (rankValue - 1) * 4 + suitValue;

        if(cache[cachePosition] == null) cache[cachePosition] = new TrucoCard(rank, suit);
        return cache[cachePosition];
    }

    public int compareValueTo(TrucoCard card, TrucoCard vira){
        return computeCardValue(this, vira) - computeCardValue(card, vira);
    }

    private int computeCardValue(TrucoCard card, TrucoCard vira) {
        if (!card.isManilha(vira)) return card.getRank().value();
        else return switch (card.getSuit()) {
                case DIAMONDS -> 11;
                case SPADES -> 12;
                case HEARTS -> 13;
                case CLUBS -> 14;
                case HIDDEN -> throw new IllegalStateException("Closed card can not be manilha!");
            };
    }

    public boolean isManilha(TrucoCard vira){
        return getRank() == vira.getRank().next();
    }

    public boolean isZap(TrucoCard vira){
        return isManilha(vira) && suit == CardSuit.CLUBS;
    }

    public boolean isCopas(TrucoCard vira){
        return isManilha(vira) && suit == CardSuit.HEARTS;
    }

    public boolean isEspadilha(TrucoCard vira){
        return isManilha(vira) && suit == CardSuit.SPADES;
    }

    public boolean isOuros(TrucoCard vira){
        return isManilha(vira) && suit ==  CardSuit.DIAMONDS;
    }

    public CardRank getRank() {
        return rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrucoCard card = (TrucoCard) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public String toString() {
        return "["+ rank + suit +"]";
    }
}
