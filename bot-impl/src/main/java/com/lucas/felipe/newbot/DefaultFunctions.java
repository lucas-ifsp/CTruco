/*
 *  Copyright (C) 2023 Lucas Pereira dos Santos and Felipe Santos Lourenço
 *  Contact: lucas <dot> pereira3 <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: santos <dot> lourenco <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.lucas.felipe.newbot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefaultFunctions {
    private List<TrucoCard> roundCards;
    private TrucoCard vira;

    public DefaultFunctions(List<TrucoCard> roundCards, TrucoCard vira) {
        this.roundCards = roundCards;
        this.vira = vira;
    }

    Comparator<TrucoCard> byValue = (card1, card2) ->
            Integer.compare(card1.relativeValue(vira), card2.relativeValue(vira));

    protected List<TrucoCard> sortCards(List<TrucoCard> cards){
        List<TrucoCard> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(byValue);

        return sortedCards;
    }

    protected boolean isPowerfull(List<TrucoCard> ordenedCards){
        if (ordenedCards.size() == 3) {
            return ordenedCards.get(2).relativeValue(vira) >= 9 && ordenedCards.get(1).relativeValue(vira) >= 9;
        } else if (ordenedCards.size() == 2) {
            return ordenedCards.get(1).relativeValue(vira) >= 9 && ordenedCards.get(0).relativeValue(vira) >= 9;
        } else {
            return ordenedCards.get(0).relativeValue(vira) >= 9;
        }
    }

    protected boolean isMedium(List<TrucoCard> ordenedCards){
        if (ordenedCards.size() == 3) {
            return ordenedCards.get(2).relativeValue(vira) >= 7 && ordenedCards.get(1).relativeValue(vira) >= 7;
        } else if (ordenedCards.size() == 2) {
            return ordenedCards.get(1).relativeValue(vira) >= 7 && ordenedCards.get(0).relativeValue(vira) >= 7;
        } else {
            return ordenedCards.get(0).relativeValue(vira) >= 7;
        }
    }

    public boolean maoDeOnzeResponse(List<TrucoCard> ordendedCards, GameIntel intel) {
        DefaultFunctions defaultFunctions = new DefaultFunctions(intel.getCards(), intel.getVira());
        int opponentScore = intel.getOpponentScore();
        boolean isPowerfull = defaultFunctions.isPowerfull(ordendedCards);
        boolean isMedium = defaultFunctions.isMedium(ordendedCards);
        if (opponentScore <= 7) return isMedium;
        return isPowerfull;
    }
}
