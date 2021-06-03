package com.bueno.truco.domain.usecases.hand;

import com.bueno.truco.domain.entities.deck.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Player {

    protected List<Card> cards;
    protected String id;

    public Player(String id) {
        this.id = id;
    }

    public void setCards(List<Card> cards){
        this.cards = new ArrayList<>(cards);
    }

    public abstract Card playCard();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }
}
