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

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Optional;

public abstract class AbstractEstrategiaRound implements EstrategiaRound {

    protected boolean checkIfIsTheFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    protected GameIntel.RoundResult getlastRoundResult(GameIntel intel){
        return intel.getRoundResults().get(0);
    }

    protected TrucoCard getLowestCard(GameIntel intel) {
        return intel.getCards().stream()
                .min((card1, card2) -> Integer.compare(card1.relativeValue(intel.getVira()), card2.relativeValue(intel.getVira())))
                .orElseThrow();
    }

    protected TrucoCard getHighestCard(GameIntel intel) {
        return intel.getCards().stream()
                .max((card1, card2) -> Integer.compare(card1.relativeValue(intel.getVira()), card2.relativeValue(intel.getVira())))
                .orElseThrow();
    }

    protected TrucoCard getHighestNormalCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> !card.isManilha(vira))
                .max((card1, card2) -> Integer.compare(card1.relativeValue(vira), card2.relativeValue(vira)))
                .orElseThrow();
    }

    protected Optional<TrucoCard> getLowestToWin(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getOpponentCard()
                .flatMap(opponentCard -> intel.getCards().stream()
                        .filter(card -> card.relativeValue(vira) > opponentCard.relativeValue(vira))
                        .min((card1, card2) -> Integer.compare(card1.relativeValue(vira), card2.relativeValue(vira)))
                );
    }

    protected double getAverageCardValue(GameIntel intel) {
        return intel.getCards().stream()
                .mapToInt(card -> card.relativeValue(intel.getVira()))
                .average()
                .orElse(0.0);
    }

    protected int getNumberOfManilhas(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return (int) intel.getCards().stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    protected boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    protected boolean hasCopas(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isCopas(intel.getVira()));
    }

    protected boolean hasEspadilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isEspadilha(intel.getVira()));
    }

    protected boolean hasOuros(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isOuros(intel.getVira()));
    }

    protected boolean isMaoDeOnze(GameIntel intel) {
        return intel.getScore() == 11;
    }
}
