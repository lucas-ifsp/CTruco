/*
 *  Copyright (C) 2024 Matheus Sabatini Pacífico - IFSP/SCL
 *  Copyright (C) 2024 Dylan Tomaz Petrucelli - IFSP/SCL
 *
 *  Contact: matheus <dot> pacifico <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: dylan <dot> petrucelli <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.matheus.dylan.superidolbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.Optional;

public class SuperIdolBot implements BotServiceProvider {
    private static final int GOOD_HAND_STRENGTH_THRESHOLD = 21;
    private static final int AVERAGE_HAND_STRENGTH_THRESHOLD = 14;
    private static final int BAD_HAND_STRENGTH_THRESHOLD = 9;
    private static final int STRONG_CARD_THRESHOLD = 9;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return totalHandValue(intel) > GOOD_HAND_STRENGTH_THRESHOLD;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int roundCount = intel.getRoundResults().size();
        int manilhas = countManilhas(intel);
        int strongCards = countStrongCard(intel);

        if (roundCount == 0) return false;

        if (roundCount == 1){
            if (wonFirstRound(intel)) return true;
            if (manilhas == 2) return true;
            if (manilhas > 0  && strongCards > 0) return true;
        }

        if (roundCount == 2){
            Optional<TrucoCard> opponentCard = intel.getOpponentCard();
            if (opponentCard.isPresent()){
                TrucoCard myCard = intel.getCards().get(0);
                return myCard.relativeValue(intel.getVira()) > opponentCard.get().relativeValue(intel.getVira());
            }

            if (strongCards > 0) return true;
            return manilhas > 0;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int roundCount = intel.getRoundResults().size();
        int manilhas = countManilhas(intel);

        if (roundCount == 0) {
            Optional<TrucoCard> opponentCard = intel.getOpponentCard();
            if (opponentCard.isPresent()) {
                // Weakest card that beats the opponent card or else the weakest from hand
                TrucoCard optimalCard = intel.getCards().stream()
                        .filter(card -> card.relativeValue(intel.getVira()) > opponentCard.get().relativeValue(intel.getVira()))
                        .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                        .orElse(weakestCard(intel));

                return CardToPlay.of(optimalCard);
            }

            if (totalHandValue(intel) <= BAD_HAND_STRENGTH_THRESHOLD) return CardToPlay.of(weakestCard(intel));
            return CardToPlay.of(strongestCard(intel));
        }

        if (roundCount == 1) {
            Optional<TrucoCard> opponentCard = intel.getOpponentCard();
            if (opponentCard.isPresent()){
                // Weakest card that beats the opponent card or else the strongest from hand
                TrucoCard optimalCard = intel.getCards().stream()
                        .filter(card -> card.relativeValue(intel.getVira()) >  opponentCard.get().relativeValue(intel.getVira()))
                        .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                        .orElse(strongestCard(intel));

                return CardToPlay.of(optimalCard);
            }

            // Will save a manilha for the next round
            if (totalHandValue(intel) <= AVERAGE_HAND_STRENGTH_THRESHOLD && manilhas > 0) return CardToPlay.of(weakestCard(intel));

            return CardToPlay.of(strongestCard(intel));
        }

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int roundCount = intel.getRoundResults().size();
        int manilhas = countManilhas(intel);
        int strongCards = countStrongCard(intel);

        if (roundCount == 0) {
            if (manilhas == 2) return 1;
            if (manilhas > 0 && strongCards > 0) return 1;

            if (manilhas > 0) return 0;
            if (totalHandValue(intel) > GOOD_HAND_STRENGTH_THRESHOLD) return 0;

            return -1;
        }

        if (roundCount == 1){
            if (manilhas == 2) return 1;
            if (manilhas > 0 && strongCards > 0) return 1;
            if (wonFirstRound(intel) && strongCards > 0) return 1;

            if (manilhas > 0) return 0;
            if (totalHandValue(intel) > AVERAGE_HAND_STRENGTH_THRESHOLD) return 0;

            return -1;
        }

        if (roundCount == 2){
            if (manilhas > 0) return 1;
            if (wonFirstRound(intel) && totalHandValue(intel) >= BAD_HAND_STRENGTH_THRESHOLD) return 1;

            if (totalHandValue(intel) >= BAD_HAND_STRENGTH_THRESHOLD) return 0;

            return -1;
        }

        return -1;
    }

    @Override
    public String getName() {
        return "SuperIdol的笑容Bot";
    }

    private boolean wonFirstRound(GameIntel intel) {
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    private int countManilhas(GameIntel intel) {
        return (int) intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    private int countStrongCard(GameIntel intel){
        return (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= STRONG_CARD_THRESHOLD)
                .count();
    }

    private int totalHandValue(GameIntel intel) {
        return intel.getCards().stream().mapToInt(card -> card.relativeValue(intel.getVira()))
                .sum();
    }

    private TrucoCard strongestCard(GameIntel intel) {
        return intel.getCards().stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .orElse(intel.getCards().get(0));
    }

    private TrucoCard weakestCard(GameIntel intel) {
        return intel.getCards().stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())))
                .orElse(intel.getCards().get(0));
    }
}


