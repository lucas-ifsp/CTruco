package com.bueno.truco.domain.entities.game;

import com.bueno.truco.domain.entities.deck.Card;

import java.util.List;
import java.util.Optional;

public class Round {

    private final Card vira;
    private final Card card1;
    private final Card card2;

    public Round(Card card1, Card card2, Card vira) {
        validateInput(card1, card2, vira);
        this.card1 = card1;
        this.card2 = card2;
        this.vira = vira;
    }

    private void validateInput(Card card1, Card card2, Card vira) {
        if(card1 == null || card2 == null || vira == null)
            throw new IllegalArgumentException("Parameters can not be null!");
        if(card1.equals(card2) || card1.equals(vira) || card2.equals(vira))
            throw new SomeoneIsCheatingException("Cards in the deck must be unique!");
    }

    public Optional<Card> getWinner() {
        if (card1.compareValueTo(card2, vira) == 0)
            return Optional.empty();
        return card1.compareValueTo(card2, vira) > 0 ? Optional.of(card1) : Optional.of(card2);
    }

}
