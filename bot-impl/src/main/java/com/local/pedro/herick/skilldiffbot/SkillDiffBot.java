/*
 *  Copyright (C) 2024 Herick Victor Rodrigues - IFSP/SCL and Pedro Henrique Aissa - IFSP/SCL
 *  Contact: herick <dot> victor <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: a <dot> pedro <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.local.pedro.herick.skilldiffbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class SkillDiffBot extends BotUtils implements BotServiceProvider {
    private static final int REFUSE = -1;
    private static final int ACCEPT = 0;
    private static final int RAISE = 1;

    private static final double RAISE_THRESHOLD = 0.7;
    private static final double ACCEPT_THRESHOLD = 0.5;

    private final HandEvaluator evaluator = new HandEvaluator();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        int manilhas = getManilhaCount(cards, vira);
        int strongCards = getStrongCardsCount(cards, vira);

        if (intel.getOpponentScore() >= 9) {
            return manilhas >= 1 || strongCards >= 2;
        }

        if (manilhas >= 2) return true;
        return manilhas == 1 && strongCards >= 1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int currentRound = intel.getRoundResults().size();
        return strategyForTheRound(currentRound).decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int currentRound = intel.getRoundResults().size();
        return strategyForTheRound(currentRound).chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        double handStrength = evaluator.evaluateHand(intel.getCards(), intel.getVira());

        if (handStrength > RAISE_THRESHOLD) return RAISE;
        if (handStrength > ACCEPT_THRESHOLD) return ACCEPT;

        return REFUSE;
    }

    private Strategy strategyForTheRound(int round){
        return switch (round){
            case 0 -> new FirstRoundStrategy();
            case 1 -> new SecondRoundStrategy();
            case 2 -> new ThirdRoundStrategy();
            default -> throw new IllegalStateException("Unexpected value: " + round);
        };
    }
    @Override
    public String getName() {
        return "SkillDiffBot";
    }
}
