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
import com.bueno.spi.model.GameIntel.RoundResult;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;

public class FernasSecondHand implements FernasStrategy {
    public static final int MAX_HAND_VALUE = 25;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<RoundResult> roundResults = intel.getRoundResults();
        int handValue = getHandValue(intel);

        if (roundResults.get(0) == RoundResult.WON){
            return (handValue >= MAX_HAND_VALUE * 0.6 && getAmountOfManilhas(intel) == 0) || getAmountOfManilhas(intel) >= 1;
        }
        if (getAmountOfManilhas(intel) >= 1) return true;
        return handValue >= MAX_HAND_VALUE * 0.68 && getAmountOfManilhas(intel) == 0;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = getCurrentCards(intel);
        RoundResult roundResult = intel.getRoundResults().get(0);

        if (roundResult == RoundResult.DREW) return CardToPlay.of(cards.get(1));

        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isPresent()) {
            Optional<TrucoCard> minimumCardToWin = getMinimumCardToWin(intel);
            return minimumCardToWin.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(cards.get(0)));
        }

        List<TrucoCard> allCardsThree = getAllCardsThree(cards);
        if (allCardsThree.isEmpty()) return CardToPlay.of(cards.get(0));

        Optional<TrucoCard> manilha = getManilha(intel);
        return manilha.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(allCardsThree.get(0)));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int handValue = getHandValue(intel);
        List<RoundResult> roundResults = intel.getRoundResults();

        if (roundResults.get(0) == RoundResult.WON) {
            if (handValue >= MAX_HAND_VALUE * 0.56 || getAmountOfManilhas(intel) >= 1) return 1;
            if (handValue >= MAX_HAND_VALUE * 0.60 && getAmountOfManilhas(intel) == 0) return 0;
        }
        if ((handValue >= MAX_HAND_VALUE * 0.64 && getAmountOfManilhas(intel) == 0) || getAmountOfManilhas(intel) >= 1) return 0;
        return -1;
    }
}
