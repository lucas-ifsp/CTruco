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
package com.motta.impl.beepbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;


public class SecondRoundStrategy extends BotStrategy {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (getLastRoundResult().equals(GameIntel.RoundResult.LOST) && calculateAverageCardValue(intel.getCards(), intel.getVira()) > 7) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return intel.getScore() >= 10 && (shouldPlayDefensively(intel) || hasZap(intel.getCards(), intel.getVira()));
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return getLastRoundResult().equals(GameIntel.RoundResult.LOST) && hasStrongPair(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (intel.getCards().isEmpty()) {
            throw new IllegalStateException("Não há cartas disponíveis para seleção");
        }
        TrucoCard card;
        if (getLastRoundResult().equals(GameIntel.RoundResult.WON)) {
            card = getLowestCard(intel.getCards(), intel.getVira());
        } else {
            card = selectHighestNonManilhaCard(intel.getCards(), intel.getVira());
        }
        if (card == null) {
            card = intel.getCards().get(0);
        }
        return CardToPlay.of(card);
    }
}