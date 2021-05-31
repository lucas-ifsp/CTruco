package com.bueno.truco.domain.entity.deck;

import java.util.List;
import java.util.Objects;

public class Card {
    private int rank;
    private Suit suit;
    private List<String> rankNames = List.of("Invalid", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Q", "J", "K");


    public Card(int rank, Suit suit) {
        setRank(rank);
        this.suit = suit;
    }

    public Card(char rankName, Suit suit) {
        this.setRank(rankNames.indexOf(String.valueOf(rankName)));
        this.suit = suit;

    }

    private void setRank(int rank) {
        if(rank < 1 || rank > 13)
            throw new IllegalArgumentException("Invalid card rank");
        this.rank = rank;
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
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        return rankNames.get(rank) + " of " + suit.getName();
    }
}
