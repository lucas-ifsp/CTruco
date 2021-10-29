/*
 * Copyright (C) 2021 Lucas B. R. de Oliveira
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
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.truco.domain.entities.hand;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.util.Player;
import com.bueno.truco.domain.entities.round.Round;

import java.util.List;
import java.util.Optional;

public class Intel {

    private Hand hand;

    public Intel(Hand hand) {
        this.hand = hand;
    }

    public Card getVira() {
        return hand.getVira();
    }

    public HandScore getHandScore() {
        return hand.getScore();
    }

    public Optional<Card> getCardToPlayAgainst() {
        return hand.getCardToPlayAgainst();
    }

    public List<Card> getOpenCards() {
        return hand.getOpenCards();
    }

    public List<Round> getRoundsPlayed() {
        return hand.getRoundsPlayed();
    }

    public int getOpponentScore(Player requester) {
        return getOpponent(requester).getScore();
    }

    public String getOpponentId(Player requester) {
        return getOpponent(requester).getUsername();
    }

    private Player getOpponent(Player requester){
        return requester.equals(hand.getFirstToPlay()) ? hand.getLastToPlay() : hand.getFirstToPlay();
    }

    public Optional<HandResult> getResult(){
        return hand.getResult();
    }
}
