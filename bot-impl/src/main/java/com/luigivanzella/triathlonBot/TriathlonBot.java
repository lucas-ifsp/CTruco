
/*
 *  Copyright (C) 2024 Luigi A. L. Vanzella
 *  Contact: luigi <dot> vanzella <at> ifsp <dot> edu <dot> br
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

package com.luigivanzella.triathlonBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class TriathlonBot implements BotServiceProvider {
    private EstrategiaRound estrategiaAtual;

    private void definirEstrategia(GameIntel intel) {
        int roundAtual = intel.getRoundResults().size() + 1;
        switch (roundAtual) {
            case 1 -> estrategiaAtual = new EstrategiaPrimeiroRound();
            case 2 -> estrategiaAtual = new EstrategiaSegundoRound();
            case 3 -> estrategiaAtual = new EstrategiaTerceiroRound();
            default -> throw new IllegalStateException("Unexpected value: " + roundAtual);
        }
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        definirEstrategia(intel);
        return estrategiaAtual.getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        definirEstrategia(intel);
        return estrategiaAtual.decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        definirEstrategia(intel);
        return estrategiaAtual.chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        definirEstrategia(intel);
        return estrategiaAtual.getRaiseResponse(intel);
    }
}




