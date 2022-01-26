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
import com.bueno.domain.entities.game.HandScore;
import com.bueno.domain.entities.game.Intel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class Player {

    public static final int MAX_SCORE = 12;

    private List<Card> cards;
    private final String username;
    private final UUID uuid;

    private int score;
    private Intel intel;

    public Player(String username) {
        this(UUID.randomUUID(), username);
    }

    public Player(UUID uuid, String username) {
        this.username = username;
        this.uuid = uuid;
    }

    public void setIntel(Intel intel){
        this.intel = intel;
    }

    public Intel getIntel() {
        return intel;
    }

    public final Card play(Card card){
        Card cardToPlay = Objects.requireNonNull(card);
        if(doesNotOwn(cardToPlay))
            throw new IllegalArgumentException("User doesn't own card " + cardToPlay + " to play it.");
        getCards().remove(cardToPlay);
        return cardToPlay;
    }

    public final Card discard(Card card){
        Card discard = Objects.requireNonNull(card);
        if(doesNotOwn(discard))
            throw new IllegalArgumentException("User doesn't own card " + card + " to discard it.");
        getCards().remove(discard);
        return Card.closed();
    }

    private boolean doesNotOwn(Card discard) {
        return !getCards().contains(discard);
    }

    public final void addScore(HandScore handScore){
        this.score = Math.min(MAX_SCORE, this.score + handScore.get());
    }

    public final void setCards(List<Card> cards){
        this.cards = new ArrayList<>(cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return getUsername() + " (" + getUuid() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return uuid.equals(player.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
