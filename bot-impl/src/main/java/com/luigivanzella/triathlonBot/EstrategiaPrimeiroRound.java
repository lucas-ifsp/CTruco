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
import com.bueno.spi.model.TrucoCard;

public class EstrategiaPrimeiroRound extends AbstractEstrategiaRound {

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return chooseCardFirstRound(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int blefe = intel.getOpponentScore() - intel.getScore();

        if(intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;

        if(blefe > 7) return true;

        if(getNumberOfManilhas(intel) > 1) return true;

        if(getNumberOfManilhas(intel) > 0 && getAverageCardValue(intel) >= 8.3) return true;

        return false;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (getNumberOfManilhas(intel) > 1) return 1;
        if (getNumberOfManilhas(intel) > 0 && getAverageCardValue(intel) >= 8) return 1;
        if (getAverageCardValue(intel) > 8.3) return 0;
        if (getAverageCardValue(intel) >= 6) return 0;
        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore() == 11) return true;

        if(hasZap(intel) && getNumberOfManilhas(intel) > 1) return true;

        if(hasZap(intel) && hasCopas(intel)) return true;

        if(hasEspadilha(intel) && hasCopas(intel)) return true;

        if(getNumberOfManilhas(intel) > 1 && getAverageCardValue(intel) > 7 ) return true;

        if(intel.getOpponentScore() > 7) {
            return (getNumberOfManilhas(intel) > 0 && getAverageCardValue(intel) > 7);
        }
        if (intel.getOpponentScore() > 6) {
            return (getAverageCardValue(intel) >= 7);
        }

        return false;

    }

    private CardToPlay chooseCardFirstRound(GameIntel intel) {
        TrucoCard vira = intel.getVira();


        if (hasZap(intel) && hasCopas(intel)) {
            return CardToPlay.of(getLowestCard(intel));
        }

        if (checkIfIsTheFirstToPlay(intel)) {
            if (getNumberOfManilhas(intel) > 0) {
                if (hasOuros(intel) || hasEspadilha(intel)) {
                    return CardToPlay.of(intel.getCards().stream()
                            .filter(trucoCard -> trucoCard.isOuros(vira) || trucoCard.isEspadilha(vira))
                            .findFirst()
                            .orElseGet(() -> getHighestNormalCard(intel)));
                }
                if (hasZap(intel) || hasCopas(intel)) {
                    return CardToPlay.of(getHighestNormalCard(intel));
                } else {
                    return CardToPlay.of(getHighestNormalCard(intel));
                }
            }
        } else {
            return getLowestToWin(intel)
                    .map(CardToPlay::of)
                    .orElseGet(() -> CardToPlay.of(getLowestCard(intel)));
        }

        return CardToPlay.of(getHighestCard(intel));
    }
}
