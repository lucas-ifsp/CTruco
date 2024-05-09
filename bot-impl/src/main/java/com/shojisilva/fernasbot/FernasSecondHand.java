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
    public static final int HAND_MAX_VALUE = 25;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<RoundResult> roundResults = intel.getRoundResults();
        int handValue = getHandValue(intel);
        if (roundResults.get(0) == RoundResult.WON){
            return (handValue >= HAND_MAX_VALUE * 0.6 && getAmountManilhas(intel) == 0) || getAmountManilhas(intel) >= 1;
        }
        if (getAmountManilhas(intel) >= 1) return true;
        return handValue >= HAND_MAX_VALUE * 0.68 && getAmountManilhas(intel) == 0;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = getCurrentCards(intel);

        RoundResult roundResult = intel.getRoundResults().get(0);

        if (roundResult == RoundResult.DREW) return CardToPlay.of(cards.get(1));

        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        if (opponentCard.isPresent()) {
            Optional<TrucoCard> menorCarta = getMenorCarta(intel);
            return menorCarta.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(cards.get(0)));
        }

        List<TrucoCard> cartasTres = getTres(cards);
        if (cartasTres.isEmpty()) return CardToPlay.of(cards.get(0));
        else if (getAmountManilhas(intel) == 1){
            Optional<TrucoCard> manilha = getManilha(intel);
            if (manilha.isPresent()) return CardToPlay.of(manilha.get());
        }
        return CardToPlay.of(cartasTres.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int handValue = getHandValue(intel);
        List<RoundResult> roundResults = intel.getRoundResults();
        if (roundResults.get(0) == RoundResult.WON) {
            if (handValue >= HAND_MAX_VALUE * 0.56 || getAmountManilhas(intel) >= 1) return 1;
            if (handValue >= HAND_MAX_VALUE * 0.60 && getAmountManilhas(intel) == 0) return 0;
        }
        if ((handValue >= HAND_MAX_VALUE * 0.64 && getAmountManilhas(intel) == 0) || getAmountManilhas(intel) >= 1) return 0;
        return -1;
    }
}
