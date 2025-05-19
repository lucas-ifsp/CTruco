/*
 *  Copyright (C) 2025 Breno Augusto de Oliveira - IFSP/SCL
 *  Contact: oliveira <dot> breno <at> ifsp <dot> edu <dot> br
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

package com.breno.trucoJC.botBlessed;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class BotBlessed implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        int playerScore = intel.getScore();

        long manilhaCount = intel.getCards().stream().filter(c -> c.isManilha(intel.getVira())).count();
        boolean hasZap = intel.getCards().stream().anyMatch(c -> c.isZap(intel.getVira()));
        boolean hasCopas = intel.getCards().stream().anyMatch(c -> c.isCopas(intel.getVira()));
        boolean hasThree = intel.getCards().stream().anyMatch(c -> c.getRank() == CardRank.THREE);
        long threes = intel.getCards().stream().filter(c -> c.getRank() == CardRank.THREE).count();

        boolean isMaoDeOnze = playerScore == 11 || opponentScore == 11;

        if (isMaoDeOnze) {
            if (manilhaCount >= 2) return true;
            if ((hasZap || hasCopas) && hasThree) return true;
            if (manilhaCount >= 1 && hasThree) return true;
            if (threes >= 2) return true;
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        long manilhaCount = intel.getCards().stream().filter(c -> c.isManilha(intel.getVira())).count();
        boolean hasZap = intel.getCards().stream().anyMatch(c -> c.isZap(intel.getVira()));
        long threes = intel.getCards().stream().filter(c -> c.getRank() == CardRank.THREE).count();

        if (manilhaCount >= 2) return true;
        if (manilhaCount >= 1 && threes >= 1) return true;
        if (hasZap && threes >= 1) return true;
        if (threes >= 2) return true;
        if (hasZap) return true;
        if (manilhaCount >= 1) return true;

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();
        Optional<TrucoCard> opponentCardOpt = intel.getOpponentCard();
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();


        List<TrucoCard> sortedHand = hand.stream()
                .sorted((a, b) -> Integer.compare(b.relativeValue(vira), a.relativeValue(vira)))
                .toList();

        TrucoCard better = sortedHand.get(0);
        TrucoCard pior = sortedHand.get(sortedHand.size() - 1);
        int rodadaAtual = roundResults.size() + 1;

        if (hand == null || hand.isEmpty()) {
            if (rodadaAtual == 1) {
                return CardToPlay.of(better);
            } else {
                return CardToPlay.discard(TrucoCard.closed());
            }
        }

        if (opponentCardOpt.isEmpty()) {
            if (rodadaAtual > 1 && !roundResults.isEmpty()) {
                GameIntel.RoundResult ultima = roundResults.get(roundResults.size() - 1);
                if (ultima == GameIntel.RoundResult.LOST) {
                    return CardToPlay.discard(pior);
                }
            }
            return CardToPlay.of(better);
        }

        TrucoCard cartaOponente = opponentCardOpt.get();

        if (better.compareValueTo(cartaOponente, vira) > 0) {
            return CardToPlay.of(better);
        } else {
            if (rodadaAtual == 1) {
                return CardToPlay.of(pior);
            }
            return CardToPlay.discard(pior);
        }
    }
    
    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();

        long threes = hand.stream().filter(c -> c.getRank() == CardRank.THREE).count();
        long twos = hand.stream().filter(c -> c.getRank() == CardRank.TWO).count();
        long manilhaCount = intel.getCards().stream().filter(c -> c.isManilha(intel.getVira())).count();
        int opponentScore = intel.getOpponentScore();
        int playerScore = intel.getScore();
        boolean hasZap = intel.getCards().stream().anyMatch(c -> c.isZap(intel.getVira()));
        boolean hasCopas = intel.getCards().stream().anyMatch(c -> c.isCopas(intel.getVira()));

        long difPoints = playerScore - opponentScore;

        if(difPoints <= 5 && difPoints >= 0) {
            if(twos >= 1 && threes >= 1) {
                return 0;
            }
        }

        if (manilhaCount >= 1) {
            if (threes >= 1) {
                return 1;
            }
            if(manilhaCount == 1) return -1;
            if(hasZap && hasCopas) return 1;
        }
        else{
            if (threes >= 2) {
                return 0;
            }
        }

        return -1;
    }

    @Override
    public String getName() { return "BotBlessedByJC"; }

}
