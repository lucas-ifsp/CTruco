/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.brito.macena.boteco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class BotEco implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        int handPower = calculateHandPower(hand);
        return handPower >= 17;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "BotEco :)";
    }

    public int calculateHandPower(List<TrucoCard> hand) {
        return hand.stream().mapToInt(this::getCardValue).sum();
    }

    public int getCardValue(TrucoCard card) {
        return switch (card.getRank()) {
            case FOUR -> 1;
            case FIVE -> 2;
            case SIX -> 3;
            case SEVEN -> 4;
            case QUEEN -> 5;
            case JACK -> 6;
            case KING -> 7;
            case ACE -> 8;
            case TWO -> 9;
            case THREE -> 10;
            default -> 0;
        };
    }
}
