/*
 *  Copyright (C) 2024 Vitor A. de Jesus - IFSP/SCL
 *  Contact: vitor <dot> aguiar <at> ifsp <dot> edu <dot> br
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

package com.aguiar.vitor.trucorinthians;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Trucorinthians implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int myScore = intel.getScore();
        int opponentScore = intel.getOpponentScore();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();

        if (myScore == 11 && opponentScore == 11) {
            return true;
        }

        return hasManilha(hand, vira);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int round = intel.getRoundResults().size();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();


        long manilhaCount = hand.stream()
                .filter(card -> card.isManilha(vira))
                .count();

        long strongCount = hand.stream()
                .filter(card -> card.relativeValue(vira) >= 3)
                .count();

        return switch (round) {
            case 0 -> manilhaCount >= 2;
            case 1 -> manilhaCount == 1 && strongCount >= 2;
            case 2-> hand.stream()
                    .anyMatch(card -> card.relativeValue(vira) >= 8);
            default -> false;
        };
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return getStrategicCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> hand = intel.getCards();

        long manilhaCount = hand.stream()
                .filter(card -> card.isManilha(vira))
                .count();

        if (manilhaCount >= 2) {
            return 1;
        }

        boolean hasStrongCard = hand.stream()
                .anyMatch(card -> card.relativeValue(vira) >= 8);

        if (hasStrongCard || manilhaCount == 1) {
            return 0;
        }

        return -1;
    }

    private boolean hasManilha(List<TrucoCard> hand, TrucoCard vira) {
        return hand.stream().anyMatch(card -> card.isManilha(vira));
    }

    private CardToPlay getStrategicCard(GameIntel intel) {
        int round = intel.getRoundResults().size();
        boolean isFirstToPlay = intel.getOpponentCard().isEmpty();
        boolean isLosing = intel.getScore() < intel.getOpponentScore();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(TrucoCard.closed());
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        return switch (round) {
            case 0 -> {
                if (isLosing) {
                    yield isFirstToPlay ? getWeakest(hand, vira) : getSmallestWinning(hand, vira, opponentCard);
                }
                yield isFirstToPlay ? getWeakest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            }
            case 1 -> getSecondRoundStrategy(intel);
            case 2 -> getStrongest(hand, vira);
            default -> CardToPlay.of(hand.get(0));
        };

    }

    private CardToPlay getSecondRoundStrategy(GameIntel intel) {
        GameIntel.RoundResult firstResult = intel.getRoundResults().get(0);
        boolean isFirstToPlay = intel.getOpponentCard().isEmpty();
        boolean isLosing = intel.getScore() < intel.getOpponentScore();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(TrucoCard.closed());
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();

        return switch (firstResult) {
            case WON -> {
                if (isLosing) {
                    yield isFirstToPlay ? getStrongest(hand, vira) : getSmallestWinning(hand, vira, opponentCard);
                }
                yield isFirstToPlay ? getWeakest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            }
            case LOST -> getStrongest(hand, vira);
            case DREW -> {
                if (isLosing) {
                    yield isFirstToPlay ? getStrongest(hand, vira) : getSmallestWinning(hand, vira, opponentCard);
                }
                yield isFirstToPlay ? getStrongest(hand, vira) : getSmallestNonLosing(hand, vira, opponentCard);
            }
        };
    }

    private CardToPlay getWeakest(List<TrucoCard> hand, TrucoCard vira) {
        TrucoCard weakest = hand.stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(TrucoCard.closed());
        return CardToPlay.of(weakest);
    }

    private CardToPlay getSmallestNonLosing(List<TrucoCard> hand, TrucoCard vira, TrucoCard opponentCard) {
        return hand.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) >= 0)
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .map(CardToPlay::of)
                .orElseGet(() -> getWeakest(hand, vira));
    }

    private CardToPlay getStrongest(List<TrucoCard> hand, TrucoCard vira) {
        TrucoCard strongest = hand.stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(TrucoCard.closed());
        return CardToPlay.of(strongest);
    }

    private CardToPlay getSmallestWinning(List<TrucoCard> hand, TrucoCard vira, TrucoCard opponentCard) {
        Optional<TrucoCard> winning = hand.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)));

        return winning.map(CardToPlay::of).orElseGet(() -> getSmallestNonLosing(hand, vira, opponentCard));
    }
}
