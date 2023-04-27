/*
 *  Copyright (C) 2023 Vinicius R. Noli and Vitor Bonelli
 *  Contact: vinicius <dot> noli <at> ifsp <dot> edu <dot> br
 *  Contact: vitor <dot> bonelli <at> ifsp <dot> edu <dot> br
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

package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ThirdRound implements Strategy {

    public ThirdRound() {
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getCards().size() == 1) {
            if (intel.getCards().get(0).isZap(intel.getVira())) return 1;
        }
        TrucoCard cardPlayed = intel.getOpenCards().get(intel.getOpenCards().size() - 1);
        if (cardPlayed.isZap(intel.getVira())) return 1;
        if (intel.getRoundResults().get(1) == GameIntel.RoundResult.LOST) return 0;
        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, intel.getVira());
        });

        TrucoCard cardMedium = getCardWithMediumStrength(intel.getCards(), intel.getVira());

        if (calculateCurrentHandValue(intel) >= 25) {
            if (hasManilha(intel)) return true;
            else if (intel.getOpponentScore() < 9 && hasTwoOrThree(intel)) return true;
            else if (intel.getOpponentScore() >= 9 && cardMedium.getRank().value() >= 9) return true;
            return true;
        }

        if (intel.getOpponentCard().isPresent()) {
            if (cards.get(0).relativeValue(intel.getVira()) >= intel.getOpponentCard().get().relativeValue(intel.getVira()))
                return true;
        }

        if (intel.getOpponentScore() <= 6) return true;

        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira())) return true;
        }

        return intel.getScore() >= intel.getOpponentScore() + 3;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getRoundResults().get(1) == GameIntel.RoundResult.WON) return intel.getCards().get(0).relativeValue(intel.getVira()) >= 10;
        if (intel.getCards().size() == 1) {
            return intel.getCards().get(0).isZap(intel.getVira());
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }

    public boolean hasTwoOrThree(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.getRank() == CardRank.TWO
                || card.getRank() == CardRank.THREE);
    }

    public TrucoCard getCardWithMediumStrength(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .sorted(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .skip(1)
                .limit(1)
                .findFirst()
                .orElseThrow();
    }

    public int calculateCurrentHandValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }
}
