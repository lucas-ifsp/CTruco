/*
 *  Copyright (C) 2024 Cauã V. S. Olivio  and Felipe M. Vilela - IFSP SCL
 *  Contact: caua <dot> o <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: felipe <dot> vilela <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.caua.felipe.cfbot;

import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CFBot implements BotServiceProvider {
    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        myCards.sort((card1, card2) -> card1.compareValueTo(card2, intel.getVira()));
        TrucoCard vira = intel.getVira();

        if (isBiggestCoupleOrBlack(intel, myCards)) return 1;
        if (toTwoOrThree(intel, myCards) && myCards.stream().anyMatch(card -> card.isManilha(vira))) return 1;
        if (whatRound(intel) == 3 && myCards.stream().anyMatch(card -> card.isZap(intel.getVira()))) return 1;
        if (isClubsAndDiamonds(intel, myCards)) return 0;
        if (whatRound(intel) == 3 && toTwoOrThree(intel, myCards)) return 0;

        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        boolean hasManilhas = myCards.stream().anyMatch(card -> card.isManilha(vira));
        boolean hasZap = myCards.stream().anyMatch(card -> card.isZap(vira));

        return hasManilhas || hasZap || isBiggestCoupleOrBlack(intel, myCards);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        boolean hasHighCards = myCards.stream().anyMatch(card -> card.relativeValue(vira) > 6);
        boolean hasManilhas = myCards.stream().anyMatch(card -> card.isManilha(vira));

        if (hasManilhas || isBiggestCoupleOrBlack(intel, myCards)) {
            return true;
        }
        return hasHighCards;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        cards.sort((card1, card2) -> card1.compareValueTo(card2, vira));

        if (intel.getRoundResults().isEmpty()) return firstToPlay(intel, cards);
        return playLater(intel, cards);
    }

    private CardToPlay lowCard(List<TrucoCard> cards) {
        return CardToPlay.of(cards.get(0));
    }

    private CardToPlay highCard(List<TrucoCard> cards) {
        return CardToPlay.of(cards.get(cards.size() - 1));
    }

    private CardToPlay firstToPlay(GameIntel intel, List<TrucoCard> cards) {
        if (whatRound(intel) == 1) return lowCard(cards);
        if (whatRound(intel) == 2 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)
            return highCard(cards);

        return lowCard(cards);
    }

    private CardToPlay playLater(GameIntel intel, List<TrucoCard> cards) {
        if (whatRound(intel) == 1) return CardToPlay.of(cards.get(cards.size() - 1));

        return getLessToKill(intel, cards, intel.getVira());
    }

    private CardToPlay getLessToKill(GameIntel intel, List<TrucoCard> cards, TrucoCard vira) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isEmpty()) {
            return lowCard(cards);
        }

        List<TrucoCard> winningCards = cards.stream()
                .filter(card -> card.compareValueTo(opponentCard.get(), vira) > 0)
                .sorted(Comparator.comparingInt(card -> card.compareValueTo(opponentCard.get(), vira)))
                .toList();

        if (winningCards.isEmpty()) return lowCard(cards);
        return CardToPlay.of(winningCards.get(0));
    }

    public boolean isManilhaClubs(GameIntel gameIntel, List<TrucoCard> myCards) {
        return myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.CLUBS);
    }

    public boolean isManilhaHearts(GameIntel gameIntel, List<TrucoCard> myCards) {
        return myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.HEARTS);
    }

    public boolean isManilhaSpades(GameIntel gameIntel, List<TrucoCard> myCards) {
        return myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.SPADES);
    }

    public boolean isManilhaDiamonds(GameIntel gameIntel, List<TrucoCard> myCards) {
        return myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.DIAMONDS);
    }

    public boolean isBiggestCoupleOrBlack(GameIntel gameIntel, List<TrucoCard> myCards) {
        if (isManilhaClubs(gameIntel, myCards) && isManilhaHearts(gameIntel, myCards)) return true;
        return isManilhaClubs(gameIntel, myCards) && isManilhaSpades(gameIntel, myCards);
    }

    public boolean isClubsAndDiamonds(GameIntel gameIntel, List<TrucoCard> myCards) {
        return isManilhaClubs(gameIntel, myCards) && isManilhaDiamonds(gameIntel, myCards);
    }


    public boolean toTwoOrThree(GameIntel gameIntel, List<TrucoCard> myCards) {
        return myCards.stream().anyMatch(card -> card.getRank().value() == 9 || card.getRank().value() == 10);
    }

    public int whatRound(GameIntel gameIntel) {
        List<TrucoCard> cards = gameIntel.getCards();
        if (cards.size() == 3) return 1;
        if (cards.size() == 2) return 2;
        return 3;
    }
}
