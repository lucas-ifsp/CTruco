/*
 *  Copyright (C) 2024 Lucas Jundi Hikazudani - IFSP/SCL
 *  Copyright (C) 2024 Priscila de Luna Farias - IFSP/SCL
 *
 *  Contact: h <dot> jundi <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: luna <dot> p <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.luna.jundi.jokerBot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;
import com.luna.jundi.jokerBot.exceptions.InvalidNumberOfRoundsException;
import com.luna.jundi.jokerBot.states.*;

import static com.luna.jundi.jokerBot.utils.RoundUtils.getRoundNumber;
import static com.luna.jundi.jokerBot.utils.RoundUtils.jokerBotStartsTheRound;

public final class JokerBot implements BotServiceProvider {

    private RoundState state;

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        setState(intel);
        return state.cardChoice();
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        setState(intel);
        return state.raiseDecision();
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        setState(intel);
        return state.raiseResponse();
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        setState(intel);
        if (!(state instanceof FirstToPlayRoundOneState || state instanceof SecondToPlayRoundOneState)) {
            throw new IllegalStateException("Asking mao de onze response in round" + getRoundNumber(intel) + " is not valid.\n");
        }
        return state.maoDeOnzeDecision(intel);
    }

    private void setState(GameIntel intel) throws InvalidNumberOfRoundsException {
        boolean start = jokerBotStartsTheRound().test(intel);
        int roundNumber = getRoundNumber(intel);
        switch (roundNumber) {
            case 1 -> this.state = start ? new FirstToPlayRoundOneState(intel) : new SecondToPlayRoundOneState(intel);
            case 2 -> this.state = start ? new FirstToPlayRoundTwoState(intel) : new SecondToPlayRoundTwoState(intel);
            case 3 -> this.state = new RoundThreeState(intel);
            default -> throw new InvalidNumberOfRoundsException(roundNumber + " is not a valid round number\n");
        }
    }
}