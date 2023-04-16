/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
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

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FirstRound implements Strategy {

    private final TrucoCard vira;

    private final List<TrucoCard> cardList;

    private final List<TrucoCard> openCards;

    private final GameIntel intel;

    public FirstRound(GameIntel intel) {
        this.vira = intel.getVira();
        this.cardList = new ArrayList<>(intel.getCards());
        this.openCards = new ArrayList<>(intel.getOpenCards());
        this.intel = intel;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        Optional<TrucoCard> opponentCard = getWhichBotShouldPlayFirst();
        if (opponentCard.isPresent()) {
            TrucoCard opponentCardBot = opponentCard.get();
            int countCardsHigherOfOpponent = getCountCardsAreHigherOfOpponent(this.cardList, opponentCardBot, this.vira);
            TrucoCard card;
            switch (countCardsHigherOfOpponent) {
                case 1 -> {
                    card = checkWhichCardHasHigherValue(this.cardList, this.vira);
                    if (!opponentCardBot.isManilha(this.vira)) {
                        if (card.isCopas(this.vira) || card.isZap(this.vira))
                            return CardToPlay.of(playCardWithSameValueOfOpponent(this.cardList, this.vira));
                    }
                    return CardToPlay.of(card);
                }
                case 2, 3 -> {
                    return CardToPlay.of(chooseCardThatBeatsTheOpponent(this.cardList, this.vira, opponentCardBot));
                }
                default -> {
                    if (cardsHaveSameValue(this.cardList))
                        return CardToPlay.of(intel.getCards().get(0));
                    return CardToPlay.of(checkWhichCardHasLowerValue(this.cardList, this.vira));
                }
            }
        }
        return null;
    }

    private boolean cardsHaveSameValue(List<TrucoCard> cardList) {
        for (int i = 0; i < 1; i++) {
             if (cardList.get(i).getRank().value() == cardList.get(i + 1).getRank().value() &&
                cardList.get(i).getRank().value() == cardList.get(i + 2).getRank().value())
                 return true;
        }
        return false;
    }

    private TrucoCard playCardWithSameValueOfOpponent(List<TrucoCard> cardList, TrucoCard opponentCardBot) {
        return cardList.stream()
                .filter(card -> card.getRank().value() == opponentCardBot.getRank().value())
                .findFirst()
                .orElse(null);
    }

    private TrucoCard chooseCardThatBeatsTheOpponent(List<TrucoCard> cardList, TrucoCard vira, TrucoCard opponentCard) {
        return cardList.stream()
                .filter(carta -> carta.relativeValue(vira) > opponentCard.relativeValue(vira))
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(null);
    }

    private int getCountCardsAreHigherOfOpponent(List<TrucoCard> cards, TrucoCard card, TrucoCard vira) {
        int count = 0;
        for (TrucoCard trucoCard : cards) {
            if (trucoCard.compareValueTo(card, vira) > 0)
                count++;
        }
        return count;
    }

    private TrucoCard checkWhichCardHasLowerValue (List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(null);
    }

    private TrucoCard checkWhichCardHasHigherValue (List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .max(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(cards.get(0));
    }

    public Optional<TrucoCard> getWhichBotShouldPlayFirst () {
        if (this.intel.getOpponentCard().isEmpty())
            return Optional.empty();
        return Optional.of(this.intel.getOpponentCard().get());
    }
}
