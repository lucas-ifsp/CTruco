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

/**
 * <p>Represents a playing order containing a {@link TrucoCard} and a decision whether this card should be played
 * or discarded. Objects of this class are final and must be created using the static constructors
 * {@link #of(TrucoCard card)} or {@link #discard(TrucoCard card)}.
 * */
public final class CardToPlay {
    private final TrucoCard content;
    private final boolean discard;

    private CardToPlay(TrucoCard card, boolean discard) {
        this.content = card;
        this.discard = discard;
    }

    /**
     * <p>Represents the card that will be played in the round. The resulting {@link CardToPlay} object is final
     * and will always return {@code false} when {@link #isDiscard()} is invoked.</p>
     *
     * @param card a {@link TrucoCard} that will be played in the round, must be non-null
     * @return a {@link CardToPlay} order describing a {@link TrucoCard} to be played in the round
     * @throws NullPointerException if {@code card} is null
     * */
    public static CardToPlay of(TrucoCard card){
        Objects.requireNonNull(card, "The card to be played must not be null.");
        return new CardToPlay(card, false);
    }

    /**
     * <p>Represents the card that will be discarded in the round. The resulting {@link CardToPlay} object is final
     * and will always return {@code true} when {@link #isDiscard()} is invoked.</p>
     *
     * @param card a {@link TrucoCard} that will be discarded in the round, must be non-null
     * @return a {@link CardToPlay} order describing a {@link TrucoCard} to be discarded in the round
     * @throws NullPointerException if {@code card} is null
     * */
    public static CardToPlay discard(TrucoCard card) {
        Objects.requireNonNull(card, "The card to be discarded must not be null.");
        return new CardToPlay(card, true);
    }

    /**
     * <p>Returns the wrapped {@link TrucoCard} object if the {@link CardToPlay} is planed to be played or
     * {@link TrucoCard#closed()} if the {@link TrucoCard} is planed to be discarded.</p>
     *
     * @return an ordinary {@link TrucoCard} to be played or {@link TrucoCard#closed()}
     * */
    public TrucoCard value() {
        return discard ? TrucoCard.closed() : content;
    }

    /**
     * <p>Returns the wrapped {@link TrucoCard} object without any changes.</p>
     * @return the same {@link TrucoCard} object used to create the {@link CardToPlay}
     * */
    public TrucoCard content() {
        return content;
    }

    /**
     * <p>Indicates if the {@link CardToPlay} is intended to be discarded or not.</p>
     * @return {@code true} if the card is intended to be discarded or {@code false} otherwise
     * */
    public boolean isDiscard() {
        return discard;
    }

    /**
     * <p>Check object equality based on content and card type (discarded or not discarded).</p>
     * @return {@code true} if both content and card type (discarded or not discarded) are equals in the current object
     * and the {@code out} object. Otherwise, return {@code false}.
     * */
    @Override
    public boolean equals(Object out) {
        if (this == out) return true;
        if (out == null || getClass() != out.getClass()) return false;
        CardToPlay that = (CardToPlay) out;
        return discard == that.discard && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, discard);
    }

    @Override
    public String toString() {
        return "CardToPlay{" +
                "content=" + content +
                ", discard=" + discard +
                '}';
    }
}
