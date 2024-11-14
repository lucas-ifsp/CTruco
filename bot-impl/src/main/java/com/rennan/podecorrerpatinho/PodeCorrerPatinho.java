/*
 *  Copyright (C) 2024 Rennan Marcile Lazarini - IFSP/SCL
 *  Contact: lazarini <dot> rennan <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.rennan.podecorrerpatinho;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PodeCorrerPatinho implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int strongCardsCount = (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= 10)
                .count();
        if (intel.getOpponentScore() < 11) {
            if (PCPUtils.hasCasalMaior(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasCasalPreto(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasCasalMenor(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasCasalVermelho(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasZapOuros(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasCopasEspadilha(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasManilha(intel.getVira(), intel.getCards())
                    && PCPUtils.hasZap(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasManilha(intel.getVira(), intel.getCards()) && strongCardsCount >= 1)
                return true;
            if (strongCardsCount == 3)
                return true;
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int strongCardsCount = (int) intel.getCards().stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= 10)
                .count();
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11)
            return false;

        int roundNumber = intel.getRoundResults().size() + 1;
        List<GameIntel.RoundResult> roundsResults = intel.getRoundResults();

        if (roundNumber == 1) {
            return false;
        } else if (roundNumber == 2) {
            if (!(roundsResults.isEmpty())) {
                // >:(
                if (roundsResults.get(0).equals(GameIntel.RoundResult.WON) && strongCardsCount >= 1)
                    return true;
                if (PCPUtils.hasZap(intel.getVira(), intel.getCards()))
                    return false;

                if (PCPUtils.zapCopasAndEspadaAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasOuros(intel.getVira(), intel.getCards()) || strongCardsCount >= 1)
                    return true;

                if (PCPUtils.zapAndCopasAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasEspada(intel.getVira(), intel.getCards()) || strongCardsCount > 1)
                    return true;

                if (PCPUtils.zapAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasCopas(intel.getVira(), intel.getCards()) || strongCardsCount > 1)
                    return true;
            } else if (!roundsResults.isEmpty() && roundsResults.get(0).equals(GameIntel.RoundResult.DREW)) {
                if (PCPUtils.hasZap(intel.getVira(), intel.getCards()))
                    return true;
                if (PCPUtils.hasCopas(intel.getVira(), intel.getCards())
                        || PCPUtils.hasEspada(intel.getVira(), intel.getCards()) && intel.getHandPoints() < 3)
                    return true;
            }
        } else if (roundNumber == 3) {
            Optional<TrucoCard> opponentCard = intel.getOpponentCard();
            if (opponentCard.isPresent()) {
                TrucoCard enemyCard = opponentCard.get();
                if (PCPUtils.getStrongest(intel.getVira(), intel.getCards()).relativeValue(intel.getVira()) > enemyCard
                        .relativeValue(intel.getVira()))
                    return true;
            }
            if (PCPUtils.hasZap(intel.getVira(), intel.getCards()))
                return true;
            if (PCPUtils.hasCopas(intel.getVira(), intel.getCards()) && intel.getHandPoints() < 3)
                return true;
            if (roundsResults.get(0).equals(GameIntel.RoundResult.WON) && strongCardsCount == 1)
                return true;
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int roundNumber = intel.getRoundResults().size() + 1;
        List<GameIntel.RoundResult> roundsResults = intel.getRoundResults();
        List<TrucoCard> myHand = intel.getCards();

        if (roundNumber == 1) {
            if (intel.getOpenCards().isEmpty()) {
                return CardToPlay.of(PCPUtils.getStrongest(intel.getVira(), myHand));
            } else {
                TrucoCard opponentCard = intel.getOpenCards().get(0);

                Optional<TrucoCard> higherThanEnemy = myHand.stream()
                        .filter(card -> card.relativeValue(intel.getVira()) > opponentCard
                                .relativeValue(intel.getVira()))
                        .min(Comparator.comparingInt(card -> card.relativeValue(intel.getVira())));

                return CardToPlay.of(higherThanEnemy.orElseGet(() -> PCPUtils.getWeakest(intel.getVira(), myHand)));
            }
        } else if (roundNumber == 2) {
            if (!roundsResults.isEmpty() && roundsResults.get(0).equals(GameIntel.RoundResult.WON)) {
                return CardToPlay.of(PCPUtils.getWeakest(intel.getVira(), myHand));
            }
            return CardToPlay.of(PCPUtils.getStrongest(intel.getVira(), myHand));
        } else {
            return CardToPlay.of(PCPUtils.getStrongest(intel.getVira(), myHand));
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int roundNumber = intel.getRoundResults().size() + 1;
        List<GameIntel.RoundResult> roundsResults = intel.getRoundResults();
        List<TrucoCard> myHand = intel.getCards();

        if (myHand == null || myHand.isEmpty()) {
            return -1;
        }

        boolean hasZap = PCPUtils.hasZap(intel.getVira(), myHand);
        boolean hasManilha = PCPUtils.hasManilha(intel.getVira(), myHand);
        int strongCardsCount = (int) myHand.stream()
                .filter(card -> card.relativeValue(intel.getVira()) >= 10)
                .count();

        if (strongCardsCount >= 2) {
            return 0;
        } else if (hasZap || hasManilha) {
            return 1;
        }

        if (roundNumber == 1) {
            if (PCPUtils.hasEspada(intel.getVira(), myHand) || PCPUtils.hasOuros(intel.getVira(), myHand)) {
                return 0;
            }
        } else if (roundNumber == 2) {
            if (!roundsResults.isEmpty() &&
                    (roundsResults.get(0).equals(GameIntel.RoundResult.WON) ||
                            roundsResults.get(0).equals(GameIntel.RoundResult.DREW))) {
                if (PCPUtils.hasEspada(intel.getVira(), myHand) || PCPUtils.hasOuros(intel.getVira(), myHand) ||
                PCPUtils.hasCopas(intel.getVira(), myHand) || strongCardsCount > 1) {
                    return 0;
                }
            }
        } else if (roundNumber == 3) {
            Optional<TrucoCard> opponentCard = intel.getOpponentCard();
            if (opponentCard.isPresent()) {
                TrucoCard enemyCard = opponentCard.get();
                if (!myHand.isEmpty() &&
                        PCPUtils.getStrongest(intel.getVira(), myHand).relativeValue(intel.getVira()) > enemyCard
                                .relativeValue(intel.getVira())) {
                    return 1;
                }
            }
        }

        return -1;
    }
}