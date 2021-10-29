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

package com.bueno.truco.domain.entities.player.mineirobot;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.util.Player;
import com.bueno.truco.domain.entities.player.util.PlayingStrategy;

import java.util.List;
import java.util.Optional;

public class ThirdRoundMineiroStrategy extends PlayingStrategy {

    public ThirdRoundMineiroStrategy(List<Card> cards, Player player) {
        this.cards = cards;
        this.player = player;
        this.intel = player.getIntel();
        this.vira = intel.getVira();
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
    }

    @Override
    public Card playCard() {
        final Optional<Card> possibleOpponentCard = intel.getCardToPlayAgainst();

        if(possibleOpponentCard.isEmpty()) return cards.remove(0);

        final Card remainingCard = cards.get(0);
        final Card opponentCard = possibleOpponentCard.get();

        if(remainingCard.compareValueTo(opponentCard, vira) > 0) return cards.remove(0);

        return Card.getClosedCard();
    }

    @Override
    public int getTrucoResponse(int newScoreValue) {
        final Card lastOpenedCard = intel.getOpenCards().get(intel.getOpenCards().size() - 1);
        final int playerCardValue = cards.isEmpty() ?
                getCardValue(lastOpenedCard, vira) : getCardValue(cards.get(0), vira);

        if (playerCardValue < 9 || (playerCardValue < 12 && newScoreValue >= 6)) return -1;
        if (playerCardValue == 13) return 1;
        return 0;
    }

    @Override
    public boolean requestTruco() {
        final Optional<Card> possibleOpponentCard = intel.getCardToPlayAgainst();
        final Optional<Player> firstRoundWinner = intel.getRoundsPlayed().get(0).getWinner();
        final int handScoreValue = intel.getHandScore().get();
        final Card playingCard = cards.get(0);

        if(possibleOpponentCard.isEmpty()) {
            if (handScoreValue > 1 && getCardValue(playingCard, vira) >= 12) return true;
            if (getCardValue(playingCard, vira) >= 10) return true;
            return false;
        }

        final Card opponentCard = possibleOpponentCard.get();

        if(getCardValue(opponentCard, vira) < 5) return true;
        if(playingCard.compareValueTo(opponentCard, vira) > 0) return true;
        if(isPlayerFirstRoundWinner(firstRoundWinner) && playingCard.compareValueTo(opponentCard, vira) == 0) return true;

        return false;
    }
}
