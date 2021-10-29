/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.entities.player.mineirobot;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.entities.player.util.PlayingStrategy;

public class MineiroBot extends Player {

    public MineiroBot() {
        super("MineiroBot");
    }

    @Override
    public Card playCard() {
        return PlayingStrategy.of(cards, this).playCard();
    }

    @Override
    public boolean requestTruco() {
        return PlayingStrategy.of(cards, this).requestTruco();
    }

    @Override
    public int getTrucoResponse(HandScore newHandScore) {
        return PlayingStrategy.of(cards, this).getTrucoResponse(newHandScore.get());
    }

    @Override
    public boolean getMaoDeOnzeResponse() {
        return PlayingStrategy.of(cards, this).getMaoDeOnzeResponse();
    }
}
