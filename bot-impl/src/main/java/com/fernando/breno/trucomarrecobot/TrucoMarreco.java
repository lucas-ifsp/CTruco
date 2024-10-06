/*
 *  Copyright (C) 2024 Breno Augusto de Oliveira - IFSP/SCL
 *  Copyright (C) 2024 Fernando Candido Rodrigues - IFSP/SCL
 *  Contact: Breno <dot> Oliveira <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: Fernando <dot> Rodrigues <at> aluno <dot> ifsp <dot> edu <dot> br
 *
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

package com.fernando.breno.trucomarrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class TrucoMarreco implements BotServiceProvider {

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
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }


    private boolean CasalMaior(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        boolean encontrouZap = false;
        boolean encontrouCopas = false;

        // Itera sobre as cartas para verificar se tem Zap e Copas
        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(cardVira)) {
                encontrouZap = true;
            } else if (card.isCopas(cardVira)) {
                encontrouCopas = true;
            }

            // Se encontrar ambos, já pode retornar true
            if (encontrouZap && encontrouCopas) {
                return true;
            }
        }


        return false;
    }

    boolean temManilhas(GameIntel intel) {

        for (TrucoCard carta : intel.getCards()) {
            // Verifica se a carta atual é uma manilha em relação à carta vira
            if (carta.isManilha(intel.getVira())) {
                return true;
            }
        }
        return false;
    }

    private int avaliarForcaDaMao(GameIntel intel) {
        int forca = 0;
        TrucoCard vira = intel.getVira();

        for (TrucoCard carta : intel.getCards()) {
            // Adiciona a força da carta com base no valor relativo em relação à vira
            forca += carta.relativeValue(vira);
        }

     /*
         Considerar se precisa de um ajuste extra fixo para as manilhas
        if (temManilhas(intel)) {
            forca += 5;
        }
       */
        return forca;
    }

}
