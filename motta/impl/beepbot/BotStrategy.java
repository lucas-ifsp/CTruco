/*
 *  Copyright (C) 2024 Erick Motta and Thiago Maciel - IFSP/SCL
 *  Contact: erick <dot> motta <at> ifsp <dot> edu <dot> br
 *  Contact: thiago <dot> maciel <at> ifsp <dot> edu <dot> br
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
package com.motta.impl.beepbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class BotStrategy {
    private List<GameIntel.RoundResult> roundResults = Collections.emptyList();

    public abstract int getRaiseResponse(GameIntel intel);
    public abstract boolean getMaoDeOnzeResponse(GameIntel intel);
    public abstract boolean decideIfRaises(GameIntel intel);
    public abstract CardToPlay chooseCard(GameIntel intel);

    public int getManilhaAmount(List<TrucoCard> cards, TrucoCard vira){
        return (int) cards.stream().filter(card -> card.isManilha(vira)).count();
    }

    public boolean hasManilha(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().anyMatch(card -> card.isManilha(vira));
    }

    public boolean hasZap(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isZap(vira));
    }

    public boolean hasCopas(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isCopas(vira));
    }

    public boolean hasOuros(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isOuros(vira));
    }

    public boolean hasEspadiha(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isEspadilha(vira));
    }

    public boolean hasStrongPair(GameIntel intel){
        return intel.getCards().stream().filter(card -> card.relativeValue(intel.getVira()) > 10).count() >= 2;
    }

    public double calculateAverageCardValue(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().mapToInt(card -> card.relativeValue(vira)).average().orElse(0.0);
    }

    public boolean shouldPlayDefensively(GameIntel intel){
        return intel.getScore() > intel.getOpponentScore() && (intel.getScore() - intel.getOpponentScore() > 2);
    }

    public boolean shouldPlayAggressively(GameIntel intel){
        return intel.getOpponentScore() > intel.getScore() && (intel.getOpponentScore() - intel.getScore() > 2);
    }

    public TrucoCard selectHighestNonManilhaCard(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream()
                .filter(card -> !card.isManilha(vira))
                .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(null);
    }

    public TrucoCard getHighestCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(null);
    }

    public TrucoCard getLowestCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(null);
    }

    public void setRoundResults(List<GameIntel.RoundResult> newRoundResults) {
        roundResults = Optional.ofNullable(newRoundResults).orElse(Collections.emptyList());
    }

    public Optional<GameIntel.RoundResult> getLastRoundResult() {
        if (roundResults.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(roundResults.get(roundResults.size() - 1));
    }
}
