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

package com.bueno.domain.entities.player.mineirobot;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;

import java.util.List;
import java.util.Optional;

public class ThirdRoundStrategy extends PlayingStrategy {

    public ThirdRoundStrategy(List<Card> cards, Player player) {
        this.cards = cards;
        this.player = player;
        this.intel = player.getIntel();
        this.vira = intel.vira();
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
    }

    @Override
    public CardToPlay playCard() {
        final Optional<Card> possibleOpponentCard = intel.cardToPlayAgainst();
        final Card remainingCard = cards.get(0);

        if(possibleOpponentCard.isEmpty()) return CardToPlay.of(remainingCard);

        final Card opponentCard = possibleOpponentCard.get();
        if(remainingCard.compareValueTo(opponentCard, vira) > 0) return CardToPlay.of(remainingCard);

        return CardToPlay.ofDiscard(remainingCard);
    }

    @Override
    public int getTrucoResponse(int newScoreValue) {
        final Card lastOpenedCard = intel.openCards().get(intel.openCards().size() - 1);
        final int playerCardValue = cards.isEmpty() ?
                getCardValue(lastOpenedCard, vira) : getCardValue(cards.get(0), vira);

        if (playerCardValue < 9 || (playerCardValue < 12 && newScoreValue >= 6)) return -1;
        if (playerCardValue == 13) return 1;
        return 0;
    }

    @Override
    public boolean requestTruco() {
        final Optional<Card> possibleOpponentCard = intel.cardToPlayAgainst();
        final Optional<String> firstRoundWinner = intel.roundWinners().get(0);
        final int handScoreValue = intel.handScore();
        final Card playingCard = cards.get(0);

        if(possibleOpponentCard.isEmpty()) {
            if (handScoreValue > 1 && getCardValue(playingCard, vira) >= 12) return true;
            return getCardValue(playingCard, vira) >= 10;
        }

        final Card opponentCard = possibleOpponentCard.get();

        if(getCardValue(opponentCard, vira) < 5) return true;
        if(playingCard.compareValueTo(opponentCard, vira) > 0) return true;
        return isPlayerFirstRoundWinner(firstRoundWinner.orElse(null))
                && playingCard.compareValueTo(opponentCard, vira) == 0;
    }
}
