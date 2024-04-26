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

package com.contiero.remote.utils.model;

import java.util.Objects;

/**
 * <p>Represents a valid truco card described in terms of a {@link CardRank} and a {@link CardSuit}. It also
 * encompasses a method to compare its value based on a vira card, as well as methods to check if the card is
 * considered a manilha (zap, copas, espadilha or ouros) based on such vira. Objects of this class are final,
 * cached, and must be created using the static constructors  {@link #of(CardRank rank, CardSuit suit)} or
 * {@link #closed()}.
 * */
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
     *           TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)) returns positive;
     *
     *    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
     *       .compareValueTo(TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
     *           TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)) returns 0;
     *
     *    TrucoCard.of(CardRank.THREE, CardSuit.CLUBS)
     *       .compareValueTo(TrucoCard.of(CardRank.SIX, CardSuit.CLUBS),
     *          TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS)) returns negative,
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
     * @return returns a positive number if the TrucoCard represented by the object is greater than the
     * {@code otherCard}, a negative number if the object card is lower, and 0 if both cards have the same
     * relative value. The returned value is the difference between the values of the compared cards.
     * @throws NullPointerException if {@code otherCard} or/and {@code vira} is/are null.
     */
    public int compareValueTo(TrucoCard otherCard, TrucoCard vira) {
        Objects.requireNonNull(otherCard, "TrucoCard to be compared must not be null.");
        Objects.requireNonNull(vira, "TrucoCard representing the vira must not be null.");
        return this.relativeValue(vira) - otherCard.relativeValue(vira);
    }

    /**
     * <p>Get the relative card value based on the current {@code vira} card parameter.</p>
     *
     * @param vira TrucoCard representing the current vira, must be non-null.
     * @return It returns 0 if the card is hidden. Returns 13 for zap, 12 for copas, 11 for espadilha, and 10 for ouros.
     * If the card is not hidden nor manilha, returns a value based on the card rank value and the {@code vira} rank
     * value. For instance, if the card rank is 4 and the vira rank is 7, then the relative card value is 1 (the lowest
     * card value for an open card). If the card rank is 7 and the vira rank is 4, then the relative card value is 3 —
     * because the absolute value for rank 7 is 4, but the rank 5 is for manilhas and does not count in the sequence.
     * @throws NullPointerException if {@code vira} is null.
     */
    public int relativeValue(TrucoCard vira) {
        Objects.requireNonNull(vira, "Vira card must not be null.");
        if (isManilha(vira))
            return switch (suit) {
                case DIAMONDS -> 10;
                case SPADES -> 11;
                case HEARTS -> 12;
                case CLUBS -> 13;
                case HIDDEN -> throw new IllegalStateException("Closed card can not be manilha!");
            };
        if(rank.value() > vira.rank.value()) return rank.value() - 1;
        return rank.value();
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
