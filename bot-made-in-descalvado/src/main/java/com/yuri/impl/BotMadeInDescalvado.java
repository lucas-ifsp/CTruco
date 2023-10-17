/*
 *  Copyright (C) 2022 Yuri Soares Menon
 *  Contact: y <dot> menon <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.yuri.impl;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.WON;

public class BotMadeInDescalvado implements BotServiceProvider {

    public static final String INVALID_ROUND_MSG = "Invalid round";

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return switch (round(intel)) {
            case 1 -> FirstRound.getRaiseResponse(intel);
            case 2 -> SecondRound.getRaiseResponse(intel);
            case 3 -> ThirdRound.getRaiseResponse(intel);
            default -> throw new RuntimeException(INVALID_ROUND_MSG);
        };
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return switch (round(intel)) {
            case 1 -> FirstRound.decideIfRaises(intel);
            case 2 -> SecondRound.decideIfRaises(intel);
            case 3 -> ThirdRound.decideIfRaises(intel);
            default -> throw new RuntimeException(INVALID_ROUND_MSG);
        };
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return switch (round(intel)) {
            case 1 -> FirstRound.chooseCard(intel);
            case 2 -> SecondRound.chooseCard(intel);
            case 3 -> ThirdRound.chooseCard(intel);
            default -> throw new RuntimeException(INVALID_ROUND_MSG);
        };
    }

    private int round(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    private static class FirstRound {
        public static int getRaiseResponse(GameIntel intel) {
            return 0;
        }

        public static boolean decideIfRaises(GameIntel intel) {
            return false;
        }

        public static CardToPlay chooseCard(GameIntel intel) {
            boolean isFirst = intel.getOpponentCard().isEmpty();
            List<TrucoCard> cards = new ArrayList<>(intel.getCards());
            TrucoCard vira = intel.getVira();
            Optional<TrucoCard> maybeOpponentCard = intel.getOpponentCard();

            if (isFirst) {
                long manilhaCount = cards.stream().filter((c) -> c.isManilha(vira)).count();

                if (manilhaCount >= 2) {
                    TrucoCard notManilha = cards.stream()
                        .filter((c) -> !c.isManilha(vira))
                        .findFirst()
                        .orElse(cards.get(0));

                    return CardToPlay.discard(notManilha);
                } else {
                    TrucoCard card = cards.stream()
                        .max((a, b) -> a.compareValueTo(b, vira))
                        .orElse(cards.get(0));

                    return CardToPlay.of(card);
                }
            } else {
                TrucoCard opponentCard = maybeOpponentCard.get();

                Optional<TrucoCard> maybeCardToWin = cards.stream()
                    .filter((c) -> c.compareValueTo(opponentCard, vira) > 0)
                    .min((a, b) -> a.compareValueTo(b, vira));

                if (maybeCardToWin.isPresent()) {
                    return CardToPlay.of(maybeCardToWin.get());
                }

                Optional<TrucoCard> maybeCardToDraw = cards.stream()
                    .filter((c) -> c.compareValueTo(opponentCard, vira) == 0)
                    .min((a, b) -> a.compareValueTo(b, vira));

                if (maybeCardToDraw.isPresent()) {
                    return CardToPlay.of(maybeCardToDraw.get());
                }

                TrucoCard card = cards.stream()
                    .min((a, b) -> a.compareValueTo(b, vira))
                    .orElse(cards.get(0));

                return CardToPlay.of(card);
            }
        }
    }

    private static class SecondRound {
        public static int getRaiseResponse(GameIntel intel) {
            return 0;
        }

        public static boolean decideIfRaises(GameIntel intel) {
            return false;
        }

        public static CardToPlay chooseCard(GameIntel intel) {
            GameIntel.RoundResult firstRoundResult = intel.getRoundResults().get(0);
            boolean isFirst = intel.getOpponentCard().isEmpty();
            List<TrucoCard> cards = new ArrayList<>(intel.getCards());
            TrucoCard vira = intel.getVira();
            Optional<TrucoCard> maybeOpponentCard = intel.getOpponentCard();

            if (firstRoundResult == WON) {
                if (isFirst) {
                    TrucoCard card = cards.stream()
                        .min((a, b) -> a.compareValueTo(b, vira))
                        .orElse(cards.get(0));

                    return CardToPlay.discard(card);
                } else {
                    TrucoCard opponentCard = maybeOpponentCard.get();

                    Optional<TrucoCard> maybeStrong = cards.stream()
                        .filter((c) -> c.compareValueTo(opponentCard, vira) > 0)
                        .max((a, b) -> a.compareValueTo(b, vira));

                    if (maybeStrong.isPresent()) {
                        return CardToPlay.of(maybeStrong.get());
                    }

                    TrucoCard card = cards.stream()
                        .min((a, b) -> a.compareValueTo(b, vira))
                        .orElse(cards.get(0));

                    return CardToPlay.of(card);
                }
            } else {
                if (isFirst) {
                    TrucoCard card = cards.stream()
                        .max((a, b) -> a.compareValueTo(b, vira))
                        .orElse(cards.get(0));

                    return CardToPlay.of(card);
                } else {
                    TrucoCard opponentCard = maybeOpponentCard.get();

                    TrucoCard card = cards.stream()
                        .filter((c) -> c.compareValueTo(opponentCard, vira) > 0)
                        .min((a, b) -> a.compareValueTo(b, vira))
                        .orElse(cards.get(0));

                    return CardToPlay.of(card);
                }
            }
        }
    }

    private static class ThirdRound {
        public static int getRaiseResponse(GameIntel intel) {
            return 0;
        }

        public static boolean decideIfRaises(GameIntel intel) {
            return false;
        }

        public static CardToPlay chooseCard(GameIntel intel) {
            return CardToPlay.of(intel.getCards().get(0));
        }
    }
}
