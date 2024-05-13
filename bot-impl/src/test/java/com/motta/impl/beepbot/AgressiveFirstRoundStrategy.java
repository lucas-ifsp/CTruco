/*
 *  Copyright (C) 2024 Erick Motta and Thiago Maciel - IFSP/SCL
 *  Contact: erick <dot> motta <at> ifsp <dot> edu <dot> br
 *  Contact: thiago <dot> maciel <at> ifsp <dot> edu <dot> br
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

public class AgressiveFirstRoundStrategy extends BotStrategy{
    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(getManilhaAmount(intel.getCards(), intel.getVira())==2) return 1;
        return hasStrongPair(intel) ? 0 : -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return hasManilha(intel.getCards(), intel.getVira());
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(getManilhaAmount(intel.getCards(), intel.getVira())==2) return true;
        if(hasManilha(intel.getCards(), intel.getVira())) return true;
        return calculateAverageCardValue(intel.getCards(), intel.getVira()) > 8;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(intel.getCards().isEmpty()){
            throw new IllegalStateException("Não há cartas disponíveis para seleção.");
        }
        TrucoCard card;
        if(intel.getScore() > 3){
            card = getHighestCard(intel.getCards(), intel.getVira());
        }else{
            card = selectHighestNonManilhaCard(intel.getCards(), intel.getVira());
        }
        if (card == null){
            card = intel.getCards().get(0);
        }
        return CardToPlay.of(card);
    }
}
