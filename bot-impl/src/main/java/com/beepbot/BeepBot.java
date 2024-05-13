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

package com.motta.impl.beepbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class BeepBot implements BotServiceProvider {

    private BotStrategy strategy;
    private GameIntel gameIntel;

    public BeepBot(){
        this.strategy = new FirstRoundStrategy();
    }

    public void updateGameIntelAndStrategy(GameIntel intel) {
        this.gameIntel = intel;
        updateStrategyBasedOnRound();
    }

    private void updateStrategyBasedOnRound() {
        int roundNumber = gameIntel.getRoundResults().size() + 1;
        if(allOrNothing(gameIntel)){
            this.strategy = chooseAgressiveStrategy(roundNumber);
        }else{
            this.strategy = chooseStrategy(roundNumber);
        }
    }

    private BotStrategy chooseStrategy(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> new FirstRoundStrategy();
            case 2 -> new SecondRoundStrategy();
            case 3 -> new ThirdRoundStrategy();
            default -> throw new IllegalStateException("Número de rodada inválido: " + roundNumber);
        };
    }

    private BotStrategy chooseAgressiveStrategy(int roundNumber) {
        return switch (roundNumber) {
            case 1 -> new AgressiveFirstRoundStrategy();
            case 2 -> new AgressiveSecondRoundStrategy();
            case 3 -> new AgressiveThirdRoundStrategy();
            default -> throw new IllegalStateException("Número de rodada inválido: " + roundNumber);
        };
    }

    public boolean allOrNothing(GameIntel intel){
        return intel.getOpponentScore() > intel.getScore() && (intel.getOpponentScore() - intel.getScore() > 6);
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        updateGameIntelAndStrategy(intel);
        strategy.setRoundResults(intel.getRoundResults());
        return strategy.getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        updateGameIntelAndStrategy(intel);
        strategy.setRoundResults(intel.getRoundResults());
        return strategy.decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        updateGameIntelAndStrategy(intel);
        strategy.setRoundResults(intel.getRoundResults());
        return strategy.chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        updateGameIntelAndStrategy(intel);
        strategy.setRoundResults(intel.getRoundResults());
        return strategy.getRaiseResponse(intel);
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }
}
