package com.bueno.truco.domain.entities.player;

import com.bueno.truco.domain.entities.deck.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    public static final int MAX_SCORE = 12;

    protected List<Card> cards;
    protected String id;
    private int score;

    public Player(String id) {
        this.id = id;
    }

    public void setCards(List<Card> cards){
        this.cards = new ArrayList<>(cards);
    }

    public abstract Card playCard();

    public void incrementScoreBy(int value){
        if(isValidScoreIncrement(value))
            throw new InvalidTrucoScoreIncrementException("Invalid value increment for player score!");
        this.score = Math.min(MAX_SCORE, value + score);
    }

    public static boolean isValidScoreIncrement(int value) {
        return value != 1 && value != 3 && value != 6 && value != 9 && value != 12;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    public abstract boolean requestTruco();

    public abstract int getTrucoResponse(int newHandPoints);

    @Override
    public String toString() {
        return getId();
    }
}
