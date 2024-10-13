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

import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.DREW;

public class TrucoMarreco implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        var hasManilha = numberOfManilhas(intel) >= 2;
        return hasManilha;
    }

    public long numberOfManilhas(GameIntel intel) {
        return intel.getCards().stream().filter(card -> card.isManilha(intel.getVira())).count();
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (handStrong(intel) && wonFirstRound(intel)) {
            return true;
        }
        if(!intel.getRoundResults().isEmpty() && biggestCouple(intel)){
            return true;
        };
        return  false;
    }

    public boolean wonFirstRound(GameIntel intel){
        return intel.getRoundResults().getFirst().equals(GameIntel.RoundResult.WON);

    }

    public boolean handStrong(GameIntel intel){
        var hasManilha = numberOfManilhas(intel) >= 1;
        var containsThree = intel.getCards().contains(3);
        return hasManilha || containsThree;
    }

    public boolean biggestCouple(GameIntel intel){
        var hasZap = intel.getCards().stream().filter(card -> card.isZap(intel.getVira())).count() > 0;
        var hasCopas = intel.getCards().stream().filter(card -> card.isCopas(intel.getVira())).count() > 0;
        return hasZap && hasCopas;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        if (intel.getOpponentCard().get().isManilha(intel.getVira())){
            return CardToPlay.of(weakCard(intel).orElse(null));
        }

        if (biggestCouple(intel) && intel.getRoundResults().isEmpty()) {
            return CardToPlay.of(weakCard(intel).orElse(null));
        }
        if (intel.getRoundResults().equals(DREW) || wonFirstRound(intel)) {
            return CardToPlay.of(strongCard(intel).orElse(null));
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (biggestCouple(intel) || handStrong(intel)) { return 1; }
        if (wonFirstRound(intel) && numberOfManilhas(intel) >= 1) { return 1; }
        return 0;
    }

    private Optional<TrucoCard> weakCard(GameIntel intel) {
        return intel.getCards().stream().min((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
    }

    private Optional<TrucoCard> strongCard(GameIntel intel) {
        return intel.getCards().stream().max((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
    }

    @Override
    public String getName() { return "Truco Marreco!"; }
}