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
        for (int i = 1; i <= 13; i++) {
            if(isNotATrucoCard(i))
                continue;
            for (var suit : Suit.values())
                cards.add(new Card(i, suit));
        }
    }

    private boolean isNotATrucoCard(int i) {
        return i == 8 || i == 9 || i == 10;
    }

    public List<Card> take(int numberOfCards) {
        List<Card> cardsTaken = new ArrayList<>(cards.subList(0, numberOfCards));
        cards.removeAll(cardsTaken);
        return cardsTaken;
    }

    public Card takeOne() {
        return take(1).get(0);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int size() {
        return cards.size();
    }
}
