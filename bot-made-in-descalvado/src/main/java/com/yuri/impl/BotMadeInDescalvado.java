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
        return switch (round(intel)) {
            case 1 -> FirstRound.getMaoDeOnzeResponse(intel);
            case 2 -> SecondRound.getMaoDeOnzeResponse(intel);
            case 3 -> ThirdRound.getMaoDeOnzeResponse(intel);
            default -> throw new RuntimeException(INVALID_ROUND_MSG);
        };
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

        public static boolean getMaoDeOnzeResponse(GameIntel intel) {
            return false;
        }

        public static boolean decideIfRaises(GameIntel intel) {
            return false;
        }

        public static CardToPlay chooseCard(GameIntel intel) {
            TrucoCard vira = intel.getVira();

            List<TrucoCard> cards = new ArrayList<>(intel.getCards());
            cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));

            boolean playsFirst = intel.getOpponentCard().isEmpty();

            if (playsFirst) {
                int manilhaCount = 0;

                for (var card : intel.getCards()) {
                    if (card.isManilha(intel.getVira())) {
                        manilhaCount += 1;
                    }
                }

                if (manilhaCount >= 2) {
                    return CardToPlay.discard(intel.getCards().get(2));
                } else {
                    return CardToPlay.of(intel.getCards().get(0));
                }
            } else {
                return CardToPlay.of(intel.getCards().get(0));
            }
        }
    }

    private static class SecondRound {
        public static int getRaiseResponse(GameIntel intel) {
            return 0;
        }

        public static boolean getMaoDeOnzeResponse(GameIntel intel) {
            return false;
        }

        public static boolean decideIfRaises(GameIntel intel) {
            return false;
        }

        public static CardToPlay chooseCard(GameIntel intel) {
            return CardToPlay.of(intel.getCards().get(0));
        }
    }

    private static class ThirdRound {
        public static int getRaiseResponse(GameIntel intel) {
            return 0;
        }

        public static boolean getMaoDeOnzeResponse(GameIntel intel) {
            return false;
        }

        public static boolean decideIfRaises(GameIntel intel) {
            return false;
        }

        public static CardToPlay chooseCard(GameIntel intel) {
            return CardToPlay.of(intel.getCards().get(0));
        }
    }
}
