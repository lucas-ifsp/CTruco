/*
 *  Copyright (C) 2023 Lucas Matheus dos Santos
 *  Contact: matheus <dot> lucas1 <at> ifsp <dot> edu <dot> br
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


package com.lucassantos;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class TiaoDoCorote implements BotServiceProvider {
    private boolean alreadyRaisedThisHand = false;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true; // Agora sempre aceita mão de onze
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // Se for mão de 11, não pede truco
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;

        // Se não for primeira rodada, não pede truco
        if (!intel.getRoundResults().isEmpty()) return false;

        // Se já pediu truco nesta mão, não pede novamente
        if (alreadyRaisedThisHand) return false;

        // Se o oponente jogou uma manilha e não tenho manilha, não peço truco
        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            if (opponentCard.isManilha(intel.getVira())) {
                boolean hasManilha = intel.getCards().stream()
                        .anyMatch(card -> card.isManilha(intel.getVira()));
                if (!hasManilha) return false;
            }
        }

        alreadyRaisedThisHand = true;
        return true;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        List<GameIntel.RoundResult> results = intel.getRoundResults();

        // Se for a primeira rodada
        if (results.isEmpty()) {
            if (intel.getOpponentCard().isPresent()) {
                // Oponente jogou primeiro, tento matar com a menor carta possível
                TrucoCard opponentCard = intel.getOpponentCard().get();
                return CardToPlay.of(findSmallestWinningCard(myCards, opponentCard, intel.getVira()));
            } else {
                // Sou o primeiro a jogar, jogo a menor carta
                return CardToPlay.of(findLowestCard(myCards, intel.getVira()));
            }
        }

        // Se for a segunda rodada
        if (results.size() == 1) {
            if (results.get(0) == GameIntel.RoundResult.WON) {
                // Se ganhei a primeira, jogo a menor carta
                return CardToPlay.of(findLowestCard(myCards, intel.getVira()));
            } else {
                // Se perdi a primeira, tento matar com a menor carta possível
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    return CardToPlay.of(findSmallestWinningCard(myCards, opponentCard, intel.getVira()));
                } else {
                    return CardToPlay.of(findLowestCard(myCards, intel.getVira()));
                }
            }
        }

        // Se for a terceira rodada, jogo a única carta que sobrou
        return CardToPlay.of(myCards.get(0));
    }

    private TrucoCard findSmallestWinningCard(List<TrucoCard> myCards, TrucoCard opponentCard, TrucoCard vira) {
        return myCards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((card1, card2) -> card1.compareValueTo(card2, vira))
                .orElse(findLowestCard(myCards, vira)); // Se não tiver carta para matar, joga a mais baixa
    }

    private TrucoCard findLowestCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .min((card1, card2) -> card1.compareValueTo(card2, vira))
                .orElse(cards.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
        //aceita sempre o trucokkkkk
    }

    @Override
    public String getName() {
        return "Tião do Corote";
    }
}