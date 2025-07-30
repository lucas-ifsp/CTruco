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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Trucador extends Analyzer {
    private final GameIntel intel;
    private final TrucoCard vira;
    private final int bestCardValue;
    private final int secondBestCardValue;
    private final List<TrucoCard> opponentPlayedCards = new ArrayList<>();

    public Trucador(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        MyHand myHand = new MyHand(intel.getCards(), vira);
        this.bestCardValue = myHand.getBestCard().relativeValue(vira);
        this.secondBestCardValue = myHand.getSecondBestCard().relativeValue(vira);
    }

    @Override
    public Status threeCardsHandler(List<TrucoCard> myCards) {
        if (haveAtLeastTwoManilhas()) return Status.EXCELLENT;
        if (haveAtLeastOneManilha()) {
            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                updateOpponentPlayedCards(oppCard);
                if (myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard, vira) > 0)
                        .count() == 2 && secondBestCardValue >= 7) {
                    return Status.GOOD;
                }
            }
            return secondBestCardValue >= 8 ? Status.GOOD : Status.MEDIUM;
        }
        long handPower = powerOfTheTwoBestCards();
        if (handPower >= 14) return Status.GOOD;
        if (handPower >= 11) return Status.MEDIUM;
        return calculateWinProbability() > 0.5 ? Status.GOOD : Status.BAD;
    }

    public Status twoCardsHandler(List<TrucoCard> myCards) {
        if (Game.wonFirstRound(intel)) {
            if (bestCardValue >= 9) return Status.EXCELLENT;
            return bestCardValue >= 7 ? Status.GOOD : Status.MEDIUM;
        }
        if (Game.lostFirstRound(intel)) {
            if (haveAtLeastTwoManilhas()) return Status.EXCELLENT;
            if (intel.getOpponentCard().isPresent()) {
                TrucoCard oppCard = intel.getOpponentCard().get();
                updateOpponentPlayedCards(oppCard);
                if (secondBestCardValue > oppCard.relativeValue(vira) && bestCardValue >= 9) return Status.GOOD;
                return Status.MEDIUM;
            }
            if (haveAtLeastOneManilha()) {
                if (secondBestCardValue >= 9) return Status.EXCELLENT;
                if (secondBestCardValue == 8) return Status.GOOD;
                return secondBestCardValue == 7 ? Status.MEDIUM : Status.BAD;
            }
            long handPower = powerOfTheTwoBestCards();
            if (handPower >= 17) return Status.GOOD;
            return handPower >= 13 ? Status.MEDIUM : Status.BAD;
        }
        if (shouldLoseSecondRound()) return Status.BAD;
        return isIntermediateCardOvervalued() ? Status.MEDIUM : adjustAggressivenessBasedOnGame();
    }

    @Override
    public Status oneCardHandler() {
        TrucoCard myCard = getLowestValueCard();
        Optional<TrucoCard> oppCard = intel.getOpponentCard();
        oppCard.ifPresent(this::updateOpponentPlayedCards);
        if (Game.wonFirstRound(intel)) {
            if (oppCard.isPresent()) {
                if (myCard.compareValueTo(oppCard.get(), vira) > 0) return Status.EXCELLENT;
                return intel.getHandPoints() <= 6 ? Status.GOOD : Status.BAD;
            }
        }
        if (Game.lostFirstRound(intel)) {
            if (bestCardValue >= 9) return Status.EXCELLENT;
            return bestCardValue >= 7 ? Status.GOOD : bestCardValue >= 3 ? Status.MEDIUM : Status.BAD;
        }
        if (shouldUseManilhaNow()) return Status.EXCELLENT;
        if (shouldAskForTruco()) return Status.GOOD;
        if (intel.getHandPoints() <= 3) return Status.EXCELLENT;
        return bestCardValue >= 5 ? Status.GOOD : bestCardValue >= 3 ? Status.MEDIUM : Status.BAD;
    }

    private boolean haveAtLeastTwoManilhas() {
        return getManilhaAmount() >= 2;
    }

    private boolean haveAtLeastOneManilha() {
        return getManilhaAmount() >= 1;
    }

    private long getManilhaAmount() {
        return intel.getCards().stream().filter(card -> card.isManilha(vira)).count();
    }

    private long powerOfTheTwoBestCards() {
        return intel.getCards().stream().mapToLong(card -> card.relativeValue(vira)).sorted().limit(2).sum();
    }

    private boolean opponentMightBeBluffing() {
        long bluffCount = intel.getOpponentCard().stream().filter(card -> card.relativeValue(vira) < 5).count();
        long totalCards = intel.getOpponentCard().map(card -> 1).orElse(0);
        return totalCards > 0 && (double) bluffCount / totalCards > 0.3;
    }

    private void updateOpponentPlayedCards(TrucoCard card) {
        opponentPlayedCards.add(card);
    }

    private boolean canWinWithIntermediateCards() {
        return opponentPlayedCards.stream().noneMatch(card -> card.relativeValue(vira) > 7);
    }

    private boolean shouldUseManilhaNow() {
        return intel.getRoundResults().size() == 2 || opponentMightBeBluffing();
    }

    private boolean shouldLoseSecondRound() {
        return bestCardValue < 5 && intel.getRoundResults().size() == 1 && !haveAtLeastOneManilha();
    }

    private Status adjustAggressivenessBasedOnGame() {
        return Game.lostFirstRound(intel) ? Status.MEDIUM : Status.GOOD;
    }

    private boolean isIntermediateCardOvervalued() {
        return (bestCardValue == 7 || bestCardValue == 8) && canWinWithIntermediateCards();
    }

    private TrucoCard getLowestValueCard() {
        return intel.getCards().stream().min(Comparator
                .comparingInt(card -> card.relativeValue(vira))).orElse(intel.getCards().get(0));
    }

    private boolean shouldAskForTruco() {
        return Game.isCriticalSituation(intel) && opponentMightBeBluffing();
    }

    private double calculateWinProbability() {
        long highValueCards = intel.getCards().stream().filter(card -> card.relativeValue(vira) > 7).count();
        long totalCards = intel.getCards().size();
        return totalCards > 0 ? (double) highValueCards / totalCards : 0;
    }
}