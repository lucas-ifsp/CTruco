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

package com.zampieri.impl.bot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Bot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) { return 1;
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
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> openCards = intel.getOpenCards();
        List<GameIntel.RoundResult> result = intel.getRoundResults();

        int numberOfManilhas = 0;

        for ( TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                numberOfManilhas ++;
            }
        }

        if ( intel.getOpponentCard().isPresent()) {
           TrucoCard opponentCard = intel.getOpponentCard().get();

           if (opponentCard.getRank().equals(CardRank.HIDDEN)) {
               TrucoCard lowestCard = cards.get(0);
               for (TrucoCard trucoCard : cards) {
                   if (trucoCard.relativeValue(vira) < lowestCard.relativeValue(vira)) {
                       lowestCard = trucoCard;
                   } else {
                       lowestCard = opponentCard;
                   }
               }

               return CardToPlay.of(lowestCard);
           }
           if ( opponentCard.isManilha(vira) && numberOfManilhas >= 1) {
               for (TrucoCard trucoCard : cards) {
                   if ( trucoCard.relativeValue(vira) > opponentCard.relativeValue(vira)) {
                       return CardToPlay.of(trucoCard);
                   }
               }

               TrucoCard lowestCard = cards.get(0);
               for (TrucoCard trucoCard : cards) {
                   if (trucoCard.relativeValue(vira) < lowestCard.relativeValue(vira)) {
                       lowestCard = trucoCard;
                   } else {
                       lowestCard = opponentCard;
                   }
               }

               return CardToPlay.of(lowestCard);
           }
            if ( opponentCard.isManilha(vira) && numberOfManilhas == 0) {
                TrucoCard lowestCard = cards.get(0);
                for (TrucoCard trucoCard : cards) {
                    if (trucoCard.relativeValue(vira) < lowestCard.relativeValue(vira)) {
                        lowestCard = trucoCard;
                    } else {
                        lowestCard = opponentCard;
                    }
                }

                return CardToPlay.of(lowestCard);
            }
            else {
                for (TrucoCard trucoCard : cards) {
                    if ( trucoCard.relativeValue(vira) > opponentCard.relativeValue(vira)) {
                        return CardToPlay.of(trucoCard);
                    }
                }

                TrucoCard lowestCard = cards.get(0);
                for (TrucoCard trucoCard : cards) {
                    if (trucoCard.relativeValue(vira) < lowestCard.relativeValue(vira)) {
                        lowestCard = trucoCard;
                    } else {
                        lowestCard = opponentCard;
                    }
                }

                return CardToPlay.of(lowestCard);
            }
        }

        return CardToPlay.of(intel.getCards().get(0));
    }
}
