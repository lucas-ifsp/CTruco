/*
 *  Copyright (C) 2024 Henrique Shoji Kato - IFSP/SCL and Luiz Henrique do Nascimento Silva - IFSP/SCL
 *  Contact: henrique <dot> kato <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: nascimento <dot> henrique1 <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.shojisilva.fernasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public class FernasThirdHand implements FernasStrategy {
    public static final int HAND_MAX_VALUE = 13;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int handValue = getHandValue(intel);
        return handValue >= HAND_MAX_VALUE * 0.69;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int handValue = getHandValue(intel);
        if (handValue >= HAND_MAX_VALUE * 0.75) return 1;
        if (handValue >= HAND_MAX_VALUE * 0.69) return 0;
        return -1;
    }
}
