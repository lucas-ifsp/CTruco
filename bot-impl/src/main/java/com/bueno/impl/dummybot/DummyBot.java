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

package com.bueno.impl.dummybot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class DummyBot implements BotServiceProvider {

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
        printValues(intel);
        return CardToPlay.of(intel.getCards().get(0));
    }

    private static void printValues(GameIntel intel) {
        System.out.println("Carta que vamos jogar " + CardToPlay.of(intel.getCards().get(0)).content());
        for (int i = 0; i < intel.getCards().size(); i++) {
            System.out.printf("Valor carta %d é : %d\n", (i + 1), intel.getCards().get(i).getRank().value());
            if (intel.getOpponentCard().isPresent()) {
                System.out.println("Valor carta se for manilha: " + intel.getCards().get(i).compareValueTo(
                        intel.getOpponentCard().get(), intel.getVira()
                ));
            }
        }
        System.out.println("Vira da mesa: " + intel.getVira());
        System.out.println("Cartas abertas " + intel.getOpenCards());
        System.out.println("Carta que o oponente vai jogar " + intel.getOpponentCard());
        System.out.println("Quanto ta valendo mão: " + intel.getHandPoints());
        System.out.println("Conclusão da mão: " + intel.getOpenCards());
        System.out.println("Resultado dos rounds " + intel.getRoundResults() + "\n\n");
    }
}
