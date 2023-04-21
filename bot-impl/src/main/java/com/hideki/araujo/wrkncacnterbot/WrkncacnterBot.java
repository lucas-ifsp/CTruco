/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: x45danilo45x <at> gmail <dot> com or
 *  lucashideki87 <at> gmail <dot> com
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

package com.hideki.araujo.wrkncacnterbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class WrkncacnterBot implements BotServiceProvider {
    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (calculateDeckValue(intel) >= CardRank.KING.value() * intel.getCards().size()) {
            var numberOfManilhas = calculateNumberOfManilhas(intel);
            if (intel.getScore() > intel.getOpponentScore()) {
                if (numberOfManilhas >= 2) return 0;
                return -1;
            }
            else if (intel.getScore() == intel.getOpponentScore()) {
                if (numberOfManilhas >= 2) return 0;
                return -1;
            }
            else if (intel.getScore() < intel.getOpponentScore()) {
                if (numberOfManilhas >= 2 && hasCardRankHigherThan(intel, CardRank.KING)) return 1;
                return -1;
            }
        }
        if (calculateDeckValue(intel) <= CardRank.QUEEN.value() * intel.getCards().size()) {
            if (intel.getScore() > intel.getOpponentScore()) return -1;
            else if (intel.getScore() == intel.getOpponentScore()) return 0;
            else if (intel.getScore() < intel.getOpponentScore()) return 1;
        }
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        var hasThreeTwoAndManilha =
                hasCardRank(intel, CardRank.THREE) &&
                hasCardRank(intel, CardRank.TWO) &&
                        calculateNumberOfManilhas(intel) == 1;
        var hasTwoManilhas = calculateNumberOfManilhas(intel) >= 2;

        return hasThreeTwoAndManilha || hasTwoManilhas || hasZapAndManilhaHearts(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getRoundResults().isEmpty())
            return calculateDeckValue(intel) <= 6 || calculateDeckValue(intel) >= 24;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return CardToPlay.of(chooseKillCard(intel).orElse(chooseWeakestCard(intel).get()));
    }

    public int calculateDeckValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }

    public Optional<TrucoCard> chooseWeakestCard(GameIntel intel) {
        return intel.getCards().stream().min((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
    }

    public Optional<TrucoCard> chooseKillCard(GameIntel intel) {
        if (intel.getOpponentCard().isEmpty()) return Optional.empty();
        return intel
                .getCards()
                .stream()
                .filter(card -> card.compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0)
                .max((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
    }

    public long calculateNumberOfManilhas(GameIntel intel) {
        return intel.getCards().stream().filter(card -> card.isManilha(intel.getVira())).count();
    }

    public boolean hasZapAndManilhaHearts(GameIntel intel) {
        var hasZap = intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
        var hasManilhaHearts = intel.getCards().stream().anyMatch(card -> card.isCopas(intel.getVira()) && card.isManilha(intel.getVira()));

        return hasZap && hasManilhaHearts;
    }

    public boolean hasCardRank(GameIntel intel, CardRank cardRank) {
        return intel.getCards().stream().anyMatch(card -> card.getRank().equals(cardRank));
    }

    public boolean hasCardRankHigherThan(GameIntel intel, CardRank cardRank) {
        return intel.getCards().stream().allMatch(card -> card.getRank().value() >= cardRank.value());
    }
}
