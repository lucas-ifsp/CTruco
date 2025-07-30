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

package com.local.brito.macena.boteco.utils;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;

public class MyHand {

    List<TrucoCard> myHand;
    List<Integer> myHandValuesSorted;
    TrucoCard vira;


    public MyHand(List<TrucoCard> myHand, TrucoCard vira) {
        this.myHand = myHand;
        this.vira = vira;
        myHandValuesSorted = this.myHand.stream()
                .map(trucoCard -> trucoCard.relativeValue(vira))
                .sorted()
                .toList();
    }


    public TrucoCard getBestCard() {
        Integer valueOfBestCard;
        if (myHand.size() == 3) valueOfBestCard = myHandValuesSorted.get(2);
        else if (myHand.size() == 2) valueOfBestCard = myHandValuesSorted.get(1);
        else valueOfBestCard = myHandValuesSorted.get(0);

        return myHand.stream()
                .filter(trucoCard -> trucoCard.relativeValue(vira) == valueOfBestCard)
                .findAny()
                .orElse(myHand.get(0));
    }

    public TrucoCard getSecondBestCard() {
        Integer valueOfSecondBestCard;
        if (myHand.size() == 3) valueOfSecondBestCard = myHandValuesSorted.get(1);
        else valueOfSecondBestCard = myHandValuesSorted.get(0);

        return myHand.stream()
                .filter(trucoCard -> trucoCard.relativeValue(vira) == valueOfSecondBestCard)
                .findAny()
                .orElse(myHand.get(0));
    }

    public TrucoCard getWorstCard() {

        Integer worstCardValue = myHandValuesSorted.get(0);

        return myHand.stream()
                .filter(trucoCard -> trucoCard.relativeValue(vira) == worstCardValue)
                .findAny()
                .orElse(myHand.get(0));
    }

    public long powerOfCard(GameIntel intel, int index) {
        return intel.getCards().stream()
                .map(card -> card.relativeValue(intel.getVira()))
                .sorted(Comparator.reverseOrder())
                .toList()
                .get(index);
    }
}