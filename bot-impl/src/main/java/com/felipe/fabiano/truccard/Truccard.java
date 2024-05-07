/*
 *  Copyright (C) 2024 Felipe G. de Marchi and Fabiano R. Junior - IFSP/SCL
 *  Contact: felipe <dot> corsi <at> ifsp <dot> edu <dot> br or
 *  fabiano <dot> rocha <at> ifsp <dot> edu <dot> br
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
 *  along with CTruco. If not, see <https://www.gnu.org/licenses/>
 */

package com.felipe.fabiano.truccard;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class Truccard implements BotServiceProvider {
    //DECISION-MAKING PROCESS
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return hasStrongHand(intel, 8, 9) || (intel.getOpponentScore() >= 8 && handStrength(intel) > 7);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return hasStrongHand(intel, 8, 9) || (intel.getOpponentScore() >= 8 && handStrength(intel) > 7);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (hasStrongHand(intel, 9, 9)) return 1;
        if (handStrength(intel) >= 7) return 0;
        return -1;
    }

    //AUX METHODS FOR DECISION-MAKING
    private boolean hasStrongHand(GameIntel intel, double averageThreshold, int highCardThreshold) {
        int numOfTrumpCards = numOfTrumpCards(intel);
        double handStrength = handStrength(intel);
        int strongCardsCount = filterStrongestCards(intel, highCardThreshold);

        return numOfTrumpCards > 0 || handStrength >= averageThreshold || strongCardsCount >= 2;
    }

    private double handStrength(GameIntel intel) {
        return intel.getCards().stream()
                .mapToInt(card -> card.relativeValue(intel.getVira()))
                .average()
                .orElse(0);
    }

    private int numOfTrumpCards(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    private TrucoCard pickStrongestCard(GameIntel intel) {
        return intel.getCards().stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .orElseThrow(() -> new NoSuchElementException("No cards available to play."));
    }

    private TrucoCard pickWeakestCard(GameIntel intel) {
        return intel.getCards().stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .orElseThrow(() -> new NoSuchElementException("No cards available to play."));
    }

    private Optional<TrucoCard> optimalCardPick(GameIntel intel) {
        Optional<TrucoCard> enemyCardOpt = intel.getOpponentCard();
        if (enemyCardOpt.isEmpty()) return Optional.empty();

        TrucoCard enemyCard = enemyCardOpt.get();
        TrucoCard turnup = intel.getVira();

        return intel.getCards().stream()
                .filter(card -> card.relativeValue(turnup) > enemyCard.relativeValue(turnup))
                .min(Comparator.comparingInt(card -> card.relativeValue(turnup)));
    }

    private Optional<TrucoCard> strongestNonTrumpCard(GameIntel intel) {
        TrucoCard turnup = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        TrucoCard highestNonTrumpCard = null;
        for (TrucoCard card : cards) {
            if (!card.isManilha(turnup)) {
                if (highestNonTrumpCard == null || card.compareValueTo(highestNonTrumpCard, turnup) > 0) {
                    highestNonTrumpCard = card;
                }
            }
        }

        if (highestNonTrumpCard != null) {
            return Optional.of(highestNonTrumpCard);
        }

        return Optional.empty();
    }

    private int filterStrongestCards(GameIntel intel, int value) {
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) > value)
                .count();
    }

    //ROUND PLAY PATTERNS
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return switch (intel.getRoundResults().size()) {
            case 0 -> firstRoundPlay(intel);
            case 1 -> secondRoundPlay(intel);
            default -> playRemainingCard(intel);
        };
    }
    private CardToPlay firstRoundPlay(GameIntel intel){
        if (playingSecond(intel)) {
            return CardToPlay.of(optimalCardPick(intel).orElse(pickWeakestCard(intel)));
        }

        int numOfTrumpCards = numOfTrumpCards(intel);

        if (numOfTrumpCards >= 2) {
            return CardToPlay.of(pickStrongestCard(intel));
        }

        if (numOfTrumpCards == 1) {
            int strongNonTrumpCount = filterStrongestCards(intel, 7);

            if (strongNonTrumpCount >= 2) {
                return CardToPlay.of(strongestNonTrumpCard(intel).orElse(pickWeakestCard(intel)));
            }

            return CardToPlay.of(strongestNonTrumpCard(intel).orElse(pickStrongestCard(intel)));
        }

        return CardToPlay.of(pickStrongestCard(intel));
    }

    private CardToPlay secondRoundPlay(GameIntel intel){
        if (playingSecond(intel)) {
            if (Math.random() <= 0.3) {
                return CardToPlay.of(pickStrongestCard(intel));
            } else {
                return CardToPlay.of(optimalCardPick(intel).orElse(pickWeakestCard(intel)));
            }
        }

        return CardToPlay.of(pickStrongestCard(intel));
    }

    private CardToPlay playRemainingCard(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }

    private boolean playingSecond(GameIntel intel) {
        return intel.getOpponentCard().isPresent();
    }

    private boolean wonLastRound(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) return false;

        int lastRound = intel.getRoundResults().size() - 1;

        return intel.getRoundResults().get(lastRound) == GameIntel.RoundResult.WON;
    }

    @Override
    public String getName() {
        return "Truccard";
    }
}