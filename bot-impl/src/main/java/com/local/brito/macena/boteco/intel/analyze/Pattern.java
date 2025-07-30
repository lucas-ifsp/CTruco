/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.local.brito.macena.boteco.intel.analyze;

import com.local.brito.macena.boteco.utils.Status;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.local.brito.macena.boteco.interfaces.Analyzer;
import com.local.brito.macena.boteco.utils.Game;
import com.local.brito.macena.boteco.utils.MyHand;

import java.util.List;
import java.util.Optional;

public class Pattern extends Analyzer {

    private final GameIntel intel;
    private final TrucoCard vira;
    private final int bestCardValue;
    private final int secondBestCardValue;


    public Pattern(GameIntel intel) {
        this.intel = intel;
        vira = intel.getVira();

        MyHand myCards = new MyHand(intel.getCards(),vira);

        bestCardValue = myCards.getBestCard().relativeValue(vira);
        secondBestCardValue = myCards.getSecondBestCard().relativeValue(vira);
    }


    @Override
    public Status threeCardsHandler(List<TrucoCard> myCards) {
        if (haveAtLeastTwoManilhas()) {
            return Status.EXCELLENT;
        }
        if (haveAtLeastOneManilha()) {
            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                if (myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard, vira) > 0)
                        .count() == 3) {
                    return Status.EXCELLENT;
                }
            }

            if (secondBestCardValue >= 5) return Status.GOOD;
            return Status.MEDIUM;
        }
        long handPower = powerOfTheTwoBestCards();
        if (handPower >= 17) {
            return Status.GOOD;
        }
        if (handPower >= 10) {
            return Status.MEDIUM;
        }
        return Status.BAD;
    }

    @Override
    public Status twoCardsHandler(List<TrucoCard> myCards) {
        if (Game.wonFirstRound(intel)) {
            if (haveAtLeastOneManilha()) return Status.EXCELLENT;
            if (bestCardValue >= 8) return Status.GOOD;
            if (bestCardValue >= 4) return Status.MEDIUM;
            return Status.BAD;
        }
        if (Game.lostFirstRound(intel)) {

            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                if (myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard, vira) > 0)
                        .count() == 2) {
                    return Status.MEDIUM;
                }
                return Status.BAD;
            }

            if (haveAtLeastTwoManilhas()) return Status.EXCELLENT;

            if (haveAtLeastOneManilha()) {
                if (secondBestCardValue >= 8) return Status.EXCELLENT;
                if (secondBestCardValue >= 6) return Status.GOOD;
                return Status.MEDIUM;
            }
            if (powerOfTheTwoBestCards() >= 17) return Status.GOOD;
            if (powerOfTheTwoBestCards() >= 14) return Status.MEDIUM;
            return Status.BAD;
        }

        if (haveAtLeastOneManilha()) return Status.EXCELLENT;
        if (bestCardValue == 9) return Status.GOOD;
        if (bestCardValue >= 6) return Status.MEDIUM;
        return Status.BAD;
    }

    @Override
    public Status oneCardHandler() {
        TrucoCard myCard = intel.getCards().get(0);
        Optional<TrucoCard> oppCard = intel.getOpponentCard();

        if (Game.wonFirstRound(intel)) {
            if (oppCard.isPresent()) return oneCardHandlerWinningFirstRound(oppCard.get(), myCard);
        }

        if (Game.lostFirstRound(intel)) {
            return oneCardHandlerLosingFirstRound();
        }

        if (intel.getHandPoints() <= 3) return Status.EXCELLENT;
        if (bestCardValue >= 5) return Status.GOOD;
        return Status.BAD;
    }

    private Status oneCardHandlerLosingFirstRound() {
        if (haveAtLeastOneManilha()) {
            return Status.EXCELLENT;
        }
        if (bestCardValue == 9) {
            long numberOfCardsBetterThanThree = intel.getOpenCards().stream()
                    .filter(card -> card.isManilha(vira) || card.relativeValue(vira) == 9)
                    .count();
            if (numberOfCardsBetterThanThree >= 2) {
                return Status.EXCELLENT;
            }
            return Status.GOOD;
        }
        if (bestCardValue == 8) {
            return Status.BAD;
        }
        if (bestCardValue >= 6) {
            return Status.MEDIUM;
        }
        return Status.BAD;
    }

    private boolean haveAtLeastTwoManilhas() { return getManilhaAmount() >= 2; }

    private boolean haveAtLeastOneManilha() {
        return getManilhaAmount() >= 1;
    }

    private Status oneCardHandlerWinningFirstRound(TrucoCard oppCard, TrucoCard myCard) {
        if (intel.getHandPoints() <= 3) {
            return Status.EXCELLENT;
        }
        if (myCard.compareValueTo(oppCard, vira) > 0) {
            return Status.EXCELLENT;
        }
        return Status.BAD;
    }

    private long getManilhaAmount() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    private long powerOfTheTwoBestCards() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .mapToLong(card -> card.relativeValue(vira))
                .sorted()
                .limit(2)
                .sum();
    }
}
