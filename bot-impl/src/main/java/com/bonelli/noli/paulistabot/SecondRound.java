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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SecondRound implements Strategy {

    public SecondRound() {
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getHandPoints() == 12) return 0;
        if (intel.getScore() + 3 >= 12) return 0;
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, intel.getVira());
        });
        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);
        switch (roundResult) {
            case WON -> {
                if (hasManilha(intel)) return 1;
                else if (calculateCurrentHandValue(intel) >= 16) return 0;
                else return -1;
            }
            case LOST -> {
                if (hasZap(intel) && calculateCurrentHandValue(intel) >= 21) return 0;
                else return -1;
            }
            case DREW -> {
                if (hasManilha(intel)) return 0;
                if (intel.getCards().size() == 2) {
                    return cards.get(1).getRank().value() == 10 ? 0: -1;
                }
                else if (cards.get(0).getRank().value() == 10) return 0;
                else return -1;
            }
            default -> {
                return -1;
            }
        }
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
            if (cards.get(1).relativeValue(intel.getVira()) >= intel.getOpponentCard().get().relativeValue(intel.getVira()))
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
        if (intel.getScore() == 11) return false;
        if (intel.getHandPoints() == 12) return false;
        if (intel.getScore() + 3 > 12) return false;
        TrucoCard vira = intel.getVira();

        // Uma lista que contém as 2 cartas jogadas na primeira rodada em ordem crescente
        List<TrucoCard> openCards = new ArrayList<>(intel.getOpenCards().subList(0, 2));
        openCards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, vira);
        });

        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);

        switch (roundResult) {
            case WON -> {
                if (openCards.get(1).relativeValue(intel.getVira()) >= 8) {
                    return calculateCurrentHandValue(intel) <= 8;
                }
                return calculateCurrentHandValue(intel) >= 15 || hasManilha(intel);
            }
            case LOST -> {
                if (intel.getHandPoints() <= 3) return calculateCurrentHandValue(intel) >= 18;
            }
            case DREW -> {
                if (hasManilha(intel)) return true;
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, vira);
        });

        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);

        switch (roundResult) {
            case WON -> {
                if (hasZap(intel)) return CardToPlay.discard(cards.get(0));
                else if (calculateCurrentHandValue(intel) >= 17) return CardToPlay.of(cards.get(0));
                else {
                    if (calculateCurrentHandValue(intel) >= 5 && calculateCurrentHandValue(intel) < 13) return CardToPlay.of(cards.get(1));
                    return CardToPlay.of(cards.get(0));
                }
            }
            case LOST, DREW -> {
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if (opponentCard.getRank() != CardRank.HIDDEN) {
                        int countCardsHigherOfOpponent = getCountCardsAreHigherOfOpponent(cards, opponentCard, vira);
                        if (countCardsHigherOfOpponent == 0) {
                            if (cardWithSameValueOfOpponent(cards, opponentCard)) return CardToPlay.of(getCardWithSameValueOfOpponent(cards, opponentCard)); //Corre risco de amarrar dessa forma
                            return CardToPlay.discard(cards.get(0));
                        }
                        else if (countCardsHigherOfOpponent == 1) return CardToPlay.of(cards.get(1));
                        else return CardToPlay.of(chooseCardThatBeatsTheOpponent(cards, opponentCard, vira));
                    } else return CardToPlay.of(cards.get(0));
                } else return CardToPlay.of(cards.get(1));
            }
            default -> {
                return CardToPlay.of(cards.get(0));
            }
        }
    }

    public boolean hasTwoOrThree(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.getRank() == CardRank.TWO
                || card.getRank() == CardRank.THREE);
    }

    public TrucoCard getCardWithMediumStrength(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .sorted(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .skip(1)
                .limit(1)
                .findFirst()
                .orElseThrow();
    }

    public TrucoCard getCardWithSameValueOfOpponent(List<TrucoCard> cards, TrucoCard opponentCard) {
        return cards.stream().filter(card -> card.getRank().value() == opponentCard.getRank().value()).findFirst().orElse(cards.get(0));
    }

    @Override
    public boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }

    public boolean cardWithSameValueOfOpponent(List<TrucoCard> cards, TrucoCard opponentCard) {
        return cards.stream().anyMatch(card -> card.getRank().value() == opponentCard.getRank().value());
    }

    private boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    public int calculateCurrentHandValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }

    public int getCountCardsAreHigherOfOpponent(List<TrucoCard> cards, TrucoCard card, TrucoCard vira) {
        int count = 0;
        for (TrucoCard trucoCard : cards) {
            if (trucoCard.compareValueTo(card, vira) > 0)
                count++;
        }
        return count;
    }

    public TrucoCard chooseCardThatBeatsTheOpponent(List<TrucoCard> cardList, TrucoCard vira, TrucoCard opponentCard) {
        return cardList.stream()
                .filter(carta -> carta.relativeValue(vira) > opponentCard.relativeValue(vira))
                .min(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .orElse(cardList.get(1));
    }
}
