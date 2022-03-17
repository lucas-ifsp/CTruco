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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        generateSortedDeck();
    }

    private void generateSortedDeck() {
        for(Rank rank : Rank.values())
            for(Suit suit : Suit.values())
                if(rank != Rank.HIDDEN && suit != Suit.HIDDEN)
                    cards.add(Card.of(rank, suit));
    }

    public List<Card> take(int numberOfCards) {
        final List<Card> cardsTaken = new ArrayList<>(cards.subList(0, numberOfCards));
        cards.removeAll(cardsTaken);
        return cardsTaken;
    }

    public Card takeOne() {
        return cards.remove(0);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int size() {
        return cards.size();
    }
}
