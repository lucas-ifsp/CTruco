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

package com.bueno.domain.entities.game;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.player.Player;

import java.util.Objects;
import java.util.Optional;

public class Round {

    private final Player firstToPlay;
    private final Player lastToPlay;
    private Player winner;
    private final Card vira;
    private final Card firstCard;
    private final Card lastCard;

    public Round(Player firstToPlay, Card firstCard, Player lastToPlay, Card lastCard, Card vira) {
        this.firstToPlay = Objects.requireNonNull(firstToPlay, "First to play must not be null!");
        this.lastToPlay = Objects.requireNonNull(lastToPlay, "Second to play must not be null!");
        this.firstCard = Objects.requireNonNull(firstCard, "First card played must not be null!");
        this.lastCard = Objects.requireNonNull(lastCard, "Last card played must not be null!");
        this.vira = Objects.requireNonNull(vira, "Vira must not be null!");
        validateCards();
    }

    private void validateCards() {
        if(!firstCard.equals(Card.closed()) && firstCard.equals(lastCard))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!firstCard.equals(Card.closed()) && firstCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!lastCard.equals(Card.closed()) && lastCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
    }

    public void play() {
        final Optional<Card> possibleWinnerCard = getWinnerCard();
        this.winner = possibleWinnerCard.map(c -> c.equals(firstCard)? firstToPlay : lastToPlay).orElse(null);
    }

    public Optional<Card> getWinnerCard(){
        if (firstCard.compareValueTo(lastCard, vira) == 0) return Optional.empty();
        return firstCard.compareValueTo(lastCard, vira) > 0 ? Optional.of(firstCard) : Optional.of(lastCard);
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    @Override
    public String toString() {
        return "Round{winner=" + winner + '}';
    }
}
