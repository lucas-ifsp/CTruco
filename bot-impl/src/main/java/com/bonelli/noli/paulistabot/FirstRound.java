/*
 *  Copyright (C) 2023 Vinicius R. Noli and Vitor Bonelli
 *  Contact: vinicius <dot> noli <at> ifsp <dot> edu <dot> br
 *  Contact: vitor <dot> bonelli <at> ifsp <dot> edu <dot> br
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

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.*;

public class FirstRound implements Strategy {

    public FirstRound() {
    }

    @Override
    public int getRaiseResponse (GameIntel intel) {
        TrucoCard vira = intel.getVira();

        if (intel.getCards().size() == 2) { // Nosso bot jogou a primeira carta e o bot inimigo chama Truco
            TrucoCard cardPlayed = intel.getOpenCards().get(intel.getOpenCards().size() - 1);
            if (cardPlayed.relativeValue(vira) + calculateCurrentHandValue(intel) >= 28) return 0;
            if (hasTwoOrThree(intel)) {
                if (hasCopasOrZap(intel) && intel.getHandPoints() != 12) return 1;
                if (hasManilha(intel)) return 0;
            } else if (hasTwoManilhas(intel) == 2) return 0;
        }
        if (hasCoupleBigger(intel) == 1 || hasCouplaBlack(intel) == 1 && intel.getHandPoints() != 12) return 1;
        else return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {

        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, intel.getVira());
        });

        TrucoCard cardMedium = getCardWithMediumStrength(intel.getCards(), intel.getVira());

        if (calculateCurrentHandValue(intel) >= 25) {
            if (hasManilha(intel)) return true;
            else if (intel.getOpponentScore() < 9 && hasTwoOrThree(intel)) return true;
            else if (intel.getOpponentScore() >= 9 && cardMedium.getRank().value() >= 9) return true;
            return true;
        }

        if (intel.getOpponentCard().isPresent()) {
            if (cards.get(2).relativeValue(intel.getVira()) >= intel.getOpponentCard().get().relativeValue(intel.getVira()))
                return true;
        }

        if (intel.getOpponentScore() <= 6) return true;

        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira())) return true;
        }

        return intel.getScore() >= intel.getOpponentScore() + 3;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard vira = intel.getVira();
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, vira);
        });
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;
        if (intel.getHandPoints() == 12 || intel.getOpponentScore() + 3 > 12 || intel.getScore() + 3 > 12) return false;
        if (intel.getOpponentCard().isPresent()) return cards.get(1).compareValueTo(intel.getOpponentCard().get(), vira) >= 8;
        if (intel.getScore() >= intel.getOpponentScore() + 3) return true;
        if (intel.getOpponentCard().isPresent()) {
            return cards.get(2).relativeValue(intel.getVira()) >= intel.getOpponentCard().get().relativeValue(intel.getVira());
        }
        else return calculateCurrentHandValue(intel) >= 22 || hasTwoManilhas(intel) == 2;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, intel.getVira());
        });
        Optional<TrucoCard> opponentCard = getWhichBotShouldPlayFirst(intel);
        TrucoCard card;
        if (opponentCard.isPresent()) {
            TrucoCard opponentCardBot = opponentCard.get();
            int countCardsHigherOfOpponent = getCountCardsAreHigherOfOpponent(cards, opponentCardBot, intel.getVira());
            switch (countCardsHigherOfOpponent) {
                case 1 -> {
                    card = cards.get(2);
                    if (!opponentCardBot.isManilha(intel.getVira())) {
                        if (card.isCopas(intel.getVira()) || card.isZap(intel.getVira()))
                            return CardToPlay.of(playCardWithSameOrLessValueOfOpponent(cards, intel.getVira()));
                    }
                    return CardToPlay.of(card);
                }
                case 2, 3 -> {
                    return CardToPlay.of(chooseCardThatBeatsTheOpponent(cards, intel.getVira(), opponentCardBot));
                }
                default -> {
                    if (cardsHaveSameValue(cards))
                        return CardToPlay.of(cards.get(0));
                    return CardToPlay.of(cards.get(0));
                }
            }
        } else if (calculateCurrentHandValue(intel) >= 23) {
            if (hasManilha(intel)) {
                if (hasOurosOrEspadilha(intel))
                    return CardToPlay.of(intel.getCards().stream().filter(trucoCard -> trucoCard.isOuros(intel.getVira()) ||
                            trucoCard.isEspadilha(intel.getVira())).findFirst().orElseThrow());
                if (hasCopasOrZap(intel))
                    return CardToPlay.of(intel.getCards().stream().filter(card1 -> !card1.isCopas(intel.getVira()) ||
                            !card1.isZap(intel.getVira())).findFirst().orElseThrow());
            }
            return CardToPlay.of(cards.get(1));
        } else {
            if (hasManilha(intel)) {
                if (!hasCopasOrZap(intel))
                    return CardToPlay.of(intel.getCards().stream().filter(trucoCard -> trucoCard.isManilha(intel.getVira()))
                        .findFirst().orElseThrow());
                return CardToPlay.of(cards.get(0));
            }
            return CardToPlay.of(cards.get(2));
        }
    }

    public int hasCouplaBlack(GameIntel intel) {
        return intel.getCards().stream()
                .filter(card -> card.isEspadilha(intel.getVira()) || card.isZap(intel.getVira()))
                .count() >= 2 ? 1 : 0;
    }

    public int hasCoupleBigger(GameIntel intel) {
        return intel.getCards().stream()
                .filter(card -> card.isCopas(intel.getVira()) || card.isZap(intel.getVira()))
                .count() >= 2 ? 1 : 0;
    }

    public int hasTwoManilhas(GameIntel intel) {
        return (int) intel.getCards().stream().filter(card -> card.isManilha(intel.getVira())).count();
    }

    public boolean hasCopasOrZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isCopas(intel.getVira()) || card.isZap(intel.getVira()));
    }

    public boolean hasTwoOrThree(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.getRank() == CardRank.TWO
            || card.getRank() == CardRank.THREE);
    }

    public boolean hasOurosOrEspadilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(trucoCard -> trucoCard.isOuros(intel.getVira()) ||
                trucoCard.isEspadilha(intel.getVira()));
    }

    @Override
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
        return cardList.get(0).getRank().value() == cardList.get(1).getRank().value() &&
                cardList.get(0).getRank().value() == cardList.get(2).getRank().value();
    }

    public TrucoCard playCardWithSameOrLessValueOfOpponent(List<TrucoCard> cardList, TrucoCard opponentCardBot) {
        return cardList.stream()
                .filter(card -> card.getRank().value() == opponentCardBot.getRank().value() || card.getRank().value() <= opponentCardBot.getRank().value())
                .findFirst()
                .orElse(cardList.get(0));
    }

    public TrucoCard chooseCardThatBeatsTheOpponent(List<TrucoCard> cardList, TrucoCard vira, TrucoCard opponentCard) {
        return cardList.stream()
                .filter(carta -> carta.relativeValue(vira) > opponentCard.relativeValue(vira))
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(cardList.get(1));
    }

    public int getCountCardsAreHigherOfOpponent(List<TrucoCard> cards, TrucoCard card, TrucoCard vira) {
        int count = 0;
        for (TrucoCard trucoCard : cards) {
            if (trucoCard.compareValueTo(card, vira) > 0)
                count++;
        }
        return count;
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

    public Integer calculateCurrentHandValueBasedVira(List<TrucoCard> cards, TrucoCard vira) {
        if (cards.isEmpty()) return 0;
        return cards.stream().map(card -> card.compareValueTo(card, vira)).reduce(Integer::sum).orElseThrow();
    }

    public Optional<TrucoCard> getWhichBotShouldPlayFirst (GameIntel intel) {
        if (intel.getOpponentCard().isEmpty())
            return Optional.empty();
        return Optional.of(intel.getOpponentCard().get());
    }
}
