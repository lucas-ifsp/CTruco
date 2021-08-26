package com.bueno.truco.domain.entities.player;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.Game;
import com.bueno.truco.domain.entities.game.GameIntel;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.utils.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Player implements Observer<GameIntel> {

    public static final int MAX_SCORE = 12;

    protected List<Card> cards;
    protected String id;
    private int score;
    private GameIntel gameIntel;

    private final static Logger LOGGER = Logger.getLogger(Game.class.getName());

    public Player(String id) {
        this.id = id;
    }

    public abstract Card playCard();
    public abstract boolean requestTruco();
    public abstract int getTrucoResponse(int newHandPoints);
    public abstract boolean getMaoDeOnzeResponse();
    public void handleRoundConclusion(){}
    public void handleOpponentPlay(){}

    @Override
    public final void update(GameIntel gameIntel){
        this.gameIntel = gameIntel;
    }

    protected final GameIntel getGameIntel() {
        return gameIntel;
    }

    protected final Card discard(Card card){
        if(card == null || !cards.contains(card))
            throw new IllegalArgumentException("Card can not be null or out of player cards set!");
        cards.remove(card);
        return Card.getClosedCard();
    }

    public void incrementScoreBy(int value){
        if(isValidScoreIncrement(value))
            throw new GameRuleViolationException("Invalid value increment for player score!");
        this.score = Math.min(MAX_SCORE, value + score);
    }

    public static boolean isValidScoreIncrement(int value) {
        return value != 1 && value != 3 && value != 6 && value != 9 && value != 12;
    }

    public void setCards(List<Card> cards){
        LOGGER.info(id + " received: " + cards.get(0) + " | " + cards.get(1)  + " | " + cards.get(2));
        this.cards = new ArrayList<>(cards);
    }

    public String getNickname() {
        return id;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return getNickname();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }
}
