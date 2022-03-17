/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.entities.player;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.hand.HandPoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Player {

    public static final int MAX_SCORE = 12;

    private User user;
    private List<Card> cards;
    private final String username;
    private final UUID uuid;
    private int score;

    private Player(UUID uuid, String username) {
        this.username = username;
        this.uuid = uuid;
    }

    public static Player of(User user){
        Player player = new Player(user.getUuid(), user.getUsername());
        player.user = user;
        return player;
    }

    public static Player ofBot(String botName){
        return new Player(UUID.randomUUID(), botName);
    }

    public static Player ofBot(UUID uuid, String botName){
        return new Player(uuid, botName);
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

    public final void addScore(HandPoints handPoints){
        this.score = Math.min(MAX_SCORE, this.score + handPoints.get());
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

    public boolean isBot(){
        return user == null;
    }

    @Override
    public String toString() {
        return String.format("Player = %s (%s) has %d point(s)", username, uuid, score);
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
