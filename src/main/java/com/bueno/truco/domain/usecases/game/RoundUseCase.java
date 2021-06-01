package com.bueno.truco.domain.usecases.game;

import com.bueno.truco.domain.entities.deck.Card;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RoundUseCase {

    private final Card vira;
    private final Card card1;
    private final Card card2;

    public RoundUseCase(Card card1, Card card2, Card vira) {
        if(card1 == null || card2 == null || vira == null)
            throw new NullPointerException("Parameters can not be null");
        this.card1 = card1;
        this.card2 = card2;
        this.vira = vira;
    }

    public Optional<Card> getWinner() {
        if (getCardValue(card1) == getCardValue(card2))
            return Optional.empty();
        return getCardValue(card1) > getCardValue(card2) ? Optional.of(card1) : Optional.of(card2);
    }

    private int getCardValue(Card card) {
        List<Integer> values = List.of(4, 5, 6, 7, 11, 12, 13, 1, 2, 3);
        int manilha;

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


}
