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

import java.util.*;

public class FirstRound implements Strategy {

    public FirstRound() {
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        if (calculateCurrentHandValue(intel) >= 20) return 0;

        if (hasManilha(intel)) return 1;

        return -1;
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
        Optional<TrucoCard> opponentCard = getWhichBotShouldPlayFirst(intel);
        TrucoCard card;
        if (opponentCard.isPresent()) {
            TrucoCard opponentCardBot = opponentCard.get();
            int countCardsHigherOfOpponent = getCountCardsAreHigherOfOpponent(intel.getCards(), opponentCardBot, intel.getVira());
            switch (countCardsHigherOfOpponent) {
                case 1 -> {
                    card = checkWhichCardHasHigherValue(intel.getCards(), intel.getVira());
                    if (!opponentCardBot.isManilha(intel.getVira())) {
                        if (card.isCopas(intel.getVira()) || card.isZap(intel.getVira()))
                            return CardToPlay.of(playCardWithSameValueOfOpponent(intel.getCards(), intel.getVira()));
                    }
                    return CardToPlay.of(card);
                }
                case 2, 3 -> {
                    return CardToPlay.of(chooseCardThatBeatsTheOpponent(intel.getCards(), intel.getVira(), opponentCardBot));
                }
                default -> {
                    if (cardsHaveSameValue(intel.getCards()))
                        return CardToPlay.of(intel.getCards().get(0));
                    return CardToPlay.of(checkWhichCardHasLowerValue(intel.getCards(), intel.getVira()));
                }
            }
        } else if (calculateCurrentHandValue(intel) >= 23) {
            if (hasManilha(intel)) {
                if (hasOurosOrEspadilha(intel))
                    return CardToPlay.of(intel.getCards().stream().filter(trucoCard -> trucoCard.isOuros(intel.getVira()) ||
                            trucoCard.isEspadilha(intel.getVira())).findFirst().orElseThrow());
            }
            return CardToPlay.of(getCardWithMediumStrength(intel.getCards(), intel.getVira()));
        } else {
            if (hasManilha(intel))
                return CardToPlay.of(intel.getCards().stream().filter(trucoCard -> trucoCard.isManilha(intel.getVira()))
                        .findFirst().orElseThrow());
            return CardToPlay.of(intel.getCards().stream().filter(trucoCard -> trucoCard.relativeValue(intel.getVira()) >= 1)
                    .findFirst().orElseThrow());
        }
    }

    public boolean hasOurosOrEspadilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(trucoCard -> trucoCard.isOuros(intel.getVira()) ||
                trucoCard.isEspadilha(intel.getVira()));
    }

    public boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(trucoCard -> trucoCard.isManilha(intel.getVira()));
    }

    public TrucoCard getCardWithMediumStrength(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .sorted(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .skip(1)
                .limit(1)
                .findFirst()
                .orElseThrow();
    }

    public boolean cardsHaveSameValue(List<TrucoCard> cardList) {
        for (int i = 0; i < 1; i++) {
             if (cardList.get(i).getRank().value() == cardList.get(i + 1).getRank().value() &&
                cardList.get(i).getRank().value() == cardList.get(i + 2).getRank().value())
                 return true;
        }
        return false;
    }

    public TrucoCard playCardWithSameValueOfOpponent(List<TrucoCard> cardList, TrucoCard opponentCardBot) {
        return cardList.stream()
                .filter(card -> card.getRank().value() == opponentCardBot.getRank().value())
                .findFirst()
                .orElseThrow();
    }

    public TrucoCard chooseCardThatBeatsTheOpponent(List<TrucoCard> cardList, TrucoCard vira, TrucoCard opponentCard) {
        return cardList.stream()
                .filter(carta -> carta.relativeValue(vira) > opponentCard.relativeValue(vira))
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElseThrow();
    }

    public int getCountCardsAreHigherOfOpponent(List<TrucoCard> cards, TrucoCard card, TrucoCard vira) {
        int count = 0;
        for (TrucoCard trucoCard : cards) {
            if (trucoCard.compareValueTo(card, vira) > 0)
                count++;
        }
        return count;
    }

    public TrucoCard checkWhichCardHasLowerValue (List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElseThrow();
    }

    public TrucoCard checkWhichCardHasHigherValue (List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .max(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElseThrow();
    }

    public Integer calculateCurrentHandValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }

    public Optional<TrucoCard> getWhichBotShouldPlayFirst (GameIntel intel) {
        if (intel.getOpponentCard().isEmpty())
            return Optional.empty();
        return Optional.of(intel.getOpponentCard().get());
    }
}
