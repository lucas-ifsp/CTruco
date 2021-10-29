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

package com.bueno.domain.entities.player.util;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.hand.Intel;

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
        if(cards.size() == 3) // to enable receiving less cards during tests
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
