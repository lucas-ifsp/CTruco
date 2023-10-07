/*
 *  Copyright (C) 2023 João Pedro da Silva and Renan Brufato
 *  Contact: jps <dot> spj <at> gmail <dot> com 
 *  Contact: brufato17 <dot> renan <at> gmail <dot> com
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

package com.silvabrufato.impl.silvabrufatobot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class SilvaBrufatoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (intel.getOpponentScore() >= 9) return false;

        if(!checkIfExistsManilhaInCards(cards, vira).isEmpty()){
            if (checkIfExistsManilhaInCards(cards,vira).size() == 1){  //just one manilha
                if (checkIfOtherCardAreHigherOrEqualTOA(cards, vira)) return true; //check if other cards they are high or equal to ACE
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decideIfRaises'");
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        BotStrategy botStrategy = null;
        if(intel.getRoundResults().isEmpty()) botStrategy = BotStrategy.FIRST_HAND_STRATEGY;
        if(intel.getRoundResults().size() == 1) botStrategy = BotStrategy.SECOND_HAND_STRATEGY;
        if(intel.getRoundResults().size() == 2) botStrategy = BotStrategy.THIRD_HAND_STRATEGY;
        return botStrategy.throwCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRaiseResponse'");
    }

    private List<TrucoCard> checkIfExistsManilhaInCards(List<TrucoCard> cards, TrucoCard vira){
        List<TrucoCard> manilhas = new ArrayList<>();
        for (TrucoCard card:cards) {
            if(card.isManilha(vira)) manilhas.add(card);
            System.out.println(manilhas.size());
        }
        return manilhas;
    }

    private Boolean checkIfOtherCardAreHigherOrEqualTOA(List<TrucoCard> cards, TrucoCard vira) {
        for (TrucoCard card : cards) {
            if (!card.isManilha(vira)) {
                if (card.getRank().value() >= CardRank.ACE.value()) {
                    return true;
                }
            }
        }
        return false;
    }

}
