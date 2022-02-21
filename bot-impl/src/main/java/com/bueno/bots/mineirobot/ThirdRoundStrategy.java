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

package com.bueno.bots.mineirobot;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.bots.mineirobot.PlayingStrategy.*;

public class ThirdRoundStrategy implements PlayingStrategy {

    private final Player player;
    private final List<Card> cards;
    private final Card vira;
    private final Intel intel;
    private final List<Card> openCards;

    public ThirdRoundStrategy(Player player, Intel intel) {
        this.player = player;
        this.intel = intel;
        this.vira = intel.vira();
        this.cards = new ArrayList<>(player.getCards());
        this.cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        this.openCards = intel.openCards();
    }

    @Override
    public CardToPlay chooseCard() {
        final Optional<Card> possibleOpponentCard = intel.cardToPlayAgainst();
        final Card remainingCard = cards.get(0);

        if (possibleOpponentCard.isEmpty()) return CardToPlay.of(remainingCard);

        final Card opponentCard = possibleOpponentCard.get();
        if (remainingCard.compareValueTo(opponentCard, vira) >= 0) return CardToPlay.of(remainingCard);

        return CardToPlay.ofDiscard(remainingCard);
    }

    @Override
    public int getRaiseResponse(int newScoreValue) {
        if(knowsTheOdds() && canWin(cards.get(0), intel.cardToPlayAgainst().orElseThrow())) return 1;

        final Card lastOpenedCard = intel.openCards().get(intel.openCards().size() - 1);
        final int playerCardValue = cards.isEmpty() ?
                getCardValue(openCards, lastOpenedCard, vira) : getCardValue(openCards, cards.get(0), vira);

        if (playerCardValue < 9 || (playerCardValue < 12 && newScoreValue >= 6)) return -1;
        if (playerCardValue == 13) return 1;
        return 0;
    }

    private boolean knowsTheOdds() {
        return cards.size() == 1 && intel.cardToPlayAgainst().isPresent();
    }


    @Override
    public boolean decideIfRaises() {
        final Optional<Card> possibleOpponentCard = intel.cardToPlayAgainst();
        final int handScoreValue = intel.handScore();
        final Card playingCard = cards.get(0);

        if (possibleOpponentCard.isEmpty()) {
            if (handScoreValue > 1 && getCardValue(openCards, playingCard, vira) >= 12) return true;
            return getCardValue(openCards, playingCard, vira) >= 10;
        }

        final Card opponentCard = possibleOpponentCard.get();

        if (getCardValue(openCards, opponentCard, vira) < 5) return true;
        return canWin(playingCard, opponentCard);
    }

    private boolean canWin(Card remainingCard, Card opponentCard) {
        final String firstRoundWinner = intel.roundWinners().get(0).orElse(null);
        return (!isPlayerFirstRoundLoser(firstRoundWinner, player.getUsername())
                && remainingCard.compareValueTo(opponentCard, vira) > 0)
                || (isPlayerFirstRoundWinner(firstRoundWinner, player.getUsername())
                && remainingCard.compareValueTo(opponentCard, vira) == 0);
    }
}
