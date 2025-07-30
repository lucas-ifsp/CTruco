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
import com.bueno.spi.service.BotServiceProvider;


public class GapBot implements BotServiceProvider {
    private TrucoCard vira;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        vira = intel.getVira();
        int opponentScore = intel.getOpponentScore();
        long strongCards;
        if(opponentScore < 6){
            strongCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) > 8)
                    .count();

            return strongCards >= 2;

        }
        if (opponentScore < 8) {
            strongCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) > 7)
                    .count();

            return strongCards >= 2;

        }
        if (opponentScore < 11) {
            strongCards = intel.getCards().stream()
                    .filter(card -> card.relativeValue(vira) > 7)
                    .count();

            return strongCards >= 1 && intel.getCards().stream().anyMatch(card -> card.relativeValue(vira) > 6);
        }
        return false;
    }
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return strategyForTheRound(intel.getRoundResults().size()).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return strategyForTheRound(intel.getRoundResults().size()).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return strategyForTheRound(intel.getRoundResults().size()).getRaiseResponse(intel);
    }
    @Override
    public String getName() {return "TomeGapBot";
    }

    private Strategy strategyForTheRound(int round){
        return switch (round){
            case 0 -> new FirstRound();
            case 1 -> new SecondRound();
            case 2 -> new ThirdRound();
            default -> throw new IllegalStateException("Unexpected value: " + round);
        };
    }

}
