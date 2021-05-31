package com.bueno.truco.domain.entities.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrucoDeck {
    private final List<Card> cards = new ArrayList<>();

    public TrucoDeck() {
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

    public int size() {
        return cards.size();
    }

    public List<Card> take(int numberOfCards) {
        List<Card> cardsTaken = new ArrayList<>(cards.subList(0, numberOfCards));
        cards.removeAll(cardsTaken);
        return cardsTaken;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}
