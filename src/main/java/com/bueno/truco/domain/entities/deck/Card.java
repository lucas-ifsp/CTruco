package com.bueno.truco.domain.entities.deck;

import java.util.List;

public class Card {

    private int rank;
    private Suit suit;
    private static final List<String> rankNames = List.of("Hidden", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Q", "J", "K");

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
        final List<Integer> values = List.of(0, 4, 5, 6, 7, 11, 12, 13, 1, 2, 3);
        final int manilha;

        if (vira.getRank() == 3)
            manilha = 4;
        else
            manilha = values.get(values.indexOf(vira.getRank()) + 1);

        if (card.getRank() != manilha)
            return values.indexOf(card.getRank());
        else
            return switch (card.getSuit()) {
                case DIAMONDS -> 11;
                case SPADES -> 12;
                case HEARTS -> 13;
                case CLUBS -> 14;
            };
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
