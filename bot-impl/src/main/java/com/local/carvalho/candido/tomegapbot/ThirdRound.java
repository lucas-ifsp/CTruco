package com.local.carvalho.candido.tomegapbot;
/*
 *  Copyright (C) 2024 Matheus H. S. Carvalho and Pedro C. Salvio - IFSP/SCL
 *  Contact: souza <dot> carvalho1 <at> ifsp <dot> edu <dot> br or
 *  p <dot> candido <at> ifsp <dot> edu <dot> br
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
 *  along with CTruco. If not, see <https://www.gnu.org/licenses/>
 */
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public class ThirdRound implements Strategy {
    private TrucoCard vira;

    public ThirdRound(){
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        vira = intel.getVira();
        boolean isPe = intel.getOpponentCard().isPresent();
        TrucoCard lastCard = intel.getCards().get(0);

        if(isPe) return lastCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        vira = intel.getVira();
        TrucoCard lastCard = intel.getCards().get(0);
        return CardToPlay.of(lastCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        vira = intel.getVira();
        if (intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) > 9)) return 1;
        if (intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) > 6)) return 0;
        return -1;
    }

}
