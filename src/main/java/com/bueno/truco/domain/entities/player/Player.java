package com.bueno.truco.domain.entities.player;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.hand.Intel;
import com.bueno.truco.domain.entities.hand.HandScore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Player {

    public static final int MAX_SCORE = 12;

    protected List<Card> cards;
    protected String username;
    private int score;
    private Intel intel;

    private final static Logger LOGGER = Logger.getLogger(Player.class.getName());

    public Player(String username) {
        this.username = username;
    }

    public abstract Card playCard();
    public abstract boolean requestTruco();
    public abstract int getTrucoResponse(HandScore newHandScore);
    public abstract boolean getMaoDeOnzeResponse();
    public void handleRoundConclusion(){}
    public void handleOpponentPlay(){}

    public final void setIntel(Intel intel){
        this.intel = intel;
    }

    public final Intel getIntel() {
        return intel;
    }

    protected final Card discard(Card card){
        if(card == null || !cards.contains(card))
            throw new IllegalArgumentException("Card can not be null or out of player cards set!");
        cards.remove(card);
        return Card.getClosedCard();
    }

    public void addScore(HandScore handScore){
        this.score = Math.min(MAX_SCORE, this.score + handScore.get());
    }

    public void setCards(List<Card> cards){
        LOGGER.info(username + " received: " + cards.get(0) + " | " + cards.get(1)  + " | " + cards.get(2));
        this.cards = new ArrayList<>(cards);
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return username.equals(player.username);
    }
}
