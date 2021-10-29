/*
 * Copyright (C) 2021 Lucas B. R. de Oliveira
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
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.truco.domain.entities.deck;

import java.util.List;

public class Card {

    private int rank;
    private Suit suit;
    private static final List<String> rankNames = List.of("Hidden", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Q", "J", "K");
    private final List<Integer> ascendingCardRankValues = List.of(0, 4, 5, 6, 7, 11, 12, 13, 1, 2, 3);;

    private Card(){
    }

    public Card(int rank, Suit suit) {
        setRank(rank);
        setSuit(suit);
    }

    public Card(char rankName, Suit suit) {
        this(rankNames.indexOf(String.valueOf(rankName).toUpperCase()), suit);
    }

    private void setRank(int rank) {
        if(rank < 0 || rank > 13)
            throw new IllegalArgumentException("Invalid card rank!");
        if(rank == 8 || rank == 9 || rank == 10)
            throw new IllegalArgumentException("Invalid card rank for truco a game!");
        this.rank = rank;
    }

    private void setSuit(Suit suit) {
        if(suit == null)
            throw new IllegalArgumentException("Card suit must not be null!");
        this.suit = suit;
    }

    public int compareValueTo(Card card, Card vira){
        return computeCardValue(this, vira) - computeCardValue(card, vira);
    }

    private int computeCardValue(Card card, Card vira) {
        if (!card.isManilha(vira))
            return ascendingCardRankValues.indexOf(card.getRank());
        else
            return switch (card.getSuit()) {
                case DIAMONDS -> 11;
                case SPADES -> 12;
                case HEARTS -> 13;
                case CLUBS -> 14;
            };
    }

    public boolean isManilha(Card vira){
        return getRank() == getManilhaRank(vira);
    }

    private int getManilhaRank(Card vira) {
        if (vira.getRank() == 3) return 4;
        return ascendingCardRankValues.get(ascendingCardRankValues.indexOf(vira.getRank()) + 1);
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

    public static Card getClosedCard(){
        return new Card();
    }

    public int getRank() {
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
        return this.equals(getClosedCard())?
                "[Xx]" :
                "["+rankNames.get(rank) + toUnicodeSymbol(suit)+"]";
    }

    private String toUnicodeSymbol(Suit suit){
        return switch (suit){
            case DIAMONDS-> "\u2666";
            case HEARTS -> "\u2665";
            case CLUBS -> "\u2663";
            case SPADES -> "\u2660";
        };
    }
}
