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

package com.bueno.domain.entities.round;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.GameRuleViolationException;
import com.bueno.domain.entities.hand.Hand;
import com.bueno.domain.entities.hand.HandResult;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.truco.Truco;
import com.bueno.domain.entities.truco.TrucoResult;

import java.util.Optional;
import java.util.logging.Logger;

public class Round {

    private final Player firstToPlay;
    private final Player lastToPlay;
    private final Card vira;
    private final Hand hand;
    private Player winner;
    private Card firstCard;
    private Card lastCard;

    private final static Logger LOGGER = Logger.getLogger(Round.class.getName());

    public Round(Player firstToPlay, Player lastToPlay, Hand hand) {
        validateConstructorInputs(firstToPlay, lastToPlay, hand);
        this.firstToPlay = firstToPlay;
        this.lastToPlay = lastToPlay;
        this.hand = hand;
        this.vira = hand.getVira();
        this.hand.setCardToPlayAgainst(null);
    }

    private void validateConstructorInputs(Player firstToPlay, Player lastToPlay, Hand hand) {
        if(firstToPlay == null || lastToPlay == null || hand == null)
            throw new IllegalArgumentException("Parameters must not be null!");
    }

    public void play(){
        winner = null;

        if(isAbleToRequestScoreIncrement(firstToPlay)) {
            final boolean hasWinnerByRun = handleTruco(firstToPlay, lastToPlay).isPresent();
            if (hasWinnerByRun) return;
        }

        firstCard = firstToPlay.playCard();
        hand.setCardToPlayAgainst(firstCard);
        hand.addOpenCard(firstCard);
        lastToPlay.handleOpponentPlay();

        if(isAbleToRequestScoreIncrement(lastToPlay)) {
            final boolean hasWinnerByRun = handleTruco(lastToPlay, firstToPlay).isPresent();
            if (hasWinnerByRun) return;
        }

        lastCard = lastToPlay.playCard();
        hand.setCardToPlayAgainst(null);
        hand.addOpenCard(lastCard);
        firstToPlay.handleOpponentPlay();

        validateCards();
        Optional<Card> highestCard = getHighestCard();

        highestCard.ifPresent(card -> winner = (card.equals(firstCard) ? firstToPlay : lastToPlay));

        LOGGER.info(firstToPlay.getUsername() + ": " + firstCard + " | " + lastToPlay.getUsername() +
                ": " + lastCard + " | Result: " + (winner == null? "Draw" : winner.getUsername()));
    }

    private boolean isAbleToRequestScoreIncrement(Player requester) {
        final Player previousRequester = hand.getLastScoreIncrementRequester();
        final boolean isAbleToRequest = previousRequester == null || ! previousRequester.equals(requester);

        LOGGER.info(requester.getUsername() + " is " + (isAbleToRequest? "able" : "not able")
                + " to request to increase hand score. Previous requester: " + previousRequester);

        return isAbleToRequest;
    }

    private Optional<HandResult> handleTruco(Player requester, Player responder) {
        final Truco truco = new Truco(requester, responder);
        final TrucoResult trucoResult = truco.handle(hand.getScore());
        HandResult handResult = null;

        if(trucoResult.hasWinner()) {
            winner = trucoResult.getWinner().orElseThrow();
            handResult = new HandResult(trucoResult);
            hand.setResult(handResult);
        }

        trucoResult.getLastRequester().ifPresent(hand::setLastScoreIncrementRequester);
        hand.setScore(trucoResult.getScore());

        return Optional.ofNullable(handResult);
    }

    private void validateCards() {
        if(firstCard == null || lastCard == null || vira == null)
            throw new GameRuleViolationException("Cards must not be null!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(lastCard))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!firstCard.equals(Card.getClosedCard()) && firstCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
        if(!lastCard.equals(Card.getClosedCard()) && lastCard.equals(vira))
            throw new GameRuleViolationException("Cards in the deck must be unique!");
    }

    public Optional<Card> getHighestCard() {
        if (firstCard.compareValueTo(lastCard, vira) == 0)
            return Optional.empty();
        return firstCard.compareValueTo(lastCard, vira) > 0 ?
                Optional.of(firstCard) : Optional.of(lastCard);
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }
}
