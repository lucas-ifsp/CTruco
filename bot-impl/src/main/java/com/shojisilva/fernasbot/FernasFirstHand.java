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

import com.bueno.spi.model.*;

import java.util.List;
import java.util.Optional;

public class FernasFirstHand implements FernasStrategy {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (intel.getOpponentScore() <= 6){
            return getAmountOfCardsBetween(intel, 13, 7) >= 2;
        }
        return getAmountOfManilhas(intel) >= 2 || getAmountOfCardsBetween(intel, 13, 9) >= 2;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (getAmountOfCardsBetween(intel, 7, 3) >= 2) return true;
        if (getAmountOfCardsBetween(intel, 13, 8) >= 2) return true;
        return getAmountOfCardsBetween(intel, 9, 5) >= 1 && getAmountOfManilhas(intel) >= 1;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = getCurrentCards(intel);

        if (hasThreeManilhas(intel)) return CardToPlay.of(cards.get(0));

        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isPresent()){
            Optional<TrucoCard> minimumCardToWin = getMinimumCardToWin(intel);
            return minimumCardToWin.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(cards.get(0)));
        }

        if (hasCasalMaior(intel) || hasCasalPreto(intel) || hasPausOuros(intel) || hasCopasEspadilha(intel)){
            return CardToPlay.of(cards.get(0));
        } else if (hasCasalMenor(intel) || hasCasalVermelho(intel)) {
            return CardToPlay.of(cards.get(1));
        }

        List<TrucoCard> allCardsThree = getAllCardsThree(cards);
        if (allCardsThree.isEmpty()) return CardToPlay.of(cards.get(0));

        Optional<TrucoCard> manilha = getManilha(intel);
        return manilha.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(allCardsThree.get(0)));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (getAmountOfCardsBetween(intel, 13, 9) >= 2) return 1;
        if (getAmountOfCardsBetween(intel, 10, 6) >= 1 && getAmountOfManilhas(intel) >= 1) return 0;
        return -1;
    }

    public boolean hasThreeManilhas(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isManilha(intel.getVira()))
                .toList()
                .size() == 3;
    }
}
