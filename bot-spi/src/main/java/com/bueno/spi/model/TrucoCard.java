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
        this.suit = suit;
    }

    /**
     * <p>Creates a value object representing a Truco card.
     * The returned card is final and cached, therefore two cards of same value are represented by the same object.
     * This method does not allow creating a card that has a hidden rank and non-hidden suit or vice-versa. Use the
     * {@link #TrucoCard closed()} method to create a closed card that represents a discard.
     * </p>
     *
     * @param rank a card rank represented by the CardRank enum, must be non-null
     * @param suit a card suit represented by the CardSuit enum, must be non-null
     * @return a TrucoCard representing the given {@code rank} and {@code suit}
     * @throws NullPointerException if {@code rank} or/and {@code suit} is/are null
     * @throws IllegalArgumentException if rank or suit is HIDDEN and the other parameter is not HIDDEN
     */
    public static TrucoCard of(CardRank rank, CardSuit suit) {
        Objects.requireNonNull(rank);
        Objects.requireNonNull(suit);
        if (rank == CardRank.HIDDEN ^ suit == CardSuit.HIDDEN)
            throw new IllegalArgumentException("Both rank and suit must be HIDDEN or none: " + rank + suit);

        return fromCache(rank, suit);
    }

    /**
     * <p>Creates a value object representing a closed Truco card.
     * The returned card is final and cached, therefore two closed cards are represented by the same object.
     * </p>
     *
     * @return TrucoCard representing a closed card, i.e., a discard
     */
    public static TrucoCard closed() {
        return fromCache(CardRank.HIDDEN, CardSuit.HIDDEN);
    }

    private static TrucoCard fromCache(CardRank rank, CardSuit suit) {
        int rankValue = rank.value();
        int suitValue = suit.value();
        int cachePosition = rankValue == 0 || suitValue == 0 ? 0 : (rankValue - 1) * 4 + suitValue;

        if (cache[cachePosition] == null) cache[cachePosition] = new TrucoCard(rank, suit);
        return cache[cachePosition];
    }


    /**
     * <p>Compares two TrucoCard objects based on their relative values defined using the {@code vira} card parameter.
     * Examples:
     * </p>
     * <pre>{@code
     *    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
     *       .compareValueTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
     *           TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)) returns 1;
     *
     *    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
     *       .compareValueTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
     *           TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)) returns 0;
     *
     *    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
     *       .compareValueTo(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
     *          TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)) returns -1,
     *          because TrucoCard.of(CardRank.SIX, CardSuit.CLUBS) is the zap.
     *    }
     * </pre>
     * <p>
     * Notice that cards of same rank and different suits can have the same relative value if they are not manilhas,
     * but they still different according to the {@link #equals(Object o)} method.
     * </p>
     *
     * @param otherCard TrucoCard to be compared to the reference, must be non-null
     * @param vira TrucoCard representing the current vira, must be non-null
     * @return returns 1 if the TrucoCard represented by the object is greater than the {@code otherCard},
     * -1 if the object card is lower, and 0 if both cards have the same relative value
     * @throws NullPointerException if {@code otherCard} or/and {@code vira} is/are null
     */
    public int compareValueTo(TrucoCard otherCard, TrucoCard vira) {
        Objects.requireNonNull(otherCard, "TrucoCard to be compared must not be null.");
        Objects.requireNonNull(vira, "TrucoCard representing the vira must not be null.");
        return computeCardValue(this, vira) - computeCardValue(otherCard, vira);
    }

    private int computeCardValue(TrucoCard card, TrucoCard vira) {
        if (!card.isManilha(vira)) return card.getRank().value();
        return switch (card.getSuit()) {
            case DIAMONDS -> 11;
            case SPADES -> 12;
            case HEARTS -> 13;
            case CLUBS -> 14;
            case HIDDEN -> throw new IllegalStateException("Closed card can not be manilha!");
        };
    }

    /**
     * <p>Checks if the truco card represented by the object is a manilha using the {@code vira} card parameter.</p>
     * <pre>{@code
     *    //Returns true because the object relative value is
     *    //a manilha (zap) based on the vira parameter
     *    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
     *       .isManilha(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
     *    }
     * @param vira TrucoCard representing the current vira, must be non-null
     * @return true if the object is a manilha because of the {@code vira} card parameter and false otherwise
     * @throws NullPointerException if {@code vira} is null
     */
    public boolean isManilha(TrucoCard vira) {
        Objects.requireNonNull(vira, "TrucoCard representing the vira must not be null.");
        return getRank() == vira.getRank().next();
    }

    /**
     * <p>Checks if the truco card represented by the object is a zap using the {@code vira} card parameter.</p>
     * <pre>{@code
     *    //Returns true because the object relative value is
     *    //a zap based on the vira parameter
     *    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)
     *       .isZap(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
     *    }
     * @param vira TrucoCard representing the current vira, must be non-null
     * @return true if the object is a zap because of the {@code vira} card parameter and false otherwise
     * @throws NullPointerException if {@code vira} is null
     */
    public boolean isZap(TrucoCard vira) {
        return isManilha(vira) && suit == CardSuit.CLUBS;
    }

    /**
     * <p>Checks if the truco card represented by the object is a copas using the {@code vira} card parameter.</p>
     * <pre>{@code
     *    //Returns true because the object relative value is
     *    //a copas based on the vira parameter
     *    TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS)
     *       .isCopas(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
     *    }
     * @param vira TrucoCard representing the current vira, must be non-null
     * @return true if the object is a copas because of the {@code vira} card parameter and false otherwise
     * @throws NullPointerException if {@code vira} is null
     */
    public boolean isCopas(TrucoCard vira) {
        return isManilha(vira) && suit == CardSuit.HEARTS;
    }

    /**
     * <p>Checks if the truco card represented by the object is an espadilha using the {@code vira} card parameter.</p>
     * <pre>{@code
     *    //Returns true because the object relative value is
     *    //an espadilha based on the vira parameter
     *    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
     *       .isEspadilha(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
     *    }
     * @param vira TrucoCard representing the current vira, must be non-null
     * @return true if the object is an espadilha because of the {@code vira} card parameter and false otherwise
     * @throws NullPointerException if {@code vira} is null
     */
    public boolean isEspadilha(TrucoCard vira) {
        return isManilha(vira) && suit == CardSuit.SPADES;
    }

    /**
     * <p>Checks if the truco card represented by the object is an ouros using the {@code vira} card parameter.</p>
     * <pre>{@code
     *    //Returns true because the object relative value is
     *    //an ouros based on the vira parameter
     *    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
     *       .isOuros(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS));
     *    }
     * @param vira TrucoCard representing the current vira, must be non-null
     * @return true if the object is an ouros because of the {@code vira} card parameter and false otherwise
     * @throws NullPointerException if {@code vira} is null
     */
    public boolean isOuros(TrucoCard vira) {
        return isManilha(vira) && suit == CardSuit.DIAMONDS;
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
        return "[" + rank + suit + "]";
    }
}
