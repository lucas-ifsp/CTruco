/*
 *  Copyright (C) 2024 Lucas Lazarini and Murilo M. Podenciano
 *  Contact: lazarini <dot> lucas <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: murilo <dot> marques <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.lucasmurilo.m.lazarinipodenciano;

import java.util.List;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class Akkosocorrompido implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decideIfRaises'");
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        // dividir em funçoes para cada round
        throw new UnsupportedOperationException("Unimplemented method 'chooseCard'");
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRaiseResponse'");
    }

    //low card
    public TrucoCard getLowestRankInHand(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();

        TrucoCard lowestCard = botCards.get(0);
        for (TrucoCard card : botCards) {
          if (card.getRank().value()< lowestCard.getRank().value()) {
            lowestCard = card;
          }
        }
        return lowestCard;
      }
    //high card
    //manilha
    //lowtowin
}