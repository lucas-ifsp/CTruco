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

package com.caueisa.destroyerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class DestroyerBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int roundNumber = getRoundNumber(intel);
        if (!isTheFirstToPlay(intel)) {
            switch(roundNumber) {
                case 1 -> {
                    Optional<TrucoCard> lowestCardStrongerThanOpponentCard =
                            getLowestCardStrongerThanTheOpponentCard(intel);
                    TrucoCard lowestDeckCard = getLowestCardBetweenAllCardsAvailableToBePlayed(intel);
                    return CardToPlay.of(lowestCardStrongerThanOpponentCard.orElse(lowestDeckCard));
                }
            }
        }
        return CardToPlay.of(intel.getCards().get(0));
    }

    private boolean isTheFirstToPlay(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    private int getRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    private Optional<TrucoCard> getLowestCardStrongerThanTheOpponentCard(GameIntel intel) {
        TrucoCard opponentCard = intel.getOpponentCard().get();
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min( (card1, card2) -> card1.compareValueTo(card2, vira));
    }

    private TrucoCard getLowestCardBetweenAllCardsAvailableToBePlayed(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .min( (card1, card2) -> card1.compareValueTo(card2, vira))
                .get();
    }
}
